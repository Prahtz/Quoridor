package gj.quoridor.player.falai;

public abstract class JPSAbstractNode<T extends JPSAbstractNode<T>> {
	
	private T parent;
	private int x;
	private int y;
	private int g;
	private int h;
	private int f;
	private int pathLength;

	public void setParent(T parent) {
		this.parent = parent;
	}

	public T getParent() {
		return parent;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getPathLength() {
		return pathLength;
	}

	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}

}
