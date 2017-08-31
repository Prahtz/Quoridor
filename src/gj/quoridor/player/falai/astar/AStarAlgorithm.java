package gj.quoridor.player.falai.astar;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AStarAlgorithm {

	public AStarAlgorithm() {}
	
	public static <T extends AStarNode<T>, R extends AStarInterface<T, E>, E> boolean minimumPath(int x, int y, int goal, R f, T goalNode) {
		PriorityQueue<T> open = new PriorityQueue<T>((T o, T s) -> Integer.valueOf(o.getF()).compareTo(Integer.valueOf(s.getF())));
		LinkedList<T> closed = new LinkedList<T>();
		open.add(f.getNewNode(null, x, y, 0, 0));
		while(!open.isEmpty()) {
			T q = open.poll();
			ArrayList<E> directions = f.generateDirections();
			for(int i = 0; i < directions.size(); i++) {
				E w = directions.get(i);
				if(f.getDirection(q,w)) {
					T s = f.generateSuccessor(q, w, goal);
					if(f.checkGoal(s.getX(), goal)) {
						savePath(goalNode,s);
						return true;
					}
					else if(checkSuccessor(open.iterator(), s) && checkSuccessor(closed.iterator(), s)) open.add(s);
				}
			}
			closed.add(q);
		}
		return false;
	}
	
	private static <T extends AStarNode<T>> boolean checkSuccessor(Iterator<T> it, T s) {
		while(it.hasNext()) {
			T e = it.next();
			if(e.getX() == s.getX() && e.getY() == s.getY()) {
				if(e.getF() < s.getF()) return false;
				return true;
			}
		}
		return true;
	}
	
	public static <T extends AStarNode<T>> void savePath(T goalNode, T node) {
		T a = node;
		T b = goalNode;
		int pathLength = 0;
		while(a != null) {
			pathLength++;
			b.setParent(a.getParent());
			b.setX(a.getX());
			b.setY(a.getY());
			a = a.getParent();
			b = b.getParent();
		}
		goalNode.setPathLength(pathLength);
	}

}
