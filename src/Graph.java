import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * 
 * @author Xiaoning Guo
 * No partner
 * 
 * Contains the main method
 * 
 */

public class Graph{
		JFrame frame;
		Canvas canvas = new Canvas();
		final static double INFINITY = Double.MAX_VALUE;
		final static int R = 6371; // Radius of the earth
		
		HashMap<String, Vertex> adjMap;
		HashMap<String, Edge> edgeMap;
		Heap minHeap;
	
		static double maxLat = 0, minLat = 0, maxLong = 0, minLong = 0;
		static String i1, i2;
		
		static boolean show, meridianMap;
		
		public static void main(String args[]){
			if(args.length == 0)
				eclipseManual();
			else
				commandLine(args);
		}
		
		//Use for eclipse IDE
		public static void eclipseManual(){
			String s = "nys"; //Change this value for accessing different datasets
			Graph g = createFromFile(s);
			
//			g.shortestPath("GOERGEN-ATHLETIC","CSB");
//			g.shortestPath("i50", "i310000");
			g.minimumSpanningTree();
		
			g.drawGraphics();
		}
		
		public static void commandLine(String args[]){

			for(int i = 0; i < args.length; i++){
				if(args[i].contains("show")){
					show = true;
				}else if(args[i].contains("ridian")){
					meridianMap = true;
				}else if(args[i].contains("irectio")){
					i1 = args[i+1].replace("[", "");
					i2 = args[i+2].replace("]", "");
				}
			}
			
			Graph g = createFromFile(args[0]);
			if(meridianMap)
				g.minimumSpanningTree();
			if(i1!=null)
				g.shortestPath(i1, i2);
			if(show)
				g.drawGraphics();
		}
		
		public static Graph createFromFile(String filename){
			
			try{
				
				//This just checks if the file is accidentally named input.txt which would be inputted as input.txt.txt
				FileReader inputFile = null;
				File check = new File(filename); if(check.exists()){inputFile = new FileReader(filename);}
				File check2 = new File(filename+".txt");if(check2.exists()){inputFile = new FileReader(filename+".txt");}
				File check3 = new File(filename+".txt.txt");if(check3.exists()){inputFile = new FileReader(filename+".txt.txt");}
				
				if(inputFile!=null){
					
						Graph g = new Graph(filename);
						BufferedReader in = new BufferedReader(inputFile);
						double latitude, longitude, distance = 0;
						boolean firstOne = true;
					
						String line;
						String[] s;
						
						while ((line = in.readLine()) != null){
							
							s = line.split("\\s+");
							
							if(s[0].equals("i")){
								latitude = Double.parseDouble(s[2]);
								longitude = Double.parseDouble(s[3]);
								
								g.insertVertex(new Vertex(s[1], latitude, longitude));
								
								if(firstOne){
									maxLat=latitude;minLat=latitude;maxLong=longitude;minLong=longitude;
									firstOne = false;
									continue;
								}else{
									if(latitude>maxLat)
										maxLat = latitude;
									if(latitude<minLat)
										minLat = latitude;
									if(longitude>maxLong)
										maxLong = longitude;
									if(longitude<minLong)
										minLong = longitude;
								}
							}else{
								distance = distance(g.adjMap.get(s[2]).latitude, g.adjMap.get(s[3]).latitude,
												g.adjMap.get(s[2]).longitude, g.adjMap.get(s[3]).longitude); 
								g.insertEdge(new Edge(s[1], g.adjMap.get(s[2]), g.adjMap.get(s[3]), distance));
							}
						}
					
						in.close();
						return g;
				}else{
					System.out.println("Cannot load file. Make sure the name is correct or the input file is in the right location.");
					return null;
				}
				

			}catch(FileNotFoundException e){
				System.out.println("This file cannot be found. \nMake sure you typed the correct directory.\n"
						+ "Sometimes files named Input.txt are actually Input.txt.txt.");
				return null;
			}catch(NumberFormatException e){
				System.out.println("Text format is not correct");
				return null;
			}catch(IOException e){
				System.out.println("Input/output exception occurred");
				return null;
			}

		}
		
		public Graph(String map){ 
			adjMap = new HashMap<String, Vertex>();
			edgeMap = new HashMap<String, Edge>();
		}
	
		public void insertVertex(Vertex v){
			adjMap.put(v.name, v);
		}
		
