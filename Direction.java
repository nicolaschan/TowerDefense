
public class Direction {

	public static final double NORTH = Math.PI / 2;
	public static final double SOUTH = -1 * Math.PI / 2;
	public static final double EAST = 0;
	public static final double WEST = Math.PI;

	public static double getRandomDirection() {
		int random = (int) (Math.random() * 4);
		switch (random) {
		case 0:
			return NORTH;
		case 1:
			return SOUTH;
		case 2:
			return EAST;
		default:
			return WEST;
		}
	}
}
