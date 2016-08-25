
public class TowerDefenseMain {

	public static void main(String[] args) {
		TowerDefenseModel model = new TowerDefenseModel();
		TowerDefenseView view = new TowerDefenseView();
		TowerDefenseController controller = new TowerDefenseController(model, view);
		model.start();
	}
}
