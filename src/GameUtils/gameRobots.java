package GameUtils;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class gameRobots {

    graph Graph;
    ArrayList<Robot> allRobots = new ArrayList<>();

    /**
     * Initialize robots for game
     * @param graph
     * @param game
     */
    public gameRobots(graph graph, game_service game){
        this.Graph = graph;
        List<String> robotsStrings = game.getRobots();
        int src = 0;
        if (robotsStrings != null){
            for (int i = 0 ; i < this.getRobotAmount(game); i++){
              // allRobots.add(new Robot(i,graph.getNode(i+src).getLocation(),i+src));
                game.addRobot(i);

            }
        }
        robotsStrings = game.getRobots();

        for (int i = 0 ; i < robotsStrings.size(); i++){
            String str = robotsStrings.get(i);
            try{
                JSONObject currentLine = new JSONObject(str);
                JSONObject rob = currentLine.getJSONObject("Robot");
                int id = rob.getInt("id");
                int first_node = rob.getInt("src");
                String location = rob.getString("pos");
                String pos[] = location.split(",");
                double x = Double.parseDouble(pos[0]);
                double y = Double.parseDouble(pos[1]);
                Robot robot = new Robot(id, new Point3D(x,y),first_node);
                allRobots.add(robot);
            }catch (Exception e){
                System.out.println("Problem with parsing the robot's JSON");
            }
        }

    }


    int getRobotAmount(game_service game)
    {
        int rs = 0;
        //String g = game.getGraph();
        String info = game.toString();
        JSONObject line;

        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
             rs = ttt.getInt("robots");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return rs;
    }

    /**
     * This method return robot by it's id
     * @param id
     * @return
     */
    public Robot getRobotByID(int id){
        Iterator<Robot> robIter = allRobots.iterator();
        while (robIter.hasNext()){
            if (robIter.next().getId() == id){
                return robIter.next();
            }
        }
        return null;
    }

    /**
     * This method return amount of robots
     * @return int size
     */
    public int size(){
        return allRobots.size();
    }

    /**
     * This method return the arraylist of robots
     * @return Collecction robots
     */
    public Collection<Robot> RobotsCollection(){
        return allRobots;
    }
    public ArrayList<Robot> Robots(){
        return new ArrayList<Robot>(allRobots);
    }

    /**
     * This method is similar with to nextNode, the difference is noticing the speed parameter when choosing
     * the closest fruit. If speed is true then it gives better path based on Shortest path.
     * If speed is false, then algorithm based on proximity of points, which is inferior.
     *
     * @param game
     * @param graph
     * @param robot_id
     * @param fruits
     * @param speed
     * @param src
     * @return
     */
    public static int nextNodePriority(game_service game, graph graph, int robot_id, gameFruits fruits, boolean speed, int src){
        gameRobots allRobots = new gameRobots(graph, game);
        Graph_Algo algo = new Graph_Algo(graph);

        Robot robot = allRobots.getRobotByID(robot_id);
        Fruit fru = fruits.getnearFruit(speed, src);
        int fru_id = fru.getId();

        edge_data edge_of_fruit = fruits.edgeOfFruit(fru_id);

        if( edge_of_fruit != null){
            if (edge_of_fruit.getSrc() == src) return edge_of_fruit.getDest();
            if (edge_of_fruit.getDest() == src) return edge_of_fruit.getSrc();
            if (fru.getType() == GameUtils.fruits.BANANA){
                List<node_data> Path = algo.shortestPath(src, edge_of_fruit.getDest());
                return Path.get(1).getKey();
            }else{
                List<node_data> Path = algo.shortestPath(src, edge_of_fruit.getSrc());
                return Path.get(1).getKey();
            }
        }

        return -1;
    }

    /**
     * In case of automatic mode finding the next node based on high value fruit
     * @param game
     * @param dgraph
     * @param robot_id
     * @param node_src
     * @param fruits
     * @return
     */
    private static int autoNextNode(game_service game, graph dgraph, int robot_id, int node_src, gameFruits fruits){
        Graph_Algo algo =  new Graph_Algo(dgraph);
        Fruit maxValue =  fruits.MaxFruit();
        int maxId = maxValue.getId();

        edge_data edge_of_fruit = fruits.edgeOfFruit(maxId);
        if (edge_of_fruit != null){
            if (edge_of_fruit.getDest() == node_src ){
                return edge_of_fruit.getSrc();
            }

            if (edge_of_fruit.getSrc() == node_src){
                return edge_of_fruit.getDest();
            }

            if (maxValue.getType() == GameUtils.fruits.BANANA ){
                List<node_data> Path = algo.shortestPath(node_src, edge_of_fruit.getDest());
                return Path.get(1).getKey();
            }else {
                List<node_data> Path = algo.shortestPath(node_src, edge_of_fruit.getSrc());
                return Path.get(1).getKey();
            }
        }

        return 0;
    }

    /**
     * The General method to find next node key
     * @param game
     * @param fruits
     * @param graph
     * @param robot_id
     * @param node_src
     * @return node key
     */
    private static int nextNode2(game_service game, gameFruits fruits, graph graph, int robot_id, int node_src,boolean auto_mode){
        if (auto_mode){
            return autoNextNode(game, graph, robot_id, node_src, fruits);
        }else{
            /**
             * get X and Y from mouse
             */
            //node_src = clickToNode(x, y);
            return node_src;
        }
    }

    public ArrayList<Robot> getAllRobots() {
        return allRobots;
    }

    public void moveRobots(game_service service, graph graph, gameFruits gameFruits){
        List<String> log = service.move();

        if (log != null) {
            for (int i = 0 ; i <log.size(); i++){
                Robot robotFromServer = modifyRobot(log.get(i));
                if (robotFromServer.getNext_node() == -1){
                    robotFromServer.setNext_node(gameRobots.nextNode2(service, gameFruits, graph, robotFromServer.getId(), robotFromServer.current_node, true));
                    service.chooseNextEdge(robotFromServer.getId(), robotFromServer.next_node);
                }
                allRobots.add(robotFromServer.getId(), robotFromServer) ;
            }
        }else {
            System.out.println("BIG PROBLEM");
        }
    }
    public Robot modifyRobot(String str){
        //String str = robotsStrings.get(i);
        try{
            JSONObject currentLine = new JSONObject(str);
            JSONObject rob = currentLine.getJSONObject("Robot");
            int id = rob.getInt("id");
            int first_node = rob.getInt("src");
            String location = rob.getString("pos");
            String pos[] = location.split(",");
            double x = Double.parseDouble(pos[0]);
            double y = Double.parseDouble(pos[1]);
            Robot robot = new Robot(id, new Point3D(x,y),first_node);
            return robot;
        }catch (Exception e){
            System.out.println("Problem with parsing the robot's JSON");
        }
    return null;
    }

}
