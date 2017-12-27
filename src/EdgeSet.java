/**
 * 
 * @author Xiaoning Guo
 * This is just a heap with a few differences from the one I use for vertexes
 *
 */


public class EdgeSet{
	Edge[] array;
	int size;

	public EdgeSet(int capacity){
		array = new Edge[capacity+1];
		this.size = 0;
	}
	
	public void insert(Edge item) {
		array[size+1] = item;
		size++;
		bubbleUp(size);
	}
	
	//k is position
	public void bubbleUp(int k){
		if(k==1){return;}
		else{
			if(array[k].weight < array[k/2].weight){
				swap(k, k/2);
				bubbleUp(k/2);
			}else{
				return;
			}
		}
	}
	
	//k is position
	public void bubbleDown(int k){
		if(2*k+1>size){return;}
		else{
			if(array[k].weight > array[2*k].weight || array[k].weight > array[2*k+1].weight){
				if( array[2*k].weight > array[2*k+1].weight ){
					swap(2*k+1, k);
					bubbleDown(2*k+1);
				}else{
					swap(2*k, k);
					bubbleDown(2*k);
				}
			}else{
				return;
			}
		}
	}
	
	public void swap(int e1, int e2){
		Edge data = array[e1];
		array[e1] = array[e2];
		array[e2] = data;
	}

	public boolean isEmpty() {
		if(size==0){
			return true;
		}
		return false;
	}

	public Edge deleteMin() {
		array[1] = array[size];
		size--;
		bubbleDown(1);
		Edge data = array[size];
		array[size+1] = null;
		return data;
	}

}
