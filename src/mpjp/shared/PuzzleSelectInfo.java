package mpjp.shared;

import java.util.Date;

public class PuzzleSelectInfo extends PuzzleInfo {

	private static final long serialVersionUID = 1L;
	int percentageSolved;
	Date start;

	// como é que mexemos aqui com o puzzle info, confusion
	public PuzzleSelectInfo(PuzzleInfo info, int percentageSolved, Date start) {
		super();
		this.percentageSolved = percentageSolved;
		this.start = start;
	}

	public int getPercentageSolved() {
		return percentageSolved;
	}

	public Date getStart() {
		return start;
	}

}
