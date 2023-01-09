package polyominoe;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.awt.BorderLayout;
import java.awt.Color;

public class MainFrame extends JFrame {
	private final String APP_NAME = "Polyominoe Builder";		// Name of application
	private final static int FRAME_WIDTH = 2000;						// Width of window in pixels
	private final static int FRAME_HEIGHT = 1500;						// Height of window in pixels
	private static int gridWidth = 20;									// The grid will be a width * width grid of cells
	protected static int cellWidth = FRAME_WIDTH / gridWidth;

	private final Color GRID_PANEL_COLOR = new Color(210, 210, 210);


	private JPanel title_panel;
	private JLabel titleField;
	static JLayeredPane grid_panel;
	
	static Face[] faceArray = new Face[gridWidth * gridWidth];
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

		// Make and add the title panel to the MainFrame


		// Make and add the grid to the MainFrame
		grid_panel = new JLayeredPane();
		grid_panel.setBackground(GRID_PANEL_COLOR);
		grid_panel.setOpaque(true);
		grid_panel.setVisible(true);
		
		// Get the mouse location using position relative to grid_panel or top-left corner (0,0)
		grid_panel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// Draw a Face and Vertices where the user clicked
				int x = e.getX()/cellWidth;
				int y = e.getY()/cellWidth;
				if (x < gridWidth && y < gridWidth && x > 0 && y > 0) {
					Face face = new Face(x, y);
					
					//Print the Face coordinates on the Face
					//face.setText("(" + x + "," + y + ")");
					
					grid_panel.add(face, JLayeredPane.DEFAULT_LAYER);
					faceArray[x + y*gridWidth] = face;
					drawVertices(face);
				}
			}

		});
		add(grid_panel);
	}

	/*
	 * Draw Vertices around a Face wherever it cannot share a Vertex with another Face.
	 */
	public void drawVertices(Face face) {
		Vertex v;
		int x = face.getGridX();
		int y = face.getGridY();
		int count = 0;
		
		// Determine if Vertex at face's top-left exists already. If not, create a Vertex there.
		v = vertexArray[x + y*gridWidth];
		if (v == null) {
			v = face.createVertex(0);
			vertexArray[x + y*gridWidth] = v;
			grid_panel.add(v, JLayeredPane.PALETTE_LAYER);
			count++;
		}
		face.setV0(v);
		
		// Determine if Vertex at face's top-right exists already. If not, create a Vertex there.
		v = vertexArray[(x+1) + y*gridWidth];
		if (v == null) {
			v = face.createVertex(1);
			vertexArray[(x+1) + y*gridWidth] = v;
			grid_panel.add(v, JLayeredPane.PALETTE_LAYER);
			count++;
		}
		face.setV1(v);
		
		// Determine if Vertex at face's bottom-right exists already. If not, create a Vertex there.
		v = vertexArray[(x+1) + (y+1)*gridWidth];
		if (v == null) {
			v = face.createVertex(2);
			vertexArray[(x+1) + (y+1)*gridWidth] = v;
			grid_panel.add(v, JLayeredPane.PALETTE_LAYER);
			count++;
		}
		face.setV2(v);
		
		// Determine if Vertex at face's bottom-left exists already. If not, create a Vertex there.
		v = vertexArray[x + (y+1)*gridWidth];
		if (v == null) {
			v = face.createVertex(3);
			vertexArray[x + (y+1)*gridWidth] = v;
			grid_panel.add(v, JLayeredPane.PALETTE_LAYER);
			count++;
		}
		face.setV3(v);
		System.out.println("Drew " + count + " vertices");
	}

	/*
	 * Remove a face. Any of its vertices that are not shared
	 * with other Faces are also removed.
	 */
	public static void removeFace(Face f) {
		removeVertices(f);
		grid_panel.remove(f);
		faceArray[f.getGridX() + f.getGridY()*gridWidth] = null;
		
		grid_panel.revalidate();
		grid_panel.repaint();
	}

	/*
	 * Remove a Face's vertices if they are not shared
	 * with other Faces.
	 */
	private static void removeVertices(Face f) {
		int x = f.getGridX();
		int y = f.getGridY();
		int count = 0;
		
		// Booleans determining whether an adjacent Face exists. f0 corresponds to top-left and proceeds clockwise around Face f.
		boolean f0 = faceArray[(x-1) + (y-1)*gridWidth] == null;
		boolean f1 = faceArray[x + (y-1)*gridWidth] == null;
		boolean f2 = faceArray[(x+1) + (y-1)*gridWidth] == null;
		boolean f3 = faceArray[(x+1) + y*gridWidth] == null;
		boolean f4 = faceArray[(x+1) + (y+1)*gridWidth] == null;
		boolean f5 = faceArray[x + (y+1)*gridWidth] == null;
		boolean f6 = faceArray[(x-1) + (y+1)*gridWidth] == null;
		boolean f7 = faceArray[(x-1) + y*gridWidth] == null;
		
		// Check top-left Vertex
		if (f0 && f1 && f7) {
			grid_panel.remove(vertexArray[x + y*gridWidth]);
			vertexArray[x + y*gridWidth] = null;
			count++;
		}
		
		// Check top-right Vertex
		if (f1 && f2 && f3) {
			grid_panel.remove(vertexArray[(x+1) + y*gridWidth]);
			vertexArray[(x+1) + y*gridWidth] = null;
			count++;
		}
		
		// Check bottom-right Vertex
		if (f3 && f4 && f5) {
			grid_panel.remove(vertexArray[(x+1) + (y+1)*gridWidth]);
			vertexArray[(x+1) + (y+1)*gridWidth] = null;
			count++;
		}
		
		// Check bottom-left Vertex
		if (f5 && f6 && f7) {
			grid_panel.remove(vertexArray[x + (y+1)*gridWidth]);
			vertexArray[x + (y+1)*gridWidth] = null;
			count++;
		}
		System.out.println("Removed " + count + " vertices");
	}

}
