/**
 * 
 * @author Xiaoning Guo
 * No partner
 * 
 */

public class Vertex{
	String name;
	double distance, latitude, longitude;
	boolean known;
	
	int heapIndex; //For heap index tracking for faster access time
	
	//For printing out shortest path
	Vertex path;
	Edge pathEdge;
	
	//For adjacency list
	LinkedList<Edge> edgeList;
	
	//For disjoint set unioning
	Vertex parent;
			
	public Vertex(String name, double latitude, double longitude){
		distance = Graph.INFINITY;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		edgeList = new LinkedList<Edge>();
	}
	
	//Contructor for disjoint sets (this will be a faux vertex for unioning)
	public Vertex(String name){
		this.name = name;
		parent = null;
	}
			
	//Find the disjoint set union identity
	public Vertex root(){
		if(parent==null){
			return null;
		}else if(parent.parent==null){
			return parent;
		}else{
			return parent.root();
		}
	}
	
	public String toString(){
		return name;
	}

}