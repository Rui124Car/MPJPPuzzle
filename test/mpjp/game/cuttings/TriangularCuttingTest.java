package mpjp.game.cuttings;

import mpjp.game.PuzzleViewer;
import mpjp.shared.geom.LineTo;

/**
 * Class for testing TriangularCutting.
 * 
 * All tests are inherited from CuttingTest. 
 * This class implements abstract methods declared in its super class.
 * 
 * You can run this class as Java Application to view this cutting
 * applied to a puzzle structure.
 * 
 *  @author Jos&eacute; paulo Leal {@code zp@dcc.fc.up.pt}
 */
class TriangularCuttingTest extends CuttingTest {

	@Override
	Cutting getFreshCutting() {
		return new TriangularCutting();
	}
	
	@Override
	Class<?> getShapeSegmentClass() {
		return LineTo.class;
	}
	
	@Override
	int getNumberOfSegments() {
		return 8;
	}
	
	public static void main(String[] args) {
		
		new PuzzleViewer(new TriangularCutting(),Puzzle.P2.getStructure());
	}
}
