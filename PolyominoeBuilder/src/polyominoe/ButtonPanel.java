package polyominoe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ButtonPanel extends JPanel{

	/*
	 * A Toggle Button that allows the user to choose whether or not Faces can be 
	 * drawn or removed. When engaged, Faces cannot be drawn or removed, and the user 
	 * can input values into Vertices. When disengaged, Faces can be drawn and/or removed
	 * but the user cannot input Vertex values.
	 */
	static JToggleButton lockButton;

	/*
	 * A Button that calculates all Face values. Is only enabled when all Vertices have 
	 * a number.
	 */
	static JButton calculateButton;

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
	 * A Panel that contains the UI buttons. Uses a FlowLayout for storing buttons.
	 */
	public ButtonPanel(GridPanel gridPanel) {
		// Create the lockButton
		lockButton = new JToggleButton("Lock");
		lockButton.setFocusable(false);
		boolean locked = false;
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

		// Create the calculateButton
		calculateButton = new JButton("Calculate");
		calculateButton.setFocusable(false);
		calculateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Face f : MainFrame.faceArray) {
					if (f != null) {
						f.setText("" + f.getSum());
					}
				}
			}
		});

		// Create the clearButton
		clearButton = new JButton("Clear");
		clearButton.setFocusable(false);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridPanel.clearGrid();
			}
		});
		
		// Create the clearVertexNumbersButton
		clearVertexNumbersButton = new JButton("Renumber");
		clearVertexNumbersButton.setFocusable(false);
		clearVertexNumbersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridPanel.clearVertexNumbers();
			}
		});

		// Add the buttons to it
		add(lockButton);
		add(calculateButton);
		add(clearButton);
		add(clearVertexNumbersButton);
	}
}
