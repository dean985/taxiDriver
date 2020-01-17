package GUI_graph;//import Point3D;
import algorithms.Graph_Algo;
import dataStructure.*;
import dataStructure.Robot;
import utils.Point3D;
import utils.Range;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class gui_graph extends JFrame implements  MenuListener, ActionListener, MouseListener {
    ///////////////////////////////////////////////////////////////////
    /////////////////////////// FIELDS ////////////////////////////////
    ///////////////////////////////////////////////////////////////////

    private DGraph Graph;
    private Graph_Algo algo;
    private ArrayList<Fruit> fruits_list;
    private ArrayList<Robot> robots_list;

    int width_window;
    int height_window;

    int x_offset = 4000;
    Range world_range_x;
    Range world_range_y;
    Range frame_range_x;
    Range frame_range_y;

    private boolean custom_graph = false;       // for user drawing mode
    private final int size_node = 5;

    private boolean tsp = false;
    private boolean tsp_rec = false;

    private boolean connected = false;
    private boolean is_connected_on = false;
    private boolean shortest_path = false;

    private ArrayList<node_data> targets = new ArrayList<node_data>();          // for input of nodes from user
    ///////////////////////////////////////////////////////////////////
    /////////////////////////// Constructors //////////////////////////
    ///////////////////////////////////////////////////////////////////


    public gui_graph()
    {
        super("Catch The Packet");
        initGraph();
    }


    public  gui_graph(graph g,ArrayList<Fruit> fruits_list, ArrayList<Robot> robots_list){
        super("Truck Manger");
//        if(fruits_list == null) throw Exception("no fruit list added");
        this.fruits_list = fruits_list;
        this.robots_list = robots_list;
        algo = new Graph_Algo();
        this.Graph = (DGraph) g;
        algo.init(g);
        initGraph();
        //repaint();
    }
    ///////////////////////////////////////////////////////////////////
    /////////////////////////// METHODS ///////////////////////////////
    ///////////////////////////////////////////////////////////////////
    public void initGraph(){

        width_window = 900;
        height_window = 800;
        world_range_x = new Range(algo.get_minimal_location().x(),algo.get_max_location().x());
        world_range_y = new Range(algo.get_minimal_location().y(),algo.get_max_location().y());

        frame_range_x  = new Range(30.0000000001 ,width_window - width_window*0.25 );
        frame_range_y = new Range(height_window - 150 , height_window*0.125 - 200.0000000001);

        this.setSize(width_window, height_window);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        // closes the program when clicking on close
        setVisible(true);
        this.addMouseListener(this);


    }

    void draw_graph(Graphics p)
    {

        Point3D p_n;
        Point3D p0;
        Point3D p1;
        p.setFont((new Font("Arial", Font.BOLD, 18)));


        for (node_data n: Graph.getV()){
            p.setColor(Color.BLACK);
            p_n = n.getLocation();
            p_n = world_to_frame(p_n);
            p.fillOval(p_n.ix()- (size_node/2), p_n.iy() - (size_node/2), size_node, size_node);
            //p.drawString(p_n.y()+", "+p_n.x(),p_n.ix() + 50,p_n.iy());


              p.drawString(""+n.getKey(), p_n.ix() ,p_n.iy()  );
            for( edge_data edge : Graph.getE(n.getKey())){
                // paint edges of each node in the graph
                p.setColor(Color.BLUE);
                node_data n0 = Graph.getNode(edge.getSrc());
                node_data n1 = Graph.getNode(edge.getDest());
                p0 = n0.getLocation();
                p1 = n1.getLocation();
                p0 = world_to_frame(p0);
                p1 = world_to_frame(p1);
                int x0 = p0.ix();
                int y0 =p0.iy();
                int x1 =p1.ix();
                int y1 =p1.iy();

                p.drawLine(x0, y0,x1, y1);


                NumberFormat formatter = new DecimalFormat("#0.0");         // format strings to be with only one digit after the decimal point
                p.setColor(Color.YELLOW);
                p.fillRect((7*x1 +x0)/8 - size_node/2 , (7*y1 +y0)/8 -size_node/2, size_node, size_node);


            }

        }
    }
    void draw_fruit(Graphics p)
    {
        ImageIcon apple = new ImageIcon("assets/red_box.png");
        ImageIcon banana = new ImageIcon("assets/yellow_box.png");

        Fruit temp_fruit;
        Point3D temp_location;
        Iterator<Fruit> fruitsIterator = fruits_list.iterator();

        while (fruitsIterator.hasNext())
        {
           temp_fruit = fruitsIterator.next();
           temp_location =  this.world_to_frame(temp_fruit.getLocation());

            if(temp_fruit.getType() == fruits.BANANA)
                p.drawImage(banana.getImage(),temp_location.ix(),temp_location.iy()-20,(int)(width_window*0.042),(int)(height_window*0.042),this);
            if(temp_fruit.getType() == fruits.APPLE)
                p.drawImage(apple.getImage(),temp_location.ix(),temp_location.iy()-20,(int)(width_window*0.042),(int)(height_window*0.042),this);
        }
    }

    void draw_npc(Graphics p)
    {
        ImageIcon car = new ImageIcon("assets/car_npc.png");
        Iterator<Robot> robotIterator = robots_list.iterator();
        Robot temp_robot;
        Point3D temp_location;
        /*
        temp stuff
         */
        int x_p =127, y_p = 128;

        while(robotIterator.hasNext())
        {
           temp_robot = robotIterator.next();
          //  temp_location =  this.world_to_frame(temp_robot.);

            p.drawImage(car.getImage(),x_p,y_p,(int)(width_window*0.082),(int)(height_window*0.072),this);

            x_p += 2;
        }
    }



    @Override
    public void paint(Graphics p) {
        super.paint(p);

        draw_graph(p);
        draw_fruit(p);
        draw_npc(p);
    }

    void update_frame()
    {

    }

    Point3D world_to_frame(Point3D p)
    {
        double x = world_range_x.proportional_point(p.x());
        double y = world_range_y.proportional_point(p.y());
        Point3D new_point = new Point3D(frame_range_x.form_proportion(x),frame_range_y.form_proportion(y),0);

        if(new_point.x() > width_window)
            new_point.set_x(width_window);
        if(new_point.x() < 0)
            new_point.set_x(0);

        if(new_point.y() < 0)
            new_point.set_y(0);
        if(new_point.y() > height_window)
            new_point.set_y(height_window);

        return new_point;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void clearData() {
        tsp = false;
        is_connected_on = false;
        connected = false;
        tsp_rec = false;
        custom_graph = false;
        shortest_path = false;

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
      /*  //Now x and y are the coordinates of the mouse click
        int x = e.getX();
        int y = e.getY();



        if(tsp && x>50 && x<80 && y>130 && y<160) {
            tsp_rec = true;
            repaint();

        }

        node_data user_node = null;
        Point3D point_temp = new Point3D(x,y);


        double max_dist = 100000;
        double min_dist = 2*size_node;

        Collection<node_data> nodes = Graph.getV();
        Iterator<node_data> itr_nodes = nodes.iterator();
        while(itr_nodes.hasNext()) {
            node_data n = itr_nodes.next();
            Point3D p = n.getLocation();
            double dist = point_temp.distance2D(p);
            if(dist<min_dist && dist<max_dist) {
                max_dist = dist;
                user_node = n;
            }
        }
        if(custom_graph && user_node == null) {
            NodeData new_n = new NodeData(Graph.getV().size() , 0, new Point3D(x,y,0));
            Graph.addNode(new_n);
            targets.clear();
        }
        if(user_node!=null && !targets.contains(user_node)){
            targets.add(user_node);
        }

        if(  targets.size() == 2 && custom_graph) {
            node_data begin = targets.get(0);
            node_data end = targets.get(1);
            double w;
            try {
                w = Double.parseDouble(JOptionPane.showInputDialog("Enter weight for the edge "+begin.getKey()+"-->"+end.getKey()+":"));
            } catch (Exception e1) {        // if input isn't a double, just choose a double for user in range [0,30)
                w = Math.random()*30;
            }
            Graph.connect(begin.getKey(), end.getKey(), w);
            targets.clear();
        }
        repaint();*/
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void menuSelected(MenuEvent menuEvent) {

    }

    @Override
    public void menuDeselected(MenuEvent menuEvent) {

    }

    @Override
    public void menuCanceled(MenuEvent menuEvent) {

    }



}
