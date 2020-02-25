package gj.quoridor.player.falai;

public class FollowPath extends MoveStrategy {

	@Override
	public int[] getMove() {
		JPSNode startNode = new JPSNode();
		JPSNode goalNode = new JPSNode();
		JPSNode path = new JPSNode();

		startNode.setX(players[PlayerIndexes.myPlayer.getValue()].getRow());
		startNode.setY(players[PlayerIndexes.myPlayer.getValue()].getColumn());
		goalNode.setX(players[PlayerIndexes.myPlayer.getValue()].getGoal());

		JPSCore.getInstance().findPath(startNode, goalNode, path);

		Ways w = pathToDirection(path);

		PawnMove p = new PawnMove();
		p.encode(w, players[PlayerIndexes.myPlayer.getValue()].isFirst());
		Table.getInstance().applyMove(p.decode(players[PlayerIndexes.myPlayer.getValue()].isFirst()), PlayerIndexes.myPlayer.getValue());
		return p.getMove();
	}

	private Ways pathToDirection(JPSNode path) {
		Ways w;
		int[] pawnCoordinates = new int[2];
		pawnCoordinates[0] = players[PlayerIndexes.myPlayer.getValue()].getRow();
		pawnCoordinates[1] = players[PlayerIndexes.myPlayer.getValue()].getColumn();

		while (path.getParent().getX() != pawnCoordinates[0] || path.getParent().getY() != pawnCoordinates[1]) path = path.getParent();

		if (path.getY() == pawnCoordinates[1] - 2) w = Ways.LEFT;
		else if (path.getY() == pawnCoordinates[1] + 2) w = Ways.RIGHT;
		else if (path.getX() == pawnCoordinates[0] - 2) w = Ways.UP;
		else if (path.getX() == pawnCoordinates[0] + 2) w = Ways.DOWN;
		else w = null;

		return w;
	}

}
