package gj.quoridor.player.falai;
import java.util.ArrayList;

public class StretchPath extends MoveStrategy {

	private int hisDistance;

	@Override
	public int[] getMove() {
		JPSNode startNode = new JPSNode();
		JPSNode goalNode = new JPSNode();
		JPSNode path = new JPSNode();

		startNode.setX(players[PlayerIndexes.hisPlayer.getValue()].getRow());
		startNode.setY(players[PlayerIndexes.hisPlayer.getValue()].getColumn());
		goalNode.setX(players[PlayerIndexes.hisPlayer.getValue()].getGoal());

		JPSCore.getInstance().findPath(startNode, goalNode, path);

		hisDistance = path.getPathLength();

		ArrayList<int[]> walls;
		int[] bestStretchingWall = new int[] { -1, -1 };
		int maxCell = hisDistance;
		int[] bestStretchingWallValues;

		while (path != null) {
			walls = getStretchingWallsByCell(path, PlayerIndexes.hisPlayer.getValue());
			bestStretchingWallValues = getBestStretchingWallValues(walls);
			if (bestStretchingWallValues[0] > maxCell) {
				maxCell = bestStretchingWallValues[0];
				bestStretchingWall = walls.get(bestStretchingWallValues[1]);
			} else if (bestStretchingWallValues[0] == maxCell) {
				if (bestStretchingWall[0] != -1) {
					maxCell = bestStretchingWallValues[0];
					bestStretchingWall = walls.get(bestStretchingWallValues[1]);
				}
			}
			path = path.getParent();
		}
		return getResult(bestStretchingWall);
	}

	private int[] getBestStretchingWallValues(ArrayList<int[]> walls) {
		int newDistance;
		int max = hisDistance;
		int maxIndex = -1;
		for (int i = 0; i < walls.size(); i++) {
			Table.getInstance().applyMove(walls.get(i), PlayerIndexes.myPlayer.getValue());
			newDistance = Table.getInstance().getDistance(PlayerIndexes.hisPlayer.getValue());
			Table.getInstance().restoreMove(walls.get(i), PlayerIndexes.myPlayer.getValue());
			if (newDistance > max) {
				max = newDistance;
				maxIndex = i;
			}
		}
		return new int[] { max, maxIndex };
	}

	private int[] getResult(int[] bestStretchingWall) {
		if (bestStretchingWall[0] == -1) {
			bestStretchingWall = (new AvoidDeadEnd()).getMove();
		} else {
			Table.getInstance().applyMove(bestStretchingWall, PlayerIndexes.myPlayer.getValue());
			WallMove w = new WallMove();
			w.encode(bestStretchingWall);
			bestStretchingWall = w.getMove();
		}
		return bestStretchingWall;
	}

	private ArrayList<int[]> getStretchingWallsByCell(JPSNode path, int playerIndex) {
		ArrayList<int[]> result = new ArrayList<int[]>();

		int row = path.getX();
		int column = path.getY();

		// Muro verticale vicino SX
		int[] wall = new int[2];
		wall[0] = row;
		wall[1] = column - 1;

		if (wall[1] >= 0 && Table.getInstance().checkMove(wall, PlayerIndexes.myPlayer.getValue())) {
			result.add(wall);
		}

		// Muro verticale vicino DX
		wall = new int[2];
		wall[0] = row;
		wall[1] = column + 1;

		if (wall[1] < Table.getInstance().getDimension()
				&& Table.getInstance().checkMove(wall, PlayerIndexes.myPlayer.getValue())) {
			result.add(wall);
		}

		// Muro verticale lontano SX
		wall = new int[2];
		wall[0] = row - 2;
		wall[1] = column - 1;

		if (wall[0] >= 0 && wall[1] >= 0 && Table.getInstance().checkMove(wall, PlayerIndexes.myPlayer.getValue())) {
			result.add(wall);
		}

		// Muro verticale lontano DX
		wall = new int[2];
		wall[0] = row - 2;
		wall[1] = column + 1;

		if (wall[0] >= 0 && wall[1] < Table.getInstance().getDimension()
				&& Table.getInstance().checkMove(wall, PlayerIndexes.myPlayer.getValue())) {
			result.add(wall);
		}

		if (players[playerIndex].isFirst()) {
			row = row + 1;
		} else {
			row = row - 1;
		}

		// Muro orizzontale frontale
		wall = new int[2];
		wall[0] = row;
		wall[1] = column;
		
		if (wall[0] < Table.getInstance().getDimension() && wall[0] >= 0
				&& Table.getInstance().checkMove(wall, PlayerIndexes.myPlayer.getValue())) {
			result.add(wall);
		}

		// Muro orizzontale spostato
		wall = new int[2];
		wall[0] = row;
		wall[1] = column - 2;
		
		if (wall[0] < Table.getInstance().getDimension() && wall[0] >= 0 && wall[1] >= 0
				&& Table.getInstance().checkMove(wall, PlayerIndexes.myPlayer.getValue())) {
			result.add(wall);
		}

		return result;
	}

}
