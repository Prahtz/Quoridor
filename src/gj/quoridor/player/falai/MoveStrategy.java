package gj.quoridor.player.falai;

public abstract class MoveStrategy {
	
	protected PawnInfo[] players = Table.getInstance().getPlayers();
	public abstract int[] getMove();

}
