public class HugeInteger {

	private int[] bigArray = null;
	private int intLength;
	public static final char NEGSIGN = '-';
	boolean isPos = true;

	public HugeInteger(String val) {
		
		// input error checking
		if (val == null || val.length() == 0) { // input must contain 1 or more characters
			throw new IllegalArgumentException("invalid input. String must contain one or more characters.");
		} else if (val.length() == 1 && val.charAt(0) == NEGSIGN) { //input cannot be a lone negative sign
			throw new IllegalArgumentException("invalid input. String must contain one or more digits.");
		}
		
		int x = 0; //initialize flag
		if (val.charAt(0) == NEGSIGN) { //determines -ve numbers
			isPos = false;
			x = 1;
		}
		
		int bigArrayIndex = 0; //array index after removal of leading zeros
		for (int i = x; i < val.length(); i++) { //x bc for negative it will skip the negative sign with x = 1 and start counting at index 1
			if (((int)(val.charAt(i)) < 48) || ((int)(val.charAt(i)) > 57)) { //nothing other than digits after index 0
				throw new IllegalArgumentException("Invalid input. Only characters allowed after index 0 (for positive numbers) or 1 (for negative numbers) are digits from 0-9.");
			} 
			if(bigArray == null && val.charAt(i) == '0' && i < val.length()-1) { //Is it a leading 0 (that's not the last character)? If yes, remove
				continue;
			}
			if (bigArray == null) { //initialize bigArray if not yet initialized
				intLength = val.length()-i; //val length subtract negative sign and leading zeros
				this.bigArray = new int[intLength];
			}
			bigArray[bigArrayIndex] = (val.charAt(i)-48); //add all valid digits to bigArray
			bigArrayIndex++;
		}
	}

	public HugeInteger(int n) {

		this.intLength = n;
		this.bigArray = new int[intLength];

		Random randNum = new Random(intLength);

		if (n>0) {
			for (int i = 0; i < intLength; i++) {
				this.bigArray[i] = randNum.nextInt(10); //populate with digit from 0-9 at each index
			}
		} else {
			throw new IllegalArgumentException("Invalid input. Number of digits must be greater than 0.");
		}

	}

	public HugeInteger add(HugeInteger h){    

	int thisIndex = this.intLength-1; //initialize LSB index of this
	int hIndex = h.intLength-1; //initialize LSB index of h
	int carry = 0;	
	String result = "";
	
	while(thisIndex >=0 || hIndex >=0) { //will stop when out of digits in the shorter array
		int sum = carry; //initialize sum with carry value for a given iteration
		
		if (thisIndex >=0) { //if more digits in this to add
			sum += this.bigArray[thisIndex];
			thisIndex--;
		}
		
		if(hIndex >=0) { //if more digits in h to add
			sum += h.bigArray[hIndex];
			hIndex--;
		}
		
		if (sum>9) { //overflow case; carry required
			carry = 1;
			result = sum%10 + result;
		} else { //no carry required
			carry = 0;
			result = sum + result;
		}	
	}
	if (carry == 1) { //if there is a final carry from MSD
		result = 1 + result;
	}
	
	HugeInteger sum = new HugeInteger(result);
	
	return sum;

	}

	public String toString() {
		String bigIntString = new String();
		
		if (isPos) {
			bigIntString = "";
		} else {
			bigIntString = "-" + ""; //add the negative sign back for negative numbers
		}
		
		for (int i = 0; i < intLength; i++) {
			bigIntString += bigArray[i];
		}

		return bigIntString;
	}

}
