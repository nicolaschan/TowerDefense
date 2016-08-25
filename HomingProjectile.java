import java.util.ArrayList;
import java.util.List;

public class HomingProjectile extends Projectile {

	private Enemy target;
	private List<Enemy> enemies;
	private double speed;

	public HomingProjectile(Turret source, EntityCoordinate position, Enemy target, List<Enemy> enemies) {
		this(source, position, target, enemies, 1);
	}

	public HomingProjectile(Turret source, EntityCoordinate position, Enemy target, List<Enemy> enemies, double speed) {
		super(source, position);
		this.target = target;
		this.enemies = enemies;
		this.speed = speed;
	}

	@Override
	public void tick() {
		if (target == null || target.isDead()) {
			ArrayList<Entity> entities = new ArrayList<Entity>();
			entities.addAll(enemies);
			target = (Enemy) this.getPosition().closestEntity(entities);
		}
		if (target != null && target.isAlive())
			move(new VelocityVector(speed, this.getPosition().angleTo(target.getPosition())));
		else if (getVelocity().getSpeed() > 0)
			move();
		else
			kill();
	}

}
