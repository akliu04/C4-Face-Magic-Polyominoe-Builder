package polyominoe;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Vertex extends CircleButton{
	/*
	 * The number a Vertex will take on when clicked. 
	 */
	private static int nextInt = 1;
	
	/*
	 * The queued numbers waiting to be inputed into vertices.
	 */
	private static Stack<Integer> numberStack = new Stack<Integer>();
	
	private int value;
	
	
	/*
	 * Create a Vertex represented by a circle. The Vertex's location is determined by its top-left corner, as if it
	 * was drawn like a square.
	 */
	public Vertex(int radius) {
		super("");
		setFont(new Font("Arial", Font.PLAIN, radius / 3));
		setSize(radius, radius);
		if (numberStack.isEmpty()) {
			numberStack.push(1);
		}
		
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (value == 0) {
					setValue(numberStack.peek());
					setText("" + numberStack.pop());
					
				} else {
					numberStack.push(getValue());
					setValue(0);
					setText("");
				}
				if (numberStack.isEmpty()) {
					numberStack.push(++nextInt);
				}
				ButtonPanel.calculateFaces();
				ButtonPanel.updateNumberQueueLabel();
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
		numberStack = new Stack<Integer>();
		numberStack.push(nextInt);
		ButtonPanel.updateNumberQueueLabel();
	}
	
	@SuppressWarnings("unchecked")
	public static Stack<Integer> getNumberQueue(){
		return (Stack<Integer>) numberStack.clone();
	}
}
