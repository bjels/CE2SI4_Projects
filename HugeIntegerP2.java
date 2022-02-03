package lab1;

import java.util.Random;

public class HugeIntegerP2 {

	private int[] bigArray = null;
	private int intLength;
	public static final char NEGSIGN = '-';
	boolean isPos = true;

	public HugeIntegerP2(String val) {

		// input error checking
		if (val == null || val.length() == 0) { // input must contain 1 or more characters
			throw new IllegalArgumentException("invalid input. String must contain one or more characters.");
		} else if (val.length() == 1 && val.charAt(0) == NEGSIGN) { // input cannot be a lone negative sign
			throw new IllegalArgumentException("invalid input. String must contain one or more digits.");
		}

		int x = 0; // initialize flag
		if (val.charAt(0) == NEGSIGN) { // determines -ve numbers
			isPos = false;
			x = 1;
		}

		int bigArrayIndex = 0; // array index after removal of leading zeros
		for (int i = x; i < val.length(); i++) { // x bc for negative it will skip the negative sign with x = 1 and
													// start counting at index 1
			if (((int) (val.charAt(i)) < 48) || ((int) (val.charAt(i)) > 57)) { // nothing other than digits after index
																				// 0
				throw new IllegalArgumentException(
						"Invalid input. Only characters allowed after index 0 (for positive numbers) or 1 (for negative numbers) are digits from 0-9.");
			}
			if (bigArray == null && val.charAt(i) == '0' && i < val.length() - 1) { // Is it a leading 0 (that's not the
																					// last character)? If yes, remove
				continue;
			}
			if (bigArray == null) { // initialize bigArray if not yet initialized
				intLength = val.length() - i; // val length subtract negative sign and leading zeros
				this.bigArray = new int[intLength];
			}
			bigArray[bigArrayIndex] = (val.charAt(i) - 48); // add all valid digits to bigArray
			bigArrayIndex++;
		}
	}

	public HugeIntegerP2(int n) {

		this.intLength = n;
		this.bigArray = new int[intLength];

		Random randNum = new Random(intLength);

		if (n > 0) {
			for (int i = 0; i < intLength; i++) {
				this.bigArray[i] = randNum.nextInt(10); // populate with digit from 0-9 at each index
			}
		} else {
			throw new IllegalArgumentException("Invalid input. Number of digits must be greater than 0.");
		}

	}

	public HugeIntegerP2 add(HugeIntegerP2 h) {

		String addNums = ""; // stores the result of the addition

		if (this.isPos == h.isPos) { // if both of same sign
			addNums = addArrays(this.bigArray, h.bigArray);
			if (!this.isPos) {
				addNums = NEGSIGN + addNums;
			}
		} else {
			int magnitude = this.compareAbsVal(h); // only called once, when absolutely needed
			if (this.isPos && !h.isPos) { // this +ve, h -ve
				if (magnitude == 1) { // this is larger
					addNums = subtractArrays(this, h);
				} else if (magnitude == -1) { // this is smaller
					addNums = NEGSIGN + subtractArrays(h, this);
				} else { // equal
					addNums = "0";
				}
			} else if (!this.isPos && h.isPos) { // this -ve, h +ve
				if (magnitude == 1) { // this is larger
					addNums = NEGSIGN + subtractArrays(this, h);
				} else if (magnitude == -1) { // this is smaller
					addNums = subtractArrays(h, this);
				} else { // equal
					addNums = "0";
				}
			}
		}

		HugeIntegerP2 sum = new HugeIntegerP2(addNums);

		return sum;

	}

	private String addArrays(int[] h1, int[] h2) {

		int index1 = h1.length - 1; // initialize LSB index of h1
		int index2 = h2.length - 1; // initialize LSB index of h2
		int carry = 0;
		String result = "";

		while (index1 >= 0 || index2 >= 0) { // will stop when out of digits in the shorter array
			int sum = carry; // initialize sum with carry value for a given iteration

			if (index1 >= 0) { // if more digits in h1 to add
				sum += h1[index1];
				index1--;
			}

			if (index2 >= 0) { // if more digits in h2 to add
				sum += h2[index2];
				index2--;
			}

			if (sum > 9) { // overflow case; carry required
				carry = 1;
				result = sum % 10 + result;
			} else { // no carry required
				carry = 0;
				result = sum + result;
			}
		}
		if (carry == 1) { // if there is a final carry from MSD
			result = 1 + result;
		}

		return result;
	}

	public HugeIntegerP2 subtract(HugeIntegerP2 h) {

		String result = "";

		if (this.isPos && !h.isPos) { // pos num - neg num is equivalent to two pos nums added
			result = addArrays(this.bigArray, h.bigArray);
		} else if (!this.isPos && h.isPos) { // neg num - pos num is equivalent to two neg nums added
			result = NEGSIGN + addArrays(this.bigArray, h.bigArray);
		} else {
			int magnitude = this.compareAbsVal(h);
			if (magnitude == 0) { // both same sign and same magnitude
				result = "0";
			} else if (!this.isPos && !h.isPos) { // if both -ve
				if (magnitude == 1) { // if this is larger
					result = NEGSIGN + subtractArrays(this, h);
				} else if (magnitude == -1) { // if this is smaller
					result = subtractArrays(h, this);
				}
			} else { // if both +ve
				if (magnitude == 1) { // if this is larger
					result = subtractArrays(this, h);
				} else if (magnitude == -1) { // if this is smaller
					result = NEGSIGN + subtractArrays(h, this);
				}
			}
		}

		HugeIntegerP2 sub = new HugeIntegerP2(result);
		return sub;

	}