		public void insertEdge(Edge e){
			edgeMap.put(e.name, e);
			e.v.edgeList.prepend(e);
			e.w.edgeList.prepend(e);
		}

		/**
		 * My implementation of Dijksha's Algorithm 
		 * Based off of pseudocode from Weiss
		 * 
		 * Major difference is that I use my own heap (priority queue)
		 * to find the shortest distance unknown vertex.
		 * 
		 */
		public void shortestPath(String n1, String n2){
			boolean found = false;
			Vertex i1 = adjMap.get(n1);
			Vertex i2 = adjMap.get(n2);
		
			if(i1==null || i2==null){
				System.out.printf("Intersection %s or %s does not exist.\n", n1, n2);
				return;
			} 
			
			i1.distance = 0;
			minHeap = new Heap(adjMap.size()+(int)(adjMap.size()*0.05));
			
			for(Vertex v: adjMap.values()){
				minHeap.insert(v);
			}
			
			Vertex min;
			Vertex w;
			
			while(!minHeap.isEmpty() && !found){
				min = minHeap.array[1];
				minHeap.deleteMin();
				min.known = true;
			
				for(int i = 0; i < min.edgeList.size; i++){
					if(min.edgeList.get(i).v==min){
						w = min.edgeList.get(i).w;
					}else{
						w = min.edgeList.get(i).v;
					}
					
					if(!w.known){
						double dist = min.edgeList.get(i).weight;
						if(min.distance + dist < w.distance){
							w.distance = min.distance + dist;
							w.pathEdge = min.edgeList.get(i);
							w.path = min;
							minHeap.bubbleUp(w.heapIndex); //Updates the minimum unknown vertex distance
						}
					}	
					
					if(w==i2){
						found = true;
					}
				}
				
				min = null;
			}	
			
			//Degenerate cases
			if(i2.distance==INFINITY){
				System.out.println(i2 + " is not connected to " + i1 + ".");
				return;
			}
			if(i1==i2){
				System.out.println("You are already here.");
				return;
			}
			
			
			//Prints the output
			LinkedList<Vertex> path = new LinkedList<>();
			Vertex current = i2;
			while(current.path!=null){
				path.prepend(current.path);
				roadMap.append(current.pathEdge);
				current = current.path;
			}
			
			System.out.println("Start at " + path.get(0));
			for(int i=1; i<path.size; i++){
				System.out.println("Get to " + path.get(i) + " through road " + path.get(i).pathEdge + ".");
			}
			System.out.println("Get to " + i2 + " through road " + i2.pathEdge + ". This is the final destination.");
			System.out.println("Total distance to travel: " + i2.distance + " miles.");
		}
		
		/** @author Xiaoning Guo (me)
		 * Implementation of Kruskal's Algorithm for minimum spanning tree
		 * Uses a heap to find the minimum road weight in O(1) runtime
		 * Deletemin has O(log n) runtime
		 * Uses disjoint sets and chaining to merge trees of the same group to prevent cycles.
		 * Unioning only takes O(1) runtime while reaching the union identity for the group requires O(log n) traversal
		 * 
		 * Total runtime is O(n1/2) * [ O(log n1) + c*O(log n2) ] = O(n log n)
		 * 
		 */
		public void minimumSpanningTree(){
			EdgeSet edgeSet = new EdgeSet(edgeMap.size());
			for(Edge e: edgeMap.values()){
				edgeSet.insert(e);
			}
			
			Edge e;
			Vertex root1;
			Vertex root2;
			
			while(!edgeSet.isEmpty()){
				e = edgeSet.array[1];
				edgeSet.deleteMin();
				
				root1 = e.v.root();
				root2 = e.w.root();
				
				//Base case where all vertexes are their own disjoint sets marked by null
				if(e.v.parent == null && e.w.parent == null){
					Vertex root = new Vertex(e.v.name);
					e.v.parent = e.w.parent = root;
					roadMap.append(e);
				}
				//Cases where a group merges with a group of 1
				else if(e.v.parent == null){
					e.v.parent = root2;
					roadMap.append(e);
				}else if(e.w.parent == null){
					e.w.parent = root1;
					roadMap.append(e);
				}
				//Case where the vertexes belong in different groups so not cycles
				else if(root1 != root2){
					Vertex root = new Vertex(e.v.name);
					root1.parent = root2.parent = root;
					roadMap.append(e);
				}
			}	
			
			System.out.println("These are the roads you must take for minimal spanning path:");
			for(Edge e2: edgeMap.values()){
				System.out.println(e2);
			}
		}
		
		
		
