package polyominoe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ButtonPanel extends JPanel{

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
<<<<<<< Updated upstream
	
=======

	/*
	 * A Button that attempts to find a C4-Face-Magic labelling.
	 */
	private static JButton findLabelingButton;

>>>>>>> Stashed changes
	/*
	 * A Button that calculates EVERY permutation of Vertex labellings.
	 */
<<<<<<< Updated upstream
	private static JButton testAllPermutationsButton;
=======
	private PermutationGenerator permGen;

	/*
	 * Text used by the experimental findLabellingButton
	 */
	private static String startSearchText, endSearchText, startSearchTooltipText, endSearchTooltipText;
>>>>>>> Stashed changes


	/*
	 * A Panel that contains the UI buttons. Uses a default FlowLayout for storing buttons.
	 */
	public ButtonPanel(GridPanel gridPanel) {
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
<<<<<<< Updated upstream
					if (face != null && face.hasAllLabeledVertices()) {
						face.setFrozenTrue();
					}
				}
				gridPanel.clearVertexNumbers();
=======
					if (face != null) {
						face.setFrozen(true);
					}
				}
				Vertex.resetCounter();
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
		clearVertexNumbersButton = new JButton("Renumber");
=======
		clearVertexNumbersButton = new JButton("Clear Labelings");
>>>>>>> Stashed changes
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
		nextNumberLabel.setFocusable(false);
		nextNumberLabel.setToolTipText("<html>" 
				+ "Numbers waiting to be inputted will appear here");

		// Create the numberQueueLabel
		numberQueueLabel = new JLabel("1");
		numberQueueLabel.setFocusable(false);
		numberQueueLabel.setToolTipText("<html>" 
				+ "Numbers waiting to be inputted will appear here");
<<<<<<< Updated upstream
		
		// Create the testAllPermutationsButton
		testAllPermutationsButton = new JButton("Test All Labellings");
		testAllPermutationsButton.setFocusable(false);
		testAllPermutationsButton.setToolTipText("<html>" 
				+ "Test all permutations of possible labellings"
				+ "<br>"
				+ "WARNING: Time to calculate all permutations is (#vertices)!"
				+ "Ideally used for polyominoes with < 14 vertices");
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
=======



		// Create the testAllPermutationsButton
		startSearchText = "Find a Labeling";
		endSearchText = "Stop";
		startSearchTooltipText = "<html>" 
				+ "Attempt to find a C4-Face-Magic Labeling"
				+ "<br>"
				+ "WARNING: Time to find a labelling, if it exists, depends on polyominoe and system";
		endSearchTooltipText = "<html>" 
				+ "Stop the search for a C4-Face-Magic Labeling.";
		findLabelingButton = new JButton(startSearchText);
		findLabelingButton.setFocusable(false);
		findLabelingButton.setToolTipText(startSearchTooltipText);
		findLabelingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// If a search is not already in progress
				if (!PermutationGenerator.searchInProgress) {
					// Copy all non-null Vertices in vertexArray to vArr for more efficient permutation generation and clear all labelings
					Face[] fArr = getNonFrozenFaces();
					if (fArr.length != 0) {
						Vertex[] vArr = getNonFrozenVertices();
						for (Face f : fArr) {
							f.setFrozen(true);
						}
						Vertex.resetCounter();
						// Start finding a labeling
						permGen = new PermutationGenerator(vArr, fArr);
						permGen.start();
						// Once search has started, behave like a stop button
						findLabelingButton.setText(endSearchText);
						findLabelingButton.setToolTipText(endSearchTooltipText);
					}
				} else {
					// If a search is in progress, behave like a stop button and reset
					permGen.interrupt();
>>>>>>> Stashed changes
				}
			}
		});
		
		

		// Add the buttons to it
<<<<<<< Updated upstream
		add(testAllPermutationsButton);
		add(freezeCompleteButton);
		add(lockButton);
=======
		add(findLabelingButton);
		add(freezeButton);
		//add(lockButton);
>>>>>>> Stashed changes
		add(clearVertexNumbersButton);
		add(clearButton);
		add(nextNumberLabel);
		add(numberQueueLabel);
	}

<<<<<<< Updated upstream
=======
	/*
	 * Reset the findLabellingButton if not stopped manually.
	 */
	public static void resetFindLabellingButton() {
		findLabelingButton.setText(startSearchText);
		findLabelingButton.setToolTipText(startSearchTooltipText);
	}

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
	 * Helper method to return an array of copies of non-null values of vertexArray
	 * with labels 1 - numVertices
=======
	 * Returns an array of all non-frozen Vertices
>>>>>>> Stashed changes
	 */
	private Vertex[] getNonFrozenVertices() {
		Vertex[] vArr;
		int length = 0;
		int index = 0;
		for (Vertex v : GridPanel.vertexArray) {
			if (v != null && !v.isFrozen()) {
				length++;
			}
		}
		vArr = new Vertex[length];
		for (Vertex v : GridPanel.vertexArray) {
<<<<<<< Updated upstream
			if (v != null) {
				v.setValue(index+1);
=======
			if (v != null && !v.isFrozen()) {
>>>>>>> Stashed changes
				vArr[index++] = v;
			}
		}
		return vArr;
	}
	/*
	 * Returns an array of all non-frozen Faces
	 */
	private Face[] getNonFrozenFaces() {
		Face[] fArr;
		int length = 0;
		int index = 0;
		for (Face f : GridPanel.faceArray) {
			if (f != null && !f.isFrozen()) {
				length++;
			}
		}
		fArr = new Face[length];
		for (Face f : GridPanel.faceArray) {
<<<<<<< Updated upstream
			if (f != null) {
=======
			if (f != null && !f.isFrozen()) {
>>>>>>> Stashed changes
				fArr[index++] = f;
			}
		} 
		return fArr;
	}
}
