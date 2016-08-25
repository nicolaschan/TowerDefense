import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class TowerDefenseView extends JFrame {

	public final int WIDTH = 1280;
	public final int HEIGHT = 720;

	private GamePanel gamePanel;
	private ControlPanel controlPanel;

	private TowerDefenseModel model;
	private ArrayList<JRadioButton> storeButtons = new ArrayList<JRadioButton>();
	private long purchaseErrorMessageShown;
	private SquareCoordinate selectedSquare;

	public TowerDefenseView() {

		gamePanel = new GamePanel();
		controlPanel = new ControlPanel();
		gamePanel.setPreferredSize(new Dimension(WIDTH - 260, HEIGHT));
		controlPanel.setPreferredSize(new Dimension(260, HEIGHT));

		this.add(gamePanel, BorderLayout.WEST);
		this.add(controlPanel, BorderLayout.EAST);

		this.setSize(1280, 720);
		this.setTitle("Tower Defense");
		this.setResizable(false);
		this.setVisible(true);
		
		this.requestFocus();
	}

	public void setState(TowerDefenseModel model) {
		this.model = model;
		if (model.getCurrentTick() - purchaseErrorMessageShown > 4000 / model.TICK_FREQUENCY)
			clearPurchaseErrorMessage();
	}

	public void setSelectedSquare(SquareCoordinate selectedSquare) {
		this.selectedSquare = selectedSquare;
	}

	public List<JRadioButton> getStoreButtons() {
		return storeButtons;
	}

	public void setPurchaseErrorMessage(String message) {
		controlPanel.setPurchaseErrorMessage(message);
		purchaseErrorMessageShown = model.getCurrentTick();
	}

	public void clearPurchaseErrorMessage() {
		setPurchaseErrorMessage("");
	}

	public JTextField getNameTextField() {
		return gamePanel.getNameTextField();
	}
	
	private class GamePanel extends JPanel {
		
		private JTextField name = new JTextField("Name");
		
		public GamePanel() {
			this.setLayout(null);
			this.add(name);
			name.setVisible(false);
			name.setSize(500, 100);
			name.setFont(new Font("Helvetica", 0, 80));
			name.setLocation(100, 600);
		}
		
		public JTextField getNameTextField() {
			return name;
		}
		
		public void paintComponent(Graphics g) {
			if (model == null)
				return;
		
			// Draw grid
			Square[][] squares = model.getMap().getSquares();
			int width = 100;
			for (int row = 0; row < squares.length; row++) {
				for (int col = 0; col < squares[row].length; col++) {
					g.setColor(Color.LIGHT_GRAY);
					g.drawRect(col * width + 10, row * width + 10, width, width);

					if (squares[row][col].getTurret() != null) {
						Turret turret = squares[row][col].getTurret();
						if (turret instanceof BasicTurret)
							g.setColor(Color.GRAY);
						else if (turret instanceof CircleTurret)
							g.setColor(Color.BLUE);
						else if (turret instanceof AimTurret)
							g.setColor(Color.YELLOW);
						else if (turret instanceof HomingTurret)
							g.setColor(Color.PINK);
						else
							g.setColor(Color.BLACK);
						
						int turretWidth = 20 + turret.numberOfDamageUpgrades() * 6;
						
						g.fillOval(turret.getX() * 100 + 50 + 10 - turretWidth / 2, turret.getY() * 100 + 50 + 10 - turretWidth / 2, turretWidth, turretWidth);
						g.setColor(Color.BLACK);

						if (turret.numberOfCooldownUpgrades() > 0) {
							g.setFont(new Font("Helvetica", 0, 16));
							g.drawString(Integer.toHexString(turret.numberOfCooldownUpgrades()),
									turret.getX() * 100 + 50 + 6, turret.getY() * 100 + 50 + 16);
						}
					}
				}
			}

			// Draw entities
			for (Entity entity : model.getEntities()) {
				g.setColor(entity.getColor());
				int x = (int) (entity.getX() * width + 10 - entity.getSize() / 2);
				int y = (int) (entity.getY() * width + 10 - entity.getSize() / 2);
				g.fillOval(x, y, entity.getSize(), entity.getSize());
			}

			// Draw selected box
			if (selectedSquare != null) {
				g.setColor(Color.BLACK);
				g.drawRect(selectedSquare.getX() * width + 10, selectedSquare.getY() * width + 10, width, width);
			}

			// Draw base
			int baseWidth = 30;
			if (model.getMap().getBase().getPercentDamaged() > 80)
				g.setColor(Color.RED);
			else if (model.getMap().getBase().getPercentDamaged() > 30)
				g.setColor(Color.ORANGE);
			else if (model.getMap().getBase().getPercentDamaged() > 0)
				g.setColor(Color.YELLOW);
			else
				g.setColor(Color.GREEN);
			g.fillRect(500 + 10 - baseWidth / 2, 350 + 10 - baseWidth / 2, baseWidth, baseWidth);

			if (model.isGameOver()) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Helvetica", 100, 100));
				g.drawString("Game over!", 100, 100);
				g.drawString("R to restart", 100, 200);
				
				g.setFont(new Font("Helvetica", 0, 40));
				g.drawString("Highscores: ", 100, 400);
				for (int i = 0; i < model.getHighScores().size(); i++) {
					g.drawString(model.getHighScores().get(i).toFormattedString(), 100, 460 + i * 40);
				}
				
				if (model.isHighScore() && !name.isVisible()) {
					name.setVisible(true);
					name.requestFocus();
					name.selectAll();
				} else if (!model.isHighScore()) {
					name.setVisible(false);
					getFrame().requestFocus();
				}
			}
		}
	}
	
	private JFrame getFrame() {
		return this;
	}

	private class ControlPanel extends JPanel {

		private JLabel pointsLabel;
		private JLabel scoreLabel;
		private JLabel baseLabel;
		private JLabel purchaseErrorLabel;

		public ControlPanel() {
			this.setLayout(new GridLayout(3, 1));

			JPanel pointsPanel = new JPanel();
			pointsPanel.setLayout(new GridLayout(3, 1));
			scoreLabel = new JLabel("Score");
			pointsPanel.add(scoreLabel);
			pointsLabel = new JLabel("Points");
			pointsPanel.add(pointsLabel);
			purchaseErrorLabel = new JLabel("");
			purchaseErrorLabel.setForeground(Color.RED);
			pointsPanel.add(purchaseErrorLabel);
			this.add(pointsPanel);

			baseLabel = new JLabel("Base Health");
			this.add(baseLabel);
			this.add(new StorePanel());
		}

		public void paintComponent(Graphics g) {
			if (model == null)
				return;

			scoreLabel.setText("Score: " + model.getScore());
			pointsLabel.setText("Points: " + model.getPoints());
			if (model.getMap() != null && model.getMap().getBase() != null)
				baseLabel.setText("Base Health: " + model.getMap().getBase().getHealth());
		}

		public void setPurchaseErrorMessage(String message) {
			purchaseErrorLabel.setText(message);
		}
	}

	private class StorePanel extends JPanel {
		public StorePanel() {
			String[] itemsForSale = new String[] { "[10 points] Basic Turret", "[20 points] Circle Turret",
					"[30 points] Aim Turret", "[50 points] Homing Turret", "[40 points] Reduce cooldown",
					"[60 points] Damage upgrade", "[20% value] Sell" };

			this.setLayout(new GridLayout(itemsForSale.length + 10, 1));
			this.add(new JLabel("Store"));

			ButtonGroup buttons = new ButtonGroup();
			for (String buttonLabel : itemsForSale) {
				JRadioButton button = new JRadioButton(buttonLabel);
				buttons.add(button);
				this.add(button);
				storeButtons.add(button);
			}
		}
	}
}
