package mpjp.game.cuttings;

import mpjp.game.PuzzleViewer;
import mpjp.shared.geom.LineTo;

/**
 * Class for testing StraightCutting.
 * 
 * All tests are inherited from CuttingTest. 
 * This class implements abstract methods declared in its super class.
 * 
 * You can run this class as Java Application to view this cutting
 * applied to a puzzle structure.
 * 
 *  @author Jos&eacute; paulo Leal {@code zp@dcc.fc.up.pt}
 */
class StraightCuttingTest extends CuttingTest {

	@Override
	Cutting getFreshCutting() {
		return new StraightCutting();
	}
	
	@Override
	Class<?> getShapeSegmentClass() {
		return LineTo.class;
	}
	
	@Override
	int getNumberOfSegments() {
		return 4;
	}
	
	public static void main(String[] args) {
		
		new PuzzleViewer(new StraightCutting(),Puzzle.P2.getStructure());
	}
	
}
