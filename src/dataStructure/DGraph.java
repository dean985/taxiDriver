package dataStructure;

import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_node_data;
import oop_elements.OOP_Edge;
import oop_elements.OOP_NodeData;
import utils.Point3D;

import java.io.Serializable;
import java.util.*;

public class DGraph implements graph, Serializable {
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////Fields//////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	int changes = 0;	// For MC
	private int N;		//Number of nodes in graph
	private int E;		// Number of edges in graph
	public Hashtable<Integer, node_data> connectivity=
			new Hashtable<Integer, node_data>();
	// Hashtable: <key, value>
	// key - node.key
	// value - node that has that key.

	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////Constructor/////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////

	public DGraph(){
		N = 0;
		E = 0;
		connectivity.clear();
	};



	public DGraph(int n){
		if(n < 0) throw new IllegalArgumentException("number of nodes should be non-negative ");
		this.N = n;
		this.E = 0;
		for (int i =0; i< n; i++){
			NodeData node = new NodeData(i);
			connectivity.put(node.getKey(), node);
		}
	}

	public DGraph(OOP_DGraph oopDGraph)
	{
		//this.N = oopDGraph.nodeSize();
		//this.E = oopDGraph.edgeSize();
		DGraph thisDgraph = this;

		Iterator<oop_node_data> iterator = oopDGraph.getV().iterator();
		while (iterator.hasNext())
		{
			OOP_NodeData temp =(OOP_NodeData) iterator.next();
			thisDgraph.addNode(temp.getLocation().x(),temp.getLocation().y(),temp.getKey(),temp.getWeight());

			Iterator<oop_edge_data> oop_edge_dataIterator = oopDGraph.getE(temp.getKey()).iterator();
			while (oop_edge_dataIterator.hasNext())
			{
				OOP_Edge oopEdge = (OOP_Edge) oop_edge_dataIterator.next();
				((NodeData)thisDgraph.getNode(temp.getKey())).adjacency.put(oopEdge.getDest(),new Edge(oopEdge.getSrc(),oopEdge.getDest(),oopEdge.getWeight()));

			}
		}





	}
	public DGraph(DGraph g)
	{
		this.N = g.N;
		this.E = g.E;
		DGraph thisDgraph = this;
		g.connectivity.forEach((k,v)->{
			thisDgraph.connectivity.put(k,v);
		});
	}

	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////Methods ////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////

	/**
	 * return the node_data by the node_id,
	 * @param key - the node_id
	 * @return the node_data by the node_id, null if none.
	 */
	@Override
	public node_data getNode(int key) {
		return connectivity.get(key);

	}

	/**
	 * return the data of the edge (src,dest), null if none.
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		NodeData src_node = (NodeData) connectivity.get(src);
		NodeData dest_node = (NodeData) connectivity.get(dest);
		if (dest_node == null || src_node == null){
			throw new IllegalArgumentException("Source or Destination isn't found");
		}
		return src_node.getEdgesByKey(dest);
	}

	/**
	 * add a new node to the graph with the given node_data.
	 * Note: this method should run in O(1) time.
	 * @param n
	 */
	@Override
	public void addNode(node_data n) {
		this.connectivity.put(n.getKey(),(NodeData) n);
		this.N ++;
		changes++;
	}

	/**
	 * the same as addNode but with x and y value
	 * @param x
	 * @param y
	 * @param destination_key
	 * @param weight
	 */
	public void addNode (double x , double y, int destination_key, double weight) {
		// User must enter a node key as a destination to the newly added node
		// That node key must be of an existing node in the graph
		if (!this.connectivity.containsKey(destination_key)){
			//Adds a node without connections
			Point3D p = new Point3D(x,y,0);
			NodeData n1 = new NodeData(this.connectivity.size(), weight, p);
			this.addNode(n1);
			return;

		}
		NodeData n = new NodeData(this.connectivity.size());
		n.setLocation(new Point3D(x,y,0));
		n.setWeight(weight);

		this.addNode(n);
		this.connect(n.getKey(), destination_key, weight );
	}


	/**
	 * Connect an edge with weight w between node src to node dest.
	 * * Note: this method should run in O(1) time.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */
	@Override
	public void connect(int src, int dest, double w) {

		if (this.getNode(dest)== null){
			throw new IllegalArgumentException("Source key not found");
		}
		if (this.getNode(dest) == null){
			throw new IllegalArgumentException("dest key not found: " + dest);
		}

		Edge e = new Edge (src, dest, w);
		node_data destNode = this.connectivity.get(dest);
		node_data srcNode = this.connectivity.get(src);
		((NodeData)srcNode).adjacency.put(dest, e);
		this.E++;
		changes++;
	}
	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph.
	 * Note: this method should run in O(1) time.
	 * @return Collection<node_data>
	 */
	@Override
	public Collection<node_data> getV() {
		return this.connectivity.values();
	}
	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the edges getting out of
	 * the given node (all the edges starting (source) at the given node).
	 * Note: this method should run in O(1) time.
	 * @return Collection<edge_data>
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		// returns all edges that goes from node_id
		Collection<edge_data> e = ((NodeData)this.connectivity.get(node_id)).adjacency.values();
		return e;
	}

	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(n), |V|=n, as all the edges should be removed.
	 * @return the data of the removed node (null if none).
	 * @param key
	 */
	@Override
	public node_data removeNode(int key) {
		if (this.connectivity.containsKey(key)){
			//remove the node
			NodeData nr = (NodeData) connectivity.remove(key);
			// run on each node and remove the key in each adjacency so we wont hear from that node again
			connectivity.forEach((i,n) -> {
				edge_data e_deleted = ((NodeData)n).adjacency.remove(key);
				if (e_deleted != null){
					E--;
				}
			} );
			N--;
			changes++;
			return nr;
		}
		return null;
	}

	/**
	 * Delete the edge from the graph,
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return the data of the removed edge (null if none).
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		if (this.connectivity.containsKey(src) && this.connectivity.containsKey(dest) ){
			edge_data er = ((NodeData)connectivity.get(src)).adjacency.remove(dest);
			E--;
			changes++;
			return er;
		}
		return null;
	}

	/**
	 * clear the graph
	 */
	public void clearGraph(){
		int i = 0;
		while (this.connectivity.size() != 0){
			this.removeNode(i);
			i++;
		}
	}
	/** return the number of vertices (nodes) in the graph.
	 * Note: this method should run in O(1) time.
	 * @return
	 */
	@Override
	public int nodeSize() {
		//return this.connectivity.size();
		return N;
	}

	/**
	 * return the number of edges (assume directional graph).
	 * Note: this method should run in O(1) time.
	 * @return
	 */

	@Override
	public int edgeSize() {
		return E;
	}

	/**
	 * return the Mode Count - for testing changes in the graph.
	 * @return
	 */
	@Override
	public int getMC() {

		return changes;
	}
}
