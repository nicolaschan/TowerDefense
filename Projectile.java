import java.awt.Color;
import java.util.List;

public abstract class Projectile extends Entity {
	
	private Turret source;
	
	public Projectile(Turret source, EntityCoordinate position) {
		this(source, position, Color.BLUE);
	}
	public Projectile(Turret source, EntityCoordinate position, Color color) {
		super(position, 10, color);
		this.source = source;
	}
	
	public Turret getSource() {
		return source;
	}
	
	@Override
	public int interactWith(Entity entity) {
		if (entity instanceof Enemy) {
			((Enemy) entity).damage(getSource().getDamage());
			kill();
			return (entity.isAlive()) ? 0 : 1;
		}
		return 0;
	}
	
	@Override
	public void setX(double x) {
		if (x < 0 || x > Map.COLUMNS)
			kill();
		else
			super.setX(x);
	}
	@Override
	public void setY(double y) {
		if (y < 0 || y > Map.ROWS)
			kill();
		else
			super.setY(y);
	}
}
