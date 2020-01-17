package dataStructure;

import netscape.javascript.JSObject;
import utils.Point3D;

public class Robot {

    private int id;
    private double value;
    private Point3D location;

    public Robot(){
        ;
    }

    public Robot (int id, Point3D p){
        this.value = 0;
        this.id = id;
        this.location = p;
    }

    public int getId() {
        return id;
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
}
