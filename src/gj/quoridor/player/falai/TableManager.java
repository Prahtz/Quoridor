package gj.quoridor.player.falai;

import java.util.ArrayList;
import java.util.Stack;

import gj.quoridor.player.falai.astar.AStarAlgorithm;
import gj.quoridor.player.falai.astar.AStarInterface;

public class TableManager implements AStarInterface<Cell, Ways> {

	private final int N = 17;
	private Cell[][] table;
	private PlayerPosition[] players;
	private int myDistance;
	private int hisDistance;
	private int myIndex = PlayerIndexes.myPlayer.getValue();
	private int hisIndex = PlayerIndexes.hisPlayer.getValue();
	private final int MAX_PATH_DISTANCE = 15;
	private final int MIN_PATH_DISTANCE = 3;

	private Stack<ArrayList<int[]>> moveStack;
	private boolean goLeft = false, goRight = false;

	public TableManager() {
		createTable();
		players = new PlayerPosition[2];
		moveStack = new Stack<ArrayList<int[]>>();
	}

	private void createTable() {
		table = new Cell[N][N];
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
			players[myIndex] = new PlayerPosition(0, 8, 16);
			players[hisIndex] = new PlayerPosition(16, 8, 0);
			players[myIndex].setFirst(isFirst);
		} else {
			players[myIndex] = new PlayerPosition(16, 8, 0);
			players[hisIndex] = new PlayerPosition(0, 8, 16);
			players[hisIndex].setFirst(!isFirst);
		}
	}

	public PlayerPosition[] getPlayers() {
		return players;
	}

	public void setPlayers(PlayerPosition[] players) {
		this.players = players;
	}

	public int[] makeMove() {
		myDistance = getDistance(myIndex);
		hisDistance = getDistance(hisIndex);
		int[] result;

		if (moveType()) {
			if(pathType()) {
				result = avoidDeadEnd();
			}
			else {
				result = followPath();
			}
		} else {
			result = stretchPath();
		}
		return result;
	}

	public boolean checkMove(Ways w, int playerIndex) {
		PlayerPosition p = players[playerIndex];
		if (!table[p.getRow()][p.getColumn()].getDirection(w)) return false;
		return true;
	}

	public void applyMove(Ways w, int playerIndex) {
		PlayerPosition p = players[playerIndex];
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
		if (wall[0] >= 0 && wall[0] < N && wall[1] >= 0 && wall[1] < N) {
			if (players[playerIndex].getWallCount() != 0) {
				if (table[wall[0]][wall[1]].getCellType() == CellTypes.WALL) {
					int myRow = players[myIndex].getRow();
					int myColumn = players[myIndex].getColumn();
					int myGoal = players[myIndex].getGoal();

					int hisRow = players[hisIndex].getRow();
					int hisColumn = players[hisIndex].getColumn();
					int hisGoal = players[hisIndex].getGoal();

					applyMove(wall, playerIndex);
					if (AStarAlgorithm.minimumPath(myRow, myColumn, myGoal, this, new Cell()) && AStarAlgorithm.minimumPath(hisRow, hisColumn, hisGoal, this, new Cell())) {
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
			if (wall[0] != 0) {
				table[wall[0] - 2][wall[1]].setCellType(CellTypes.NULL);
			}
			table[wall[0] + 1][wall[1] - 1].setCellType(CellTypes.NULL);
			table[wall[0] + 2][wall[1]].setCellType(CellTypes.NULL);

			table[wall[0]][wall[1] + 1].setDirection(Ways.LEFT, false);
			table[wall[0]][wall[1] - 1].setDirection(Ways.RIGHT, false);
			table[wall[0] + 2][wall[1] + 1].setDirection(Ways.LEFT, false);
			table[wall[0] + 2][wall[1] - 1].setDirection(Ways.RIGHT, false);
		} else {
			if (wall[1] != 0) {
				table[wall[0]][wall[1] - 2].setCellType(CellTypes.NULL);
			}
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
			if (wall[0] != 0 && table[wall[0] - 2][wall[1]].getCellType() == CellTypes.NULL)
				result.add(new int[] {wall[0] - 2, wall[1]});
			if (table[wall[0] + 1][wall[1] - 1].getCellType() == CellTypes.NULL)
				result.add(new int[] {wall[0] + 1, wall[1] - 1});
			if (table[wall[0] + 2][wall[1]].getCellType() == CellTypes.NULL)
				result.add(new int[] {wall[0] + 2, wall[1]});
		} else {
			if (wall[1] != 0 && table[wall[0]][wall[1] - 2].getCellType() == CellTypes.NULL)
				result.add(new int[] {wall[0], wall[1] - 2});
			if (table[wall[0]][wall[1] + 2].getCellType() == CellTypes.NULL)
				result.add(new int[] {wall[0], wall[1] + 2});
			if (table[wall[0] - 1][wall[1] + 1].getCellType() == CellTypes.NULL)
				result.add(new int[] {wall[0] - 1, wall[1] + 1});
		}

		moveStack.push(result);
	}


	public void restoreMove(Ways w, int playerIndex) {
		PlayerPosition p = players[playerIndex];
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

	private void restoreMove(int[] wall, int playerIndex) {
		players[playerIndex].setWallCount(players[playerIndex].getWallCount() + 1);
		table[wall[0]][wall[1]].setCellType(CellTypes.WALL);
		if (wall[0] % 2 == 0) {
			table[wall[0] + 1][wall[1] - 1].setCellType(CellTypes.WALL);
			table[wall[0] + 2][wall[1]].setCellType(CellTypes.WALL);

			table[wall[0]][wall[1] + 1].setDirection(Ways.LEFT, true);
			table[wall[0]][wall[1] - 1].setDirection(Ways.RIGHT, true);
			table[wall[0] + 2][wall[1] + 1].setDirection(Ways.LEFT, true);
			table[wall[0] + 2][wall[1] - 1].setDirection(Ways.RIGHT, true);

			if (wall[0] != 0)
				table[wall[0] - 2][wall[1]].setCellType(CellTypes.WALL);
		} else {
			if (wall[1] + 2 != N - 1)
				table[wall[0]][wall[1] + 2].setCellType(CellTypes.WALL);
			table[wall[0] - 1][wall[1] + 1].setCellType(CellTypes.WALL);

			table[wall[0] - 1][wall[1]].setDirection(Ways.DOWN, true);
			table[wall[0] + 1][wall[1]].setDirection(Ways.UP, true);
			table[wall[0] - 1][wall[1] + 2].setDirection(Ways.DOWN, true);
			table[wall[0] + 1][wall[1] + 2].setDirection(Ways.UP, true);

			if (wall[1] != 0)
				table[wall[0]][wall[1] - 2].setCellType(CellTypes.WALL);
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
	
	private boolean moveType() {
		boolean result = false;

		if(hisDistance > MAX_PATH_DISTANCE || myDistance > MAX_PATH_DISTANCE + 2) return true;
		
		if (players[hisIndex].getWallCount() == PlayerPosition.MAX_WALL && players[myIndex].getWallCount() == PlayerPosition.MAX_WALL) {
			if (hisDistance <= MIN_PATH_DISTANCE) {
				result = false;
			} else {
				result = true;
			}

		} else {
			if (players[myIndex].getWallCount() == PlayerPosition.MAX_WALL && players[hisIndex].getWallCount() == PlayerPosition.MAX_WALL - 1) {
				result = false;
			} else if (myDistance <= hisDistance) {
				if ((players[myIndex].getWallCount() == players[hisIndex].getWallCount() && hisDistance > MIN_PATH_DISTANCE)
						|| myDistance <= MIN_PATH_DISTANCE - 1)
					result = true;
				else {
					result = false;
				}
			} else {
				if (players[myIndex].getWallCount() > players[hisIndex].getWallCount() || hisDistance <= MIN_PATH_DISTANCE) {
					result = false;
				} else {
					result = true;
				}
			}
		}
		return result;
	}

	private boolean pathType() {
		int row = players[myIndex].getRow();
		int column = players[myIndex].getColumn();
		int goal = players[myIndex].getGoal();
		if (players[hisIndex].getWallCount() != 0 && ((goal == 0 && !table[row][column].getDirection(Ways.UP)) || (goal == N - 1 && !table[row][column].getDirection(Ways.DOWN)))) {
			for (int i = column; i >= 2; i -= 2) {
				if (!table[row][i].getDirection(Ways.LEFT)) {
					return false;
				}
			}
			for (int i = column; i < N - 1; i += 2) {
				if (!table[row][i].getDirection(Ways.RIGHT)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public int[] followPath() {
		goLeft = false;
		goRight = false;
		int row = players[myIndex].getRow();
		int column = players[myIndex].getColumn();
		int goal = players[myIndex].getGoal();
		Cell path = new Cell();
		AStarAlgorithm.minimumPath(row, column, goal, this, path);
		Ways w = pathToDirection(path);

		PawnMove p = new PawnMove();
		p.encode(w, players[myIndex].isFirst());
		applyMove(p.decode(players[myIndex].isFirst()), myIndex);
		return p.getMove();
	}
	
	public Ways pathToDirection(Cell goalNode) {
		Ways w;
		int[] pawnCoordinates = new int[2];
		pawnCoordinates[0] = players[myIndex].getRow();
		pawnCoordinates[1] = players[myIndex].getColumn();

		while (goalNode.getParent().getX() != pawnCoordinates[0] || goalNode.getParent().getY() != pawnCoordinates[1]) {
			goalNode = goalNode.getParent();
		}

		if (goalNode.getY() == pawnCoordinates[1] - 2) {
			w = Ways.LEFT;
		} else if (goalNode.getY() == pawnCoordinates[1] + 2) {
			w = Ways.RIGHT;
		} else if (goalNode.getX() == pawnCoordinates[0] - 2) {
			w = Ways.UP;
		} else if (goalNode.getX() == pawnCoordinates[0] + 2) {
			w = Ways.DOWN;
		} else {
			w = null;
		}

		return w;
	}

	private int[] avoidDeadEnd() {
		if (!goLeft && !goRight) {
			Ways wlr = leftOrRight();
			if (wlr == Ways.LEFT) goLeft = true;
			else goRight = true;
			return continueAvoidingDeadEnd(wlr);
		} else {
			Ways w = Ways.RIGHT;
			if (goLeft) w = Ways.LEFT;
			return continueAvoidingDeadEnd(w);
		}
	}

	public Ways leftOrRight() {
		int row = players[myIndex].getRow();
		int column = players[myIndex].getColumn();
		int goal = players[myIndex].getGoal();
		Ways w = Ways.UP;
		if (goal == N - 1) w = Ways.DOWN;

		int i;
		for (i = column; i >= 0 && !table[row][i].getDirection(w); i -= 2);
		if (i < 0) return Ways.RIGHT;

		int k;
		for (k = column; k < N && !table[row][i].getDirection(w); k += 2);
		if (k >= N) return Ways.LEFT;

		int j;
		for (j = i; i >= 0 && table[row][i].getDirection(w); i -= 2, j++);
		if (j % 2 == 0) return Ways.RIGHT;
		return Ways.LEFT;
	}

	public int[] continueAvoidingDeadEnd(Ways w) {
		if (checkDeadEnd(w) && checkMove(w, myIndex)) {
			PawnMove p = new PawnMove();
			p.encode(w, players[myIndex].isFirst());
			applyMove(p.decode(players[myIndex].isFirst()), myIndex);
			return p.getMove();
		}
		goLeft = false;
		goRight = false;
		return followPath();
	}

	public boolean checkDeadEnd(Ways w) {
		int row = players[myIndex].getRow();
		int column = players[myIndex].getColumn();
		int goal = players[myIndex].getGoal();

		int q = -1;

		if (w == Ways.LEFT) {
			for (int i = column; i >= 0; i -= 2) {
				if ((goal == 0 && table[row][i].getDirection(Ways.UP)) || (goal == N - 1 && table[row][i].getDirection(Ways.DOWN))) {
					q = i;
					break;
				}
			}
		} else {
			for (int i = column; i < N; i += 2) {
				if ((goal == 0 && table[row][i].getDirection(Ways.UP)) || (goal == N - 1 && table[row][i].getDirection(Ways.DOWN))) {
					q = i;
					break;
				}
			}
		}

		if (goal == 0) {
			for (int i = row; (i >= 2 && i >= row - 4); i -= 2) {
				if (!table[i][q].getDirection(Ways.UP)) {
					return false;
				}
			}
		} else {
			for (int i = row; (i < N - 1 && i <= row + 4); i += 2) {
				if (!table[i][q].getDirection(Ways.DOWN)) {
					return false;
				}
			}
		}

		return true;
	}

	private int[] stretchPath() {
		int x = players[hisIndex].getRow();
		int y = players[hisIndex].getColumn();
		int goal = players[hisIndex].getGoal();
		Cell path = new Cell();
		AStarAlgorithm.minimumPath(x, y, goal, this, path);

		ArrayList<int[]> walls;
		int[] result = new int[] { -1, -1 };
		int maxCell = hisDistance;
		
		while (path != null) {
			walls = getStretchingWallsByCell(path, hisIndex);
			int newDistance, myNewDistance;
			int max = hisDistance;
			int myMin = myDistance;
			int maxIndex = -1;
			for (int i = 0; i < walls.size(); i++) {
				applyMove(walls.get(i), myIndex);
				newDistance = getDistance(hisIndex);
				myNewDistance = getDistance(myIndex);
				restoreMove(walls.get(i), myIndex);
				if (newDistance > max) {
					max = newDistance;
					maxIndex = i;
					myMin = myNewDistance;
				} else if (newDistance == max) {
					if(myNewDistance < myMin) {
						max = newDistance;
						maxIndex = i;
						myMin = myNewDistance;
					}
				}
			}
			if (max > maxCell) {
				maxCell = max;
				result = walls.get(maxIndex);
			} else if (max == maxCell) {
				if (result[0] != -1) {
					maxCell = max;
					result = walls.get(maxIndex);
				}
			}
			path = path.getParent();
		}
		if (result[0] == -1) {
			result = followPath();
		} else {
			applyMove(result, myIndex);
			WallMove w = new WallMove();
			w.encode(result);
			result = w.getMove();
		}
		return result;
	}

	private ArrayList<int[]> getStretchingWallsByCell(Cell path, int playerIndex) {
		ArrayList<int[]> result = new ArrayList<int[]>();

		int row = path.getX();
		int column = path.getY();

		int[] wall = new int[2];
		
		// Muro verticale vicino SX
		wall[0] = row; 
		wall[1] = column - 1;

		if (wall[1] >= 0 && checkMove(wall, myIndex)) {
			result.add(wall);
		}

		wall = new int[2];
		
		// Muro verticale vicino DX
		wall[0] = row; 
		wall[1] = column + 1;

		if (wall[1] < N && checkMove(wall, myIndex)) {
			result.add(wall);
		}

		wall = new int[2];
		
		// Muro verticale lontano SX
		wall[0] = row - 2; 
		wall[1] = column - 1;

		if (wall[0] >= 0 && wall[1] >= 0 && checkMove(wall, myIndex)) {
			result.add(wall);
		}

		wall = new int[2];
		
		// Muro verticale lontano DX
		wall[0] = row - 2; 
		wall[1] = column + 1;

		if (wall[0] >= 0 && wall[1] < N && checkMove(wall, myIndex)) {
			result.add(wall);
		}

		if (players[playerIndex].isFirst()) {
			row = row + 1;
		} else {
			row = row - 1;
		}

		wall = new int[2];
		
		// Muro orizzontale frontale
		wall[0] = row; 
		wall[1] = column;
		if (wall[0] < N && wall[0] >= 0 && checkMove(wall, myIndex)) {
			result.add(wall);
		}

		wall = new int[2];
		
		// Muro orizzontale spostato
		wall[0] = row; 
		wall[1] = column - 2;
		if (wall[0] < N && wall[0] >= 0 && wall[1] >= 0 && checkMove(wall, myIndex)) {
			result.add(wall);
		}

		return result;
	}

	public void printMatrix(Cell[][] table) {
		System.out.println("My Player:  " + players[myIndex].getWallCount()
				+ "\nHis Player:  " + players[hisIndex].getWallCount());
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				if (table[i][j].getCellType() == CellTypes.WALL) {
					System.out.print("x ");
				} else if (i == players[myIndex].getRow()
						&& j == players[myIndex].getColumn()) {
					if (players[myIndex].isFirst()) {
						System.out.print("R ");
					} else {
						System.out.print("B ");
					}
				} else if (i == players[hisIndex].getRow()
						&& j == players[hisIndex].getColumn()) {
					if (players[hisIndex].isFirst()) {
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

	public int getDistance(int playerIndex) {
		int x = players[playerIndex].getRow();
		int y = players[playerIndex].getColumn();
		int goal = players[playerIndex].getGoal();
		Cell path = new Cell();
		AStarAlgorithm.minimumPath(x, y, goal, this, path);
		return getPathLength(path);
	}

	private int getPathLength(Cell rootNode) {
		return rootNode.getPathLength();
	}
	
	@Override
	public Cell getNewNode(Cell parent, int x, int y, int g, int h) {
		Cell s = new Cell();
		s.setParent(parent);
		s.setG(g);
		s.setH(h);
		s.setX(x);
		s.setY(y);
		return s;
	}

	@Override
	public Cell generateSuccessor(Cell q, Ways w, int goal) {
		Cell s = new Cell();
		switch (w) {
		case UP:
			s = new Cell();
			s.setParent(q);
			s.setX(q.getX() - 2);
			s.setY(q.getY());
			s.setG(q.getG() + 1);
			break;
		case DOWN:
			s = new Cell();
			s.setParent(q);
			s.setX(q.getX() + 2);
			s.setY(q.getY());
			s.setG(q.getG() + 1);
			break;
		case LEFT:
			s = new Cell();
			s.setParent(q);
			s.setX(q.getX());
			s.setY(q.getY() - 2);
			s.setG(q.getG() + 1);
			break;
		case RIGHT:
			s = new Cell();
			s.setParent(q);
			s.setX(q.getX());
			s.setY(q.getY() + 2);
			s.setG(q.getG() + 1);
			break;
		default:
			return null;
		}
		s.setH(generateH(s.getX(), goal));
		s.setF(s.getG() + s.getH());
		return s;
	}

	@Override
	public ArrayList<Ways> generateDirections() {
		ArrayList<Ways> a = new ArrayList<Ways>();

		for (Ways w : Ways.values()) {
			a.add(w);
		}
		return a;

	}

	@Override
	public boolean getDirection(Cell q, Ways w) {
		return table[q.getX()][q.getY()].getDirection(w);
	}

	@Override
	public int generateH(int x, int goal) {
		return Math.abs(x - goal);
	}

	@Override
	public boolean checkGoal(int x, int goal) {
		return x == goal;
	}

}
