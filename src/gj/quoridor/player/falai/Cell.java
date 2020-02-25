package gj.quoridor.player.falai;

public class Cell {

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

}
