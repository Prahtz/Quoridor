package gj.quoridor.player.falai;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public abstract class JPSAlgorithm<T extends JPSAbstractNode<T>> {
	
	public boolean findPath(T startNode, T goalNode, T path) {
		PriorityQueue<T> open = new PriorityQueue<T>((T o, T s) -> Integer.valueOf(o.getF()).compareTo(Integer.valueOf(s.getF())));
		LinkedList<T> closed = new LinkedList<T>();
		open.add(getNewNode(null, startNode.getX(), startNode.getY(), 0, goalNode));
		while (!open.isEmpty()) {
			T q = open.poll();
			closed.add(q);
			ArrayList<T> successors = identifySuccessors(q, goalNode);
			for (T s : successors) {
				if (checkGoal(s, goalNode)) {
					savePath(path, s);
					return true;
				} else if (checkSuccessor(open.iterator(), s) && checkSuccessor(closed.iterator(), s)) open.add(s);
			}
		}
		return false;
	}

	private ArrayList<T> identifySuccessors(T q, T goalNode) {
		ArrayList<T> successors = new ArrayList<T>();
		ArrayList<T> neighbors = findNeighbors(q, goalNode);

		for (T neighbor : neighbors) {
			T jumpNode = jump(neighbor, q, goalNode);
			if (jumpNode != null) successors.add(jumpNode);
		}
		return successors;
	}

	private boolean checkSuccessor(Iterator<T> it, T s) {
		while (it.hasNext()) {
			T e = it.next();
			if (e.getX() == s.getX() && e.getY() == s.getY()) {
				if (e.getF() < s.getF())
					return false;
				return true;
			}
		}
		return true;
	}

	public void savePath(T path, T node) {
		T a = node, b = path;
		b.setX(a.getX());
		b.setY(a.getY());
		int pathLength = 0;
		while (a != null && a.getParent() != null) {
			generatePath(a, a.getParent(), b);
			while (b.getParent() != null) {
				b = b.getParent();
				pathLength++;
			}
			a = a.getParent();
		}
		pathLength++;
		path.setPathLength(pathLength);
	}

	private void generatePath(T a, T parent, T path) {
		int dx = parent.getX() - a.getX();
		int dy = parent.getY() - a.getY();
		int df = 2;
		if (dx != 0) {
			if (dx < 0) df = -2;
			for (int i = 0, j = df; i < Math.abs(dx / 2); i++, j += df) {
				path.setParent(getNewNode(null, a.getX() + j, a.getY(), 0, 0, 0));
				path = path.getParent();
			}
		} else {
			if (dy < 0) df = -2;
			for (int i = 0, j = df; i < Math.abs(dy / 2); i++, j += df) {
				path.setParent(getNewNode(null, a.getX(), a.getY() + j, 0, 0, 0));
				path = path.getParent();
			}
		}
	}

	public abstract T getNewNode(T parent, int x, int y, int i, T goalNode);
	public abstract T getNewNode(T parent, int x, int y, int g, int h, int f);
	public abstract boolean checkGoal(T node, T goalNode);
	public abstract ArrayList<T> findNeighbors(T node, T goalNode);
	public abstract T jump(T neighbor, T node, T goalNode);

}
