package gj.quoridor.player.falai;
import gj.quoridor.player.falai.Ways;
import gj.quoridor.player.falai.PawnMove;

public class AvoidDeadEnd extends MoveStrategy {

	private int myIndex = PlayerIndexes.myPlayer.getValue();

	@Override
	public int[] getMove() {
		Ways w = Ways.UP;
		if (players[myIndex].isFirst()) w = Ways.DOWN;
		if (!Table.getInstance().getTable()[players[myIndex].getRow()][players[myIndex].getColumn()].getDirection(w)) {
			int rightSpace = spaceCounter(w, Ways.RIGHT, 2);
			int leftSpace = spaceCounter(w, Ways.LEFT, -2);

			if ((leftSpace % 2 == 0 && rightSpace % 2 == 0) || (leftSpace % 2 != 0 && rightSpace % 2 != 0)) return (new FollowPath()).getMove();

			int dy = 2;
			Ways wy = Ways.RIGHT;
			if (leftSpace % 2 != 0) {
				dy = -2;
				wy = Ways.LEFT;
			}

			if (!Table.getInstance().getTable()[players[myIndex].getRow()][players[myIndex].getColumn()].getDirection(wy)) return (new FollowPath()).getMove();

			JPSNode originalPath = getPath(players[myIndex].getRow(), players[myIndex].getColumn(), players[myIndex].getGoal());
			JPSNode secondPath = getPath(players[myIndex].getRow(), players[myIndex].getColumn() + dy, players[myIndex].getGoal());

			return getBestPath(originalPath, secondPath, wy);
		}
		return (new FollowPath()).getMove();
	}

	private JPSNode getPath(int x, int y, int goal) {
		JPSNode startNode = new JPSNode();
		JPSNode goalNode = new JPSNode();
		JPSNode path = new JPSNode();

		startNode.setX(x);
		startNode.setY(y);
		goalNode.setX(goal);
		JPSCore.getInstance().findPath(startNode, goalNode, path);
		return path;
	}

	private int spaceCounter(Ways wx, Ways wy, int dy) {
		int x = players[myIndex].getRow();
		int y = players[myIndex].getColumn();
		
		while (!Table.getInstance().getTable()[x][y].getDirection(wx) && Table.getInstance().getTable()[x][y].getDirection(wy)) y += dy;
		
		int space = 1;
		while (Table.getInstance().getTable()[x][y].getDirection(wy) && Table.getInstance().getTable()[x][y].getDirection(wx)) {
			space++;
			y += dy;
		}
		return space;
	}

	private int[] getBestPath(JPSNode firstPath, JPSNode secondPath, Ways w) {
		if (firstPath.getPathLength() >= secondPath.getPathLength()) {
			JPSNode app = secondPath;
			JPSNode prev = app;
			while (app != null) {
				prev = app;
				app = app.getParent();
			}
			JPSNode startNode = new JPSNode();
			startNode.setX(players[myIndex].getRow());
			startNode.setY(players[myIndex].getColumn());
			if (prev.equals(startNode)) return (new FollowPath()).getMove();

			PawnMove p = new PawnMove();
			p.encode(w, players[PlayerIndexes.myPlayer.getValue()].isFirst());
			Table.getInstance().applyMove(p.decode(players[PlayerIndexes.myPlayer.getValue()].isFirst()), PlayerIndexes.myPlayer.getValue());
			return p.getMove();
		}
		return (new FollowPath()).getMove();
	}

}
