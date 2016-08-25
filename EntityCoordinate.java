import java.util.List;

public class EntityCoordinate {

	private double x;
	private double y;
	
	public EntityCoordinate() {
		this(0, 0);
	}
	public EntityCoordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double angleTo(EntityCoordinate coord) {
		double deltaY = coord.getY() - this.getY();
		double deltaX = coord.getX() - this.getX();
		if (deltaX > 0)
			return Math.atan(deltaY / deltaX);
		return Math.atan(deltaY / deltaX) + Math.PI;
	}
	public double distanceTo(EntityCoordinate coord) {
		double deltaX = coord.getX() - this.getX();
		double deltaY = coord.getY() - this.getY();
		return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
	}
	public EntityCoordinate closestTo(List<EntityCoordinate> coords) {
		if (coords.size() <= 0)
			return null;
		EntityCoordinate closest = coords.get(0);
		for (EntityCoordinate coord : coords)
			if (this.distanceTo(coord) < this.distanceTo(closest))
				closest = coord;
		return closest;
	}
	public Entity closestEntity(List<Entity> entities) {
		if (entities.size() <= 0)
			return null;
		Entity closest = entities.get(0);
		for (Entity entity : entities)
			if (this.distanceTo(entity.getPosition()) < this.distanceTo(closest.getPosition()))
				closest = entity;
		return closest;
	}
	public String toString() {
		return "EntityCoordinate: {x: " + getX() + ", y: " + getY() + "}";
	}
}
