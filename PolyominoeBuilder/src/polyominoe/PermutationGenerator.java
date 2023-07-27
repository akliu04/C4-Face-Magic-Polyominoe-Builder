package polyominoe;

import java.util.Arrays;
import java.util.HashSet;

/*
<<<<<<< Updated upstream
 * Use Heap's algorithm to generate all possible Vertex labellings. 
 * Counter for number of permutations generated limited to 20! (due to use of long over BigInteger)
 * for speed optimization
 */
public class PermutationGenerator {
	Vertex[] vArr;
	Face[] fArr;
	
	public void generateAllLabellings(Vertex[] vertices, Face[] faces, int n) {
		this.vArr = vertices;
		this.fArr = faces;
		long numC4Labellings = 0;
		long numPermutationsGenerated = 0;
		HashSet<Integer> c4FaceMagicValueSet = new HashSet<Integer>();
		
		// Generate all permutations of Vertex labellings
		int[] c = new int[n];
		numPermutationsGenerated++;
		if (isC4FaceMagic()) {
			numC4Labellings++;
=======
 * Use Heap's algorithm to generate all possible Vertex labellings using the user's initial inputs for Vertices. 
 * If a c4-Face-Magic labelling is found, the polyominoe is labelled and 
 * permutation generation stops.
 */
public class PermutationGenerator extends Thread {
	static boolean searchInProgress = false;
	long numPermutationsGenerated = 0;


	private Vertex[] vArr;
	private Vertex[] emptyVertices;
	private int[] filledVertexValues;
	private Face[] fArr;

	public PermutationGenerator(Vertex[] vertices, Face[] faces) {
		this.vArr = vertices;
		this.fArr = faces;
		fillEmptyAndFilledArrays();
		fillMissingValues(emptyVertices);
	}

	public void run() {
		generateAllLabellings();
	}

	private void generateAllLabellings() {
		searchInProgress = true;
		// Generate all permutations of Vertex labellings or until a c4-Face-Magic labelling is found
		int n = emptyVertices.length;
		int[] c = new int[n];
		// Generates the first permutation. All vertices are assigned values 1 through n.
		if (isC4FaceMagic() && !isInterrupted()) {
>>>>>>> Stashed changes
			updateC4FaceMagicLabels();
			printC4FaceMagicVertexLabels();
		}
		int i = 0;
		while (i < n) {
			if (c[i] < i) {
				if (i % 2 == 0) {
					swap(0, i);
				} else {
					swap(c[i], i);
				}
				if (isC4FaceMagic()) {
					numC4Labellings++;
					c4FaceMagicValueSet.add(faces[0].getSum());
					printC4FaceMagicVertexLabels();
					updateC4FaceMagicLabels();
				} 
				numPermutationsGenerated++; 
				c[i]++;
				i = 0;
			} else {
				c[i] = 0;
				i++;
			}
		}
<<<<<<< Updated upstream
		
		// Print out useful information to console
		System.out.println("Done!");
		System.out.println("Generated " + vArr.length + "! ("+ numPermutationsGenerated + ") total labellings.");
		System.out.println("There exists " + numC4Labellings + " c4-face-magic labellings (including symmetric labellings) for this graph.");
		System.out.println("All possible c4-face-magic values: " + sortedC4FaceMagicValues(c4FaceMagicValueSet));
=======
		// Cleanup if thread is interrupted
		if (!isC4FaceMagic() || isInterrupted()) {
			for (Vertex v : vArr) {
				v.setFrozen(false);
				v.setValue(0);
				v.setText("");
			}
			for (Face f : fArr) {
				f.setFrozen(false);
				f.setText("");
			}
			ButtonPanel.resetFindLabellingButton();
		} else if (isC4FaceMagic()) {
			// If a C4-Face-Magic Labelling was found instead, freeze the labelling
			for (Face f : fArr) {
				f.setFrozen(true);
			}
		}
		searchInProgress = false;
		ButtonPanel.resetFindLabellingButton();
	}
	
	/*
	 * Fills the emptyVertices array with all Vertices in vArr that have value 0
	 * and the filledVertexValues array with all integers in vArr that are non-zero.
	 */
	private void fillEmptyAndFilledArrays() {
		int length = 0;
		int i = 0;
		int j = 0;
		for (Vertex v : vArr) {
			if (v.getValue() == 0) {
				length++;
			}
		}
		emptyVertices = new Vertex[length];
		filledVertexValues = new int[vArr.length - length];
		for (Vertex v : vArr) {
			if (v.getValue() == 0) {
				emptyVertices[i++] = v;
			} else {
				filledVertexValues[j++] = v.getValue();
			}
		}
	}
	
	/*
	 * Fills in the given array of Vertices with values 1 to N where N is the
	 * number of Vertices and each value is missing from vArr.
	 */
	private void fillMissingValues(Vertex[] arr) {
		Arrays.sort(filledVertexValues);
		int indexOfFilled = 0;
		int indexOfEmpty = 0;
		int num = 1;
		for (int i = 0; i < vArr.length; i++) {
			if (indexOfFilled >= filledVertexValues.length || num != filledVertexValues[indexOfFilled]) {
				arr[indexOfEmpty++].setValue(num);
			} else {
				indexOfFilled++;
			}
			num++;
		}
>>>>>>> Stashed changes
	}

	/*
	 * Returns a String representing all possible c4-face-magic values
	 * in sorted ascending order.
	 */
	private String sortedC4FaceMagicValues(HashSet<Integer> set) {
		int[] arr = new int[set.size()];
		int i = 0;
		for (Integer num : set) {
			arr[i++] = num;
		}
		Arrays.sort(arr);
		
		String s = "";
		for (int num : arr) {
			s += num + ", ";
		}
		
		return "[" + s.substring(0, s.length() - 2) + "]";
	}

	/*
	 * Helper method to swap two Vertex values
	 */
	private void swap(int i, int j) {
		int temp = emptyVertices[i].getValue();
		emptyVertices[i].setValue(emptyVertices[j].getValue());
		emptyVertices[j].setValue(temp);
	}

	/*
	 * Returns whether the polyominoe is c-4-face-magic.
	 * Assumes arr contains at least one Face.
	 */
	private boolean isC4FaceMagic() {
		int targetSum = fArr[0].getSum();

		for (int i = 1; i < fArr.length; i++) {
			if (fArr[i].getSum() != targetSum) {
				return false;
			}
		}

		return true;
	}

	/*
	 * Updates the labels of all Faces and Vertices on screen.
	 * 
	 */
	private void updateC4FaceMagicLabels() {
		for (Vertex v : vArr) {
			v.setText(v.getValue() + "");
		}
		for (Face f : fArr) {
			f.setText(f.getSum() + "");
		}
	}

	/*
	 * Print the values of Vertices in vArr and the c4-face-magic value.
	 * 
	 * Assumes all Faces are c4-face-magic
	 */
	private void printC4FaceMagicVertexLabels() {
		String s = "";
		for (Vertex v : vArr) {
			s += v.getValue() + ", ";
		}
		System.out.println(s.substring(0, s.length() - 2) + " : " + fArr[0].getSum());
	}
	
	private void printArray(Vertex[] arr) {
		String s = "";
		for (Vertex v : arr) {
			s += v.getValue() + ", ";
		}
		System.out.println(s);
	}
}
