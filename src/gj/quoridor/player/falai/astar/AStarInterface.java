package gj.quoridor.player.falai.astar;
import java.util.ArrayList;

public interface AStarInterface<T extends AStarNode<T>, R>{

	public T getNewNode(T parent, int x, int y, int g, int h);
	public T generateSuccessor(T q, R w, int goal);
	public ArrayList<R> generateDirections();
	public boolean getDirection(T q, R w);
	public int generateH(int x, int goal);
	public boolean checkGoal(int x, int goal);

}
