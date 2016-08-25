
public class Base {

	public static final int MAX_BASE_HEALTH = 10;
	private int health = MAX_BASE_HEALTH;

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void damage() {
		damage(1);
	}
	
	public void damage(int amount) {
		if (health > 0)
			health -= amount;
	}
	
	public boolean alive() {
		return health > 0;
	}
	public boolean dead() {
		return !alive();
	}
	
	public int getPercentDamaged() {
		return (int) ((((double) (MAX_BASE_HEALTH - health)) / MAX_BASE_HEALTH) * 100);
	}
}
