package polyominoe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ButtonPanel extends JPanel{

	/*
	 * A Button that prevents making any changes to ALL Faces & Vertices currently on screen,
	 * as well as resets Vertex numbering for any future Vertices. Frozen Faces and Vertices
	 * CANNOT be unfrozen.
	 */
	private JButton freezeAllButton;

	/*
	 * A Button that prevents making any changes to Faces that have all their Vertices labeled,
	 * as well as resets Vertex numbering for any future Vertices. Frozen Faces and Vertices
	 * CANNOT be unfrozen.
	 */
	private JButton freezeCompleteButton;

	/*
	 * A Toggle Button that allows the user to choose whether or not Faces can be 
	 * drawn or removed. When engaged, Faces cannot be drawn or removed, and the user 
	 * can input values into Vertices. When disengaged, Faces can be drawn and/or removed
	 * but the user cannot input Vertex values.
	 */
	private JToggleButton lockButton;

	/*
	 * A Button that clears all Faces and their Vertices from grid_panel, and empties
	 * their respective arrays.
	 */
	private JButton clearButton;

	/*
	 * Resets the values of all Vertices.
	 */
	private JButton clearVertexNumbersButton;

	/*
	 * A Label that indicates a reservoir of numbers that are queued to be inputed
	 * into Vertices. Used in conjunction with numberQueueLabel.
	 */
	private JLabel nextNumberLabel;

	/*
	 * A Label that displays the queued numbers available to be inputed into Vertices.
	 */
	private static JLabel numberQueueLabel;
	
	/*
	 * A Button that calculates EVERY permutation of Vertex labellings.
	 */
	private static JButton testAllPermutationsButton;


	/*
	 * A Panel that contains the UI buttons. Uses a default FlowLayout for storing buttons.
	 */
	public ButtonPanel(GridPanel gridPanel) {
		// Create the freezeButton
		freezeAllButton = new JButton("Freeze All");
		freezeAllButton.setFocusable(false);
		freezeAllButton.setToolTipText("<html>" 
				+ "Freeze all polyominoes. Cannot be undone");
		freezeAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Face face : GridPanel.faceArray) {
					if (face != null) {
						face.setFrozenTrue();
					}
				}
				gridPanel.clearVertexNumbers();
			}
		});

		// Create the freezeCompleteButton
		freezeCompleteButton = new JButton("Freeze Labelled");
		freezeCompleteButton.setFocusable(false);
		freezeCompleteButton.setToolTipText("<html>" 
				+ "Freeze all fully-labelled polyominoes. Cannot be undone"
				+ "<br>"
				+ "Clears all labelings of non-fully labelled polyominoes.");
		freezeCompleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Face face : GridPanel.faceArray) {
					if (face != null && face.hasAllLabeledVertices()) {
						face.setFrozenTrue();
					}
				}
				gridPanel.clearVertexNumbers();
			}
		});

		// Create the lockButton
		lockButton = new JToggleButton("Lock");
		lockButton.setFocusable(false);
		lockButton.setToolTipText("<html>" 
				+ "While enabled, only editing vertices is allowed "
				+ "<br>"
				+ "Use to prevent accidentally drawing unwanted polyominoes");
		lockButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lockButton.isSelected()) {
					gridPanel.setLockedTrue();
				} else {
					gridPanel.setLockedFalse();
				}
			}
		});

		// Create the clearButton
		clearButton = new JButton("Clear");
		clearButton.setFocusable(false);
		clearButton.setToolTipText("<html>" 
				+ "Clear all polyominoes");
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridPanel.clearGrid();
			}
		});

		// Create the clearVertexNumbersButton
		clearVertexNumbersButton = new JButton("Renumber");
		clearVertexNumbersButton.setFocusable(false);
		clearVertexNumbersButton.setToolTipText("<html>" 
				+ "Clear all vertex numbers");
		clearVertexNumbersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridPanel.clearVertexNumbers();
			}
		});

		// Create the nextNumberLabel
		nextNumberLabel = new JLabel("Next:");
		nextNumberLabel.setToolTipText("<html>" 
				+ "Numbers waiting to be inputted will appear here");

		// Create the numberQueueLabel
		numberQueueLabel = new JLabel("1");
		numberQueueLabel.setToolTipText("<html>" 
				+ "Numbers waiting to be inputted will appear here");
		
		// Create the testAllPermutationsButton
		testAllPermutationsButton = new JButton("Test All Labellings");
		testAllPermutationsButton.setFocusable(false);
		testAllPermutationsButton.setToolTipText("<html>" 
				+ "Test all permutations of possibly labellings"
				+ "<br>"
				+ "WARNING: May take longer than expected; Use at your own risk...");
		testAllPermutationsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Copy all non-null Vertices in vertexArray to vArr for more efficient permutation generation
				Vertex[] vArr = getNonNullLabelledCopiesVertex();
				Face[] fArr = getNonNullCopiesFace();
				if (fArr.length != 0) {
					PermutationGenerator permGen = new PermutationGenerator();
					permGen.generateAllLabellings(vArr, fArr, vArr.length);
					freezeCompleteButton.doClick(); 
				}
			}
		});
		
		

		// Add the buttons to it
		add(testAllPermutationsButton);
		add(freezeCompleteButton);
		add(freezeAllButton);
		add(lockButton);
		add(clearVertexNumbersButton);
		add(clearButton);
		add(nextNumberLabel);
		add(numberQueueLabel);
	}

	/*
	 * Updates the list of numbers that appear on screen to the most recent list
	 * of available numbers.
	 */
	public static void updateNumberQueueLabel() {
		String newText = "";
		for (Integer number : Vertex.getNumberSet()) {
			newText += number + ", ";
		}
		numberQueueLabel.setText(newText.substring(0, newText.length()-2));
	}

	/*
	 * Displays the sum of all Faces that have at least one numbered Vertex
	 */
	public static void calculateFaces() {
		for (Face f : GridPanel.faceArray) {
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
	 * Helper method to return an array of copies of non-null values of vertexArray
	 * with labels 1 - numVertices
	 */
	private Vertex[] getNonNullLabelledCopiesVertex() {
		Vertex[] vArr;
		int length = 0;
		int index = 0;
		for (Vertex v : GridPanel.vertexArray) {
			if (v != null) {
				length++;
			}
		}
		vArr = new Vertex[length];
		for (Vertex v : GridPanel.vertexArray) {
			if (v != null) {
				v.setValue(index+1);
				vArr[index++] = v;
			}
		}
		return vArr;
	}
	/*
	 * Helper method to return an array of copies of non-null values of faceArray
	 */
	private Face[] getNonNullCopiesFace() {
		Face[] fArr;
		int length = 0;
		int index = 0;
		for (Face f : GridPanel.faceArray) {
			if (f != null) {
				length++;
			}
		}
		fArr = new Face[length];
		for (Face f : GridPanel.faceArray) {
			if (f != null) {
				fArr[index++] = f;
			}
		} 
		return fArr;
	}
}
