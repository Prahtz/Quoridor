package gj.quoridor.player.falai;

public class WallMove extends Move {

	public WallMove() {
		this.setMove(new int[2]);
	}
	
	public WallMove(int[] move) {
		this.setMove(move);
	}
	
	public int[] decode() {
		int move = getMove()[1];
		int app = move % 8;
		move = move / 8;
		app = app * 2;
		if (move % 2 == 0) app++;
		return(new int[] { move, app });
	}
	
	public void encode(int[] wall) {
		if(wall[0] % 2 == 0) wall[1]--;
		wall[1] = wall[1] / 2;
		wall[1] = wall[1] + (wall[0] * 8);
		wall[0] = 1;
		setMove(wall);
	}

}
