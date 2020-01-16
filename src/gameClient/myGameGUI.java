package gameClient;

import GUI_graph.gui_graph;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.graph;
import oop_dataStructure.OOP_DGraph;

public class myGameGUI
{
    static DGraph dGraph;

    public static void Gameinit(int scenario_num)
    {
        game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
        String g = game.getGraph();
        OOP_DGraph oopdGraph = new OOP_DGraph();

        oopdGraph.init(g);

        dGraph  = new DGraph(oopdGraph);
    }

    public static void drawGraph()
    {

        gui_graph guiGraph = new gui_graph(dGraph);

    }

    public static void main(String[] args) {
        Gameinit(0);
        drawGraph();
    }
}