package dataStructure;

import GameUtils.Fruit;

import java.io.Serializable;
import java.util.ArrayList;

public class Edge implements edge_data, Serializable {
    int dest;              // Destination Node
    int src;
    double weight;
    String info;
    int tag;

    ArrayList<Fruit> fruits_edge = new ArrayList<>();

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////Constructor/////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    public Edge (int src, int dest){
        this.dest = dest;
        this.src = src;
    }
    public Edge (int src, int dest, double weight){
        this.dest = dest;
        this.src = src;
        this.weight = Math.abs(weight);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////Getters and Setters/////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    /**
     * The id of the source node of this edge.
     *
     * @return
     */
    @Override
    public int getSrc() {

        return this.src;
    }

    /**
     * The id of the destination node of this edge
     *
     * @return
     */
    @Override
    public int getDest() {

        return this.dest;
    }

    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * return the remark (meta data) associated with this edge.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * Allow setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public void setDest(NodeData dest) {

        this.dest = dest.getKey();
    }

    public void setSrc(NodeData src) {
        this.src = src.getKey();
    }

    /**
     * Adds fruit to the edge
     * @param f
     */
    public void addFruittoEdge(Fruit f){
        this.fruits_edge.add(f);
    }


}
