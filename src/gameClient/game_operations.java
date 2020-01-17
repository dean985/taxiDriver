package gameClient;

import Server.game_service;
import dataStructure.graph;

public interface game_operations {
    /**
     * This interface will sum up all operations that should be supported
     * on the gui
     *
     * Robot operations
     * Fruit operations
     *
     */

    public void initRobots(game_service game, graph gr);

    public void initFruits(game_service game, graph gr);

    

}
