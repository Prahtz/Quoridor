package gj.quoridor.player.falai;

public class JPSNode extends JPSAbstractNode<JPSNode> {

	public JPSNode() {
		super();
	}

	public boolean equals(JPSNode node) {
		if (node.getX() == this.getX() && node.getY() == this.getY()) return true;
		return false;
	}
	
}
