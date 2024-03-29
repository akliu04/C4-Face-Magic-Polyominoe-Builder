package polyominoe;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

/*
 * A Helper class to help make Vertices circular.
 */
public class CircleButton extends JButton {

	private Color colorNormal;
	private Color colorHighlight;
	private Color colorPressed;
	private Color colorFrozen;
	
	private boolean isFrozen;


	public CircleButton(String label) {
		super(label);
		setBorder(null);
		colorNormal = Color.white;
		colorHighlight = Color.gray;
		colorPressed = colorHighlight.darker();
		colorFrozen = Face.colorFrozen;
		
		isFrozen = false;

		setBackground(colorNormal);
		setFocusable(false);

		/*
     These statements enlarge the button so that it 
     becomes a circle rather than an oval.
		 */
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);

		/*
     This call causes the JButton not to paint the background.
     This allows us to paint a round background.
		 */
		setContentAreaFilled(false);

	}

	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			g.setColor(colorPressed);
		} else if (getModel().isRollover()) {
			g.setColor(colorHighlight);
		} else {
			g.setColor(colorNormal);
		}
		g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

		super.paintComponent(g);
	}

	protected void paintBorder(Graphics g) {
		if (!isFrozen) {
			g.setColor(Color.black);
		} else {
			g.setColor(colorFrozen);
		}
		g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
	}

	// Hit detection.
	Shape shape;

	public boolean contains(int x, int y) {
		// If the button has changed size,  make a new shape object.
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}
	
	public void setColorNormal(Color c) {
		colorNormal = c;
	}
	
	public void setColorHighlight(Color c) {
		colorHighlight = c;
	}
	
	public void setColorPressed(Color c) {
		colorPressed = c;
	}
	
	public void setColorFrozen(Color c) {
		colorFrozen = c;
	}
	
	public void setFrozen(boolean frozen) {
		this.isFrozen = frozen;
	}

}