package gj.quoridor.player.falai;

public class PawnInfo {

	private int row;
	private int column;
	private int goal;
	private int wallCount;
	private boolean isFirst;
	public static final int MAX_WALL = 10;

	public PawnInfo(int row, int column, int goal) {
		this.row = row;
		this.column = column;
		this.goal = goal;
		this.isFirst = false;
		this.setWallCount(MAX_WALL);
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public int getWallCount() {
		return wallCount;
	}

	public void setWallCount(int wallCount) {
		this.wallCount = wallCount;
	}

}