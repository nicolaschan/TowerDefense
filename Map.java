
public class Map {
	
	public static final int ROWS = 7;
	public static final int COLUMNS = 10;
	
	private Square[][] squares;
	private Base base = new Base();
	
	public Map() {
		squares = new Square[ROWS][COLUMNS];
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				squares[row][col] = new Square(col, row);
			}
		}
	}
	
	public Base getBase() {
		return base;
	}
	public Square[][] getSquares() {
		return squares;
	}
	
	public Square getSquare(SquareCoordinate position) {
		return squares[position.getY()][position.getX()];
	}
	public void setTurret(Turret turret) {
		getSquare(turret.getPosition()).setTurret(turret);
	}
	public Turret removeTurret(Turret turret) {
		getSquare(turret.getPosition()).setTurret(null);
		return turret;
	}
	public Turret getTurretAt(SquareCoordinate position) {
		return getSquare(position).getTurret();
	}
	public boolean withinBounds(SquareCoordinate position) {
		if (position.getX() < 0 || position.getX() > COLUMNS - 1)
			return false;
		if (position.getY() < 0 || position.getY() > ROWS - 1)
			return false;
		return true;
	}
	public boolean outOfBounds(SquareCoordinate position) {
		return !withinBounds(position);
	}
	public boolean positionEmpty(SquareCoordinate position) {
		return getTurretAt(position) == null;
	}
	public boolean positionOccupied(SquareCoordinate position) {
		return !positionEmpty(position);
	}
}
