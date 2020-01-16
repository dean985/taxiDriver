package GUI_graph;//import Point3D;
import algorithms.Graph_Algo;
import dataStructure.*;
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
import java.util.List;

public class gui_graph extends JFrame implements  MenuListener, ActionListener, MouseListener {
    ///////////////////////////////////////////////////////////////////
    /////////////////////////// FIELDS ////////////////////////////////
    ///////////////////////////////////////////////////////////////////

    private DGraph Graph;
    private Graph_Algo algo;

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

    public  gui_graph(graph g){
        super("Truck Manger");

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

         width_window = 1000;
         height_window = 800;
        world_range_x = new Range(algo.get_minimal_location().x(),algo.get_max_location().x());
        world_range_y = new Range(algo.get_minimal_location().y(),algo.get_max_location().y());

        frame_range_x  = new Range(50.0000000001 ,width_window - width_window/3 );
//        frame_range_y = new Range(200.0000000001,height_window - height_window/2.00000000000000001);
        frame_range_y = new Range(height_window - 200 , height_window/6 - 200.0000000001);

        this.setSize(width_window, height_window);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        // closes the program when clicking on close
        MenuBar menuBar = new MenuBar();

        Menu menu = new Menu("Menu");
        Menu menu_algo = new Menu("Algorithms");

        menuBar.add(menu);
        menuBar.add(menu_algo);

        this.setMenuBar(menuBar);

        //Menu
        MenuItem item1 = new MenuItem("Load Graph");
        item1.addActionListener(this);

        MenuItem item2 = new MenuItem("Save Graph");
        item2.addActionListener(this);

        MenuItem item6 = new MenuItem("Custom Graph");
        item6.addActionListener(this);


        // Algorithms
        MenuItem item3 = new MenuItem("Is Connected");
        item3.addActionListener(this);

        MenuItem item4 = new MenuItem("Shortest Path");
        item4.addActionListener(this);

        MenuItem item5 = new MenuItem("TSP");
        item5.addActionListener(this);

        menu.add(item1);
        menu.add(item2);
        menu.add(item6);
        menu_algo.add(item3);
        menu_algo.add(item4);
        menu_algo.add(item5);

        setVisible(true);
        this.addMouseListener(this);


    }

