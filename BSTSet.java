package lab3;

public class BSTSet {
	private TNode root;

	public BSTSet() {
		root = null; // initializes BSTSet object to represent the empty set
	}

	public BSTSet(int[] input) {
		for (int i = 0; i < input.length; i++) {
			add(input[i]); // add new node to the tree; duplicates accounted for in add()
		}
	}

	public TNode getRoot() {
		return root;
	}

	public boolean isIn(int v) {

		TNode current = root;

		while (current != null) { //traverse through tree
			if (v == current.element) { //found the element; search is successful
				return true;
			} else if (v > current.element) { //keep looking
				current = current.right;
			} else {  //keep looking
				current = current.left;
			}
		}
		return false;
	}

	public void add(int v) {
		if (root == null) {
			root = new TNode(v, null, null); // empty set; add a new root
		}

		TNode current = root; // initialize current node to first node in the tree

		while (current != null) {
			if (v < current.element) {
				if (current.left != null) {
					current = current.left; // can't insert here, occupied; keep going
				} else {
					TNode left = new TNode(v, null, null);
					current.left = left; // insert node with value v as a left child of current
					break; // done insertion
				}
			} else if (v > current.element) {
				if (current.right != null) {
					current = current.right; // can't insert here, occupied; keep going
				} else {
					TNode right = new TNode(v, null, null);
					current.right = right; // insert node with value v as a right child of current
					break; // done insertion
				}
			} else {
				break; // don't insert anything
			}
		}
	}

	public boolean remove(int v) {
		TNode current = root;
		TNode parent = null;

		while (current != null)
			if (v == current.element) {
				if (current.left == null && current.right == null) { // leaf
					if (current == parent.right) {
						parent.right = null;
					} else {
						parent.left = null;
					}
					return true;
				} else if (current.left == null ^ current.right == null) { // only one child
					if (current == parent.right) {
						if (current.right != null) {
							parent.right = current.right; // child of deleted node becomes new right node of deleted
															// node's parent
						} else {
							parent.right = current.left;
						}
					} else {
						if (current.right != null) {
							parent.left = current.right;
						} else {
							parent.left = current.left;
						}
					}
					return true;
				} else if (current.left != null && current.right != null) { // two children
					TNode minParent = current;
					TNode min = current.right;

					while (min.left != null) {
						minParent = min;
						min = min.left;
					}
					current.element = min.element; // replace data in node to be deleted with data from minimum value
					if (min.right == null) { // when minimum node has no right child
						if (min == minParent.right) {
							minParent.right = null;
						} else {
							minParent.left = null;
						}
					} else {
						minParent.right = min.right; // make parent of minimum node point to minimum's right child
														// instead
					}
					return true;
				}
			} else if (v > current.element) {
				parent = current;
				current = current.right;
			} else {
				parent = current;
				current = current.left;
			}
		return false; // element not in the set
	}

	public BSTSet union(BSTSet s) {
		MyStack thisStack = minStackSetup();
		MyStack sStack = s.minStackSetup();
		BSTSet union = new BSTSet();
		TNode thisStackNode = nextSetup(thisStack);
		TNode sStackNode = s.nextSetup(sStack);

		while (thisStackNode != null || sStackNode != null) {
			if (thisStackNode == null) { // this runs out; elements still in s
				union.add(sStackNode.element);
				sStackNode = s.nextSetup(sStack);
			} else if (sStackNode == null) { // s runs out; elements still in this
				union.add(thisStackNode.element);
				thisStackNode = nextSetup(thisStack);
			} else if (thisStackNode.element == sStackNode.element) {
				union.add(thisStackNode.element);
				thisStackNode = nextSetup(thisStack);
				sStackNode = s.nextSetup(sStack);
			} else if (thisStackNode.element > sStackNode.element) {
				union.add(sStackNode.element); // add the smaller element first
				sStackNode = s.nextSetup(sStack);
			} else {
				union.add(thisStackNode.element); // add the smaller element first
				thisStackNode = nextSetup(thisStack);
			}
		}
		return union;
	}

	public BSTSet intersection(BSTSet s) {
		MyStack thisStack = minStackSetup();
		MyStack sStack = s.minStackSetup();
		BSTSet intersection = new BSTSet();
		TNode thisStackNode = nextSetup(thisStack);
		TNode sStackNode = s.nextSetup(sStack);

		while (thisStackNode != null && sStackNode != null) { 
			if (thisStackNode.element == sStackNode.element) { //if two values are the same in both
				intersection.add(thisStackNode.element); //add it in!
				thisStackNode = nextSetup(thisStack); //advance both
				sStackNode = s.nextSetup(sStack);
			} else if (thisStackNode.element > sStackNode.element) { //if this element is greater than an s element
				sStackNode = s.nextSetup(sStack); //advance s
			} else {
				thisStackNode = nextSetup(thisStack); //an element in s is greater; advance this
			}
		}
		return intersection;
	}

