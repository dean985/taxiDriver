package gameClient;

import GUI_graph.gui_graph;
import Server.Game_Server;
import Server.game_service;
import dataStructure.*;
import dataStructure.Robot;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xml.sax.helpers.AttributesImpl;
import utils.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class myGameGUI extends JFrame
{
    static DGraph dGraph;
    static ArrayList<Fruit> fruits_list;
    static ArrayList<Robot> robots_list;
    static Timer time;
    static gui_graph guiGraph;

    public static void Gameinit(int scenario_num)
    {
        game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
        /// Read the Graph ///////
        String g = game.getGraph();
        OOP_DGraph oopdGraph = new OOP_DGraph();
        oopdGraph.init(g);
        dGraph  = new DGraph(oopdGraph);
        g = game.toString();
        JSONObject line = null;


        try {
            line = new JSONObject(g);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /////// Read the fruit //////
        fruits_list = new ArrayList<>();
        Iterator<String> f_iter = game.getFruits().iterator();
        while(f_iter.hasNext()) {

            try {
                fruits_list.add(new Fruit(new JSONObject(f_iter.next()).getJSONObject("Fruit")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        robots_list =  new ArrayList<>();
        JSONObject ttt = null;
        try {
            ttt = line.getJSONObject("GameServer");

            int rs = ttt.getInt("robots");
            int src_node = 0;  // arbitrary node, you should start at one of the fruits
            for(int a = 0;a<rs;a++) {
                game.addRobot(src_node+a);
                robots_list.add(new Robot(a, dGraph.getNode(src_node+a).getLocation(),src_node+a));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static void drawGraph()
    {
        //// Todo: if have time include intro screen

         guiGraph  = new gui_graph(dGraph,fruits_list,robots_list);

    }

    public static void update()
    {

        Iterator<Robot> robotIterator = robots_list.iterator();
        Robot r;
        while (robotIterator.hasNext())
        {

            r = robotIterator.next();

            if( 0 == dGraph.getNode(r.getNext_node()).getLocation().distance3D(r.getLocation()))
                r.setNext_node(dGraph.getNode(nextNode(dGraph,r.getCurrent_node())).getKey());
            else
            {
                r.setLocation(new Point3D(r.getLocation().x()+0.00025,r.getLocation().y(),0));
                ///move the Robot function
            }

        }

       guiGraph.update_frame(fruits_list,robots_list);
    }

    private static int nextNode(DGraph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }


    public static void main(String[] args) {

        Gameinit(5);

                drawGraph();


    while (true) {

        update();

        guiGraph.repaint();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    }
}
