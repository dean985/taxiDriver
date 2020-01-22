package gameClient;

import GUI_graph.gui_graph;
import GameUtils.Fruit;
import GameUtils.Robot;
import GameUtils.gameFruits;
import GameUtils.gameRobots;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.*;

import oop_dataStructure.OOP_DGraph;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

public class myGameGUI extends JFrame implements MouseListener
{
    static DGraph dGraph;
    static Timer time;
    static gui_graph guiGraph;
    private static gameFruits game_fruits;
    static gameRobots game_robots;
    static boolean auto_mode = true;
    int scenario;
    game_service  gameservice;


    public myGameGUI(){
       



    }


    public void init(int scenario_num){
        gameservice = Game_Server.getServer(scenario_num);
        scenario = scenario_num;
        String g = gameservice.getGraph();
        OOP_DGraph oopdGraph = new OOP_DGraph();
        oopdGraph.init(g);
        dGraph  = new DGraph(oopdGraph);

        //todo: implement input dialog for boolean auto-mode
        game_robots = new gameRobots(dGraph, gameservice);
        game_fruits = new gameFruits(gameservice, dGraph);
        fruitsToEdges(game_fruits);



    }



    public void drawGraph()
    {
        //// Todo: if have time include intro screen
        guiGraph  = new gui_graph(dGraph, (ArrayList<Fruit>) this.game_fruits.getFruitList(),this.game_robots.Robots(),gameservice);

         //guiGraph  = new gui_graph(dGraph, (ArrayList<Fruit>) this.game_fruits.getFruitList(),this.game_robots.Robots());

    }

    public void update()
    {
        Iterator<Robot> robotsIter = game_robots.RobotsCollection().iterator();

        Robot r;
        while (robotsIter.hasNext())
        {

            r = robotsIter.next();
            gameservice.move();

         if( r.move_to_dest(dGraph))
         {
             //System.out.println("changed node");
           r.setNext_node(nextNode2(this.gameservice,this.game_fruits,dGraph,r.getId(),r.getCurrent_node()));
         }
         //New function for robot
             if(r.robotCollect(dGraph))
             {
                 fruitsToEdges(game_fruits);
             }

        }

       guiGraph.update_frame((ArrayList<Fruit>) game_fruits.getFruitList(),game_robots.Robots());
       // Iterator<Fruit> fruitIter = game_fruits.getFruitList().iterator();
//        while (fruitIter)
//        System.out.println();
    }

    /**
     * This method adds fruit to a list inside each edge.
     * That results in a list for every edge containing the Fruit s on edge
     * @param fruits
     */
    public static void fruitsToEdges(gameFruits fruits){
        ArrayList<Fruit> allFruits = (ArrayList<Fruit>) fruits.getFruitList();

        for (int i = 0 ; i < allFruits.size(); i++){
            Fruit temp = allFruits.get(i);

            Edge e = (Edge) fruits.edgeOfFruit(temp.id);

            e.addFruittoEdge(temp);
        }

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
    public static int nextNodeInferior(game_service game, graph graph, int robot_id, gameFruits fruits, boolean speed, int src){
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
    private static int nextNode2(game_service game, gameFruits fruits, graph graph, int robot_id, int node_src){
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

    /**
     * this method return a node out of (x,y) coordinates from mouse click
     * @param graph
     * @param x
     * @param y
     * @return node key
     */
    private static int clickToNode(graph graph, double x, double y){
        Iterator<node_data> nodes = graph.getV().iterator();
        double EPS = 0.0005;

        while (nodes.hasNext()){
            Point3D point_node = new Point3D(nodes.next().getLocation());
            if( (Math.abs(point_node.y() - y) < EPS) &&  (Math.abs(point_node.x() - x) <= EPS)   ){
                return nodes.next().getKey();
            }
        }
        return -1;
    }






    public int getScenario() {
        return scenario;
    }

    public DGraph getdGraph() {
        return dGraph;
    }

    public  gameRobots getGame_robots() {
        return game_robots;
    }

    public  gameFruits getGame_fruits() {
        return game_fruits;
    }

    
    
    
    
    int Show_dialog_scenerio()
    {
    	boolean isok = false;
    	Object res = JOptionPane.showInputDialog(this,"set scenerio btween 0 - 23");
    	
    	int i =  Integer.parseInt(res.toString());
    	while (!isok)
    	{
    		if(i>23 || i<0)
    		{
    		
    			res = JOptionPane.showInputDialog(this,"set scenerio btween 0 - 23");
    			i = Integer.parseInt(res.toString());
    		}
    		else
    			isok = true;
    	}
    	
    	return i;
    }
    
    int Show_dialog_login()
    {
    	Object res = JOptionPane.showInputDialog(this,"enter id to login");
    	
    	int i =  Integer.parseInt(res.toString());
    	
    	
    	return i;
    }


    public static void main(String[] args) {

        myGameGUI gameGUI = new myGameGUI();
        Game_Server.login(gameGUI.Show_dialog_login());
       try {
    	   
           gameGUI.init(gameGUI. Show_dialog_scenerio());
    	   
       }
       catch (RuntimeException e)
       {
		
    	   JOptionPane.showMessageDialog(gameGUI, e.getMessage());
       }
       
        

        gameGUI.gameservice.startGame();
        gameGUI.drawGraph();

       // gameGUI.gameservice.startGame();
    while (gameGUI.gameservice.timeToEnd() > 0) {

        //System.out.println(gameGUI.gameservice.timeToEnd());
        gameGUI.update();

        guiGraph.repaint();

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
        System.out.println("finished");

    }


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


    }
