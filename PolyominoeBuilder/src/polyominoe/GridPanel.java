package polyominoe;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

public class GridPanel extends JLayeredPane {
	private int gridWidth;
	
	/*
	 * A reference to GridPanel used for enabling drawing/erasing
	 * Faces and Vertices off of the GridPanel
	 */
	private GridPanel grid;

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
	public GridPanel(int gridWidth, int cellWidth, Color panelColor) {
		this.gridWidth = gridWidth;
		grid = this;
		locked = false;

		// Create the grid_panel and add mouse behavior for adding Faces to the grid
		setBackground(panelColor);
		setOpaque(true);
		setVisible(true);

		// Get the mouse location using position relative to grid_panel (0,0)
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// Draw a Face where the user left-clicked
				Point point = e.getPoint();
				int x = (int) (point.getX()/cellWidth);
				int y = (int) (point.getY()/cellWidth);
				drawFace(e, x, y);
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				// Draw a Face wherever the user drags their mouse (left-click)
				Point point = e.getPoint();
				int x = (int) (point.getX()/cellWidth);
				int y = (int) (point.getY()/cellWidth);
				drawFace(e, x, y);
			}
		});
	}
	
	/*
	 * Helper method to draw a Face at grid coordinates (x,y).
	 * Only draws a Face on left-click
	 */
	private void drawFace(MouseEvent e, int x, int y) {
		// If a Face does not already exist at the specified location and the grid is not locked
		if (MainFrame.faceArray[x + y*gridWidth] == null && !locked) {
			// Draw a Face if input was left-click, there are no adjacent frozen Faces that shouldn't be interacted with, and location is within bounds
			if (SwingUtilities.isLeftMouseButton(e) && !hasFrozenAdjacentFaces(x, y) && x < gridWidth-1 && y < gridWidth-1 && x > 0 && y > 0) {
				// Create a new Face that belongs to the (x,y) grid coordinate
				Face face = new Face(x, y, grid);

				// Add face to the bottom-most layer of grid_panel 
				add(face, JLayeredPane.DEFAULT_LAYER);
				// Store face in the faceArray
				MainFrame.faceArray[x + y*gridWidth] = face;
				// Draw Vertices onto the Face
				drawVertices(face);
				// If face shares a labeled Vertex with a pre-existing Face, update face's label
				if (face.getSum() != 0) {
					face.setText("" + face.getSum());
				}
			}
			// Revalidate the scroll_panel
			MainFrame.scroll_panel.revalidate();
		}
	}

	/*
	 * Helper method to draw Vertices around a Face wherever it cannot share a Vertex with another already existing Face.
	 */
	public void drawVertices(Face face) {
		Vertex v;
		int x = face.getGridX();
		int y = face.getGridY();

		// Determine if Vertex at face's top-left exists already. If not, create a Vertex there.
		v = MainFrame.vertexArray[x + y*gridWidth];
		if (v == null) {
			v = face.createVertex(0);
			MainFrame.vertexArray[x + y*gridWidth] = v;
			add(v, JLayeredPane.PALETTE_LAYER);
		}
		face.setV0(v);

		// Determine if Vertex at face's top-right exists already. If not, create a Vertex there.
		v = MainFrame.vertexArray[(x+1) + y*gridWidth];
		if (v == null) {
			v = face.createVertex(1);
			MainFrame.vertexArray[(x+1) + y*gridWidth] = v;
			add(v, JLayeredPane.PALETTE_LAYER);
		}
		face.setV1(v);

		// Determine if Vertex at face's bottom-right exists already. If not, create a Vertex there.
		v = MainFrame.vertexArray[(x+1) + (y+1)*gridWidth];
		if (v == null) {
			v = face.createVertex(2);
			MainFrame.vertexArray[(x+1) + (y+1)*gridWidth] = v;
			add(v, JLayeredPane.PALETTE_LAYER);
		}
		face.setV2(v);

		// Determine if Vertex at face's bottom-left exists already. If not, create a Vertex there.
		v = MainFrame.vertexArray[x + (y+1)*gridWidth];
		if (v == null) {
			v = face.createVertex(3);
			MainFrame.vertexArray[x + (y+1)*gridWidth] = v;
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
		v = MainFrame.vertexArray[x + y*gridWidth];
		if (v != null && v.isFrozen()) {
			return true;
		}

		// Determine if Vertex at face's top-right is frozen
		v = MainFrame.vertexArray[(x+1) + y*gridWidth];
		if (v != null && v.isFrozen()) {
			return true;
		}

		// Determine if Vertex at face's bottom-right is frozen
		v = MainFrame.vertexArray[(x+1) + (y+1)*gridWidth];
		if (v != null && v.isFrozen()) {
			return true;
		}

		// Determine if Vertex at face's bottom-left is frozen
		v = MainFrame.vertexArray[x + (y+1)*gridWidth];
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
		MainFrame.faceArray[f.getGridX() + f.getGridY()*gridWidth] = null;

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
		boolean f0 = MainFrame.faceArray[(x-1) + (y-1)*gridWidth] == null;
		boolean f1 = MainFrame.faceArray[x + (y-1)*gridWidth] == null;
		boolean f2 = MainFrame.faceArray[(x+1) + (y-1)*gridWidth] == null;
		boolean f3 = MainFrame.faceArray[(x+1) + y*gridWidth] == null;
		boolean f4 = MainFrame.faceArray[(x+1) + (y+1)*gridWidth] == null;
		boolean f5 = MainFrame.faceArray[x + (y+1)*gridWidth] == null;
		boolean f6 = MainFrame.faceArray[(x-1) + (y+1)*gridWidth] == null;
		boolean f7 = MainFrame.faceArray[(x-1) + y*gridWidth] == null;

		// Check top-left Vertex
		if (f0 && f1 && f7) {
			remove(MainFrame.vertexArray[x + y*gridWidth]);
			MainFrame.vertexArray[x + y*gridWidth] = null;
		}

		// Check top-right Vertex
		if (f1 && f2 && f3) {
			remove(MainFrame.vertexArray[(x+1) + y*gridWidth]);
			MainFrame.vertexArray[(x+1) + y*gridWidth] = null;
		}

		// Check bottom-right Vertex
		if (f3 && f4 && f5) {
			remove(MainFrame.vertexArray[(x+1) + (y+1)*gridWidth]);
			MainFrame.vertexArray[(x+1) + (y+1)*gridWidth] = null;
		}

		// Check bottom-left Vertex
		if (f5 && f6 && f7) {
			remove(MainFrame.vertexArray[x + (y+1)*gridWidth]);
			MainFrame.vertexArray[x + (y+1)*gridWidth] = null;
		}
	}

	/*
	 * Remove all Faces and Vertices off the screen. Also resets
	 * all numbering of Vertices and Vertex/Face arrays. 
	 */
	public void clearGrid() {
		MainFrame.faceArray = new Face[MainFrame.faceArray.length];
		MainFrame.vertexArray = new Vertex[MainFrame.vertexArray.length];
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
		for (Vertex v : MainFrame.vertexArray) {
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
		for (Face f : MainFrame.faceArray) {
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

	public void setLockedTrue() {
		locked = true;
	}

	public void setLockedFalse() {
		locked = false;
	}

}
