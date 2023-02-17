package polyominoe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.TreeSet;

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
	static JButton freezeButton;

	/*
	 * A Toggle Button that allows the user to choose whether or not Faces can be 
	 * drawn or removed. When engaged, Faces cannot be drawn or removed, and the user 
	 * can input values into Vertices. When disengaged, Faces can be drawn and/or removed
	 * but the user cannot input Vertex values.
	 */
	static JToggleButton lockButton;

	/*
	 * A Button that clears all Faces and their Vertices from grid_panel, and empties
	 * their respective arrays.
	 */
	static JButton clearButton;
	
	/*
	 * Resets the values of all Vertices.
	 */
	static JButton clearVertexNumbersButton;
	
	/*
	 * A Label that indicates a reservoir of numbers that are queued to be inputed
	 * into Vertices. Used in conjunction with numberQueueLabel.
	 */
	static JLabel nextNumberLabel;
	
	/*
	 * A Label that displays the queued numbers available to be inputed into Vertices.
	 */
	static JLabel numberQueueLabel;
	
	

	/*
	 * A Panel that contains the UI buttons. Uses a FlowLayout for storing buttons.
	 */
	public ButtonPanel(GridPanel gridPanel) {
		// Create the freezeButton
		freezeButton = new JButton("Freeze");
		freezeButton.setFocusable(false);
		freezeButton.setToolTipText("<html>" 
										+ "Freeze all polyominoes. Cannot be undone");
		freezeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Face face : MainFrame.faceArray) {
					if (face != null) {
						face.setLockedTrue();
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

		// Add the buttons to it
		add(freezeButton);
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
		for (Face f : MainFrame.faceArray) {
			if (f != null && !f.isFrozen()) {
				if (f.getSum() != 0) {
					f.setText("" + f.getSum());
				} else {
					f.setText("");
				}
			}
		}
	}
}
