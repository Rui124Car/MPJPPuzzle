package mpjp.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mpjp.TestData;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

/**
 * Tests on Puzzle view
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt} 
 */
class PuzzleViewTest extends TestData {
	PuzzleView puzzleView;
	Map<Integer, PieceShape> shapes;
	Map<Integer, Point> locations;
	
	/**
	 * Make a fresh test sample before each run
	 */
	@BeforeEach
	void setUp() {
		shapes = new HashMap<>();
		locations = new HashMap<>();
		
		for(int id =0; id < ROWS * COLUMNS; id++) {
			shapes.put(id, new PieceShape());
		}
		
		for(int id =0; id < ROWS * COLUMNS; id++) {
			locations.put(id, new Point(X+id,Y+id));
		}
		
		puzzleView = new PuzzleView(START,
				2*WIDTH,2*HEIGHT,
				WIDTH,HEIGHT,
				WIDTH/COLUMNS,HEIGHT/ROWS,
				IMAGE_NAME,
				shapes,locations);
	}

	/**
	 * Check start from constructor
	 */
	@Test
	void testGetStart_from_constructor() {
		assertEquals(START,puzzleView.getStart());
	}

	/**
	 * Check workspace width
	 */
	@Test
	void testGetWorkspaceWidth() {
		assertEquals(2*WIDTH,puzzleView.getWorkspaceWidth());
	}

	/**
	 * Check workspace height
	 */
	@Test
	void testGetWorkspaceHeight() {
		assertEquals(2*WIDTH,puzzleView.getWorkspaceHeight());
	}

	/**
	 * Check puzzle width 
	 */
	@Test
	void testGetPuzzleWidth() {
		assertEquals(WIDTH,puzzleView.getPuzzleWidth());
	}

	/**
	 * Check puzzle height 
	 */
	@Test
	void testGetPuzzleHeight() {
		assertEquals(HEIGHT,puzzleView.getPuzzleHeight());
	}

	/**
	 * Check image name
	 */
	@Test
	void testGetImage() {
		assertEquals(IMAGE_NAME,puzzleView.getImage());
	}

	/**
	 * Check each piece shape
	 */
	@Test
	void testGetPieceShape() {
		for(int id=0; id < ROWS*COLUMNS; id++)
			assertEquals(shapes.get(id),puzzleView.getPieceShape(id));
	}

	/**
	 * Check each location
	 */
	@Test
	void testGetPieceLocation() {
		for(int id=0; id < ROWS*COLUMNS; id++)
			assertEquals(locations.get(id),puzzleView.getStandardPieceLocation(id));
	}
}
