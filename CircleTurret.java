import java.util.List;

public class CircleTurret extends Turret {

	private double currentAngle;
	
	public CircleTurret(SquareCoordinate position) {
		super(position, 40);
	}

	@Override
	public int getBasePrice() {
		return 20;
	}

	@Override
	public Projectile getProjectile(List<Enemy> enemies) {
		currentAngle += 0.1;
		return new LinearProjectile(this, getPosition().toEntityCoordinate(), new VelocityVector(0.15, currentAngle));
	}
	
	
}