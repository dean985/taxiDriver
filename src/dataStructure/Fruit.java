package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

public class Fruit {
    private fruits type;                //Type of fruit - Banana or Apple
    private double val;                 //Value of fruit
    private Point3D location;           //Location of fruit


    /**
     * Constructor of Fruit
     * @param json_fruit
     * Receives a JSONObject that has the fields : value, type and location
     * double value - points that you can gain
     * String pos - will be parsed from this pattern "x,y,z" - making a Point3D from it
     * int type - apple is 1, while banana is 0
     * @throws JSONException
     */

    public Fruit(JSONObject json_fruit) throws JSONException {
        //Value
        val = json_fruit.getDouble("value");
        int type = json_fruit.getInt("type");

        //Type  - BANANA:-1  , APPLE:0 TOCHANGE!!!!!!!!!!!!!!!!!!!!!!
        if(type == -1){
            this.type = fruits.BANANA;
        }else if (type == 0){
            this.type = fruits.APPLE;
        }

        //Location
        String pos = json_fruit.getString("pos");
        String[] point =pos.split(",");
        double x = Double.parseDouble(point[0]);
        double y = Double.parseDouble(point[1]);
        location = new Point3D(x,y,0);

    }

    /**
     * This method returns the type of a fruit
     * @return type
     */
    public fruits getType() {
        return type;
    }

    /**
     * This method is used to modify the type of the fruit
     * @param type
     */
    public void setType(fruits type) {
        this.type = type;
    }

    /**
     * This method returns the value of a fruit
     * @return value
     */
    public double getVal() {
        return val;
    }

    /**
     * This method is used to modify the value of the fruit
     * @param val
     */
    public void setVal(double val) {
        this.val = val;
    }

    /**
     * This method returns the location of the fruit
     * @return
     */
    public Point3D getLocation() {
        return location;
    }

    /**
     * This method is used to modify the location of the fruit
     * @param location
     */
    public void setLocation(Point3D location) {
        this.location = location;
    }
}
