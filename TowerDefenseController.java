import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.net.ssl.SSLContext;
import javax.swing.JRadioButton;

public class TowerDefenseController implements StateObserver, MouseListener, ActionListener, KeyListener {

	private TowerDefenseModel model;
	private TowerDefenseView view;
	private JRadioButton selectedButton;
	private SquareCoordinate selectedSquare = new SquareCoordinate(4, 3);

	public TowerDefenseController(TowerDefenseModel model, TowerDefenseView view) {
		this.model = model;
		this.view = view;
		model.registerStateObserver(this);
		view.addMouseListener(this);
		view.addKeyListener(this);
		view.getStoreButtons().get(0).setSelected(true);
		selectedButton = view.getStoreButtons().get(0);
		for (JRadioButton button : view.getStoreButtons()) {
			button.addActionListener(this);
		}
		view.getNameTextField().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				nameTyped(arg0);
			}
			
		});
	}

	public void nameTyped(ActionEvent e) {
		model.placeScore(new Highscore(e.getActionCommand(), model.getScore()));
	}
	
	@Override
	public void stateChanged() {
		view.setState(model);
		view.setSelectedSquare(selectedSquare);
		view.repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (selectedButton == null)
			return;
		if (model.isGameOver())
			return;

		int x = arg0.getX();
		int y = arg0.getY();
		// account for 10px border
		x -= 10;
		y -= 10;
		// each square is 100px side length
		x /= 100;
		y /= 100;

		if (x > Map.COLUMNS)
			return;
		if (y > Map.ROWS)
			return;

		SquareCoordinate position = new SquareCoordinate(x, y);
		attemptPurchase(position);
		
		view.requestFocus();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		selectedButton = ((JRadioButton) arg0.getSource());
		view.requestFocus();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private Purchasable getSelectedItem(SquareCoordinate position) {
		if (selectedButton == null)
			return null;
		switch (selectedButton.getText()) {
		case "[10 points] Basic Turret":
			return (new BasicTurret(position));
		case "[20 points] Circle Turret":
			return (new CircleTurret(position));
		case "[30 points] Aim Turret":
			return (new AimTurret(position));
		case "[50 points] Homing Turret":
			return (new HomingTurret(position));
		case "[40 points] Reduce cooldown":
			return (new CooldownUpgrade(position));
		case "[60 points] Damage upgrade":
			return (new DamageUpgrade(position));
		default:
			return null;
		}
	}
	
	private void attemptPurchase(SquareCoordinate position) {
		try {
			if (getSelectedItem(position) != null)
				model.purchase(getSelectedItem(position));
			else
				model.sell(position);
			view.clearPurchaseErrorMessage();
		} catch (PurchaseException e) {
			view.setPurchaseErrorMessage(e.getMessage());
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		if (e.getKeyChar() == 'm')
			model.cheat();
		if (e.getKeyChar() == ' ')
			model.togglePause();
		if (e.getKeyChar() == 'r')
			model.restart();
		if (e.getKeyChar() == 'a')
			model.advanceLevel();
		
		try {
			int n = Integer.parseInt(Character.toString(e.getKeyChar()));
			if (n > 0 && n <= view.getStoreButtons().size()) {
				view.getStoreButtons().get(n - 1).setSelected(true);
				selectedButton = view.getStoreButtons().get(n - 1);
			}
		} catch (NumberFormatException ex) {
		}
		
		if (model.isGameOver())
			return;

		// up = 38, down = 40, left = 37, right = 39
		if (code == 38)
			selectedSquare = selectedSquare.decY();
		if (code == 40)
			selectedSquare = selectedSquare.incY();
		if (code == 37)
			selectedSquare = selectedSquare.decX();
		if (code == 39)
			selectedSquare = selectedSquare.incX();
		if (code == 10)
			attemptPurchase(selectedSquare);
	}
}
