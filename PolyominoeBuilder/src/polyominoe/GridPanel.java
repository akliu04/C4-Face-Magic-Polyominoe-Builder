package polyominoe;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class GridPanel extends JLayeredPane {
	/*
	 * The width of the grid. The grid in GridPanel has 2 extra rows and columns
	 * than the gridWidth passed into the GridPanel constructor since it treats
	 * the top/bottom-most rows and left/right-most columns as buffer space.
	 */
	private final int GRID_WIDTH;

	/*
	 * A reference to GridPanel used for drawing/erasing
	 * Faces and Vertices on GridPanel.
	 */
	private GridPanel grid;

	/*
	 * Width of the Face
	 */
	private int cellWidth;

	/*
	 * An array containing all Faces. Has essentially global scope for easy access.
	 * 
	 * To translate square-grid coordinates (x,y) to a corresponding index in faceArray, use the formula:
	 * index = x + y*gridWidth
	 * 
	 * Example: A Face with coordinates (5, 3) on a 10x10 grid (whose top-left cell is (0,0)) would be placed at index 35.
	 */
	public static Face[] faceArray;

	/*
	 * An array containing all Vertices. Has essentially global scope for easy access.
	 * 
	 * To translate square-grid coordinates (x,y) to a corresponding index in vertexArray, use the formula:
	 * index = x + y*gridWidth
	 * 
	 * This array is 'overlapped' on top of faceArray for storing each Face's Vertices.
	 */
	public static Vertex[] vertexArray;

	/*
	 * Determines whether adding/removing Faces is allowed. Allowed when true,
	 * not allowed when false.
	 */
	private boolean locked;

	/*
	 * A Layered Pane that can contain instances Faces and Vertex. Faces reside
	 * on the bottom-most layer and Vertexes lay above Faces. The top-left
	 * corner of grid_panel serves as (0,0) for the purpose of creating/removing
	 * Faces and Vertices, rather than the top-left corner of the entire window.
	 */
	public GridPanel(int gridWidth, int cellWidth, JScrollPane scrollPanel, Color panelColor) {
		grid = this;
		locked = false;
		this.GRID_WIDTH = gridWidth+2;
		this.cellWidth = cellWidth;
		// faceArray and vertexArray are effectively 2D arrays (represented with a simply 1D array)
		// with a one row and one column buffer on the left and right of the grid
		// The buffer exists since Faces drawn on the edges of the window have Vertex that clip out of the window
		faceArray = new Face[GRID_WIDTH*GRID_WIDTH];
		vertexArray = new Vertex[(GRID_WIDTH+1) * (GRID_WIDTH+1)];
		// Create the grid_panel and add mouse behavior for adding Faces to the grid
		setBackground(panelColor);
		setOpaque(true);
		setVisible(true);

		// Get the mouse location using position relative to grid_panel (0,0) and
		// draw a Face if the user left-clicked and the location is in bounds of the sandbox
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// Draw a Face where the user left-clicked
				Point point = e.getPoint();
				if (SwingUtilities.isLeftMouseButton(e) && isInSandboxBounds(point)) {
					int x = (int) (point.getX()/cellWidth);
					int y = (int) (point.getY()/cellWidth);
					drawFace(e, x, y);
				}
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				// Draw a Face wherever the user drags their mouse (left-click)
				Point point = e.getPoint();
				if (SwingUtilities.isLeftMouseButton(e) && isInSandboxBounds(point)) {
					int x = (int) (point.getX()/cellWidth);
					int y = (int) (point.getY()/cellWidth);
					drawFace(e, x, y);
				}
			}
		});
	}

	/*
	 * Helper method to draw a Face at grid coordinates (x,y).
	 * Calls helper method drawVertices to draw Face's Vertices.
	 * Only draws a Face on left-click
	 */
	private void drawFace(MouseEvent e, int x, int y) {
		// If a Face does not already exist at the specified location and the grid is not locked
		if (faceArray[x + y*GRID_WIDTH] == null && !locked) {
			// Draw a Face if there are no adjacent frozen Faces that shouldn't be interacted with
			if (!hasFrozenAdjacentFaces(x, y)) {
				// Create a new Face that belongs to the (x,y) grid coordinate
				Face face = new Face(x, y, grid);

				// Add face to the bottom-most layer of grid_panel 
				add(face, JLayeredPane.DEFAULT_LAYER);
				// Store face in the faceArray
				faceArray[x + y*GRID_WIDTH] = face;
				// Draw Vertices onto the Face
				drawVertices(face);
				// If face shares a labeled Vertex with a pre-existing Face, update face's label
				if (face.getSum() != 0) {
					face.setText("" + face.getSum());
				}
			}
		}
		repaint();
		revalidate();
	}

	/*
	 * Helper method to determine if a Point is in bounds of the sandbox
	 */
	private boolean isInSandboxBounds(Point e) {
		int x = (int) (e.getX()/cellWidth);
		int y = (int) (e.getY()/cellWidth);
		return (x < GRID_WIDTH-1) && (y < GRID_WIDTH-1) && (x > 0) && (y > 0);
	}

	/*
	 * Helper method to draw Vertices around a Face wherever it cannot share a Vertex with another already existing Face.
	 */
	public void drawVertices(Face face) {
		Vertex v;
		int x = face.getGridX();
		int y = face.getGridY();

		// Determine if Vertex at face's top-left exists already. If not, create a Vertex there.
		v = vertexArray[x + y*GRID_WIDTH];
		if (v == null) {
			v = face.createVertex(0);
			vertexArray[x + y*GRID_WIDTH] = v;
			add(v, JLayeredPane.PALETTE_LAYER);
		}
		face.setV0(v);

		// Determine if Vertex at face's top-right exists already. If not, create a Vertex there.
		v = vertexArray[(x+1) + y*GRID_WIDTH];
		if (v == null) {
			v = face.createVertex(1);
			vertexArray[(x+1) + y*GRID_WIDTH] = v;
			add(v, JLayeredPane.PALETTE_LAYER);
		}
		face.setV1(v);

		// Determine if Vertex at face's bottom-right exists already. If not, create a Vertex there.
		v = vertexArray[(x+1) + (y+1)*GRID_WIDTH];
		if (v == null) {
			v = face.createVertex(2);
			vertexArray[(x+1) + (y+1)*GRID_WIDTH] = v;
			add(v, JLayeredPane.PALETTE_LAYER);
		}
		face.setV2(v);

		// Determine if Vertex at face's bottom-left exists already. If not, create a Vertex there.
		v = vertexArray[x + (y+1)*GRID_WIDTH];
		if (v == null) {
			v = face.createVertex(3);
			vertexArray[x + (y+1)*GRID_WIDTH] = v;
			add(v, JLayeredPane.PALETTE_LAYER);
		}
		face.setV3(v);
	}

	/*
	 * Returns true if there are any frozen adjacent Faces around the given
	 * grid coordinates. (A frozen Face also has 4 frozen Vertices), thus this 
	 * method checks for adjacent frozen Vertices.
	 */
	private boolean hasFrozenAdjacentFaces(int x, int y) {
		Vertex v;

		// Determine if Vertex at face's top-left is frozen
		v = vertexArray[x + y*GRID_WIDTH];
		if (v != null && v.isFrozen()) {
			return true;
		}

		// Determine if Vertex at face's top-right is frozen
		v = vertexArray[(x+1) + y*GRID_WIDTH];
		if (v != null && v.isFrozen()) {
			return true;
		}

		// Determine if Vertex at face's bottom-right is frozen
		v = vertexArray[(x+1) + (y+1)*GRID_WIDTH];
		if (v != null && v.isFrozen()) {
			return true;
		}

		// Determine if Vertex at face's bottom-left is frozen
		v = vertexArray[x + (y+1)*GRID_WIDTH];
		if (v != null && v.isFrozen()) {
			return true;
		}

		return false;
	}

	/*
	 * Remove a face. Any of its vertices that are not shared
	 * with other Faces are also removed.
	 */
	public void removeFace(Face f) {
		removeVertices(f);
		remove(f);
		faceArray[f.getGridX() + f.getGridY()*GRID_WIDTH] = null;

		revalidate();
		repaint();
	}

	/*
	 * Remove a Face's vertices if they are not shared
	 * with other Faces.
	 */
	private void removeVertices(Face f) {
		int x = f.getGridX();
		int y = f.getGridY();

		// Booleans determining whether an adjacent Face exists. f0 corresponds to top-left and proceeds clockwise around Face f.
		boolean f0 = faceArray[(x-1) + (y-1)*GRID_WIDTH] == null;
		boolean f1 = faceArray[x + (y-1)*GRID_WIDTH] == null;
		boolean f2 = faceArray[(x+1) + (y-1)*GRID_WIDTH] == null;
		boolean f3 = faceArray[(x+1) + y*GRID_WIDTH] == null;
		boolean f4 = faceArray[(x+1) + (y+1)*GRID_WIDTH] == null;
		boolean f5 = faceArray[x + (y+1)*GRID_WIDTH] == null;
		boolean f6 = faceArray[(x-1) + (y+1)*GRID_WIDTH] == null;
		boolean f7 = faceArray[(x-1) + y*GRID_WIDTH] == null;

		// Check top-left Vertex
		if (f0 && f1 && f7) {
			remove(vertexArray[x + y*GRID_WIDTH]);
			vertexArray[x + y*GRID_WIDTH] = null;
		}

		// Check top-right Vertex
		if (f1 && f2 && f3) {
			remove(vertexArray[(x+1) + y*GRID_WIDTH]);
			vertexArray[(x+1) + y*GRID_WIDTH] = null;
		}

		// Check bottom-right Vertex
		if (f3 && f4 && f5) {
			remove(vertexArray[(x+1) + (y+1)*GRID_WIDTH]);
			vertexArray[(x+1) + (y+1)*GRID_WIDTH] = null;
		}

		// Check bottom-left Vertex
		if (f5 && f6 && f7) {
			remove(vertexArray[x + (y+1)*GRID_WIDTH]);
			vertexArray[x + (y+1)*GRID_WIDTH] = null;
		}
	}

	/*
	 * Remove all Faces and Vertices off the screen. Also resets
	 * all numbering of Vertices and Vertex/Face arrays. 
	 */
	public void clearGrid() {
		faceArray = new Face[faceArray.length];
		vertexArray = new Vertex[vertexArray.length];
		removeAll();
		Vertex.resetCounter();
		revalidate();
		repaint();
	}

	/*
	 * Resets all numbering of Vertices. Also clears the values of 
	 * Faces.
	 */
	public void clearVertexNumbers() {
		for (Vertex v : vertexArray) {
			if (v != null && !v.isFrozen()) {
				v.setText("");
				v.setValue(0);
			}
		}
		Vertex.resetCounter();
		clearFaceNumbers();
		revalidate();
		repaint();
	}

	/*
	 * Clears all Face values.
	 */
	public void clearFaceNumbers() {
		for (Face f : faceArray) {
			if (f != null && !f.isFrozen()) {
				f.setText("");
			}
		}
	}

	/*
	 * Returns whether or not the grid is locked from
	 * adding or removing Faces.
	 */
	public boolean isLocked() {
		return locked;
	}

	/*
	 * Returns cellWidth, which is how wide each Face should be.
	 */
	public int getCellWidth() {
		return cellWidth;
	}

	/*
	 * Locks the GridPanel, preventing User from adding or deleting Faces
	 */
	public void setLockedTrue() {
		locked = true;
	}

	/*
	 * Unlocks the GridPanel
	 */
	public void setLockedFalse() {
		locked = false;
	}

	/*
	 * Getter for faceArray
	 */
	public Face[] getFaceArray() {
		return faceArray;
	}

	/*
	 * Getter for vertexArray
	 */
	public Vertex[] getVertexArray() {
		return vertexArray;
	}

}