		/**
		 * Code modified from http://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi
		 * 
		 * Calculate distance between two points in latitude and longitude. Uses Haversine method as its base.
		 * 
		 * lat1, lon1 Start point lat2, lon2 
		 * 
		 * Removed height
		 * @returns Distance in Miles
		 */
		public static double distance(double lat1, double lat2, double lon1, double lon2){

		    double latDistance = Math.toRadians(lat2 - lat1);
		    double lonDistance = Math.toRadians(lon2 - lon1);
		    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
		            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
		            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		    double distance = R * c * 0.621371; // convert km to miles
		    return distance;
		}
		
	
	public void drawGraphics(){
		frame = new JFrame("Map");
		frame.add(canvas);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double w = screenSize.getWidth()/2;
		double h = screenSize.getHeight()/2;
		frame.setBounds((int)(w) - 750/2, (int)(h - 700/2), 750, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Collection of roads needed to be painted.
	 * Zoom in by left clicking, zoom out by right clicking.
	 */
	LinkedList<Edge> roadMap = new LinkedList<Edge>();
	
	class Canvas extends JComponent implements MouseListener{
		Timer timer;
		boolean running; //User input from mouse
		double zoom = 1.0;
		double centerX = 0, centerY = 0;
		
		public Canvas(){
			super();
			addMouseListener(this);
		}
		
		public void paintComponent(Graphics g){
			Graphics2D g2 = (Graphics2D) g;
           
			g2.setStroke(new BasicStroke(1));
			g.setColor(Color.BLACK);
			
			for(Edge e: edgeMap.values()){
				int x1 = (int)((-minLong + e.v.longitude)*getWidth()/(maxLong - minLong));
				int x2 = (int)((-minLong + e.w.longitude)*getWidth()/(maxLong - minLong));
				int y1 = (int)((maxLat - e.v.latitude)*getHeight()/(maxLat - minLat));
				int y2 = (int)((maxLat - e.w.latitude)*getHeight()/(maxLat - minLat));
				
				x1 = (int)(zoom*(x1-centerX) - (zoom - 1)*getWidth()/(2));
				x2 = (int)(zoom*(x2-centerX) - (zoom - 1)*getWidth()/(2));
				y1 = (int)(zoom*(y1-centerY) - (zoom - 1)*getHeight()/(2));
				y2 = (int)(zoom*(y2-centerY) - (zoom - 1)*getHeight()/(2));
				
				g.drawLine(x1, y1, x2, y2);
			}
			
			g.drawString("zoom: "+zoom + "x", 12, 15);
			g2.setStroke(new BasicStroke(2));
			g.setColor(Color.RED);
			
			//Loops through the list itself so it is O(n) for the entire roadMap. Otherwise it would be O(n^2)
			for(LinkedList<Edge>.MyNode<Edge> current = roadMap.head; current!=null; current = current.nextNode){
				int x1 = (int)((-minLong + current.data.v.longitude)*getWidth()/(maxLong - minLong));
				int x2 = (int)((-minLong + current.data.w.longitude)*getWidth()/(maxLong - minLong));
				int y1 = (int)((maxLat - current.data.v.latitude)*getHeight()/(maxLat - minLat));
				int y2 = (int)((maxLat - current.data.w.latitude)*getHeight()/(maxLat - minLat));
				
				x1 = (int)(zoom*(x1-centerX) - (zoom - 1)*getWidth()/(2));
				x2 = (int)(zoom*(x2-centerX) - (zoom - 1)*getWidth()/(2));
				y1 = (int)(zoom*(y1-centerY) - (zoom - 1)*getHeight()/(2));
				y2 = (int)(zoom*(y2-centerY) - (zoom - 1)*getHeight()/(2));
			
				g.drawLine(x1, y1, x2, y2);
			}
			
		}
		
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == 1){
				centerX += (e.getX() - getWidth()/2)/zoom;
				centerY += (e.getY() - getHeight()/2)/zoom;
				zoom *= 2;
			}else{
				zoom /= 2;
				centerX += (e.getX() - getWidth()/2)/zoom;
				centerY += (e.getY() - getHeight()/2)/zoom;
				if(zoom<=1){
					zoom = 1;
					centerX = 0;
					centerY = 0;
				}
			}
			repaint();
		}

		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {running = true;}
		public void mouseExited(MouseEvent e) {running = false;}
	}
}
