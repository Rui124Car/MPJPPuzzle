package mpjp.shared;

import java.util.List;
import java.util.Map;

public class PuzzleLayout {
	Map<Integer, List<Integer>> blocks;
	Map<Integer, PieceStatus> pieces;
	int percentageSolved;

	public PuzzleLayout() {
		super();
	}

	public PuzzleLayout(Map<Integer, PieceStatus> pieces, Map<Integer, List<Integer>> blocks, int percentageSolved) {
		super();
		this.blocks = blocks;
		this.percentageSolved = percentageSolved;
		this.pieces = pieces;
	}

	public Map<Integer, List<Integer>> getBlocks() {
		return this.blocks;
	}

	public int getPercentageSolved() {
		int b = this.blocks.size();
		int p = this.pieces.size();
		
		return 100 * (p - b) / (p - 1);
	}

	public Map<Integer, PieceStatus> getPieces() {
		return this.pieces;
	}

	public boolean isSolved() {
		if(this.blocks.size()==1)
			return true;
		return false;
	}

}
