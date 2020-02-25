package gj.quoridor.player.falai;
import java.util.ArrayList;

public final class JPSCore extends JPSAlgorithm<JPSNode> {

	private static JPSCore instance = new JPSCore();

	public static JPSCore getInstance() {
		return instance;
	}

	@Override
	public boolean checkGoal(JPSNode node, JPSNode goalNode) {
		return node.getX() == goalNode.getX();
	}

	@Override
	public JPSNode getNewNode(JPSNode parent, int x, int y, int g, int h, int f) {
		JPSNode n = new JPSNode();
		n.setParent(parent);
		n.setX(x);
		n.setY(y);
		n.setG(g);
		n.setH(h);
		n.setF(f);
		return n;
	}

	@Override
	public JPSNode getNewNode(JPSNode parent, int x, int y, int g, JPSNode goalNode) {
		JPSNode n = new JPSNode();
		n.setParent(parent);
		n.setX(x);
		n.setY(y);
		n.setG(g);
		n.setH(Math.abs(x - goalNode.getX()));
		n.setF(n.getG() + n.getH());
		return n;
	}

	@Override
	public ArrayList<JPSNode> findNeighbors(JPSNode q, JPSNode goalNode) {
		ArrayList<JPSNode> neighbors = new ArrayList<JPSNode>();
		ArrayList<Ways> ways = generateDirections(q);
		for (Ways w : ways) {
			JPSNode s = new JPSNode();
			switch (w) {
			case UP:
				s.setX(q.getX() - 2);
				s.setY(q.getY());
				break;
			case DOWN:
				s.setX(q.getX() + 2);
				s.setY(q.getY());
				break;
			case LEFT:
				s.setX(q.getX());
				s.setY(q.getY() - 2);
				break;
			case RIGHT:
				s.setX(q.getX());
				s.setY(q.getY() + 2);
				break;
			default:
				return null;
			}
			s.setParent(q);
			s.setG(q.getG() + 1);
			s.setH(Math.abs((s.getX() - goalNode.getX())));
			s.setF(s.getG() + s.getH());
			neighbors.add(s);
		}
		return neighbors;
	}

	@Override
	public JPSNode jump(JPSNode neighbor, JPSNode node, JPSNode goalNode) {
		if (neighbor == null || !isWalkable(neighbor, node)) return null;
		if (checkGoal(neighbor, goalNode)) return neighbor;

		int dx = neighbor.getX() - node.getX();
		int dy = neighbor.getY() - node.getY();

		if (dx != 0) {
			if ((isWalkable(neighbor.getX(), neighbor.getY(), Ways.RIGHT) && !isWalkable(neighbor.getX() - dx, neighbor.getY(), Ways.RIGHT)) 
					|| (isWalkable(neighbor.getX(), neighbor.getY(), Ways.LEFT) && !isWalkable(neighbor.getX() - dx, neighbor.getY(), Ways.LEFT))) {
				return neighbor;
			}

			if (!isWalkable(neighbor.getX(), neighbor.getY(), Ways.UP) || !isWalkable(neighbor.getX(), neighbor.getY(), Ways.DOWN)) return neighbor;

			JPSNode n1 = getNewNode(neighbor.getParent(), neighbor.getX(), neighbor.getY() + 2, neighbor.getG() + 1, goalNode);
			JPSNode n2 = getNewNode(neighbor.getParent(), neighbor.getX(), neighbor.getY() - 2, neighbor.getG() + 1, goalNode);

			if (jump(n1, neighbor, goalNode) != null || jump(n2, neighbor, goalNode) != null) return neighbor;

		} else if (dy != 0) {
			if ((isWalkable(neighbor.getX(), neighbor.getY(), Ways.UP) && !isWalkable(neighbor.getX(), neighbor.getY() - dy, Ways.UP))
					|| (isWalkable(neighbor.getX(), neighbor.getY(), Ways.DOWN) && !isWalkable(neighbor.getX(), neighbor.getY() - dy, Ways.DOWN))) {
				return neighbor;
			}

			if (!isWalkable(neighbor.getX(), neighbor.getY(), Ways.LEFT) || !isWalkable(neighbor.getX(), neighbor.getY(), Ways.RIGHT)) return neighbor;

		} else return null;
		
		JPSNode n = getNewNode(neighbor.getParent(), neighbor.getX() + dx, neighbor.getY() + dy, neighbor.getG() + 1, goalNode);
		return jump(n, neighbor, goalNode);
	}

	private ArrayList<Ways> generateDirections(JPSNode q) {
		ArrayList<Ways> a = new ArrayList<Ways>();

		for (Ways w : Ways.values()) {
			if (Table.getInstance().getTable()[q.getX()][q.getY()].getDirection(w)) a.add(w);
		}
		return a;
	}

	private boolean isWalkable(int x, int y, Ways w) {
		if (isWalkable(x, y) && Table.getInstance().getTable()[x][y].getDirection(w)) return true;
		return false;
	}

	private boolean isWalkable(int x, int y) {
		int dimension = Table.getInstance().getDimension();
		if (x >= 0 && x < dimension && y >= 0 && y < dimension) return true;
		return false;
	}

	private boolean isWalkable(JPSNode actual, JPSNode previous) {
		if (isWalkable(actual.getX(), actual.getY()) && isWalkable(previous.getX(), previous.getY())) {
			int dx = actual.getX() - previous.getX();
			int dy = actual.getY() - previous.getY();
			
			Ways w;
			if (dx != 0) {
				w = Ways.UP;
				if (dx > 0) w = Ways.DOWN;
			} else {
				w = Ways.LEFT;
				if (dy > 0) w = Ways.RIGHT;
			}

			if (isWalkable(previous.getX(), previous.getY(), w)) return true;
		}
		return false;
	}

}
