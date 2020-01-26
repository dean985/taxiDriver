package gameClient;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import oop_utils.OOP_Point3D;
import utils.KML_Logger;

import javax.swing.*;

/**
* This class represents a simple example for using the GameServer API:
* the main file performs the following tasks:
* 0. login as a user ("999") for testing - do use your ID.
* 1. Creates a game_service [0,23] (user "999" has stage 9, can play in scenarios [0,9] not above
*    Note: you can also choose -1 for debug (allowing a 600 second game).
* 2. Constructs the graph from JSON String
* 3. Gets the scenario JSON String 
* 5. Add a set of robots  // note: in general a list of robots should be added
* 6. Starts game 
* 7. Main loop (vary simple thread)
* 8. move the robot along the current edge 
* 9. direct to the next edge (if on a node) 
* 10. prints the game results (after "game over"), and write a KML: 
*     Note: will NOT work on case -1 (debug).
*  
* @author boaz.benmoshe
*
*/
public class Ex4_Client implements Runnable{
	static int k = 0;
	public static void main(String[] a) {
		Thread client = new Thread(new Ex4_Client());
		client.start();
	}
	
	@Override
	public void run() {
	/*	myGameGUI gameGui = new myGameGUI();

		int scenario_num = 0;
		int id = 204652416;
		Game_Server.login(id);

		gameGui.init(scenario_num);
		gameGui.gameservice.startGame();

		while (gameGui.gameservice.timeToEnd() > 0){
			int j = 128;
			int time = (int)gameGui.gameservice.timeToEnd();

			if ( time % j == 0){
				gameGui.addTimePath(time);
			}
			gameGui.update();
			try{
				Thread.sleep(1);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		KML_Logger kml = new KML_Logger(gameGui);
		String remark = "";
		try {
			 remark = cat("C:\\Dean\\CS\\OOP\\assignment\\taxiDriver\\data\\sencerio_0.kml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String res = gameGui.gameservice.toString();
		System.out.println(res);

*/



		int scenario_num = 0; // current "stage is 9, can play[0,9], can NOT 10 or above
		int id = 313534133;
		Game_Server.login(id);
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games

		String g = game.getGraph();
		List<String> fruits = game.getFruits();
		OOP_DGraph gg = new OOP_DGraph();
		gg.init(g);
		init(game);

		game.startGame();
		int ind=0;
		long dt=200;
		int jj = 0;
		while(game.isRunning()) {
			moveRobots(game, gg);
			try{
				Thread.sleep(10000);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
			try {
				List<String> stat = game.getRobots();
				for(int i=0;i<stat.size();i++) {
					System.out.println(jj+") "+stat.get(i));
				}
				ind++;
				Thread.sleep(dt);
				jj++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}



		String res = game.toString();
		String remark = "This string should be a KML file!!";
		game.sendKML(remark); // Should be your KML (will not work on case -1).
		System.out.println(res);
	}

	/**
	 * This method is returning a file as a string. Same function as cat command in bash
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static String cat(String filename) throws IOException{
		final String EOL = System.getProperty("line.separator");

		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			String nextLine = "";
			StringBuilder sb = new StringBuilder();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine);
				sb.append(EOL);
			}
			return sb.toString();
		}
		finally {
			if (br != null) br.close();
			if (fr != null) fr.close();
		}


	}



	/** 
	 * Moves each of the robots along the edge, 
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveRobots(game_service game, oop_graph gg) {
		List<String> log = game.move();
		ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();



		List<String> fs =  game.getFruits();


				if(log!=null) {
			long t = game.timeToEnd();
			
			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					String p = ttt.getString("pos");
					OOP_Point3D pp = new OOP_Point3D(p);
					rs.add(pp);
					double speed =  ttt.getInt("speed");
								
					if(dest==-1) {			
						dest = nextNode(gg, src);
						if(k == 20) {
							//game.chooseNextEdge(rid, dest);
							k = 0;
						}
			//			System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(oop_graph g, int src) {
		int ans = -1;
		Collection<oop_edge_data> ee = g.getE(src);
		Iterator<oop_edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}
	private void init(game_service game) {
		
		String g = game.getGraph();
		List<String> fruits = game.getFruits();
		OOP_DGraph gg = new OOP_DGraph();
		gg.init(g);

		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			System.out.println(info);
			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();
			while(f_iter.hasNext()) {System.out.println(f_iter.next());}	
			int src_node = 0;  // arbitrary node, you should start at one of the fruits
			for(int a = 0;a<rs;a++) {
				game.addRobot(a);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		
	}
}
