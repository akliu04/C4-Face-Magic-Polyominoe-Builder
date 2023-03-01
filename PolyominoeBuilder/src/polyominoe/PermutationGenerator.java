package polyominoe;

/*
 * Uses Heap's algorithm to generate all possibly Vertex labellings
 */
public class PermutationGenerator {
	public void generateAllLabellings(Vertex[] elements, Face[] faces, int n) {
		long numLabellings = 0;
		long count = 1;
		int[] c = new int[n];
		if (isC4FaceMagic(faces)) {
			numLabellings++;
			updateC4FaceMagicLabels(elements, faces);
			printVertexLabels(elements);
		}
		int i = 0;
		while (i < n) {
			if (c[i] < i) {
				if (i % 2 == 0) {
					swap(elements, 0, i);
				} else {
					swap(elements, c[i], i);
				}
				if (isC4FaceMagic(faces)) {
					numLabellings++;
					updateC4FaceMagicLabels(elements, faces);
					printVertexLabels(elements);
				}
				count++;
				c[i] += 1;
				i = 0;
			} else {
				c[i] = 0;
				i += 1;
			}
		}
		// Print out useful information to console
		System.out.println("Done!");
		System.out.println("Generated " + count + " labellings.");
		System.out.println("There exists " + numLabellings + " labellings (not excluding symmetry) for this graph.");
	}

	public static void swap(Vertex[] elements, int i, int j) {
		int temp = elements[i].getValue();
		elements[i].setValue(elements[j].getValue());
		elements[j].setValue(temp);
	}

	/*
	 * Returns whether the polyominoe is c-4-face-magic
	 * Assumes a polyominoe with at least one Face
	 */
	private boolean isC4FaceMagic(Face[] arr) {
		int target = arr[0].getSum();
		for (int i = 1; i < arr.length; i++) {
			if (arr[i].getSum() != target) {
				return false;
			}
		}
		return true;
	}

	private void updateC4FaceMagicLabels(Vertex[] vArr, Face[] fArr) {
		if (fArr.length != 0) {
			for (Vertex v : vArr) {
				v.setText(v.getValue() + "");
			}
			int faceMagicSum = fArr[0].getSum();
			for (Face f : fArr) {
				f.setText(faceMagicSum + "");
			}
		}
	}

	private void printVertexLabels(Vertex[] arr) {
		String s = "";
		for (Vertex v : arr) {
			s += v.getValue() + ", ";
		}
		System.out.println(s.substring(0, s.length() - 2));
	}

}
