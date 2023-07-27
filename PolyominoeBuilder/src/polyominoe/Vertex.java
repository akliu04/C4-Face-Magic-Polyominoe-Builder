package polyominoe;


import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashSet;

public class Vertex extends CircleButton{
	/*
	 * References to the potential 4 Faces that share this Vertex.
	 * f0 is the Face top-left of this Vertex, and proceeds clockwise to f3.
	 */
	private Face f0, f1, f2, f3;

	/*
	 * The number the next labelled Vertex will take on. 
	 */
	private static int nextInt = 1;
	
	private static int totalNumNonfrozenVertices = 0;

	/*
	 * The queued numbers waiting to be inputed into vertices.
	 */
	private static TreeSet<Integer> numberSet = new TreeSet<Integer>();
	
	/*
	 * A set containing all integers that are already used by Vertices
	 */
	private static HashSet<Integer> usedNumberSet = new HashSet<Integer>();

	/*
	 * This Vertex's label value.
	 */
	private int value;
	
	/*
	 * A String that holds keyboard input from user.
	 */
	private String keyboardStringInput = "";

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
		totalNumNonfrozenVertices++;
		setFocusable(false);
		setFocusPainted(false);
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
						int smallestNum = numberSet.first();
						setValue(smallestNum);
						setText("" + smallestNum);
						
						usedNumberSet.add(smallestNum);
						numberSet.remove(smallestNum);
					} else { // Vertex must already be labeled, so delete labeling when clicked
						// Remove the number from Vertex and add it to the numberSet
						numberSet.add(value);
						usedNumberSet.remove(value);
						setValue(0);
						setText("");
					}
					// If all queued numbers in numberSet have been used up, increment nextInt and store it in numberSet
					if (numberSet.isEmpty()) {
						while (true) {
							nextInt++;
							if (!usedNumberSet.contains(nextInt)) {
								numberSet.add(nextInt);
								break;
							}
						}
					}
					// Recalculate the Faces and update the visual labelings
					updateFaceValues();
					ButtonPanel.updateNumberQueueLabel();
				}
			}
		});
		
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent e) {
				if (!isFrozen) {
					keyboardStringInput = "";
					setFocusable(true);
					requestFocusInWindow();
				}
			}
			public void mouseExited(java.awt.event.MouseEvent e) {
				if (!isFrozen && value == 0) {
					int newValue;
					if (keyboardStringInput.length() > 0) {
						newValue = Integer.parseInt(keyboardStringInput);
						if (!usedNumberSet.contains(newValue) && newValue <= totalNumNonfrozenVertices && newValue > 0) {
							setValue(newValue);
							setText("" + value);
							
							usedNumberSet.add(value);
							numberSet.remove(value);
							
							// If all queued numbers in numberSet have been used up, increment nextInt and store it in numberSet
							if (numberSet.isEmpty()) {
								while (true) {
									nextInt++;
									if (!usedNumberSet.contains(nextInt)) {
										numberSet.add(nextInt);
										break;
									}
								}
							}
							// Recalculate the Faces and update the visual labelings
							updateFaceValues();
							ButtonPanel.updateNumberQueueLabel();
						} else {
							setText("");
						}
					}
					
					setRequestFocusEnabled(false);
					setFocusable(false);
				}
			}
		});
		
		addKeyListener(new java.awt.event.KeyListener() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (value == 0 && keyboardStringInput.length() < 4 && c >= 48 && c <= 57) {
					keyboardStringInput += c;
					setText(keyboardStringInput);
				}
			}

			public void keyPressed(KeyEvent e) {}

			public void keyReleased(KeyEvent e) {}

		});
		
	}

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

	/*
	 * Helper method to reset the counter and empty the numberSet.
	 * Used to clear all labellings on screen.
	 */
	public static void resetCounter() {
		nextInt = 1;
		numberSet.clear();
		usedNumberSet.clear();
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
	public void setFrozen(boolean frozen) {
		if (frozen == true && !isFrozen) {
			isFrozen = true;
			super.setFrozen(true);
			paintBorder(this.getGraphics());
			setEnabled(false);
			totalNumNonfrozenVertices--;
		} else if (frozen == false && isFrozen) {
			isFrozen = false;
			super.setFrozen(false);
			paintBorder(this.getGraphics());
			setEnabled(true);
			totalNumNonfrozenVertices++;
		}
	}

	/*
	 * Getter for numberSet, which is the set of all Vertex labellings awaiting input
	 */
	public static TreeSet<Integer> getNumberSet(){
		return numberSet;
	}

	/*
	 * Returns whether or not this Vertex is labelled as well as all Vertices
	 * on any Faces that share this Vertex.
	 */
	public boolean hasFullyLabelledAdjacentFaces() {
		if (value == 0) {
			return false;
		}
		ArrayList<Face> faces = new ArrayList<Face>();
		faces.add(f0);
		faces.add(f1);
		faces.add(f2);
		faces.add(f3);
		for (Face f : faces) {
			if (f != null && !f.hasAllLabeledVertices()) {
				return false;
			}
		}
		return true;
	}

	public void setf0(Face f) {
		this.f0 = f;
	}

	public void setf1(Face f) {
		this.f1 = f;
	}

	public void setf2(Face f) {
		this.f2 = f;
	}

	public void setf3(Face f) {
		this.f3 = f;
	}
	
	
	public static int getNumNonfrozenVertices() {
		return totalNumNonfrozenVertices;
	}
	
	public static void setNumNonfrozenVertices(int n) {
		totalNumNonfrozenVertices = n;
	}
}
