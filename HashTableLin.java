package L4;

public class HashTableLin {
	
	private Integer[] table; //our hash table
	private double maxLoad; //max load factor
	private int tableSize; //number of spots in the hash table
	private int numElements; //number of keys in the table
	
	public HashTableLin(int maxNum, double load) {
		numElements = 0; //empty
		maxLoad = load;
		
		//approx. ratio to determine the number of spots needed
		double keyRatio = maxNum/maxLoad;
		int x = (int)Math.ceil(keyRatio);
		
		if(isPrime(x)) { //if the ratio is already a prime number
			tableSize = x;
		} else { //if the ratio is not prime find the next closest prime
			tableSize = NextPrime(x);
		}
		table = new Integer[tableSize];
	}
	
	private boolean isPrime(int n) { //determines if the number in question is prime
		//inspired by stack overflow
        if(n<=3) {  //numbers <= 3 can only be prime when greater than 1                      
            return n>1;                        
        }
        else if(n%2 == 0 || n%3 ==0) {  //if evenly divisible by 2 or 3, it is not prime
            return false;
        }
        
        int i=5;
        while(i*i <= n) {
            if(n%i == 0 || n%(i+2) == 0) { //final false
                return false;
            }
            i += 6;
        }
        return true;
    }
    
    private int NextPrime(int n) { //determines the next closest prime number
    	//inspired by stack overflow
        while(true) {
            if(isPrime(n)) { //if the number is prime, stay here
                return n;
            }
            n++; //otherwise keep increasing the value of n until a prime number is reached
        } 
    }
		
	public double getMaxLoadFactor() { //returns load factor
		return maxLoad;
	}
		
	public int getNumKeys() { //returns the number of keys
		return numElements;
	}
		
	public int getTableSize() { //returns the number of spots in the hash table
		return tableSize;
	}
		
	public void insert(int n) {
		//inspired by textbook
		//big theta (n)
		int x = findPos(n); //returns the index of n through linear probing
		
		if(table[x] == null) { //if there is no value in the available position for n
			table[x] = n; //insert n
			numElements++;
			
			if((double)numElements/tableSize > maxLoad) {
				rehash();
			}
			//if load exceeds maxLoad, rehash
		} 
		//else: if n is already inside, ignore; no duplicates wanted
	}
	
	public int insertCount(int n) {
		//inserts an element, returns the number of probes taken to insert it
		//big theta (n)
		
		int probeCount = 1;
		int currentPos = hash(n); //returns hash index for key
		
		while(table[currentPos]!=null) { //while not an empty slot
			if(n == table[currentPos]) { //no duplicates; already occupied with n
				break;
			}
			
			currentPos ++; //keep probing one more position over
			probeCount++; //for every update in position increment number of probes
			
			if(currentPos >= tableSize) {
				currentPos -= tableSize;
			}
		}

		if(table[currentPos] == null) { //if there is no value in the available position for n
			table[currentPos] = n; //insert n
			numElements++;
			
			if((double)numElements/tableSize > maxLoad) {
				rehash(); //if load exceeds maxLoad, rehash
			}
		} 
		return probeCount;
	}
	
	public int hash(int n) { //for hash function
		int hashIndex = n%tableSize;
		return hashIndex;
	}
		
	private void rehash() {
		//inspired by textbook
		//big theta (n)
		//create new hash table for rehashing
		Integer[] oldHashTable = table;
		int x = 2*tableSize;
		
		tableSize = NextPrime(x);
		table = new Integer[tableSize];
		numElements = 0;
		
		//copy old hash table into new hash table
		for(int i = 0; i<oldHashTable.length; i++) {
			if(oldHashTable[i] != null) { //copy all existing values into the new table
				insert(oldHashTable[i]); //rehashing with hash function accounted for here
			}
		}
	}
		
	public boolean isIn(int n) {
		//big theta (1)
		int index = findPos(n);
		
		if(table[index] != null && table[index] == n) { //n is found
			return true;
		} 	
		return false;
	}

	private int findPos(int n) { //linear probe
		//inspired by textbook
		//big theta (n)
		int currentPos = hash(n);
		
		while(table[currentPos]!=null) { //while not an empty slot
			if(n == table[currentPos]) { //value position already found
				break;
			}
			
			currentPos ++; //keep incrementing the index until value is found (or not found)
			
			if(currentPos >= tableSize) {
				currentPos -= tableSize;
			}
					
		}
		return currentPos;
	}
	
	public void printKeys() {
		//big theta (n)
		for(int i = 0; i < tableSize; i++) { //traverse through hash table
			if (table[i] != null) { //if a spot has a key, print it
				System.out.print(table[i] + " ");
			}
		}	
	}
		
	public void printKeysAndIndexes() {
		//big theta (n)
		for(int i = 0; i < tableSize; i++) { //traverse through hash table
			if (table[i] != null) { //if a spot has a key, print it along with its index
				System.out.println("[" + i + "]" + table[i]);
			} 
		}
	}
}
