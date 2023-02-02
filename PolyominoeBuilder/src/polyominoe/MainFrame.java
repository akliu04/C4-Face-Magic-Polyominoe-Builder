package polyominoe;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.BorderLayout;
import java.awt.Color;

public class MainFrame extends JFrame {
	private final String APP_NAME = "Polyominoe Builder";				// Name of application
	private final static int FRAME_WIDTH = 2000;						// Width of window in pixels
	private final static int FRAME_HEIGHT = 1500;						// Height of window in pixels
	
	/*
	 * Creates a gridWidth * gridWidth grid of cells, with the 
	 * left column and top row of cells acting as an empty. This 
	 * effectively enables (gridWidth-1) * (gridWidth-1) clickable
	 * Faces
	 */
	private static int gridWidth = 31;									
	
	protected static int cellWidth = 100;								// FRAME_WIDTH / gridWidth by default
	
	private final Color GRID_PANEL_COLOR = new Color(210, 210, 210);
	
	/*
	 * A Layered Pane that contains instances Faces and Vertex. Faces reside
	 * on the bottom-most layer and Vertexes lay above Faces. The top-left
	 * corner of grid_panel serves as (0,0) for the purpose of creating/removing
	 * Faces and Vertices, rather than the top-left corner of the entire window.
	 */
	static JLayeredPane grid_panel;
	
	/*
	 * A JPanel that contains the UI buttons. Uses a FlowLayout for storing buttons.
	 */
	static ButtonPanel button_panel;
	
	/*
	 * An array containing all Faces.
	 */
	static Face[] faceArray = new Face[(gridWidth+1) * (gridWidth+1)];
	
	/*
	 * An array containing all Vertices.
	 */
	static Vertex[] vertexArray = new Vertex[(gridWidth+1) * (gridWidth+1)];

	
	
	/**
	 * Create the frame.
	 */
	public MainFrame()  {
		// Create the Window
		setTitle(APP_NAME);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(true);

		// Create a grid_panel for enabling Face and Vertex behavior
		grid_panel = new GridPanel(gridWidth, cellWidth, GRID_PANEL_COLOR);
		
		// Create a ButtonPanel that contains all UI buttons 
		button_panel = new ButtonPanel((GridPanel) grid_panel);
		
		// Add the grid_panel to the center of the MainFrame and the button_panel to the top
		add(grid_panel, BorderLayout.CENTER);
		add(button_panel, BorderLayout.PAGE_START);
	}
}
