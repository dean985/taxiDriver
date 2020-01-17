package dataStructure;

import netscape.javascript.JSObject;
import utils.Point3D;

public class Robot {

    private int id;
    private double value;
    private Point3D location;
    int current_node;
    int next_node = -1;

    public Robot(){
        ;
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
}
