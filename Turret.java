import java.util.ArrayList;
import java.util.List;

public abstract class Turret implements Purchasable {

	private SquareCoordinate position;
	private int cooldown;
	private List<Upgrade> upgrades = new ArrayList<Upgrade>();

	public Turret(SquareCoordinate position) {
		this(position, 1);
	}

	public Turret(SquareCoordinate position, int cooldown) {
		this.position = position;
		this.cooldown = cooldown;
	}

	public Projectile fire(List<Enemy> enemies, long tick) {
		if (tick % getCooldown() == 0)
			return getProjectile(enemies);
		return null;
	}

	public abstract Projectile getProjectile(List<Enemy> enemies);

	public int getX() {
		return position.getX();
	}

	public int getY() {
		return position.getY();
	}

	public int getDamage() {
		return 1 + numberOfDamageUpgrades();
	}
	
	public int getCooldown() {
		return Math.max(cooldown - numberOfCooldownUpgrades() * 10, 1);
	}
	
	private int numberOfTypeUpgrade(Class<? extends Upgrade> clazz) {
		int count = 0;
		
		for (Upgrade upgrade : upgrades)
			if (upgrade.getClass() == clazz)
				count++;
				
		
		return count;
	}
	
	public int numberOfDamageUpgrades() {
		return numberOfTypeUpgrade(DamageUpgrade.class);
	}
	
	public int numberOfCooldownUpgrades() {
		return numberOfTypeUpgrade(CooldownUpgrade.class);
	}

	public SquareCoordinate getPosition() {
		return position;
	}

	public int getValue() {
		return getBasePrice() + getUpgradesValue();
	}

	public boolean addUpgrade(Upgrade upgrade) throws PurchaseException {
		if (upgrade instanceof CooldownUpgrade && numberOfCooldownUpgrades() >= 4)
			throw new PurchaseException("Max cooldown");
		if (upgrade instanceof DamageUpgrade && numberOfDamageUpgrades() >= 4)
			throw new PurchaseException("Max damage");
		return upgrades.add(upgrade);
	}
	
	private int getUpgradesValue() {
		int value = 0;
		
		for (Upgrade upgrade : upgrades)
			value += upgrade.getValue();
		
		return value;
	}
}
