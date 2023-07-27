package polyominoe;

import java.awt.Font;
<<<<<<< Updated upstream
=======
import java.awt.Graphics;
import java.awt.Insets;
>>>>>>> Stashed changes
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;

public class Vertex extends CircleButton{
	/*
	 * The number the next labelled Vertex will take on. 
	 */
	private static int nextInt = 1;

	/*
	 * The queued numbers waiting to be inputed into vertices.
	 */
	private static TreeSet<Integer> numberSet = new TreeSet<Integer>();
	
	/*
	 * This Vertex's label value.
	 */
	private int value;
	
	/*
	 * Whether or not this Vertex is frozen. If frozen, all behavior is disabled
	 * and this Vertex becomes uninteractable.
	 */
	private boolean isFrozen;


	/*
	 * Create a Vertex represented by a circle. The Vertex's location is determined by its top-left corner, as if it
	 * was drawn like a square.
	 */
	public Vertex(int radius) {
		super("");
		setMargin(new Insets(0, 0, 0, 0));
		setFont(new Font("Arial", Font.PLAIN, (int) (radius / 3)));
		setSize(radius, radius);
		isFrozen = false;

		// If the numberSet is empty when Vertex is created, add 1 to the set (typically only needed when first Vertex is drawn)
		if (numberSet.isEmpty()) {
			numberSet.add(1);
		}

		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isFrozen) {
					// If Vertex is not labeled, label it the smallest number in numberSet when clicked
					if (value == 0) {
						setValue(numberSet.first());
						setText("" + numberSet.first());

						numberSet.remove(numberSet.first());
					} else { // Vertex must already be labeled, so delete labeling when clicked
						// Remove the number from Vertex and add it to the numberSet
						numberSet.add(getValue());
						setValue(0);
						setText("");
					}
					// If all queued numbers in numberSet have been used up, increment nextInt and store it in numberSet
					if (numberSet.isEmpty()) {
						numberSet.add(++nextInt);
					}
					// Recalculate the Faces and update the visual labellings
					ButtonPanel.calculateFaces();
					ButtonPanel.updateNumberQueueLabel();
				}
			}
		});
	}
<<<<<<< Updated upstream
	
=======

	/*
	 * Updates the values for all Faces that share this Vertex.
	 */
	private void updateFaceValues() {
		ArrayList<Face> faces = new ArrayList<Face>();
		faces.add(f0);
		faces.add(f1);
		faces.add(f2);
		faces.add(f3);
		for (Face f : faces) {
			if (f != null && !f.isFrozen()) { 
				if (f.getSum() != 0) {
					f.setText("" + f.getSum());
				} else {
					f.setText("");
				}
			}
		}
	}

>>>>>>> Stashed changes
	/*
	 * Helper method to reset the counter and empty the numberSet.
	 * Used to clear all labellings on screen.
	 */
	public static void resetCounter() {
		nextInt = 1;
		numberSet.clear();
		numberSet.add(nextInt);
		ButtonPanel.updateNumberQueueLabel();
	}
	
	/*
	 * Returns the value this Vertex holds
	 */
	public int getValue() {
		return value;
	}

	/*
	 * Set this Vertex's value to an int value
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/*
	 * Returns whether or not this Vertex is frozen (can be interacted with on screen)
	 */
	public boolean isFrozen() {
		return isFrozen;
	}
	
	/*
	 * Freeze this Vertex
	 */
	public void setFrozenTrue() {
		isFrozen = true;
		setEnabled(false);
	}
	
	/*
	 * Getter for numberSet, which is the set of all Vertex labellings awaiting input
	 */
	public static TreeSet<Integer> getNumberSet(){
		return numberSet;
	}
}