    void draw_fruit(Graphics p)
    {
        ImageIcon ii = new ImageIcon("assets/car.png");
        JLabel lable = new JLabel(ii);
        p.drawImage(ii.getImage(),width_window/2,height_window/2,1,1,this);


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
    public void paint(Graphics p) {
        super.paint(p);


        Point3D p_n;
        Point3D p0;
        Point3D p1;
        p.setFont((new Font("Arial", Font.BOLD, 18)));
//        ImageIcon ii = new ImageIcon("data/A0.png");
//        JLabel lable = new JLabel(ii);
//        p.drawImage(ii.getImage(),0,0,width_window,height_window - 100,this);

        draw_fruit(p);
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

               // p.drawString(x0+", "+y0+" ," + x1+", "+ y1,x0,y0 );

                NumberFormat formatter = new DecimalFormat("#0.0");         // format strings to be with only one digit after the decimal point

               // p.drawString(""+ formatter.format(edge.getWeight()), (x0 + (4*x1)) / 5,(y0 + (4 * y1))/5);
                p.setColor(Color.YELLOW);
                p.fillRect((7*x1 +x0)/8 - size_node/2 , (7*y1 +y0)/8 -size_node/2, size_node, size_node);


            }
        }

//
//        if(is_connected_on && connected) {
//            p.setColor(Color.BLACK);
//            p.drawString("graph is connected", 250, 80);
//            is_connected_on = false;
//        }
//        else if(is_connected_on && !connected) {
//            p.setColor(Color.BLACK);
//            p.drawString("graph is not connected", 250, 80);
//            is_connected_on = false;
//        }
//
//        if(shortest_path) {
//            p.setColor(Color.BLACK);
//            p.drawString("Click on the Source node, then click on the Destination node:", 40, 80);
//            p.drawString("The Shortest path between them will be marked with cyan", 40, 100);
//
//            if(targets.size() == 2) {
//                algo.init(Graph);
//                System.out.println(targets.get(0).getKey()+"  , "+targets.get(1).getKey());
//                List<node_data> SP_ans = algo.shortestPath(targets.get(0).getKey(), targets.get(1).getKey());
//                System.out.println(SP_ans.toString());
//
//                p.setColor(Color.CYAN);
//
//                for(int i = 0; SP_ans != null && i<SP_ans.size()-1 ; i++) {
//                    node_data n0 = SP_ans.get(i);
//                    node_data n1 = SP_ans.get(i+1);
//                    p0 = n0.getLocation();
//                    p1 = n1.getLocation();
//
//                    p.drawLine(p0.ix()-size_node, p0.iy()-size_node, p1.ix(), p1.iy());
//                }
//
//                double sum = algo.shortestPathDist(targets.get(0).getKey(), targets.get(1).getKey());
//                p.setColor(Color.BLACK);
//                p.drawString("length of the shortest path between "+targets.get(0).getKey()+" and "+targets.get(1).getKey()+" is: "+String.format("%.1f", sum), 100, 120);
//                shortest_path = false;
//            }
//        }
//
//        if (custom_graph){
//            p.setColor(Color.BLUE);
//            p.drawString("Click to add a new node. Clicking on two nodes will create an edge ", 40,80 );
//
//        }
//        if(tsp) {
//            p.setColor(Color.BLACK);
//            p.drawString("Click on the nodes, when you finish click on the orange button ", 50, 100);
//            p.drawString("Answer is in gray", 50, 500);
//
//
//            //tsp button
//            p.setColor(Color.ORANGE);
//            p.drawRect(50, 130, 30, 30);
//            if(tsp_rec) {
//                p.setColor(Color.GRAY);
//                algo.init(Graph);
//
//                List<Integer> nodesKeys = new ArrayList<Integer>();
//                for(int i =0 ; i<targets.size(); i++)
//                    nodesKeys.add(targets.get(i).getKey());
//
//                List<node_data> tsp_ans =  algo.TSP(nodesKeys);
//                String path = "";
//                for(int i = 0; tsp_ans != null && i<tsp_ans.size()-1 ; i++) {
//                    node_data n1 = tsp_ans.get(i);
//                    node_data n2 = tsp_ans.get(i+1);
//                    path += n1.getKey()+">";
//                    p1 = n1.getLocation();
//                    Point3D p2 = n2.getLocation();
//                    p.drawLine(p1.ix()-size_node, p1.iy()-size_node, p2.ix(), p2.iy());
//
//                }
//                p.setColor(Color.BLACK);
//                if(tsp_ans!=null) {
//                    path += tsp_ans.get(tsp_ans.size() -1).getKey();
//                    p.drawString("The TSP path is: "+path, 50, 140);
//                } else {
//                    p.drawString("There is no Path that goes through all the selected nodes. ", 100, 140);
//                }
//
//                tsp_rec = false;
//                tsp = false;
//            }
//        }
//        repaint();
    }





    @Override
    public void actionPerformed(ActionEvent e) {
      /*  String str = e.getActionCommand();

        if (str.equals("Save Graph")) {
            FileDialog saver = new FileDialog(this, "Save your Graph", FileDialog.SAVE);
            saver.setVisible(true);
            String filename = saver.getFile();  //file name
            String path = saver.getDirectory(); //path of the newly created file
            System.out.println(path+filename);
            if(filename!=null) {                //  save the algorithms of the graph
                algo.init(Graph);
                algo.save(path + filename +".txt");
            }
        } else if (str.equals("Load Graph")) {
            FileDialog loader = new FileDialog(this, "Load your Graph", FileDialog.LOAD);
            loader.setVisible(true);
            String filename = loader.getFile();     //filename
            String path = loader.getDirectory();    //path of the created file


            if(filename != null) {              // loading graph
                clearData();
                algo.init(path + filename);
                Graph = (DGraph) algo.copy();

                repaint();
            }

        }else if (str.equals("Custom Graph")){
            this.clearData();
            Graph = new DGraph();
            custom_graph= true;
            repaint();
        }

        else if(str.equals("Is Connected")) {
            this.clearData();
            is_connected_on = true;
            algo.init(Graph);
            connected = algo.isConnected();
            repaint();


        } else if(str.equals("Shortest Path")) {
            this.clearData();
            shortest_path = true;
            repaint();
        } else if(str.equals("TSP")) {
            clearData();
            tsp = true;
            repaint();
        }*/

    }

    public void clearData() {
        tsp = false;
        is_connected_on = false;
        connected = false;
        tsp_rec = false;
        custom_graph = false;
        shortest_path = false;

        targets.clear();
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
