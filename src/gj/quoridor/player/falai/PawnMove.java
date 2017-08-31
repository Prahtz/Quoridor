package gj.quoridor.player.falai;

public class PawnMove extends Move {
	
	public PawnMove() {
		this.setMove(new int[2]);
	}
	
	public PawnMove(int[] move) {
		this.setMove(move);
	}
	
	public Ways decode(boolean isActualPlayerFirst) {
		int move = getMove()[1];
		switch(move) {
		case 0:
			if(isActualPlayerFirst) return Ways.DOWN;
			return Ways.UP;
		case 1:
			if(isActualPlayerFirst) return Ways.UP;
			return Ways.DOWN;
		case 2:
			if(isActualPlayerFirst) return Ways.RIGHT;
			return Ways.LEFT;
		case 3:
			if(isActualPlayerFirst) return Ways.LEFT;
			return Ways.RIGHT;
		default:
			return null;
		}
	}

	public void encode(Ways w, boolean isActualPlayerFirst) {
		switch(w) {
		case UP:
			if(isActualPlayerFirst)  {
				setMove(new int[] {0, 1});
			}
			else {
				setMove(new int[] {0, 0});
			}
			break;
		case DOWN:
			if(isActualPlayerFirst) {
				setMove(new int[] {0, 0});
			}
			else {
				setMove(new int[] {0, 1}); 
			}
			break;
		case LEFT:
			if(isActualPlayerFirst) {
				setMove(new int[] {0, 3});
			}
			else {
				setMove(new int[] {0, 2});
			}
			break;
		case RIGHT:
			if(isActualPlayerFirst) {
				setMove(new int[] {0, 2});
			}
			else {
				setMove(new int[] {0, 3});
			}
			break;
		default:
			setMove(null);
		}
	}
	
}
