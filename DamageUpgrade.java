public class DamageUpgrade extends Upgrade {

	public DamageUpgrade(SquareCoordinate position) {
		super(position);
	}

	@Override
	public int getBasePrice() {
		return 60;
	}
	
}