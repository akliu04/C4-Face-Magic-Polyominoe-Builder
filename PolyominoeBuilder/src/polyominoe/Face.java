package polyominoe;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

public class Face extends JButton {
	// The Face's four vertices. v0 corresponds to Face's top-left and proceed clockwise
	private Vertex v0;					 
	private Vertex v1;
	private Vertex v2;
	private Vertex v3;

	// Sum of four Vertices
	private int sum;

	// Width of the window in pixels
	private int width;

	// The absolute pixel coordinates of the Face (relative to the app window, not the user's screen)
	private int xAbsLoc;
	private int yAbsLoc;

	// The coordinates of the Face as if the window was partitioned into a grid
	private int xGridLoc;
	private int yGridLoc;

	// Colors
	private Color colorNormal = Color.white;
	private Color colorHighlight = new Color(200, 229, 247);

	private GridPanel parentGrid;


	/*
	 * x and y are the grid coordinates of the Face, the Face is drawn at absolute pixel coordinates
	 */
	public Face(int x, int y, GridPanel parentGrid) {
		super();
		this.parentGrid = parentGrid;
		width = MainFrame.cellWidth;
		xGridLoc = x;
		yGridLoc = y;
		xAbsLoc = x*width;
		yAbsLoc = y*width;
		setFocusable(false);
		setFont(new Font("Arial", Font.PLAIN, width / 5));
		setBackground(colorNormal);
		setSize(width, width);
		setLocation(xAbsLoc, yAbsLoc);

		setRolloverEnabled(false);

		mouseBehavior();
	}

	/*
	 * Enables hover highlight and removing the Face when the mouse 
	 * is initially pressed on the Face.
	 */
	private void mouseBehavior() {
		addMouseListener(new java.awt.event.MouseAdapter() {
			// Highlight the Face when mouse hovers over
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				setBackground(colorHighlight);
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				setBackground(colorNormal);
			}

			// Remove Face when clicked
			public void mousePressed(java.awt.event.MouseEvent evt) {
				if (!parentGrid.isLocked()) {
					parentGrid.removeFace((Face) evt.getSource());
				}
			}

		});
	}


	/*
	 * Returns a new Vertex at the position specified relative to this Face 
	 * If position is 0, a Vertex is created at the top-left of the Face,
	 * and proceeds clockwise for values of 1, 2, and 3.
	 */
	public Vertex createVertex(int position) {
		Vertex vertex = new Vertex(width / 2);
		int radius = width / 2;
		vertex.setColorHighlight(colorHighlight);
		vertex.setColorPressed(colorHighlight.darker());
		int x = 0;
		int y = 0;

		if (position == 0) {
			x = xAbsLoc - radius/2;
			y = yAbsLoc - radius/2;
			v0 = vertex;
		} else if (position == 1) {
			x = xAbsLoc - radius/2 + width;
			y = yAbsLoc - radius/2;
			v1 = vertex;
		} else if (position == 2) {
			x = xAbsLoc - radius/2 + width;
			y = yAbsLoc - radius/2 + width;
			v2 = vertex;
		} else if (position == 3) {
			x = xAbsLoc - radius/2;
			y = yAbsLoc - radius/2 + width;
			v3 = vertex;
		}
		vertex.setLocation(x, y);
		return vertex;
	}

	public int getGridX() {
		return xGridLoc;
	}

	public int getGridY() {
		return yGridLoc;
	}

	public void setV0(Vertex v) {
		v0 = v;
	}

	public void setV1(Vertex v) {
		v1 = v;
	}

	public void setV2(Vertex v) {
		v2 = v;
	}

	public void setV3(Vertex v) {
		v3 = v;
	}

	public int getSum() {
		sum = v0.getValue() + v1.getValue() + v2.getValue() + v3.getValue();
		return sum;
	}

}
