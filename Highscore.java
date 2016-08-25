public class Highscore {
	
	private String name;
	private int score;
	
	
	public Highscore(String score) {
		this(score.split(":")[0], Integer.parseInt(score.split(":")[1]));
	}
	
	public Highscore(String name, int score) {
		name = name.replace(":", "");
		name = name.replace(",", "");
		
		this.name = name;
		this.score = score;
	}
	
	public String getName() {
		return name;
	}
	public int getScore() {
		return score;
	}
	
	public String toString() {
		return name + ":" + score;
	}
	public String toFormattedString() {
		return name + ": " + score;
	}
}