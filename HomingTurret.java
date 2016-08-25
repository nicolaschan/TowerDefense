import java.util.ArrayList;
import java.util.List;

public class HomingTurret extends Turret {

	public HomingTurret(SquareCoordinate position) {
		super(position, 60);
	}

	@Override
	public Projectile getProjectile(List<Enemy> enemies) {
		if (enemies.size() <= 0)
			return null;
		ArrayList<Entity> entities = new ArrayList<Entity>();
		for (Enemy enemy : enemies)
			entities.add((Entity) enemy);
		double speed = 0.05;
		Enemy closestEnemy = (Enemy) getPosition().toEntityCoordinate().closestEntity(entities);
		return new HomingProjectile(this, getPosition().toEntityCoordinate(), closestEnemy, enemies, speed);
	}

	@Override
	public int getBasePrice() {
		return 50;
	}

}
