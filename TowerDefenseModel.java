import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

public class TowerDefenseModel implements ActionListener {

	public static final int TICK_FREQUENCY = 60; // in Hz

	private long currentTick;
	private int points;
	private boolean highscoreRecorded;
	private int totalKills;
	private int totalKillsWhenLevelChanged;
	private int level = 1;
	private boolean paused = false;
	private boolean gameOver = false;
	private Timer tick;
	private Timer update;
	private ArrayList<StateObserver> observers = new ArrayList<StateObserver>();
	private Map map = new Map();
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Turret> turrets = new ArrayList<Turret>();	
	private Path highscorePath = FileSystems.getDefault().getPath("TowerDefenseHighscores.txt");

	
	public boolean isGameOver() {
		return gameOver;
	}
	public boolean isHighscoreRecorded() {
		return highscoreRecorded;
	}
	public void restart() {
		start();
	}
	public void start() {
		ensureHighscoreFile();
		
		points = 50;
		highscoreRecorded = false;
		totalKills = 0;
		currentTick = 0;
		totalKillsWhenLevelChanged = 0;
		level = 1;
		paused = false;
		gameOver = false;
		
		if (tick != null)
			tick.stop();
		if (update != null)
			update.stop();
		update = new Timer(1000 / TICK_FREQUENCY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateStateObservers();
			}
		});
		update.start();
		
		tick = new Timer(1000 / TICK_FREQUENCY, this);
		tick.start();
		
		entities.clear();
		enemies.clear();
		turrets.clear();
		map = new Map();
	}

	public void cheat() {
		highscoreRecorded = true;
		points += 1000;
	}
	
	public void advanceLevel() {
		level++;
	}
	
	private void ensureHighscoreFile() {
		if (Files.isRegularFile(highscorePath))
			return;
		
		try {
			BufferedWriter bw = Files.newBufferedWriter(highscorePath);
			bw.write("empty:0,empty:0,empty:0");
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void tick() {
		currentTick++;

		for (Entity entity : entities)
			entity.tick();
		for (Turret turret : turrets)
			spawnProjectile(turret.fire(enemies, currentTick));
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			if (enemy.atBase()) {
				map.getBase().damage();
				enemy.kill();
			}
		}

		interactWithIntersections();
		removeDeadEntities();
		spawnEnemyChance();
		updateLevel();
		
		if (map.getBase().dead()) {
			gameOver();
		}
	}
	
	private void gameOver() {
		gameOver = true;
		pause();
	}
	
	public void placeScore(Highscore score) {
		placeScore(score, getHighScores());
	}
	
	private void placeScore(Highscore score, List<Highscore> scores) {
		for (int i = 0; i < scores.size(); i++) {
			if (scores.get(i).getScore() < score.getScore()) {
				scores.add(i, score);
				i++;
				break;
			}
		}
		scores.remove(scores.size() - 1);
		
		BufferedWriter bw;
		try {
			bw = Files.newBufferedWriter(highscorePath);
			
			String scoresString = "";
			for (Highscore highscore : scores) {
				scoresString += highscore.toString() + ",";
			}
			
			scoresString = scoresString.substring(0, scoresString.length() - 1);
			
			bw.write(scoresString);
			bw.flush();
			bw.close();
			
			highscoreRecorded = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isHighScore() {
		return isHighScore(totalKills, getHighScores()) && !highscoreRecorded;
	}
	
	private boolean isHighScore(int score, List<Highscore> scores) {
		int minScore = scores.get(scores.size() - 1).getScore();
		return (score > minScore);
	}
	
	public List<Highscore> getHighScores() {
		List<Highscore> scores = new ArrayList<Highscore>();
		
		try {
			BufferedReader br = Files.newBufferedReader(highscorePath);
			String text = "";
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				else
					text += line;
			}
			br.close();
			
			String[] words = text.split(",");
			for (int i = 0; i < words.length; i++) {
				scores.add(new Highscore(words[i]));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scores;
	}

	private void updateLevel() {
		if (totalKills - totalKillsWhenLevelChanged > 30) {
			level++;
			totalKillsWhenLevelChanged = totalKills;
		}
	}

	public int getScore() {
		return totalKills;
	}
	
	public long getCurrentTick() {
		return currentTick;
	}
	
	private int currentEnemyHealth() {
		return (int) ((level * 0.3) + 1);
	}
	
	public void togglePause() {
		if (gameOver)
			return;
		if (paused)
			unpause();
		else
			pause();
	}

	private void pause() {
		tick.stop();
		paused = true;
	}
	private void unpause() {
		tick = new Timer(1000 / TICK_FREQUENCY, this);
		tick.start();
		paused = false;
	}
	
	public void purchase(Purchasable item) throws PurchaseException {
		if (points >= item.getBasePrice()) {
			if (item instanceof Turret) {
				if (map.positionOccupied(item.getPosition()))
					throw new PurchaseException("Square occupied");
				if (addTurret((Turret) item)) {
					points -= item.getValue();
					return;
				}
			}
			if (item instanceof Upgrade) {
				if (map.positionEmpty(item.getPosition()))
					throw new PurchaseException("No turret to upgrade");
				if (map.getTurretAt(item.getPosition()).addUpgrade((Upgrade) item)) {
					points -= item.getValue();
					return;
				}
			}
			throw new PurchaseException();
		}
		throw new PurchaseException("Not enough funds");
	}

	public boolean sell(SquareCoordinate position) {
		if (map.outOfBounds(position) || map.getTurretAt(position) == null)
			return false;

		int value = map.getTurretAt(position).getValue();
		removeTurret(map.getTurretAt(position));
		points += value * 0.2;

		return true;
	}

	private boolean addTurret(Turret turret) {
		if (turret.getX() > Map.COLUMNS - 1)
			return false;
		if (turret.getY() > Map.ROWS - 1)
			return false;
		turrets.add(turret);
		map.setTurret(turret);
		return true;
	}

	private void removeTurret(Turret turret) {
		turrets.remove(turret);
		map.removeTurret(turret);
	}

	public int getPoints() {
		return points;
	}

	private void interactWithIntersections() {
		for (Entity entity : entities) {
			int kills = entity.interactWithIntersections(entities);
			points += kills;
			totalKills += kills;
		}
	}

	private void removeDeadEntities() {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).isDead()) {
				despawn(entities.get(i));
				i--;
			}
		}
	}

	private void spawnProjectile(Projectile projectile) {
		if (projectile == null)
			return;
		entities.add(projectile);
	}

	private void spawnEnemyChance() {
		spawnEnemyChance(0.01 + (0.002 * level));
	}

	private void spawnEnemyChance(double probability) {
		if (Math.random() <= probability)
			spawnEnemy();
	}

	private void spawnEnemy() {
		Enemy enemy = new Enemy(Enemy.getRandomValidStartingCoordinate(), 3, currentEnemyHealth());
		entities.add(enemy);
		enemies.add(enemy);
	}

	private void despawn(Entity entity) {
		if (entity instanceof Enemy)
			enemies.remove(entity);
		entities.remove(entity);
	}

	public void registerStateObserver(StateObserver observer) {
		observers.add(observer);
	}

	private void updateStateObservers() {
		for (StateObserver observer : observers) {
			observer.stateChanged();
		}
	}

	public Map getMap() {
		return map;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tick();
	}
}
