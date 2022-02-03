package lab3;

//sources: class notes and course textbook (Weiss)

public class MyQueue {
	
	private ListNode front = null;
	private ListNode back = null;
	
	class ListNode{
		TNode val;
		ListNode next;
		
		public ListNode(TNode element, ListNode node) {
			val = element;
			next = node;
		}
	}
	
	public boolean isEmpty() {
		return (front == null); //isEmpty() is true when front is null
	}
	
	public void enqueue(TNode t){
		ListNode newNode = new ListNode(t, null);
		if (isEmpty()) {
			front = newNode; 
			back = front; //keep adding to the back
		} else {
			back.next = newNode; //update back to be new node
			back = newNode;
		}
	}
	
	public TNode dequeue(){
		if (!isEmpty()) {
			ListNode initial = front;
			front = front.next;
			return initial.val; //return the TNode associated with the original front node
		}
		return null;
	}
}
