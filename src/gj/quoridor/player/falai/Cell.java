package gj.quoridor.player.falai;

import gj.quoridor.player.falai.astar.AStarNode;

public class Cell implements AStarNode<Cell> {
	
	// Campi AStarNode
	private Cell parent;
	private int x;
	private int y;
	private int f;
	private int g;
	private int h;
	private int pathLength;
	
	private Direction directions;
	private CellTypes cellType;
	
	public Cell() {
		directions = new Direction();
		cellType = CellTypes.NULL;
	}

	public boolean getDirection(Ways w) {
		return directions.get(w);
	}

	public void setDirection(Ways w, boolean b) {
		directions.set(w, b);
	}

	public CellTypes getCellType() {
		return cellType;
	}

	public void setCellType(CellTypes cellType) {
		this.cellType = cellType;
	}
	
	@Override
	public void setParent(Cell parent) {
		this.parent = parent;
	}

	@Override
	public Cell getParent() {
		return parent;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public void setX(int x) {
		this.x = x;

	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setY(int y) {
		this.y = y;

	}

	@Override
	public int getF() {
		return f;
	}

	@Override
	public void setF(int f) {
		this.f = f;

	}

	@Override
	public int getG() {
		return g;
	}

	@Override
	public void setG(int g) {
		this.g = g;

	}

	@Override
	public int getH() {
		return h;
	}

	@Override
	public void setH(int h) {
		this.h = h;
	}

	@Override
	public int getPathLength() {
		return pathLength;
	}

	@Override
	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}
	
}
