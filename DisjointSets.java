import java.util.*;

/**
 * @author Jordan Prosch
 * 
 * DisjointSets.java represents a series of sets that have no elements in common.
 * For example, the sets {a, b, c} {d, e, f} and {x, y, z} are all considered 
 * disjoint sets since they have no elements in common. The sets {a, b, c} and 
 * {a, d, e, f}, however, are not disjoint sets since they share the same element
 * 'a'.
 * 
 * This class mimics a Disjoint Sets ADT in that it implements an operation for union()
 * and an operation for find().
 * 
 * union() works by combining two sets into one whole set. For example, if we have sets
 * {1, 2, 3} and {4, 5, 6} where 1 and 4 are the names of the sets, respectively, then 
 * calling union(1, 4) will combine the two sets into {1, 2, 3, 4, 5, 6}.
 * 
 * find() works by finding the name of the set containing an element. For example, if we
 * have sets {1, 2, 3} and {4, 5, 6} where 1 and 4 are the names of the sets, respectively,
 * calling find(1), find(2), find(3) would all result with 1 being returned, and calling 
 * find(4), find(5), find(6) would all result with 4 being returned.
 * 
 */
public class DisjointSets {
	/**
	 * Implementation details:
	 * The sets are stored in the array <code>up</code>. An element who is the root of a set
	 * shall have the value of its set size * -1; for example, the set with name 4 that has three 
	 * elements in it would store -3 at index 4. Thus, any element that was the only element in
	 * the set would store -1.
	 * 
	 * Any element that is not a root would point to another element in the set, possibly the root.
	 * 
	 * Example:
	 * Lets say we have 6 elements that are grouped into the following sets: {1, 3}, {2}, {0, 4, 5}
	 * where 1, 2, and 0 are the names (roots) of the sets. The array <code>up</code> might look like
	 * this:
	 *          
	 *     index:   0   1   2   3   4   5 
	 *             -----------------------
	 *     value  |-3 |-2 |-1 | 1 | 0 | 0 |
	 *             ----------------------- 
	 * Note that <code>up[5]</code> might store 4 instead of 0.
	 */
	private int[] up;
	
	/**
	 * Constructor that makes an array of length numElements
	 * @param numElements
	 */
	public DisjointSets(int numElements) {
		if (numElements < 1)
			throw new IllegalArgumentException("DisjointSets must be passed a parameter > 0!");
		up = new int[numElements];
		
		// all elements are the only elements in their set, so we set all values to -1
		for (int i = 0; i < up.length; i++) 
			up[i] = -1;
	}
	
	/**
     * Union (combine) two disjoint sets into one set. 
     * The larger set name will be added to the other set.
     * @param set1 the name of set 1.
     * @param set2 the name of set 2.
     * @throws IllegalArgumentException if either of set1 or set2 are 
     * not the name of sets or are not valid elements
     */
    public void union(int set1, int set2) {
    	checkIsSetName(set1);
    	checkIsSetName(set2);

    	if (set1 != set2) { // no reason combine if set1 == set2
	    	int weight1 = Math.abs(up[set1]);
	    	int weight2 = Math.abs(up[set2]);
	    	if (set2 < set1) {
	    		up[set1] = set2;
	    		up[set2] = -1 * (weight1 + weight2);
	    	} else {
	    		up[set2] = set1;
	    		up[set1] = -1 * (weight1 + weight2);
	    	}
    	}
    }

    /**
     * Find which set element x belongs to.
     * Compresses the path during the search so all elements point to the root.
     * @param x the element being searched for.
     * @return the name of the set containing x.
     * @throws IllegalArgumentException if x is not a valid element. 
     */
    public int find(int x) {
    	checkIsValidElement(x);
    	
    	int root = x;
    	while (up[root] >= 0) { // find the root
    		root = up[root];
    	}
    	
    	if (root != x) { // if x is not a root
    		int temp = up[x];
    		
    		// compress the path
    		while (temp != root) {
    			up[x] = root;
    			x = temp;
    			temp = up[temp];
    		}
    	}
    	return root;
    }

    /**
     * Returns the current total number of sets.
     * @return the current number of sets.
     */
    public int numSets() {
    	int count = 0;
    	for (int index : up) {
    		if (index < 0)
    			count++;
    	}
    	return count;
    }
    
    /**
     * Returns the total of elements that make up the disjoint sets
     * @return the total of elements that make up the disjoint sets
     */
    public int numElements() {
    	return up.length;
    }

    /**
     * Determine if an element is the name of a set.
     * @param x an element
     * @return true if x is the name of a set
     * @throws IllegalArgumentException if x is not a valid element. 
     */
    public boolean isSetName(int x){
    	checkIsValidElement(x);
    	return up[x] < 0;
    }

    /**
     * Returns the total number of elements in the given set.
     * @param setNum the name of a set
     * @throws IllegalArgumentException if setNum is not the name of a set or is not a valid element.
     */
    public int numElements(int setNum){
    	checkIsSetName(setNum);
    	return Math.abs(up[setNum]);
    }

    /**
     * Prints out the elements in the given set in numerical order, except for the set name is first.
     * setNum is assumed to be a root and represents the name of a set.
     * @param setNum the name of a set
     * @throws IllegalArgumentException if setNum is not the name of a set or is not a valid element. 
     */
    public void printSet(int setNum) {
    	System.out.println(Arrays.toString(getElements(setNum)));
    }

    /**
     * Returns an array containing the elements in the given set with the set name first.
     * @param setNum the name of a set
     * @returns an array containing the elements in the given set.
     * @throws IllegalArgumentException if setNum is not the name of a set or is not a valid element. 
     */
    public int[] getElements(int setNum) {
    	checkIsSetName(setNum);

    	// get a list of all elements in the set
    	int[] elements = new int[numElements()];
    	int counter = 0;
    	for (int i = 0; i < numElements(); i++) {
    		if (find(i) == setNum)
    			elements[counter++] = i;
    	}
    	

    	return Arrays.copyOf(elements, counter);
    }
    
    /**
     * Returns the set name(s) that make up the disjoint set(s)
     * @return the set name(s) that make up the disjoint set(s)
     */
    public int[] getSetNames() {
    	int[] setNames = new int[numElements()];
    	int counter = 0;
    	for (int i = 0; i < numElements(); i++) {
    		if (isSetName(i)) {
    			setNames[counter] = i;
    			counter++;
    		}
    	}
    	return Arrays.copyOf(setNames, counter);
    }
    
    /**
     * Returns a String representation of the sets.
     */
    public String toString() {
    	int[] setNames = getSetNames();
    	StringBuilder sb = new StringBuilder();
    	sb.append("{" + Arrays.toString(getElements(setNames[0]))); // add 1st set to solve fence post problem
    	for (int i = 1; i < setNames.length; i++) {
    		sb.append(", " + Arrays.toString(getElements(setNames[i])));
    	}
    	sb.append("}");
    	return sb.toString();
    }
    
    // helper method that throws an exception if an element is not the name of a set 
    private void checkIsSetName(int setNum) {
    	// need to make sure the element is within range of the array
    	checkIsValidElement(setNum); 
    	
    	if (up[setNum] >= 0)
    		throw new IllegalArgumentException("Element '" + setNum + "' must be a set name.");
    }
    
    // helper method that throws an exception if an element is 
    // not valid (i.e. not within the range of the array) 
    private void checkIsValidElement(int element) {
    	if (element < 0 || element >= up.length)
    		throw new IllegalArgumentException("Element '" + element + "' is not a valid element.");
    }
}
