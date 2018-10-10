Author: Xiaoning Guo
Contact: Xguo24@u.rochester.edu

CSC172 Project 4: Street Mapping


	Synopsis:

		In this lab, I was given three datasets in the form of .txt files which were in increasing magnitude of 
		content. The objective of this assignment was to use the data structures that we built in class and apply
		them to efficient algorithms that can compute a large number of information due to the way they scale.
		Dijkstra's Algorithm was used to find the shortest path between two points and 
		Kruskal's Algorithm was used to find the minimal spanning tree of the sets. 

		How the code works:
		
			1. Reads the .txt dataset, parses each entree and enters them into hashmaps. 
			   There is a hashmap for roads and hashmap for intersections for fast retrieval.
			   The Vertex class contains a linkedList to store all roads associated with it.	
			   The hashmap/linkedlist combo acts like an adjacency list.

			2. Shortest weighted path. Uses Dijkstra's Algorithm with a heap to find the minimum
			   unknown distance vertex. For each vertex, the current shortest path is constantly updated
			   until it can finally be known. An empty heap means that all the vertexes are known and 
			   all shortest weighted path distances are known.

			3. Minimal Weight Spanning Tree. Uses Kruskal's Algorithm with a heap to find the minimum
			   road distance after every pass. An empty heap means there are no more roads to be exhausted.	
			   A disjoint set of Vertices (intersections) was used to determine cycles. If two vertices
			   had different union identities, then they would merge together and be unioned.

	Bonus/Notable Features: 
		
		*I incorporated a zoom feature into the graphics. Left-click doubles the zoom in. Right-click does the opposite.
		The difficulty was figuring out how to handle panning transformations which was resolved using the following equations: 

			zoom*(x - centerX) + (zoom - 1)*maxWidth/2
			centerX += (mouseX - maxWidth/2)/zoom

		What this does is it refocuses the center to the location of the mouse and compensates for the 
		magnification of x and y.

	Notable Obstacles:

		Dijkstra: A sequential search for minimal distance was highly inefficient at O(n) search time which made
		it impossible for NYS to load. This was solved using a heap at O(1) search time.
		
		Heapifying the heap at every level proved to be too intensive. I realized that you only 
		needed to bubbleup when the minimum distance is determined. I stored the index number in each vertex
		so bubbleup would only take O(1) search time and O(log n) bubble up time as opposed to O(n) search time.
		
		MWST: The problem with disjoint sets is that you need to change all the values of a group of vertices 
		in order to union two sets. I turned the Vertex class itself into a union parent to build a forest of 
		disjoint sets. 

	Runtime Analysis:

		Reading the data: There are n intersections and m roads. 
				  Each insertion takes O(1) time as it only takes a single step to parse and insert into hashmap.
				  O(V + E) 
				  = O(n) runtime.

		Plotting the Graph: There are E number of edges.
				    Access time for each edge is O(1).
				    Calculations are O(1).
				    Drawing lines are O(1).
			            Total is O(E * 1) 
				    O(E)
				    = O(n) runtime
	
		Dijkstra's Algorithm: Initial insertion of verticies into heap. 
				      Retrieval of verticies from hashmap takes O(1) time.
				      There are V number of verticies at O(log V) insertion time for bubbling up at worst.
				      Initial insertion takes at worst O(1 * V * log V) = O(V log V) run time.

				      For every unknown vertex, find the least distance.
				      There are on average V/2 number of verticies as it goes from V to 0,
				      because we remove one at each step.
				     	 The removal or deleteMin takes O(log V) time. 
				      	 For every minimum vertex, determine the least distance adjacent node.
					     There are on average E/V nodes adjacent to another. 
					     For all adjacent nodes, determine if it is indeed the shortest path.
					     If it is, then modify its values at O(1) cost.
					     However, we must bubble up at O(log V) cost. Since the heap index is
					     always stored, we do not traverse the heap to find the vertex.
				         The cost of this is O(V/2 * E/V(1 + log V)) or O(E/2 * log V)

				      Overall, Dijkstra's Algorithm is
				      O(V log V + E log V) 
				      O(E log V) where E > V
				      = O(n log n) runtime for shortest path.
 
		Kruskal's Algorithm: Initial insertion of edges into heap. 
				     Retrieval of edges from hashmap takes O(1) time.
				     There are E number of verticies at O(log E) insertion time for bubbling up at worst.
 			             Initial insertion takes at worst O(1 * E * log E) = O(E log E) run time.
	
				     For every unmarked Edge, find the least weight.
				     There are on average E/2 number of edges as it goes from E to 0,
				     because we remove one at each step.
				     	The removal or deleteMin takes O(log E) time. 
				   	The traversal of the union tree takes O(log V) time because the forest
					has at most log V nodes.
					Unioning two disjoint sets takes O(1) as we only add another union node.
				     The cost of this is O(E/2 * (log E + log V*1))
				     
				     Overall, Kruskal's Algorithm is
				     O(E log E) + O(E/2 * log E) 
				     O(E log E)
        			     = O(n log n) runtime for minimum weighted spanning tree.
			             
		
	Files:

		Graph.java/Graph.class contains the main method and where most of the code is.
		Edge.java contains the road information.
		Vertex.java contains the interection information.
		EdgeSet.java is a heap for finding minimal road distance.
		Heap.java is a heap for finding minimal vertex distance.
		LinkedList.java is my own linked list.
		
		
