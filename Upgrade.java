public abstract class Upgrade implements Purchasable {

	private SquareCoordinate position;

	public Upgrade(SquareCoordinate position) {
		this.position = position;
	}
	
	public int getValue() {
		return getBasePrice();
	}
	public SquareCoordinate getPosition() {
		return position;
	}
}