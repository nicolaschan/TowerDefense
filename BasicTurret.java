import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BasicTurret extends Turret {
	
	public BasicTurret(SquareCoordinate position) {
		super(position, 50);
	}

	@Override
	public Projectile getProjectile(List<Enemy> enemies) {
		if (enemies.size() <= 0)
			return null;
		ArrayList<Entity> entities = new ArrayList<Entity>();
		for (Enemy enemy : enemies)
			entities.add((Entity) enemy);
		Enemy closestEnemy = (Enemy) getPosition().toEntityCoordinate().closestEntity(entities);
		double angle = getPosition().toEntityCoordinate().angleTo(closestEnemy.getPosition());
		return new LinearProjectile(this, getPosition().toEntityCoordinate(), new VelocityVector(0.15, angle));
	}

	@Override
	public int getBasePrice() {
		return 10;
	}

}