	public BSTSet difference(BSTSet s) {
		MyStack thisStack = minStackSetup();
		MyStack sStack = s.minStackSetup();
		BSTSet difference = new BSTSet();
		TNode thisStackNode = nextSetup(thisStack);
		TNode sStackNode = s.nextSetup(sStack);

		while (thisStackNode != null) {
			if (sStackNode == null) { // s runs out; elements still in this
				difference.add(thisStackNode.element);
				thisStackNode = nextSetup(thisStack);
			} else if (thisStackNode.element == sStackNode.element) { //advance both
				thisStackNode = nextSetup(thisStack);
				sStackNode = s.nextSetup(sStack);
			} else if (thisStackNode.element > sStackNode.element) { //advance s in case there is still a duplicate in s
				sStackNode = s.nextSetup(sStack);
			} else {
				difference.add(thisStackNode.element); // add the smaller element
				thisStackNode = nextSetup(thisStack);
			}
		}
		return difference;
	}

	public int size() {
		int size = 0;

		if (root != null) {
			MyStack tStack = minStackSetup(); //set up the stack
			while (!tStack.isEmpty()) {
				nextSetup(tStack); //push new values onto the stack
				size++; //increment size flag variable
			}
		}
		return size; //final count value of size
	}

	public int height() {
		if (root == null) {
			return -1;
		}

		return height(root);
	}

	private int height(TNode t) {
		//code to find height, to make public height() above simpler
		//partially sourced from GeeksforGeeks maxdepth() pseudocode algorithm:
		//https://www.geeksforgeeks.org/write-a-c-program-to-find-the-maximum-depth-or-height-of-a-tree/
		int height = 0;
		int rightHeight = 0;
		int leftHeight = 0;

		if (t.left != null) { //
			leftHeight = height(t.left) + 1; //recursively call height of left node
		}
		if (t.right != null) { //
			rightHeight = height(t.right) + 1; //recursively call height of right node
		}

		if (leftHeight >= rightHeight) { //the greatest height will become final height
			height = leftHeight;
		} else if (leftHeight < rightHeight) { //the greatest height will become final height
			height = rightHeight;
		}

		return height;
	}

	public void printBSTSet() {
		//source: lab document
		if (root == null) {
			System.out.println("The set is empty");
		} else {
			System.out.print("The set elements are: ");
			printBSTSet(root);
			System.out.print("\n");
		}
	}

	private void printBSTSet(TNode t) {
		//source: lab document
		if (t != null) {
			printBSTSet(t.left);
			System.out.print(" " + t.element + ", ");
			printBSTSet(t.right);
		}
	}

	public void printNonRec() {
		if (root == null) {
			System.out.println("The set is empty");
		} else {
			MyStack tStack = minStackSetup(); //set up the stack
			while (!tStack.isEmpty()) {  //set up the rest of the stack
				System.out.print(nextSetup(tStack).element + " ");
			}
		}
	}

	private MyStack minStackSetup() { // method to make min value in tree the top of the stack
		//source: Java Structures: Data Structures in Java for the Principled Programmer (Duane A. Bailey)
		MyStack treeStack = new MyStack();
		TNode current = root;
		while (current != null) {
			treeStack.push(current); // push current node onto stack
			current = current.left; // keep moving left to find min value
		}
		return treeStack;
	}

	private TNode nextSetup(MyStack treeStack) { // in-order traversal to add the rest onto the stack
		//source: Java Structures: Data Structures in Java for the Principled Programmer (Duane A. Bailey)
		TNode parent = treeStack.topAndPop();
		if (parent != null && parent.right != null) { // not a leaf
			TNode current = parent.right;
			while (current != null) { //while current is not a leaf
				treeStack.push(current); 
				current = current.left;
			}
		}
		return parent;
	}

	public void printLevelOrder() {
		MyQueue treeQueue = new MyQueue();
		TNode current = root;

		//algorithm from the 2SI4 tutorial notes
		if (root == null) {
			System.out.println("The set is empty");
		} else {
			treeQueue.enqueue(root); //start with an empty queue, enqueue first value

			while (!treeQueue.isEmpty()) {
				current = treeQueue.dequeue(); //dequeue value by value from queue
				System.out.print(current.element + " "); //print every dequeued value one after another

				if (current.left != null) { //if there's a left child
					treeQueue.enqueue(current.left); 
				}
				if (current.right != null) { //if there's a right child
					treeQueue.enqueue(current.right);
				}
			}
		}
	}
}
