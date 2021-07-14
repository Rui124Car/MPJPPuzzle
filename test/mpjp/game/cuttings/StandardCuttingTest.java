package mpjp.game.cuttings;

import mpjp.game.PuzzleViewer;
import mpjp.shared.geom.CurveTo;

/**
 * Class for testing StandardCutting.
 * 
 * All tests are inherited from CuttingTest. 
 * This class implements abstract methods declared in its super class.
 * 
 * You can run this class as Java Application to view this cutting
 * applied to a puzzle structure.
 * 
 *  @author Jos&eacute; paulo Leal {@code zp@dcc.fc.up.pt}
 */
public class StandardCuttingTest extends CuttingTest {

	@Override
	Cutting getFreshCutting() {
		return new StandardCutting();
	}
	
	@Override
	Class<?> getShapeSegmentClass() {
		return CurveTo.class;
	}
	
	@Override
	int getNumberOfSegments() {
		return 8;
	}
	
	public static void main(String[] args) {
		new PuzzleViewer(new StandardCutting(),Puzzle.P2.getStructure());
	}
	
}
