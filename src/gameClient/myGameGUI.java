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
import utils.KML_Logger;
import utils.Point3D;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import static GameUtils.Robot.addTimeStamp;

public class myGameGUI extends JFrame implements MouseListener, Runnable
{
    static DGraph dGraph;
    static Timer time;
    static gui_graph guiGraph;
    private static gameFruits game_fruits;
    static gameRobots game_robots;
    static boolean auto_mode = true;
    private static myGameGUI gameGUI;
    int scenario;
    static game_service  gameservice;



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
        guiGraph  = new gui_graph(dGraph, (ArrayList<Fruit>) this.game_fruits.getFruitList(),this.game_robots.Robots(),gameservice);
         //guiGraph  = new gui_graph(dGraph, (ArrayList<Fruit>) this.game_fruits.getFruitList(),this.game_robots.Robots());

    }

    public void update()
    {
        game_robots.moveRobots(gameservice, dGraph, game_fruits );
        game_fruits.updateFruit(gameservice, dGraph);

       guiGraph.update_frame((ArrayList<Fruit>) game_fruits.getFruitList(),game_robots.Robots());

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



    private void draw_game_over(Graphics p)
    {
        p.setFont((new Font("Arial", Font.BOLD, 23)));

        NumberFormat formatter = new DecimalFormat("#0.0");



      JOptionPane.showMessageDialog(this, "Game Over! \n robot: "+ " points");


    }



    int Show_dialog_scenerio()
    {
    	boolean isok = false;
    	Object res = JOptionPane.showInputDialog(this,"set scenerio btween 0 - 23");
    	
    	int i =  Integer.parseInt(res.toString());
//    	while (!isok)
//    	{
//    		if(i>23 || i<0)
//    		{
//
//    			res = JOptionPane.showInputDialog(this,"set scenerio btween 0 - 23");
//    			i = Integer.parseInt(res.toString());
//    		}
//    		else
//    			isok = true;
//    	}
//
    	return i;
    }
    
    int Show_dialog_login()
    {
    	Object res = JOptionPane.showInputDialog(this,"enter id to login");
    	
    	int i =  Integer.parseInt(res.toString());
    	
    	
    	return i;
    }

    public static void addTimePath(int time){
    ///// Robots
        Iterator<Robot> robIter = game_robots.Robots().iterator();
        while (robIter.hasNext()){
            Robot r = robIter.next();
            addTimeStamp(r,r.getLocation(), time);
        }
    ///// Fruits
        Iterator<Fruit> fruitIter = game_fruits.getFruitList().iterator();
        while (fruitIter.hasNext()){
            Fruit fr = fruitIter.next();
            Fruit.addToPathFruits(fr, fr.getLocation(), time);
        }
    }



    public static void main(String[] args) {

        gameGUI = new myGameGUI();
        Thread client = new Thread(gameGUI);
        client.start();

    }


    @Override
    public void run() {

        Game_Server.login(gameGUI.Show_dialog_login());
        try {
            gameGUI.init(gameGUI. Show_dialog_scenerio());
        }
        catch (RuntimeException e){
            JOptionPane.showMessageDialog(gameGUI, e.getMessage());
        }

        gameGUI.gameservice.startGame();
        gameGUI.drawGraph();

        System.out.println(gameGUI.gameservice.toString());
        while (gameGUI.gameservice.isRunning()) {
            int j = 128;                                // This number should be an even number.
            // The bigger it is, the more timestamps you get
            int time = (int)gameGUI.gameservice.timeToEnd();
            if ( time % j == 0){
                addTimePath( time);
            }


            //System.out.println(gameGUI.gameservice.timeToEnd());
            gameGUI.update();
            guiGraph.repaint();

            try {
                Thread.sleep(90);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        KML_Logger kml = new KML_Logger(gameGUI);

        String kmlString = null;
        try {
            kmlString = Ex4_Client.cat("data\\sencerio_"+gameGUI.scenario+".kml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameservice.sendKML(kmlString);
        gameGUI.draw_game_over(guiGraph.getGraphics());


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
