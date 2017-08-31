package gj.quoridor.player.falai.astar;

public interface AStarNode<T extends AStarNode<T>> {
	
	public void setParent(T parent);
	public T getParent();
	public int getX();
	public void setX(int x);
	public int getY();
	public void setY(int y);
	public int getF();
	public void setF(int f);
	public int getG();
	public void setG(int g);
	public int getH();
	public void setH(int h);
	public int getPathLength();
	public void setPathLength(int pathLength);
	
}
