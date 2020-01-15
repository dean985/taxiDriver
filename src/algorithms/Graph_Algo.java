package algorithms;

import dataStructure.*;
import utils.Point3D;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms{

	public graph dGraph;


	public Graph_Algo ()
	{
		dGraph = new DGraph();
		dGraph.addNode(new NodeData(0));
	}


	public Graph_Algo (graph g)
	{
		init(g);
	}

	/**
	 * Init this set of algorithms on the parameter - graph.
	 * @param g
	 */
	@Override
	public void init(graph g) {

		dGraph = new DGraph((DGraph) g);
	}

	/**
	 * Init a graph from file
	 * @param file_name
	 */
	@Override
	public void init(String file_name)  {
		File f = new File(file_name);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			dGraph = (graph) ois.readObject();
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {

			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}


	}

	/**
	 * get a minimal location of x and y in form of Point3d
	 * @return
	 */
public 	Point3D get_minimal_location()
	{
		Iterator<node_data> iter = 	dGraph.getV().iterator();
		double min_x = Double.POSITIVE_INFINITY;
		double min_y = Double.POSITIVE_INFINITY;

		NodeData n;
		while (iter.hasNext())
		{
			n = (NodeData) iter.next();
			if(n.getLocation().x() < min_x)
				min_x = n.getLocation().x();
			if(n.getLocation().y() < min_y)
				min_y = n.getLocation().y();
		}

		return  new Point3D(min_x - Double.MIN_VALUE,min_y- Double.MIN_VALUE,0);
	}

	public Point3D get_max_location()
	{
		Iterator<node_data> iter = 	dGraph.getV().iterator();
		double max_x = 0;
		double max_y =0;

		NodeData n;
		while (iter.hasNext())
		{
			n = (NodeData) iter.next();
			if(n.getLocation().x() > max_x)
				max_x = n.getLocation().x();
			if(n.getLocation().y() > max_y)
				max_y = n.getLocation().y();
		}

		return  new Point3D(max_x + Double.MIN_VALUE,max_y + Double.MIN_VALUE,0);
	}
	/** Saves the graph to a file.
	 *
	 * @param file_name
	 */
	@Override
	public void save(String file_name) {
		File f = new File(file_name);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(dGraph);
			fos.close();

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}




	private void DFS (DGraph dg,NodeData start)
	{
		start.visited = true;


		Collection<edge_data> edge_list = dg.getE(start.getKey());

		for (edge_data e: edge_list)
		{

			if( !   ((NodeData)((DGraph)dGraph).getNode(e.getDest())).visited  )
			{
					DFS(dg ,   ((NodeData)((DGraph)dGraph).getNode(((Edge)e).getDest()))     );
			}

		}


	}

	/**
	 * Returns true if and only if (iff) there is a valid path from EVREY node to each
	 * other node. NOTE: assume directional graph - a valid path (a-->b) does NOT imply a valid path (b-->a).
	 * @return
	 */

	@Override
	public boolean isConnected() {

		Iterator<node_data> iterG =  dGraph.getV().iterator();

		while (iterG.hasNext())
		{
			node_data node = iterG.next();
			((DGraph)dGraph).connectivity.forEach((k,v)->{
				((NodeData)v).visited = false;
			});
			DFS((DGraph) dGraph,(NodeData) node);

			for (node_data _node : dGraph.getV())
			{

					if(!((NodeData)_node).visited)
					{
						return false;
					}

			}
			
		}
		return true;
	}


	/**
	 * returns the length of the shortest path between src to dest
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public double shortestPathDist(int src, int dest) {

	    NodeData [] nodeData = new NodeData[dGraph.getV().size()];

        for (int i = 0; i <dGraph.getV().size(); i++) {

			dGraph.getNode(i).setWeight(Double.POSITIVE_INFINITY);
            nodeData[i] = (NodeData) dGraph.getNode(i);
        }
        Dijkstra ds = new Dijkstra(nodeData, src);
        ds.computePaths();

		if (dGraph.getNode(dest).getWeight() == Double.POSITIVE_INFINITY) {
			throw new RuntimeException("the graph may not been connected");
		}
        return dGraph.getNode(dest).getWeight();
	}


	/**
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if(src==dest)return null;

		Collection<node_data> node_dataCollection = dGraph.getV();
		Iterator<node_data> node_dataIterator = node_dataCollection.iterator();
		NodeData temp;
		NodeData [] nodeData = new NodeData[dGraph.getV().size()];// = (NodeData[]) dGraph.getV().toArray();

		for (int i = 0 ; node_dataIterator.hasNext(); i++) {
			 temp =(NodeData) node_dataIterator.next();
				temp.setWeight(Double.POSITIVE_INFINITY);
				temp.visited = false;
				nodeData[i] =  temp;


		}

		Dijkstra ds = new Dijkstra(nodeData, src);
		ds.computePaths();

		if (dGraph.getNode(dest).getWeight() == Double.POSITIVE_INFINITY) {
			throw new RuntimeException("the graph may not been connected");
		}

		return ds.getPath(dest,(DGraph) dGraph);
	}


	/**
	 * computes a relatively short path which visit each node in the targets List.
	 * Note: this is NOT the classical traveling salesman problem,
	 * as you can visit a node more than once, and there is no need to return to source node -
	 * just a simple path going over all nodes in the list.
	 * @param targets
	 * @return
	 */
	@Override
	public List<node_data> TSP(List<Integer> targets) {

		ArrayList<node_data> path = new ArrayList<node_data>();
		NodeData src;
		boolean finished = false;

		Collection edge_list;
		Iterator<edge_data> edge_dataIterator;

		if(!this.isConnected())
			return path;

		for (int i = 0; i < targets.size() && !finished; i++)
		{
			src = (NodeData) dGraph.getNode(targets.get(i));
			path.add(src);
			edge_list	=  dGraph.getE(src.getKey());
				edge_dataIterator = edge_list.iterator();

			for (int j = 0; edge_dataIterator.hasNext() && !finished; j++)
				{
					int dest_temp = edge_dataIterator.next().getDest();

							src = (NodeData) dGraph.getNode(dest_temp);
							j = 0;
							edge_list	=  dGraph.getE(src.getKey());
							edge_dataIterator = edge_list.iterator();
							path.add(src);

					int counter = 0;
					for (int k = 0; k < targets.size() ; k++)
					{
						if(path.contains(dGraph.getNode(targets.get(k))))
						{
							counter ++;
							//path.clear();
							//finished = false;
							//break;
						}
					}
					if(counter == targets.size())
					{
						finished = true;
					}
				}

		}

		return path;
	}


	/**
	 * Compute a deep copy of this graph.
	 * @return
	 */
	@Override
	public graph copy() {
		graph g = new DGraph((DGraph) dGraph);
		return g;
	}
	
}
