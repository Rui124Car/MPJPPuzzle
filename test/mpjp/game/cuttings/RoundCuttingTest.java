package mpjp.game.cuttings;

import mpjp.game.PuzzleViewer;
import mpjp.shared.geom.QuadTo;

/**
 * Class for testing CircularCutting.
 * 
 * All tests are inherited from CuttingTest. 
 * This class implements abstract methods declared in its super class.
 * 
 * You can run this class as Java Application to view this cutting
 * applied to a puzzle structure.
 * 
 *  @author Jos&eacute; paulo Leal {@code zp@dcc.fc.up.pt}
 */
class RoundCuttingTest extends CuttingTest {

	@Override
	Cutting getFreshCutting() {
		return new RoundCutting();
	}
	
	@Override
	Class<?> getShapeSegmentClass() {
		return QuadTo.class;
	}
	
	@Override
	int getNumberOfSegments() {
		return 4;
	}
	
	public static void main(String[] args) {
		
		new PuzzleViewer(new RoundCutting(),Puzzle.P2.getStructure());
	}
}
