/**
 * 
 * @author Xiaoning Guo
 *
 */

public class Edge {
	public String name;
	public final Vertex v, w; // an edge from v to w
	public double weight;
	
	public Edge(String name, Vertex v, Vertex w, double weight) { // your code here }
		this.v = v;
		this.w = w;
		this.weight = weight;
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}