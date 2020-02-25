package gj.quoridor.player.falai;

public class Manager {

	private Table table = Table.getInstance();
	private int myDistance;
	private int hisDistance;
	private final int MIN_PATH_DISTANCE = 3;

	public int[] makeMove() {
		myDistance = table.getDistance(PlayerIndexes.myPlayer.getValue());
		hisDistance = table.getDistance(PlayerIndexes.hisPlayer.getValue());

		MoveStrategy ms;
		if (moveType()) ms = new AvoidDeadEnd();
		else ms = new StretchPath();
		return ms.getMove();
	}

	/***************************
	 * 1: Se entrambi abbiamo 10 muri, allora se è vicino alla vittoria, allora
	 * bloccalo, altrimenti vai avanti. 
	 * 2: Altrimenti se io ho 10 muri e lui 9, bloccalo; 
	 * 2.1: Altrimenti se la mia distanza è minore della sua: 
	 * - se abbiamo gli stessi muri e (lui non è vicino alla fine o io sono vicino), vai avanti 
	 * - altrimenti bloccalo 
	 * 2.2: Altrimenti se ho più muri di lui oppure lui è vicino alla fine 
	 * - bloccalo 
	 * - altrimenti vai avanti.
	 ***************************/
	private boolean moveType() {
		PawnInfo[] players = table.getPlayers();
		boolean result = false;

		if (players[PlayerIndexes.hisPlayer.getValue()].getWallCount() == PawnInfo.MAX_WALL && players[PlayerIndexes.myPlayer.getValue()].getWallCount() == PawnInfo.MAX_WALL) {
			if (hisDistance <= MIN_PATH_DISTANCE) result = false;
			else result = true;
		} else {
			if (players[PlayerIndexes.myPlayer.getValue()].getWallCount() == PawnInfo.MAX_WALL && players[PlayerIndexes.hisPlayer.getValue()].getWallCount() == PawnInfo.MAX_WALL - 1) {
				result = false;
			} else if (myDistance <= hisDistance) {
				if (hisDistance > MIN_PATH_DISTANCE || myDistance <= MIN_PATH_DISTANCE - 1) result = true;
				else result = false;
			} else {
				if (players[PlayerIndexes.myPlayer.getValue()].getWallCount() > players[PlayerIndexes.hisPlayer.getValue()].getWallCount() || hisDistance <= MIN_PATH_DISTANCE) {
					result = false;
				} else {
					result = true;
				}
			}
		}
		return result;
	}

}
