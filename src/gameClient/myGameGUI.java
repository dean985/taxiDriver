package gameClient;

import GUI_graph.gui_graph;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.graph;
import oop_dataStructure.OOP_DGraph;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class myGameGUI
{
    static DGraph dGraph;
    static ArrayList<Fruit> fruits_list;
    static ArrayList<Robot> robots_list;

    public static void Gameinit(int scenario_num)
    {
        game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
        /// Read the Graph ///////
        String g = game.getGraph();
        OOP_DGraph oopdGraph = new OOP_DGraph();
        oopdGraph.init(g);
        dGraph  = new DGraph(oopdGraph);

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

        robots_list = new ArrayList<>();
        robots_list.add(new Robot());
        robots_list.add(new Robot());
        robots_list.add(new Robot());robots_list.add(new Robot());
        //int rs = game.getRobots();






    }

    public static void drawGraph()
    {

        gui_graph guiGraph = new gui_graph(dGraph,fruits_list,robots_list);


    }

    public static void main(String[] args) {
        Gameinit(16);
        drawGraph();
    }
}
