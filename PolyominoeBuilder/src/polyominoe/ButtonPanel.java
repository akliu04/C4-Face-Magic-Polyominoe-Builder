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
	private JButton freezeButton;

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
	 * A Button that attempts to find a C4-Face-Magic labelling.
	 */
	private static JButton findLabelingButton;

	/*
	 * A permutation generator that searches for C4-Face-Magic labellings.
	 */
	private PermutationGenerator permGen;

	/*
	 * Text used by the experimental findLabellingButton
	 */
	private static String startSearchText, endSearchText, startSearchTooltipText, endSearchTooltipText;


	/*
	 * A Panel that contains the UI buttons. Uses a default FlowLayout for storing buttons.
	 */
	public ButtonPanel(GridPanel gridPanel) {
		// Create the freezeCompleteButton
		freezeButton = new JButton("Freeze");
		freezeButton.setFocusable(false);
		freezeButton.setToolTipText("<html>" 
				+ "Freeze all polyominoes. Cannot be undone.");
		freezeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Face face : GridPanel.faceArray) {
					if (face != null) {
						face.setFrozen(true);
					}
				}
				Vertex.resetCounter();
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
		clearButton = new JButton("Clear All");
		clearButton.setFocusable(false);
		clearButton.setToolTipText("<html>" 
				+ "Clear all polyominoes");
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// If a search is not already in progress
				if (!PermutationGenerator.searchInProgress) {
					gridPanel.clearGrid();
				}
			}
		});

		// Create the clearVertexNumbersButton
		clearVertexNumbersButton = new JButton("Clear Labelings");
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
						freezeButton.doClick();
						
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
				}
			}
		});

		// Add the buttons to it
		add(findLabelingButton);
		add(freezeButton);
		//add(lockButton);
		add(clearVertexNumbersButton);
		add(clearButton);
		add(nextNumberLabel);
		add(numberQueueLabel);
	}

	/*
	 * Reset the findLabellingButton if not stopped manually.
	 */
	public static void resetFindLabellingButton() {
		findLabelingButton.setText(startSearchText);
		findLabelingButton.setToolTipText(startSearchTooltipText);
	}

	/*
	 * Updates the list of numbers that appear on screen to the most recent list
	 * of available numbers.
	 */
	public static void updateNumberQueueLabel() {
		numberQueueLabel.setText(Vertex.getNumberSet().first() + "");
	}

	/*
	 * Displays the sum of all Faces that have at least one numbered Vertex.
	 * Calculates for every single Face.
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
	 * Returns an array of all non-frozen Vertices
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
			if (v != null && !v.isFrozen()) {
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
			if (f != null && !f.isFrozen()) {
				fArr[index++] = f;
			}
		} 
		return fArr;
	}

}
