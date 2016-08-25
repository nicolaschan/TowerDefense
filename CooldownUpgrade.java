
public class CooldownUpgrade extends Upgrade {

	public CooldownUpgrade(SquareCoordinate position) {
		super(position);
	}

	@Override
	public int getBasePrice() {
		return 40;
	}
}
