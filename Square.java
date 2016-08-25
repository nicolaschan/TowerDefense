
public class Square {

	private int x;
	private int y;
	private Turret turret;
	
	public Square(int x, int y) {
		this(x, y, null);
	}
	public Square(int x, int y, Turret turret) {
		this.x = x;
		this.y = y;
		this.turret = turret;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setTurret(Turret turret) {
		this.turret = turret;
	}
	public Turret getTurret() {
		return turret;
	}
}
