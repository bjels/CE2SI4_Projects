package lab3;
import java.util.EmptyStackException;

//sources: class notes and course textbook (Weiss)

public class MyStack {
	
	private ListNode head;
	
	class ListNode{
		TNode val;
		ListNode next;
		
		public ListNode(TNode element, ListNode node) {
			val = element;
			next = node;
		}
	}
	
	public boolean isEmpty() {
		if(head == null) {
			return true;
		} //if root is null, isEmpty will be true
		return false;
	}
	
	public void push(TNode t) {
		head = new ListNode(t,head); 
	}
	
	public void pop() throws EmptyStackException {
		if (!isEmpty()) { 
			head = head.next; //new head is value after popped value
		} else {
			throw new EmptyStackException(); //nothing to pop
		}
	}
	
	public TNode top() {
		if(isEmpty()) {
			return null; //no top to point to
		} else {
			return(head.val); //a top (root) exists
		}
	}
	
	public TNode topAndPop() {
		if(isEmpty()) {
			return null;
		}
		TNode topElement = head.val;
		head = head.next; //the next element is the new head/top; old is popped
		return topElement; //return new top
	}
}
