package polyominoe;

import java.util.Arrays;
import java.util.HashSet;

/*
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
		
		// Print out useful information to console
		System.out.println("Done!");
		System.out.println("Generated " + vArr.length + "! ("+ numPermutationsGenerated + ") total labellings.");
		System.out.println("There exists " + numC4Labellings + " c4-face-magic labellings (including symmetric labellings) for this graph.");
		System.out.println("All possible c4-face-magic values: " + sortedC4FaceMagicValues(c4FaceMagicValueSet));
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
		int temp = vArr[i].getValue();
		vArr[i].setValue(vArr[j].getValue());
		vArr[j].setValue(temp);
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
	 * Assumes all Faces are c4-face-magic and fArr contains at least one Face
	 */
	private void updateC4FaceMagicLabels() {
		for (Vertex v : vArr) {
			v.setText(v.getValue() + "");
		}
		int faceMagicSum = fArr[0].getSum();
		for (Face f : fArr) {
			f.setText(faceMagicSum + "");
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

}
