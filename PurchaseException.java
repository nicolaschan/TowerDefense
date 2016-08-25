public class PurchaseException extends Exception {
	
	public PurchaseException() {
		this("Unknown purchase error");
	}
	public PurchaseException(String message) {
		super(message);
	}
}