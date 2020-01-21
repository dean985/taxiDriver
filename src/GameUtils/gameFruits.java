package GameUtils;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.*;
import org.json.JSONObject;
import utils.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class gameFruits {

    ArrayList<Fruit> allFruits = new ArrayList<>();
    graph Graph;

    /**
     * Initialize fruits for game
     * @param game
     * @param graph
     */
    public gameFruits(game_service game, graph graph){
        int j = 0;
        this.Graph = graph;

        Iterator<String> fruitsIter = game.getFruits().iterator();

        while (fruitsIter.hasNext()){
            try{
                JSONObject jsonLine = new JSONObject(fruitsIter.next());
                JSONObject fruit = jsonLine.getJSONObject("Fruit");

                Fruit newFruit = new Fruit(fruit);
                newFruit.setId(j);

                allFruits.add(newFruit);

                j++;

            }
            catch(Exception e){
                throw new RuntimeException("Something is wrong in gameFruits input");
            }


        }
    }


    ///////////////////////// Methods

    /**
     * Return all the fruits in the game
     * @return collection of fruits
     */

    public Collection<Fruit> getFruitList(){
        return this.allFruits;
    }

    /**
     * This method return the fruit by it's ID
     * @param  n
     * @return Fruit
     */
    public Fruit getFruitbyID(int n){
        Iterator<Fruit> FruitIter = allFruits.iterator() ;
        while (FruitIter.hasNext()){
            Fruit f = FruitIter.next();
            if (f.getId() == n) {
                return f;
            }
        }
        System.out.println("Problem with fetching fruit by ID");
        return null;
    }

    /**
     * Return the edge of a fruit
     * @param fruit_id
     * @return edge_data
     */
    public edge_data edgeOfFruit(int fruit_id){
        Fruit f = getFruitbyID(fruit_id);
        Iterator<node_data> nodesIter = this.Graph.getV().iterator();

        while(nodesIter.hasNext()){
            node_data node = nodesIter.next();
            Iterator<edge_data> edges = Graph.getE(node.getKey()).iterator();

            while(edges.hasNext()){
                edge_data e = edges.next();

                node_data src_node = Graph.getNode(e.getSrc());
                node_data dst_node = Graph.getNode(e.getDest());

                double sum = (src_node.getLocation().x() + dst_node.getLocation().x());

                double sideA = f.getLocation().distance2D(dst_node.getLocation());
                double sideB = src_node.getLocation().distance2D( f.getLocation());

               double sumSides = sideA + sideB;
               double totalDistance = src_node.getLocation().distance2D(dst_node.getLocation());

                if (Math.abs(sumSides - totalDistance) <= 0.00001){
                    return e;
                }

            }
        }
        System.out.println("Problem with getting the edge for fruits");
        return null;
    }

    /**
     *This method gets the nearest fruit to a node in a simple way. If fast mode is enabled, algorithm for
     * shortest path is enabled, otherwise only compare coordinates in the graph.
     *
     *       if fast mode AND [Shortest Path  (Source node , Destination Node of closest fruit)
     *          is bigger then Shortest Path (Source node , The current destination node of fruit's edge) ]
     *          then ----> The closest fruit is the latter.
     *
     *       Else if [Distance of nearest fruit from the node is bigger then Distance of current fruit to source]
     *       Then take the return current fruit
     * @param fast
     * @param src
     * @return nearest fruit
     */
    public Fruit getnearFruit(boolean fast, int src){
        Iterator<Fruit> fruitsIter = allFruits.iterator();
        Graph_Algo algorithms = new Graph_Algo(Graph);
        node_data src_node = Graph.getNode(src);


        if (fruitsIter.hasNext()){

            Fruit near = fruitsIter.next();
            edge_data near_e = edgeOfFruit(near.getId());

            while (fruitsIter.hasNext()){
                Fruit current_fruit = fruitsIter.next();
                edge_data current_edge = edgeOfFruit(current_fruit.getId());

                double dist1 = algorithms.shortestPathDist(src, near_e.getDest());
                double dist2 = algorithms.shortestPathDist(src, current_edge.getDest());

                    if (fast   && (dist1 > dist2) ){
                        near = current_fruit;
                    }else{
                        double nearFToSrc = near.getLocation().distance2D(src_node.getLocation());
                        double currentFToSrc = current_fruit.getLocation().distance2D(src_node.getLocation());
                        if (currentFToSrc < nearFToSrc){
                            near = current_fruit;
                        }
                    }

                }
            return near;
            }
        return null;
        }


    public Fruit MinFruit(){
        Iterator<Fruit> iter = allFruits.iterator();
        Fruit minValue = iter.next();
        if (iter.hasNext()){
            while (iter.hasNext()){
                if (minValue.getVal() > iter.next().getVal()){
                    minValue = iter.next();
                }
            }
        }
        return minValue;
    }

    public Fruit MaxFruit(){
        Iterator<Fruit> iter = allFruits.iterator();
        Fruit maxValue = iter.next();
        Fruit temp;
        if (iter.hasNext()) {

            while (iter.hasNext()){
                temp = iter.next();
                if (maxValue.getVal() < temp.getVal()){
                    maxValue = temp;
                }
            }
        }
        return maxValue;
    }





}
