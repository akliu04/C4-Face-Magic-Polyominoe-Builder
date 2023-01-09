package polyominoe;

public class Vertex extends CircleButton{
	private int value;
	
	/*
	 * Create a Vertex represented by a circle. The Vertex's location is determined by its top-left corner, as if it
	 * was drawn like a square. The default parameters 0 & 2 construct a circular button.
	 */
	public Vertex() {
		super("");
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
}
