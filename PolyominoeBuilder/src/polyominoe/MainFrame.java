package polyominoe;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class MainFrame extends JFrame {
	private final String APP_NAME = "Polyominoe Builder";					// Name of application
	private final int USER_SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private final int USER_SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private final int FRAME_WIDTH = USER_SCREEN_WIDTH;						// Width of window in pixels
	private final int FRAME_HEIGHT = USER_SCREEN_HEIGHT;						// Height of window in pixels

	private final int SANDBOX_SPACE = USER_SCREEN_WIDTH * 3;

	protected int cellWidth = FRAME_WIDTH / 25;								// FRAME_WIDTH / gridWidth by default

	/*
	 * Creates a gridWidth * gridWidth grid of cells, with the 
	 * left/right column and top/bottom row of cells acting as empty buffer
	 * to prevent clipping. This effectively enables (gridWidth-2) * (gridWidth-2) clickable
	 * Faces
	 */
	private int gridWidth = 76;

	/*
	 * Color of the area on which the user draws Faces
	 */
	private final Color GRID_PANEL_COLOR = new Color(210, 210, 210);

	/*
	 * A Scroll Pane that enables scrolling behavior for grid_panel
	 */
	private JScrollPane scroll_panel;

	/*
	 * A Layered Pane that contains instances Faces and Vertex. Faces reside
	 * on the bottom-most layer and Vertexes lay above Faces. The top-left
	 * corner of grid_panel serves as (0,0) for the purpose of creating/removing
	 * Faces and Vertices, rather than the top-left corner of the entire window.
	 */
	private JLayeredPane grid_panel;

	/*
	 * A JPanel that contains the UI buttons and displays useful information to the user. 
	 * Uses a FlowLayout for storing buttons.
	 */
	private ButtonPanel button_panel;



	/**
	 * Create the MainFrame which contains all panels.
	 */
	public MainFrame()  {
		// Create the Window
		setTitle(APP_NAME);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(true);

		// Create a grid_panel which will contain Face and Vertex 
		grid_panel = new GridPanel(gridWidth, cellWidth, scroll_panel, GRID_PANEL_COLOR);
		grid_panel.setPreferredSize(new Dimension(SANDBOX_SPACE,SANDBOX_SPACE));

		// Create a scroll_panel that contains grid_panel, which enables user scrolling behavior for grid_panel
		scroll_panel = new JScrollPane(grid_panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_panel.getVerticalScrollBar().setUnitIncrement(FRAME_HEIGHT / 10);
		scroll_panel.getHorizontalScrollBar().setUnitIncrement(FRAME_WIDTH / 15);

		// Create a ButtonPanel that contains all UI buttons 
		button_panel = new ButtonPanel((GridPanel) grid_panel);
		button_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		// Add the scroll_panel (which contains grid_panel) to the center of the MainFrame and the button_panel to the top of the window
		add(scroll_panel, BorderLayout.CENTER);
		add(button_panel, BorderLayout.PAGE_START);
	}

}
