package L5;

public class MaxHeap {
	private Integer[] heapArray = null;
	private int arraySize = 0;
	private int heapSize = 0;

	public MaxHeap(int size) {
		arraySize = size;
		heapArray = new Integer[arraySize]; // initializes empty heap of specified size parameter
	}

	public MaxHeap(Integer[] someArray) {
		if (someArray != null) {
			arraySize = someArray.length + 1;
			heapArray = new Integer[arraySize];
			for (int i = 0; i < someArray.length; i++) {
				insert(someArray[i]);
			}
		}
	}

	public int getSizeArr() {
		return arraySize;
	}

	public int getSizeHeap() {
		return heapSize;
	}

	public Integer[] getHeap() {
		return heapArray;
	}

	public void insert(int n) {
		//based on course textbook method
		if (heapSize + 1 == arraySize) { // double array size if end of heap is end of array
			arraySize = 2 * arraySize;
			Integer[] heapArray2 = new Integer[arraySize];
			for (int i = 1; i <= heapSize; i++) { // copying data
				heapArray2[i] = heapArray[i];
			}
			heapArray = heapArray2; // allocate twice the spots for the actual heapArray
		}

		int newPos = ++heapSize; // increase size of heap once new position is added

		for (; newPos > 1 && n >= heapArray[newPos / 2]; newPos /= 2) { // percolating up, checking if child value is greater than parent value
			heapArray[newPos] = heapArray[newPos / 2]; // if child greater, swap
		}
		heapArray[newPos] = n; // update n to whatever position the percolation has determined for it
	}

	public static void heapsort(Integer[] arrayToSort) {
		MaxHeap sort = new MaxHeap(arrayToSort);
		
		for(int i = 0; i<arrayToSort.length; i++) {
			arrayToSort[i] = sort.deleteMax(); //call deleteMax repeatedly
		}
	}
	
	private int deleteMax() {
		//based on course textbook method
		int max = heapArray[1];
		heapArray[1] = heapArray[heapSize--]; //replace root with last value of heap followed by decrementing heap size (move last value to root, then get rid of it at the bottom)
		percolateDown(1); //percolate root down
		
		return max; //return the value that was deleted
	}
	
	private void percolateDown(int hole) {
		//based on course textbook method
		int child = 0;
		int parent = heapArray[hole]; //value at the position of the hole
		
		for(; hole*2 <= heapSize; hole = child) { //traverse down the heap
			child = hole*2; //update child
			if(child != heapSize && heapArray[child+1] > heapArray[child]) { //if anything but the last leaf and right child is greater than left child
				child++; //increment to the next child position if next child value is greater than the child we're currently at
			}
			if(heapArray[child] > parent) { //if (whether or not child is incremented in last statement) child value is greater than parent value
				heapArray[hole] = heapArray[child]; //move the child into the hole
			} else {
				break;
			}
		}
		heapArray[hole] = parent; //correct position found; now just filling in the hole with what should be there
	}

	public String toString() {
		String heapString = new String();

		for (int i = 1; i <= heapSize; i++) {
			heapString += heapArray[i] + ",";
		}
		return heapString;
	}
}
