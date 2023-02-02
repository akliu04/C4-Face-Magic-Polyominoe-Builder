package polyominoe;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.TreeSet;

public class Vertex extends CircleButton{
	/*
	 * The number a Vertex will take on when clicked. 
	 */
	private static int nextInt = 1;

	/*
	 * The queued numbers waiting to be inputed into vertices.
	 */
	private static TreeSet<Integer> numberSet = new TreeSet<Integer>();

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
		setFont(new Font("Arial", Font.PLAIN, radius / 3));
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
					// If Vertex is empty, assign it the smallest number in numberSet
					if (value == 0) {
						setValue(numberSet.first());
						setText("" + numberSet.first());

						numberSet.remove(numberSet.first());
					} else {
						// Otherwise, remove the number from Vertex and add it to the numberSet
						numberSet.add(getValue());
						setValue(0);
						setText("");
					}
					if (numberSet.isEmpty()) {
						numberSet.add(++nextInt);
					}
					ButtonPanel.calculateFaces();
					ButtonPanel.updateNumberQueueLabel();
				}
			}
		});
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static void resetCounter() {
		nextInt = 1;
		numberSet = new TreeSet<Integer>();
		numberSet.add(nextInt);
		ButtonPanel.updateNumberQueueLabel();
	}
	
	public boolean isFrozen() {
		return isFrozen;
	}
	
	public void setLockedTrue() {
		isFrozen = true;
		setEnabled(false);
	}

	public static TreeSet<Integer> getNumberSet(){
		return numberSet;
	}
}
