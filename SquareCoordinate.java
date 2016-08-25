
public class SquareCoordinate {

	private int x;
	private int y;

	public SquareCoordinate() {
		this(0, 0);
	}

	public SquareCoordinate(int x, int y) {
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		this.x = Math.min(x, Map.COLUMNS - 1);
		this.y = Math.min(y, Map.ROWS - 1);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		if (x >= 0 && x < Map.COLUMNS)
			this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		if (y >= 0 && y < Map.ROWS)
			this.y = y;
	}

	public SquareCoordinate incX() {
		return new SquareCoordinate(getX() + 1, getY());
	}

	public SquareCoordinate decX() {
		return new SquareCoordinate(getX() - 1, getY());
	}

	public SquareCoordinate incY() {
		return new SquareCoordinate(getX(), getY() + 1);
	}

	public SquareCoordinate decY() {
		return new SquareCoordinate(getX(), getY() - 1);
	}

	public EntityCoordinate toEntityCoordinate() {
		return new EntityCoordinate(x + 0.5, y + 0.5);
	}
}
