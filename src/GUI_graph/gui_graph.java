package GUI_graph;//import Point3D;
import GameUtils.Fruit;
import GameUtils.Robot;
import GameUtils.fruits;
import GameUtils.gameFruits;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.*;

import utils.Point3D;
import utils.Range;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class gui_graph extends JFrame implements   ActionListener, MouseListener {
    ///////////////////////////////////////////////////////////////////
    /////////////////////////// FIELDS ////////////////////////////////
    ///////////////////////////////////////////////////////////////////

    private DGraph Graph;
    private Graph_Algo algo;
    private ArrayList<Fruit> fruits_list;
    private ArrayList<GameUtils.Robot> robots_list;


    int width_window;
    int height_window;

    int x_offset = 4000;
    Range world_range_x;
    Range world_range_y;
    Range frame_range_x;
    Range frame_range_y;
    int graph_frame = 20;

    private Image i;
    private Graphics bufferring_graphic;

    game_service gameservice;

    Timer time;
    int delay = 8;

    private final int size_node = 5;


    ///////////////////////////////////////////////////////////////////
    /////////////////////////// Constructors //////////////////////////
    ///////////////////////////////////////////////////////////////////


    public gui_graph()
    {
        super("Catch The Packet");
        initGraph();
    }


    public  gui_graph(graph g,ArrayList<Fruit> fruits_list, ArrayList<GameUtils.Robot> robots_list,  game_service  gameservice){
        super("Truck Manger");
//        if(fruits_list == null) throw Exception("no fruit list added");
        this.fruits_list = fruits_list;
        this.robots_list = robots_list;
        this.gameservice = gameservice;
        algo = new Graph_Algo();
        this.Graph = (DGraph) g;
        algo.init(g);
        initGraph();



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
        frame_range_y = new Range(height_window - 200 , height_window*0.125 - 250.0000000001);

        this.setSize(width_window, height_window);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        // closes the program when clicking on close
        Graph_componnent graph_componnent = new Graph_componnent();
        graph_componnent.setLocation(0,-2000);
        this.add(graph_componnent);
        setVisible(true);
        this.addMouseListener(this);




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
            NumberFormat formatter = new DecimalFormat("#0.0");
            p.drawString(formatter.format(temp_fruit.getVal()), temp_location.ix() ,temp_location.iy() - 2 );


            if (temp_fruit.getType() == fruits.BANANA)
               p.drawImage(banana.getImage(), temp_location.ix(), temp_location.iy(), (int) (width_window * 0.042), (int) (height_window * 0.042), this);
           if (temp_fruit.getType() == fruits.APPLE)
               p.drawImage(apple.getImage(), temp_location.ix(), temp_location.iy(), (int) (width_window * 0.042), (int) (height_window * 0.042), this);

        }
    }



    void draw_npc(Graphics p)
    {
        ImageIcon car = new ImageIcon("assets/car_npc.png");
        Iterator<GameUtils.Robot> robotIterator = robots_list.iterator();
        GameUtils.Robot temp_robot;
        Point3D temp_location;

        while(robotIterator.hasNext())
        {
           temp_robot = robotIterator.next();
           temp_location =  this.world_to_frame(temp_robot.getLocation());
            p.setFont((new Font("Arial", Font.PLAIN, 11)));
            NumberFormat formatter = new DecimalFormat("#0.0");
            p.setColor(Color.GREEN);
            p.drawString(formatter.format(temp_robot.getValue()), temp_location.ix() ,temp_location.iy()  );
            p.setColor(Color.blue);
            p.drawString("id " + temp_robot.getId() , temp_location.ix() ,temp_location.iy()-10 );

            p.drawImage(car.getImage(),temp_location.ix() - car.getIconWidth()/2,temp_location.iy(),(int)(width_window*0.062),(int)(height_window*0.062),this);
        }
    }


    void draw_player(Graphics p)
    {
        ImageIcon car = new ImageIcon("assets/car_npc.png");
        Iterator<GameUtils.Robot> robotIterator = robots_list.iterator();
        GameUtils.Robot temp_robot;
        Point3D temp_location;

        while(robotIterator.hasNext())
        {
            temp_robot = robotIterator.next();
            temp_location =  this.world_to_frame(temp_robot.getLocation());
            p.drawImage(car.getImage(),temp_location.ix() - car.getIconWidth()/2,temp_location.iy() - car.getIconHeight()/2,(int)(width_window*0.082),(int)(height_window*0.072),this);
        }
    }



    @Override
    public void paint(Graphics p) {

            super.paint(p);
            draw_fruit(p);
            draw_npc(p);
            //draw_points(p);

        if(gameservice.timeToEnd()<=0)
        {
            draw_game_over(p);
        }
            p.dispose();

    }

    private void draw_game_over(Graphics p)
    {
        p.setFont((new Font("Arial", Font.BOLD, 23)));
      
        NumberFormat formatter = new DecimalFormat("#0.0");     
        
        
         Iterator<Robot> iterator = robots_list.iterator();
         double max = 0;
         int max_robot = 0;
        Robot temp;
         while (iterator.hasNext())
         {
             temp = iterator.next();

             if(temp.getValue() > max)
             {
                 max = temp.getValue();
                 max_robot = temp.getId();
             }


         }
         
         
         JOptionPane.showMessageDialog(this, "Game Over! \n robot: "+ max_robot + "\n with " + formatter.format(max) + "points");


    }


    public void update_frame(ArrayList<Fruit> fruits_list, ArrayList<GameUtils.Robot> robots_list)
    {
        this.robots_list = robots_list;
        this.fruits_list = fruits_list;
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
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    class Graph_componnent extends JComponent{

       public Graph_componnent()
        {
        }
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            draw_graph(g);

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

    }

}
