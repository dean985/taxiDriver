package GameUtils;

import dataStructure.DGraph;
import dataStructure.node_data;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import utils.Point3D;

import java.util.Queue;

public class Robot {

    private int id;
    private double value;
    private Point3D location;
    int current_node;
    int next_node = -1;
    Queue<Integer> path;
    double speed = 0.00009;


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
        Point3D nect_node_location = dGraph.getNode(next_node).getLocation();
        double dist =this.location.distance2D(dGraph.getNode(next_node).getLocation());

        if (this.location.x() >dGraph.getNode(next_node).getLocation().x() ){
            dist*=-1;
        }

        if(dist > 0.00000001)
        {
            this.location.set_x(this.location.x() + speed);


            this.location.set_y(stickToEdge(dGraph.getNode(current_node),dGraph.getNode(next_node), this.location.x()));

        }
        else
        {
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

}
