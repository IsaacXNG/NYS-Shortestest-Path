/**
 * @author Xiaoning Guo
 * No lab partner
 * My own implementation of heap
 */

public class Heap{
	Vertex[] array;
	int size;

	public Heap(int capacity){
		array = new Vertex[capacity+1];
		this.size = 0;
	}
	
	public void insert(Vertex item) {
		array[size+1] = item;
		item.heapIndex = size+1;
		size++;
		bubbleUp(size);
	}

	//k is position
	public void bubbleUp(int k){
		if(k==1){return;}
		else{
			if(array[k].distance < array[k/2].distance){
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
			if(array[k].distance > array[2*k].distance || array[k].distance > array[2*k+1].distance){
				if( array[2*k].distance > array[2*k+1].distance ){
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
		Vertex data = array[e1];
		array[e1] = array[e2];
		array[e2] = data;
		array[e1].heapIndex = e1;
		array[e2].heapIndex = e2;
	}

	public boolean isEmpty() {
		if(size==0){
			return true;
		}
		return false;
	}

	public Vertex deleteMin() {
		array[1] = array[size];
		size--;
		bubbleDown(1);
		Vertex data = array[size];
		array[size+1] = null;
		return data;
	}
}
