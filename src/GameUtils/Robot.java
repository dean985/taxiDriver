package GameUtils;

import dataStructure.*;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import utils.Point3D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Robot {

    private int id;
    private double value;
    private Point3D location;
    int current_node;
    int next_node = -1;
    Queue<Integer> path = new LinkedList<>();
    double speed = 0.00001;


    public Robot (int id, Point3D p){
        this.value = 0;
        this.id = id;
        this.location = p;
        this.current_node = 0;
        next_node = current_node;
    }
    public Robot (int id, Point3D p,int current_node){
        this.value = 0;
        this.id = id;
        this.location = p;
        this.current_node = current_node;
        next_node = current_node;
    }


    public int getId() {
        return id;
    }

    public int getCurrent_node() {
       return current_node;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Point3D getLocation() {
        return location;
    }

    public void setLocation(Point3D location) {
        this.location = location;

    }



    public void setNext_node(int next_node)
    {
       this. next_node = next_node;
    }


    public int getNext_node() {
        return next_node;
    }


    /**
     * move the robot on the graph to the next_node and tell you if you got there yet
     * @param dGraph
     * @return
     */
    public boolean move_to_dest(DGraph dGraph)
    {
        int back = 1;
        Point3D nect_node_location = dGraph.getNode(next_node).getLocation();
        double dist =this.location.distance2D(dGraph.getNode(next_node).getLocation());


        if(dist > 0.00009)
        {
            if (this.location.x() > dGraph.getNode(next_node).getLocation().x() ){
                  back = -1;
             }
//            this.location.set_x(this.location.x() + speed*back);
//            this.location.set_y(stickToEdge(dGraph.getNode(current_node),dGraph.getNode(next_node), this.location.x()));

            double x = this.location.x() + speed*back;
            double y = stickToEdge(dGraph.getNode(current_node),dGraph.getNode(next_node), x);

            this.setLocation(new Point3D(x,y));
        }
        else
        {
            this.path.add(current_node);
            this.current_node = this.next_node;

           return true;
        }

        return false;
    }

    public void setPath(Queue<Integer> path) {
        this.path = path;
    }

    public double stickToEdge(node_data n1, node_data n2, double x){
        double x0 = n1.getLocation().x();
        double y0 = n1.getLocation().y();
        double x1 = n2.getLocation().x();
        double y1 = n2.getLocation().y();

        double m = (y1-y0)/(x1-x0);
        double n = y1 - m*x1;

        return (m*x + n);
    }

    /**
     * This method return the edge the robot is currently on
     * @param graph
     * @return Edge of robot
     */
    public edge_data edgeOfRobot( graph graph){
        Edge e = (Edge) graph.getEdge(this.current_node, this.next_node);
        return e;
    }

    /**
     * check if robot collects fruit
     * add value to robot
     * @param graph
     */
    public boolean robotCollect(graph graph  ){
        boolean collectedsomthing = false;
        double EPS = 0.0009;//0.00008
        Edge e = (Edge) this.edgeOfRobot(graph);
        ArrayList<Fruit> fruitEdge = e.getFruits();
        Iterator<Fruit> fruitIter = fruitEdge.iterator();

        while(fruitIter.hasNext()){
//            System.out.println("start robotCollect() while");

            Fruit current_fruit = fruitIter.next();
           // System.out.println(current_fruit.getLocation().distance2D(this.location) );
            if (current_fruit.getLocation().distance2D(this.location) <= EPS){
                this.value+= current_fruit.getVal();
                current_fruit.replaceFruit(graph);
                collectedsomthing = true;

            }
        }
        return collectedsomthing;
    }
}
