package gj.quoridor.player.falai;

public enum PlayerIndexes {

	myPlayer(0), hisPlayer(1);
	private int value;

	private PlayerIndexes(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