	private String subtractArrays(HugeIntegerP2 h1, HugeIntegerP2 h2) {
		// assuming abs val h1 is larger than abs val of h2
		int subtraction = 0;
		String result = "";

		int[] subArray = new int[h1.intLength];

		for (int i = 0; i < h1.intLength; i++) {
			subArray[i] += h1.bigArray[i];
		}

		int subArrayIndex = h1.intLength - 1;
		int h2Index = h2.intLength - 1;

		while (subArrayIndex >= 0) {

			if (subArrayIndex >= 0 && h2Index < 0) {
				result = subArray[subArrayIndex] + result; // for when there are still extra values in h1
				subArrayIndex--;
			} else {
				if (subArray[subArrayIndex] >= h2.bigArray[h2Index]) {
					subtraction = subArray[subArrayIndex] - h2.bigArray[h2Index];
				} else {
					subtraction = 10 + subArray[subArrayIndex] - h2.bigArray[h2Index];
					borrow(subArray, subArrayIndex - 1);
				}
				result = subtraction + result; // keep adding values to result string
				subArrayIndex--;
				h2Index--;
			}
		}
		return result;
	}

	private void borrow(int[] intArray, int position) {
		boolean continueBorrowing = true;
		while (continueBorrowing) {
			if (intArray[position] > 0) { // subtract 1 from the digit if it is less than 0
				intArray[position] -= 1;
				continueBorrowing = false;
			} else { // when borrowing with zeros, it becomes 9
				intArray[position] = 9;
				position--;
			}
		}
	}

	public HugeIntegerP2 multiply(HugeIntegerP2 h) {

		int magnitude = this.compareAbsVal(h);
		String result = ""; // update this to later be multiplyArrays(x,y)

		if (this.intLength == 1 && this.bigArray[0] == 0 || h.intLength == 1 && h.bigArray[0] == 0) {
			result = "0";
		} else {
			if (magnitude == -1) { // this is smaller than h
				// put h as first multiplyArrays parameter b/c it's larger
				result = multiplyArrays(h.bigArray, this.bigArray);
				if (this.isPos != h.isPos) { // two differently-signed #s will have a -ve product
					result = NEGSIGN + result;
				}
			} else { // this is greater than or equal to h
				// put this as first multiplyArrays() parameter b/c it's larger
				result = multiplyArrays(this.bigArray, h.bigArray);
				if (this.isPos != h.isPos) { // two differently-signed #s will have a -ve product
					result = NEGSIGN + result;
				}
			}
		}

		HugeIntegerP2 product = new HugeIntegerP2(result);
		return product;
	}

	private String multiplyArrays(int[] top, int[] bottom) {
		int digitProd = 0;
		String addZeros = ""; // for adding placeholder zeros
		int bottomIndex = bottom.length - 1;
		HugeIntegerP2 tally = new HugeIntegerP2("0");

		while (bottomIndex >= 0) { // until bottom has run out of digits to multiply w each digit of top
			int topIndex = top.length - 1;
			String partialResult = addZeros;
			int carry = 0; // reset carry after each iteration
			while (topIndex >= 0) {
				digitProd = bottom[bottomIndex] * top[topIndex] + carry;
				if (digitProd > 9) {
					carry = digitProd / 10; // carry is first number of product b/w any top & bottom digits over 9
					partialResult = digitProd % 10 + partialResult;
				} else {
					carry = 0;
					partialResult = digitProd + partialResult;
				}
				topIndex--;
			}

			if (carry != 0) {
				partialResult = carry + partialResult;
			}
			HugeIntegerP2 partialInput = new HugeIntegerP2(partialResult);
			tally = tally.add(partialInput);
			bottomIndex--;
			addZeros += "0"; // update the number of zeros for the next iteration
		}

		return tally.toString(); //final count of all rows of multiplication added together

	}

	public int compareTo(HugeIntegerP2 h) {

		// cases that can be eliminated immediately
		if (this.isPos && !h.isPos) {
			return 1;
		} else if (!this.isPos && h.isPos) {
			return -1;
		}

		int magnitude = this.compareAbsVal(h);

		if (!this.isPos) { // at this point in the code, if this is negative, then h will also be negative
			if (magnitude == 1) {
				return -1; // if both are -ve and abs val of this is greater, this is in fact smaller
			} else if (magnitude == -1) {
				return 1; // if both are -ve and abs val of h is greater, h is in fact smaller
			}
		}

		return magnitude;
	}

	private int compareAbsVal(HugeIntegerP2 h) {

		if (this.intLength > h.intLength) {
			return 1;
		} else if (this.intLength < h.intLength) {
			return -1;
		}

		int index = 0; // initialize indices for both to 0

		while (index <= this.intLength - 1) {

			if (this.bigArray[index] == h.bigArray[index]) {
				index++; // advance both if digits are equal
			} else if (this.bigArray[index] > h.bigArray[index]) {
				return 1; // this will be larger
			} else {
				return -1; // h will be larger
			}
		}
		return 0;
	}

	public String toString() {
		String bigIntString = new String();

		if (isPos) {
			bigIntString = "";
		} else {
			bigIntString = "-" + ""; // add the negative sign back for negative numbers
		}

		for (int i = 0; i < intLength; i++) {
			bigIntString += bigArray[i];
		}

		return bigIntString;
	}

}
