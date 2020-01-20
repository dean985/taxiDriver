package GameUtils;

import dataStructure.DGraph;
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
    double speed = 2;


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
     * move the robot on the graph to the node and tell you if you got there yet
     * @param dGraph
     * @return
     */
    public boolean move_to_dest(DGraph dGraph)
    {
        Point3D nect_node_location = dGraph.getNode(next_node).getLocation();
        if(this.location.distance3D(dGraph.getNode(next_node).getLocation()) > 0)
        {
            if(this.location.x() > nect_node_location.x())
            {
                this.location.set_x(this.location.x() - speed);
            }
            else
            {
                this.location.set_x(this.location.x() + speed);
            }

            if(this.location.y() > nect_node_location.y())
            {
                this.location.set_y(this.location.y() - speed);
            }
            else
            {
                this.location.set_y(this.location.y() + speed);
            }
        }
        else
        {
           return true;
        }

        return false;
    }
}
