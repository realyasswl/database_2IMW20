package bplus;

public class Wrapped {
	private Node c;
	private int i;
	public Wrapped(Node c,  int i){
		this.c=c;
		this.i=i;
	}
	public Node getC() {
		return c;
	}
	public void setC(Node c) {
		this.c = c;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
}
