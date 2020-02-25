package gj.quoridor.player.falai;
import java.util.ArrayList;
import java.util.Stack;

public final class Table {

	private static final Table instance = new Table();
	private final int dimension = 17;
	private Cell[][] table;
	private PawnInfo[] players;
	private Stack<ArrayList<int[]>> moveStack;

	private Table() {}

	public static Table getInstance() {
		return instance;
	}

	public int getDimension() {
		return dimension;
	}

	public Cell[][] getTable() {
		return table;
	}

	public void restoreTable() {
		createTable();
		players = new PawnInfo[2];
		moveStack = new Stack<ArrayList<int[]>>();
	}

	private void createTable() {
		table = new Cell[dimension][dimension];
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				table[i][j] = new Cell();
			}
		}
		for (int i = 0; i < table.length; i = i + 2) {
			for (int j = 0; j < table[0].length; j = j + 2) {
				table[i][j].setCellType(CellTypes.PAWN);
				if (i == 0) {
					table[i][j].setDirection(Ways.UP, false);
					table[i][j].setDirection(Ways.DOWN, true);
				} else if (i == table.length - 1) {
					table[i][j].setDirection(Ways.UP, true);
					table[i][j].setDirection(Ways.DOWN, false);
				} else {
					table[i][j].setDirection(Ways.UP, true);
					table[i][j].setDirection(Ways.DOWN, true);
				}
				if (j == 0) {
					table[i][j].setDirection(Ways.LEFT, false);
					table[i][j].setDirection(Ways.RIGHT, true);
				} else if (j == table[0].length - 1) {
					table[i][j].setDirection(Ways.LEFT, true);
					table[i][j].setDirection(Ways.RIGHT, false);
				} else {
					table[i][j].setDirection(Ways.LEFT, true);
					table[i][j].setDirection(Ways.RIGHT, true);
				}
			}
		}
		for (int i = 0; i < table.length - 1; i++) {
			for (int j = 1 - (i % 2); j < table[0].length - 1; j = j + 2) {
				table[i][j].setCellType(CellTypes.WALL);
			}
		}
	}

	public void setPlayers(boolean isFirst) {
		if (isFirst) {
			players[PlayerIndexes.myPlayer.getValue()] = new PawnInfo(0, 8, 16);
			players[PlayerIndexes.hisPlayer.getValue()] = new PawnInfo(16, 8, 0);
			players[PlayerIndexes.myPlayer.getValue()].setFirst(isFirst);
		} else {
			players[PlayerIndexes.myPlayer.getValue()] = new PawnInfo(16, 8, 0);
			players[PlayerIndexes.hisPlayer.getValue()] = new PawnInfo(0, 8, 16);
			players[PlayerIndexes.hisPlayer.getValue()].setFirst(!isFirst);
		}
	}

	public PawnInfo[] getPlayers() {
		return players;
	}

	public void setPlayers(PawnInfo[] players) {
		this.players = players;
	}

	public boolean checkMove(Ways w, int playerIndex) {
		PawnInfo p = players[playerIndex];
		if (!table[p.getRow()][p.getColumn()].getDirection(w))
			return false;
		return true;
	}

	public void applyMove(Ways w, int playerIndex) {
		PawnInfo p = players[playerIndex];
		switch (w) {
		case UP:
			p.setRow(p.getRow() - 2);
			break;
		case DOWN:
			p.setRow(p.getRow() + 2);
			break;
		case LEFT:
			p.setColumn(p.getColumn() - 2);
			break;
		case RIGHT:
			p.setColumn(p.getColumn() + 2);
			break;
		}
	}

	public boolean checkMove(int[] wall, int playerIndex) {
		if (wall[0] >= 0 && wall[0] < dimension && wall[1] >= 0 && wall[1] < dimension) {
			if (players[playerIndex].getWallCount() != 0) {
				if (table[wall[0]][wall[1]].getCellType() == CellTypes.WALL) {
					JPSNode myStartNode = new JPSNode();
					JPSNode myGoalNode = new JPSNode();
					myStartNode.setX(players[PlayerIndexes.myPlayer.getValue()].getRow());
					myStartNode.setY(players[PlayerIndexes.myPlayer.getValue()].getColumn());
					myGoalNode.setX(players[PlayerIndexes.myPlayer.getValue()].getGoal());

					JPSNode hisStartNode = new JPSNode();
					JPSNode hisGoalNode = new JPSNode();
					hisStartNode.setX(players[PlayerIndexes.hisPlayer.getValue()].getRow());
					hisStartNode.setY(players[PlayerIndexes.hisPlayer.getValue()].getColumn());
					hisGoalNode.setX(players[PlayerIndexes.hisPlayer.getValue()].getGoal());

					applyMove(wall, playerIndex);
					if (JPSCore.getInstance().findPath(myStartNode, myGoalNode, new JPSNode()) && JPSCore.getInstance().findPath(hisStartNode, hisGoalNode, new JPSNode())) {
						restoreMove(wall, playerIndex);
						return true;
					}
					restoreMove(wall, playerIndex);
				}
			}
		}
		return false;
	}

	public void applyMove(int[] wall, int playerIndex) {
		players[playerIndex].setWallCount(players[playerIndex].getWallCount() - 1);
		table[wall[0]][wall[1]].setCellType(CellTypes.NULL);
		toRestore(wall);
		if (wall[0] % 2 == 0) {
			if (wall[0] != 0) table[wall[0] - 2][wall[1]].setCellType(CellTypes.NULL);
			
			table[wall[0] + 1][wall[1] - 1].setCellType(CellTypes.NULL);
			table[wall[0] + 2][wall[1]].setCellType(CellTypes.NULL);

			table[wall[0]][wall[1] + 1].setDirection(Ways.LEFT, false);
			table[wall[0]][wall[1] - 1].setDirection(Ways.RIGHT, false);
			table[wall[0] + 2][wall[1] + 1].setDirection(Ways.LEFT, false);
			table[wall[0] + 2][wall[1] - 1].setDirection(Ways.RIGHT, false);
		} else {
			if (wall[1] != 0) table[wall[0]][wall[1] - 2].setCellType(CellTypes.NULL);
			
			table[wall[0]][wall[1] + 2].setCellType(CellTypes.NULL);
			table[wall[0] - 1][wall[1] + 1].setCellType(CellTypes.NULL);

			table[wall[0] - 1][wall[1]].setDirection(Ways.DOWN, false);
			table[wall[0] + 1][wall[1]].setDirection(Ways.UP, false);
			table[wall[0] - 1][wall[1] + 2].setDirection(Ways.DOWN, false);
			table[wall[0] + 1][wall[1] + 2].setDirection(Ways.UP, false);
		}
	}

	public void toRestore(int[] wall) {
		ArrayList<int[]> result = new ArrayList<int[]>();

		if (wall[0] % 2 == 0) {
			if (wall[0] != 0 && table[wall[0] - 2][wall[1]].getCellType() == CellTypes.NULL) result.add(new int[] { wall[0] - 2, wall[1] });
			if (table[wall[0] + 1][wall[1] - 1].getCellType() == CellTypes.NULL) result.add(new int[] { wall[0] + 1, wall[1] - 1 });
			if (table[wall[0] + 2][wall[1]].getCellType() == CellTypes.NULL) result.add(new int[] { wall[0] + 2, wall[1] });
		} else {
			if (wall[1] != 0 && table[wall[0]][wall[1] - 2].getCellType() == CellTypes.NULL) result.add(new int[] { wall[0], wall[1] - 2 });
			if (table[wall[0]][wall[1] + 2].getCellType() == CellTypes.NULL) result.add(new int[] { wall[0], wall[1] + 2 });
			if (table[wall[0] - 1][wall[1] + 1].getCellType() == CellTypes.NULL) result.add(new int[] { wall[0] - 1, wall[1] + 1 });
		}

		moveStack.push(result);
	}

	public void restoreMove(Ways w, int playerIndex) {
		PawnInfo p = players[playerIndex];
		switch (w) {
		case UP:
			p.setRow(p.getRow() + 2);
			break;
		case DOWN:
			p.setRow(p.getRow() - 2);
			break;
		case LEFT:
			p.setColumn(p.getColumn() + 2);
			break;
		case RIGHT:
			p.setColumn(p.getColumn() - 2);
			break;
		}
	}

	public void restoreMove(int[] wall, int playerIndex) {
		players[playerIndex].setWallCount(players[playerIndex].getWallCount() + 1);
		table[wall[0]][wall[1]].setCellType(CellTypes.WALL);
		if (wall[0] % 2 == 0) {
			table[wall[0] + 1][wall[1] - 1].setCellType(CellTypes.WALL);
			table[wall[0] + 2][wall[1]].setCellType(CellTypes.WALL);

			table[wall[0]][wall[1] + 1].setDirection(Ways.LEFT, true);
			table[wall[0]][wall[1] - 1].setDirection(Ways.RIGHT, true);
			table[wall[0] + 2][wall[1] + 1].setDirection(Ways.LEFT, true);
			table[wall[0] + 2][wall[1] - 1].setDirection(Ways.RIGHT, true);

			if (wall[0] != 0) table[wall[0] - 2][wall[1]].setCellType(CellTypes.WALL);
		} else {
			if (wall[1] + 2 != dimension - 1) table[wall[0]][wall[1] + 2].setCellType(CellTypes.WALL);
			table[wall[0] - 1][wall[1] + 1].setCellType(CellTypes.WALL);

			table[wall[0] - 1][wall[1]].setDirection(Ways.DOWN, true);
			table[wall[0] + 1][wall[1]].setDirection(Ways.UP, true);
			table[wall[0] - 1][wall[1] + 2].setDirection(Ways.DOWN, true);
			table[wall[0] + 1][wall[1] + 2].setDirection(Ways.UP, true);

			if (wall[1] != 0) table[wall[0]][wall[1] - 2].setCellType(CellTypes.WALL);
		}

		ArrayList<int[]> noRestore = moveStack.pop();
		if (!noRestore.isEmpty()) {
			int x, y;
			for (int i = 0; i < noRestore.size(); i++) {
				x = noRestore.get(i)[0];
				y = noRestore.get(i)[1];
				table[x][y].setCellType(CellTypes.NULL);
			}
		}
	}

	public int getDistance(int playerIndex) {
		PawnInfo[] players = Table.getInstance().getPlayers();
		JPSNode startNode = new JPSNode();
		JPSNode goalNode = new JPSNode();
		JPSNode path = new JPSNode();

		startNode.setX(players[playerIndex].getRow());
		startNode.setY(players[playerIndex].getColumn());
		goalNode.setX(players[playerIndex].getGoal());

		JPSCore.getInstance().findPath(startNode, goalNode, path);
		return path.getPathLength();
	}
	
	// Metodo di test
	public void printMatrix() {
		System.out.println("My Player:  " + players[PlayerIndexes.myPlayer.getValue()].getWallCount()
				+ "\nHis Player:  " + players[PlayerIndexes.hisPlayer.getValue()].getWallCount());
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				if (table[i][j].getCellType() == CellTypes.WALL) {
					System.out.print("x ");
				} else if (i == players[PlayerIndexes.myPlayer.getValue()].getRow()
						&& j == players[PlayerIndexes.myPlayer.getValue()].getColumn()) {
					if (players[PlayerIndexes.myPlayer.getValue()].isFirst()) {
						System.out.print("R ");
					} else {
						System.out.print("B ");
					}
				} else if (i == players[PlayerIndexes.hisPlayer.getValue()].getRow()
						&& j == players[PlayerIndexes.hisPlayer.getValue()].getColumn()) {
					if (players[PlayerIndexes.hisPlayer.getValue()].isFirst()) {
						System.out.print("R ");
					} else {
						System.out.print("B ");
					}
				} else if (table[i][j].getCellType() == CellTypes.PAWN) {
					System.out.print("O ");
				} else {
					System.out.print("  ");
				}

			}
			System.out.println();
		}
		System.out.println();
	}

	// Metodo di test
	public void printPathMatrix() {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					if (i == players[PlayerIndexes.myPlayer.getValue()].getRow()
							&& j == players[PlayerIndexes.myPlayer.getValue()].getColumn()) {
						if (players[PlayerIndexes.myPlayer.getValue()].isFirst()) {
							System.out.print("R");
						} else {
							System.out.print("B");
						}
					} else if (i == players[PlayerIndexes.hisPlayer.getValue()].getRow()
							&& j == players[PlayerIndexes.hisPlayer.getValue()].getColumn()) {
						if (players[PlayerIndexes.hisPlayer.getValue()].isFirst()) {
							System.out.print("R");
						} else {
							System.out.print("B");
						}
					} else
						System.out.print("O");
				}
				if (i % 2 == 0 && j % 2 != 0) {
					if (table[i][j - 1].getDirection(Ways.RIGHT)) {
						System.out.print("-");
					} else {
						System.out.print(" ");
					}
				}
				if (i % 2 != 0 && j % 2 == 0) {
					if (table[i - 1][j].getDirection(Ways.DOWN)) {
						System.out.print("|");
					} else {
						System.out.print(" ");
					}
				}
				if (i % 2 != 0 && j % 2 != 0) {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println();

	}

}
