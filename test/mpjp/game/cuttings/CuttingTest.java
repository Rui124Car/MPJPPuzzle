package mpjp.game.cuttings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import mpjp.game.PuzzleData;
import mpjp.game.PuzzleStructure;
import mpjp.game.ShapeChanger;
import mpjp.shared.MPJPException;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;
import mpjp.shared.geom.Segment;

/**
 * Abstract class common to the classes testing cuttings. 
 * The tests methods in this class are parameterized by abstracts methods
 * provided by each cutting test class. These methods provide cutting 
 * instances, and define the number and type of segments 
 * (assuming that they are homogeneous).  
 * 
 *  @author Jos&eacute; paulo Leal {@code zp@dcc.fc.up.pt}
 */
public abstract class CuttingTest extends PuzzleData {
	Cutting cutting;
	
	private static final int POINTS_PER_PIECE = 10;
	
	/**
	 * A fresh instance of the class being tested,
	 * called before each test.
	 * 
	 * @return cutting
	 */
	abstract Cutting getFreshCutting();
	
	/**
	 * Segment shape type. 
	 * Tests in this abstract class assume all pieces
	 * have segments of a single type (e.g.: LineTo).
	 * 
	 * @return clazz
	 */
	abstract Class<?> getShapeSegmentClass();
	
	/**
	 * Number of segments in a piece. 
	 * Tests in this abstract class assume all pieces have 
	 * the same number of segments.
	 * 
	 * @return segments count
	 */
	abstract int getNumberOfSegments();
	
	protected final String HINT = 
		"\n*********************************************************"+
	    "\n* Execute %s as a Java Application to see what is wrong *"+
		"\n*********************************************************\n";	
	
	protected String hint;
	 
	{
		hint = String.format(HINT, getClass().getSimpleName());
	}
	
	/**
	 * Check if a cutting is created and with the corect type
	 */
	@Test
	void testCutting() {
		Cutting cutting = getFreshCutting();
		
		assertNotNull(cutting,"instance expected");
		
		assertTrue(Cutting.class.isInstance(cutting),"type Cutting expected");
	}
	
	/**
	 * Test if the cutting provided by the concrete class has 
	 * a piece shape for all pieces, each piece shape has a starting point,
	 * its segments are in the expected number and of type (depending on
	 * the cutting)
	 * 
	 * @param puzzle to be tested
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testGetShapesSegments(Puzzle puzzle) {
		PuzzleStructure structure = puzzle.getStructure();
		Cutting cutting = getFreshCutting();
		Class<?> clazz = getShapeSegmentClass();
		int numberOfSegments = getNumberOfSegments();
				
		Map<Integer, PieceShape> shapes = cutting.getShapes(structure);
		int size =  shapes.size();

		assertEquals(structure.getPieceCount(),size);

		for(Integer id: shapes.keySet()) {
			PieceShape shape = shapes.get(id);
			
			assertTrue(id >= 0,"id must be positive");
			assertTrue(id < size,"id must be smaller that number os pieces");
			
			assertNotNull(shape,"shape expected");
			assertNotNull(shape.getStartPoint(),"start point expected");
			assertNotNull(shape.getSegments(),"segments expected");
			
			assertTrue(shape.getSegments().size() > 3,
					"more than 3 segments expected");
			
			assertTrue(shape.getSegments().size() == numberOfSegments,
					numberOfSegments+" segments expected to draw this piece");
			
			for(Segment segment: shape.getSegments()) {
				assertTrue(clazz.isInstance(segment),
						"only "+clazz+" segments expected to draw this");
			}
		}
	}

	/**
	 * Test if the cutting provided by the concrete class
	 * covers all the puzzle. A large number of points is 
	 * randomly generated on the area of the complete puzzle 
	 * (with all pieces in their place) 
	 * and checked if that point is in some piece, at most 2  
	 * considering that it may be in the border.
	 * 
	 * @param puzzle to be tested
	 * @throws MPJPException 
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testGetShapesCoverage(Puzzle puzzle) throws MPJPException {
		Cutting cutting = getFreshCutting();
		
		PuzzleStructure structure = puzzle.getStructure();


		Map<Integer, PieceShape> shapes = cutting.getShapes(structure);
		int nPoints = POINTS_PER_PIECE * structure.getPieceCount();
		
		for(int i=0; i < nPoints; i++) {
			Point point = structure.getRandomPointInStandardPuzzle();
			int count = 0;
			
			for(int id: structure.getPossiblePiecesInStandarFor(point)) {
				PieceShape boundary = shapes.get(id);
				Shape shape = ShapeChanger.getShape(boundary);
				Point center = structure.getPieceStandardCenter(id);
				AffineTransform transform = new AffineTransform();
				
				transform.translate(center.getX(), center.getY());
				
				if(Path2D.contains(
						shape.getPathIterator(transform),
						point.getX(),point.getY()))
					count++;	
			}
			assertTrue(count > 0,"Point must be at least in one piece"+hint);
			assertTrue(count < 2,"More than 2 overlapping pieces"+hint);
		}
	}
	

}
