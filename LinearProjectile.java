
public class LinearProjectile extends Projectile {

	public LinearProjectile(Turret source, EntityCoordinate position, VelocityVector velocity) {
		super(source, position);
		super.setVelocityVector(velocity);
	}
	
	@Override
	public void tick() {
		move();
	}
}
