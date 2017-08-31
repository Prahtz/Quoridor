package gj.quoridor.player.falai;

import gj.quoridor.player.Player;

public class FalaiPlayer implements Player {
	
	private TableManager table;
	
	public FalaiPlayer() {}

	public void start(boolean isFirst) {
		table = new TableManager();
		table.setPlayers(isFirst);
	}
	
	public int[] move() {
		return table.makeMove();
	}

	public void tellMove(int[] move) {
		if(move[0] == 0) {
			PawnMove pawnMove = new PawnMove(move);
			Ways w = pawnMove.decode(table.getPlayers()[PlayerIndexes.hisPlayer.getValue()].isFirst());
			table.applyMove(w, PlayerIndexes.hisPlayer.getValue());
		}
		else {
			WallMove wallMove = new WallMove(move);
			int[] w = wallMove.decode();
			table.applyMove(w, PlayerIndexes.hisPlayer.getValue());
		}
	}

	public TableManager getTable() {
		return table;
	}

}
