package polyominoe;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Vertex extends CircleButton{
	private static int nextInt = 1;
	private int value;
	
	/*
	 * Create a Vertex represented by a circle. The Vertex's location is determined by its top-left corner, as if it
	 * was drawn like a square.
	 */
	public Vertex(int radius) {
		super("");
		setFont(new Font("Arial", Font.PLAIN, radius / 3));
		setSize(radius, radius);
		
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (value == 0) {
					setValue(nextInt);
					setText("" + nextInt++);
				}
			}
		});
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public static void resetCounter() {
		nextInt = 1;
	}
	
}
