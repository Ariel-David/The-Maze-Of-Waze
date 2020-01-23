package elements;

public class level {
	private int bestScore;
	private int moves;
	
	public int getMinScore() {
		return bestScore;
	}
	
	public void setMinScore(int minScore) {
		this.bestScore = minScore;
	}
	
	public int getMaxMoves() {
		return moves;
	}
	
	public void setMaxMoves(int maxMoves) {
		this.moves = maxMoves;
	}

	
}
