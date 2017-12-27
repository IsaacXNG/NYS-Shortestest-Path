/**
 * 
 * @author Xiaoning Guo
 * My own implementation of linked list
 * @param <T>
 */
public class LinkedList<T>{
	public int size = 0;
	public MyNode<T> head = null;
	public MyNode<T> tail = null;
	
	public class MyNode<T> {
		public T data;
		public MyNode<T> nextNode;
		
		public MyNode(T data) {
			this.data = data;
		}
	}
		
	//Uses a similar technique to swapping array values
	public void prepend(T e) {
		size++;
		MyNode<T> newnode = new MyNode<T>(e);
		newnode.nextNode = head;
		head = newnode;
		
		//Base case
		if(tail == null){
			tail = new MyNode<T>(e);
			head = tail;
		}
	}
	
	public void append(T e){
		size++;
		//Base case
		if(tail==null){
			tail = new MyNode<T>(e);
			head = tail;
		//Similar to prepend
		}else{
			MyNode<T> newNode = new MyNode<T>(e);
			tail.nextNode = newNode;
			tail = newNode;
		}
	}
	
	public T get(int index){
		try{
			MyNode<T> current = head;
			for (int i = 0; i < index; i++) {
				current = current.nextNode;
			}
			return (T) current.data;
		
		}catch(NullPointerException npe){
			System.out.println("No such element at index " + index);
			return null;
		}
	}
	
}
