package gj.quoridor.player.falai;

import gj.quoridor.player.Player;

public class FalaiPlayer implements Player {

	private Table table;
	private Manager manager;

	public FalaiPlayer() {}

	public void start(boolean isFirst) {
		table = Table.getInstance();
		manager = new Manager();
		table.restoreTable();
		table.setPlayers(isFirst);
	}

	public int[] move() {
		return manager.makeMove();
	}

	public void tellMove(int[] move) {
		if (move[0] == 0) {
			PawnMove pawnMove = new PawnMove(move);
			Ways w = pawnMove.decode(table.getPlayers()[PlayerIndexes.hisPlayer.getValue()].isFirst());
			table.applyMove(w, PlayerIndexes.hisPlayer.getValue());
		} else {
			WallMove wallMove = new WallMove(move);
			int[] w = wallMove.decode();
			table.applyMove(w, PlayerIndexes.hisPlayer.getValue());
		}
	}

	// Metodo di test
	public void tellMyMove(int[] move) {
		if (move[0] == 0) {
			PawnMove pawnMove = new PawnMove(move);
			Ways w = pawnMove.decode(table.getPlayers()[PlayerIndexes.myPlayer.getValue()].isFirst());
			table.applyMove(w, PlayerIndexes.myPlayer.getValue());
		} else {
			WallMove wallMove = new WallMove(move);
			int[] w = wallMove.decode();
			table.applyMove(w, PlayerIndexes.myPlayer.getValue());
		}
	}

}
