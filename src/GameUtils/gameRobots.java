package GameUtils;

import Server.game_service;
import dataStructure.graph;
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
        if (robotsStrings != null){
            for (int i = 0 ; i < robotsStrings.size(); i++){
                String str = robotsStrings.get(i);
                try{
                    JSONObject currentLine = new JSONObject(str);
                    JSONObject rob = currentLine.getJSONObject("Robot");
                    int id = rob.getInt("id");
                    String location = rob.getString("pos");
                    String pos[] = location.split(location);
                    double x = Double.parseDouble(pos[0]);
                    double y = Double.parseDouble(pos[1]);
                    Robot robot = new Robot(id, new Point3D(x,y));
                    allRobots.add(robot);
                }catch (Exception e){
                    System.out.println("Problem with parsing the robot's JSON");
                }

            }
        }
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
    public Collection<Robot> Robots(){
        return allRobots;
    }


}
