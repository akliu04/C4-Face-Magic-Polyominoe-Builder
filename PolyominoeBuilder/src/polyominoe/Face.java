package polyominoe;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class Face extends JButton {
	// The Face's four vertices. v0 corresponds to Face's top-left and proceed clockwise
	private Vertex v0, v1, v2, v3;

	// Width of grid_panel in pixels
	private int width;

	// The absolute pixel coordinates of the Face (relative to grid_panel)
	private int xAbsLoc;
	private int yAbsLoc;

	// The coordinates of the Face as if grid_panel was partitioned into a grid
	private int xGridLoc;
	private int yGridLoc;

	// Colors
	private Color colorNormal = Color.WHITE;
	private Color colorBorder = Color.BLACK;
	private Color colorHighlight = new Color(200, 229, 247);
	static Color colorFrozen = Color.RED;

	// Boolean determines whether or not this Face is interactable
	private boolean isFrozen;

	// A reference to the GridPanel on which this Face resides on. Used for removing itself off of parentGrid.
	private GridPanel parentGrid;



	/*
	 * x and y are the grid coordinates of the Face, the Face is drawn at absolute pixel coordinates
	 * relative to grid_panel
	 */
	public Face(int x, int y, GridPanel parentGrid) {
		super();
		this.parentGrid = parentGrid;
		width = parentGrid.getCellWidth();
		xGridLoc = x;
		yGridLoc = y;
		xAbsLoc = x*width;
		yAbsLoc = y*width;

		setFocusable(false);
		setMargin(new Insets(0, 0, 0, 0));
		setFont(new Font("Arial", Font.PLAIN, width / 4));
		setBackground(colorNormal);
		setSize(width, width);
		setLocation(xAbsLoc, yAbsLoc);
		isFrozen = false;

		setRolloverEnabled(false);

		mouseBehavior();
		eraserBehavior();
	}

	/*
	 * Enables behavior for deleting Faces. When clicked on with any mouse button
	 * or dragged over with right-click (similar to an eraser tool), the Face will
	 * be removed.
	 * 
	 * Also enables hover highlight.
	 */
	private void mouseBehavior() {
		// Highlight the Face when mouse hovers over
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent e) {
				if (!isFrozen) {
					setBackground(colorHighlight);
				}
			}
			public void mouseExited(java.awt.event.MouseEvent e) {
				if (!isFrozen) {
					setBackground(colorNormal);
				}
			}
		});
	}

	/*
	 * Enables behavior for deleting Faces. When clicked on with any mouse button
	 * or dragged over with right-click (similar to an eraser tool), the Face will
	 * be removed.
	 */
	private void eraserBehavior() {
		addMouseListener(new java.awt.event.MouseAdapter() {
			// Remove Face when clicked (both left or right click) only if none of its vertices are labeled
			public void mousePressed(java.awt.event.MouseEvent e) {
				if (!isFrozen && !parentGrid.isLocked() && !hasLabeledVertices()) {
					parentGrid.removeFace((Face) e.getSource());
				}
			}
			// Remove Face when dragged over (right-click) only if none of its vertices are labeled
			public void mouseEntered(java.awt.event.MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && !isFrozen && !parentGrid.isLocked() && !hasLabeledVertices()) {
					parentGrid.removeFace((Face) e.getSource());
				}
			}
		});

	}


	/*
	 * Returns a new Vertex at the position specified relative to this Face 
	 * If position is 0, a Vertex is created at the top-left position of the Face,
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

	/*
	 * Returns the x-grid coordinate of this Face (not absolute position)
	 */
	public int getGridX() {
		return xGridLoc;
	}

	/*
	 * Returns the y-grid coordinate of this Face (not absolute position)
	 */
	public int getGridY() {
		return yGridLoc;
	}

	/*
	 * Set this Face's top-left Vertex value to v
	 */
	public void setV0(Vertex v) {
		v0 = v;
	}

	/*
	 * Set this Face's top-right Vertex value to v
	 */
	public void setV1(Vertex v) {
		v1 = v;
	}

	/*
	 * Set this Face's bottom-left Vertex value to v
	 */
	public void setV2(Vertex v) {
		v2 = v;
	}

	/*
	 * Set this Face's bottom-right Vertex value to v
	 */
	public void setV3(Vertex v) {
		v3 = v;
	}

	/*
	 * Returns the top-left Vertex
	 */
	public Vertex getV0() {
		return v0;
	}

	/*
	 * Returns the top-right Vertex
	 */
	public Vertex getV1() {
		return v1;
	}

	/*
	 * Returns the bottom-right Vertex
	 */
	public Vertex getV2() {
		return v2;
	}

	/*
	 * Returns the bottom-left Vertex
	 */
	public Vertex getV3() {
		return v3;
	}

	/*
	 * Returns the sum of all vertices
	 */
	public int getSum() {
		return v0.getValue() + v1.getValue() + v2.getValue() + v3.getValue();

	}

	/*
	 * Returns whether or not this Face has at least one Vertex that is labeled
	 */
	public boolean hasLabeledVertices() {
		return v0.getValue() != 0 || v1.getValue() != 0 || v2.getValue() != 0 || v3.getValue() != 0;
	}

	/*
	 * Returns whether or not this Face has ALL Vertexes labeled
	 */
	public boolean hasAllLabeledVertices() {
		return v0.getValue() != 0 && v1.getValue() != 0 && v2.getValue() != 0 && v3.getValue() != 0;
	}

	/*
	 * Whether or not this Face is frozen (ie should not be interacted with)
	 */
	public boolean isFrozen() {
		return isFrozen;
	}


	/*
	 * Freezes this Face and its Vertices
	 */
	public void setFrozen(boolean frozen) {
		if (frozen == true) {
			isFrozen = true;
			setEnabled(false);
			setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(colorFrozen, 1), 
					BorderFactory.createEmptyBorder(
							getBorder().getBorderInsets(this).top, 
							getBorder().getBorderInsets(this).left, 
							getBorder().getBorderInsets(this).bottom, 
							getBorder().getBorderInsets(this).right)));
			v0.setFrozen(true);
			v1.setFrozen(true);
			v2.setFrozen(true);
			v3.setFrozen(true);
		} else {
			isFrozen = false;
			setEnabled(true);
			setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(colorBorder, 1), 
					BorderFactory.createEmptyBorder(
							getBorder().getBorderInsets(this).top, 
							getBorder().getBorderInsets(this).left, 
							getBorder().getBorderInsets(this).bottom, 
							getBorder().getBorderInsets(this).right)));
			v0.setFrozen(false);
			v1.setFrozen(false);
			v2.setFrozen(false);
			v3.setFrozen(false);
		}
	}
}
