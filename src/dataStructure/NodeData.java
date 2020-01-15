package dataStructure;

import utils.Point3D;

import java.io.Serializable;
import java.util.Hashtable;

public class NodeData implements node_data, Serializable {


    public static double infinity = Double.POSITIVE_INFINITY;
    String info = "";
    private int key;
    private Point3D position;
    private double weight = 0;
    private int tag = 0;
    public int previous;
    public boolean visited;
    // K - dest_node's key , V - Edge that is directing towards that dest_node
    public Hashtable<Integer, edge_data> adjacency = new Hashtable<Integer, edge_data>();


    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////Constructor/////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    public NodeData (int key){
        position = new Point3D(0,0,0);
        this.key = key;
        this.weight = infinity;
        previous = -1;
        visited = false;
    }

    public NodeData (int key, double weight){
        Point3D p1 = new Point3D(0,0,0);
        this.key = key;
        this.weight = Math.abs(weight);
        visited = false;

    }

    public NodeData(NodeData n){
        this.key = n.key;
        this.weight = n.key;

        this.visited = n.visited;
    }

   public NodeData(int key, double weight, Point3D point3D)
    {
        this.key = key;
        this.weight = weight;
        visited = false;
        this.position = point3D;
    }



      /////////////////////////////////////////////////////////////////////////////////
     /////////////////////////////////  Methods  /////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////



    public Edge getEdgesByKey(int key){
        // key of destination
        return (Edge) adjacency.get(key);
    }


    /**
     * Return the key (id) associated with this node.
     * @return
     */
    @Override
    public int getKey() {
        return this.key;
    }

    /** Return the location (of applicable) of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public Point3D getLocation() {
        return position;
    }
    /** Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(Point3D p) {
        position = p;
    }

    /**
     * return the remark (meta data) associated with this node.
     * @return
     */

    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        weight = w;
    }

    /**
     * return the remark (meta data) associated with this node.
     * @return
     */

    @Override
    public String getInfo() {

        return  info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s
     */
    @Override
    public void setInfo(String s)
    {
        info =s;
    }


    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    @Override
    public int getTag() {
        return tag;
    }


    /**
     * Allow setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {

       tag = t;
    }


}
