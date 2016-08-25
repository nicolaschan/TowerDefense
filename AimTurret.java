import java.util.ArrayList;
import java.util.List;

public class AimTurret extends Turret {

	public AimTurret(SquareCoordinate position) {
		super(position, 50);
	}

	@Override
	public Projectile getProjectile(List<Enemy> enemies) {
		if (enemies.size() <= 0)
			return null;
		ArrayList<Entity> entities = new ArrayList<Entity>();
		for (Enemy enemy : enemies)
			entities.add((Entity) enemy);
		double speed = 0.15;
		Enemy closestEnemy = (Enemy) getPosition().toEntityCoordinate().closestEntity(entities);
		double angle = closestEnemy.interceptAngle(speed, getPosition().toEntityCoordinate());
		return new LinearProjectile(this, getPosition().toEntityCoordinate(), new VelocityVector(speed, angle));
	}

	@Override
	public int getBasePrice() {
		return 30;
	}

}
