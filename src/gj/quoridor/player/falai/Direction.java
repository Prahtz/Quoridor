package gj.quoridor.player.falai;
import java.util.EnumMap;
import java.util.Map;

public class Direction {

	private Map<Ways, Boolean> directions;

	public Direction() {
		directions = new EnumMap<Ways, Boolean>(Ways.class);
		initDirections();
	}

	private void initDirections() {
		directions.put(Ways.UP, false);
		directions.put(Ways.DOWN, false);
		directions.put(Ways.LEFT, false);
		directions.put(Ways.RIGHT, false);
	}

	public void set(Ways w, Boolean b) {
		directions.put(w, b);
	}

	public boolean get(Ways w) {
		return directions.get(w);
	}

}
