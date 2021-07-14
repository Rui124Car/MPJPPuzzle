package mpjp.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import mpjp.shared.MPJPException;
import mpjp.shared.geom.Point;

/**
 * Check puzzle structures
 * 
 *  @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
class PuzzleStructureTest extends PuzzleData {
	
	/**
	 * Check instantiation of puzzle from test data
	 * @param puzzle with test data
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testPuzzleStructure(Puzzle puzzle) {
		assertNotNull(puzzle.getStructure());
	}

	/**
	 * Check piece count in test puzzles 
	 * 
	 * @param puzzle with test data
	 * @param count expected
	 */
	@ParameterizedTest
	@CsvSource({ "P0, 80", "P1, 80", "P2, 100"})
	void testGetPieceCount(Puzzle puzzle, int count) {
		assertEquals(count,puzzle.getStructure().getPieceCount());
	}

	/**
	 * Check rows in  test puzzles
	 * 
	 * @param puzzle with test data
	 * @param rows expected
	 */
	@ParameterizedTest
	@CsvSource({ "P0, 8", "P1, 8", "P2, 10"})
	void testGetRows(Puzzle puzzle, int rows) {
		assertEquals(rows,puzzle.getStructure().getRows());
	}

	/**
	 * Check rows property setter and getter
	 * 
	 * @param rows values provided by method
	 */
	@ParameterizedTest
	@MethodSource("intProvider")
	void testSetRows(int rows) {
		PuzzleStructure structure = Puzzle.P0.getStructure();
		
		structure.setRows(rows);
		assertEquals(rows,structure.getRows());
	}
	
	/**
	 * Check columns in  test puzzles
	 * 
	 * @param puzzle with test data
	 * @param columns expected
	 */
	@ParameterizedTest
	@CsvSource({ "P0, 10", "P1, 10", "P2, 10"})
	void testGetColumns(Puzzle puzzle, int columns) {
		assertEquals(columns,puzzle.getStructure().getColumns());
	}


	/**
	 * Check columns property setter and getter
	 * 
	 * @param columns values provided by method
	 */
	@ParameterizedTest
	@MethodSource("intProvider")
	void testSetColumns(int columns) {
		PuzzleStructure structure = Puzzle.P0.getStructure();
		
		structure.setColumns(columns);
		assertEquals(columns,structure.getColumns());

	}

	/**
	 * Check width in  test puzzles
	 * 
	 * @param puzzle with test data
	 * @param width expected
	 */
	@ParameterizedTest
	@CsvSource({ "P0, 100", "P1, 200", "P2, 300"})
	void testGetWidth(Puzzle puzzle, double width) {
		assertEquals(width,puzzle.getStructure().getWidth());
	}

	/**
	 * Check width property setter and getter
	 * 
	 * @param width values provided by method
	 */
	@ParameterizedTest
	@MethodSource("intProvider")
	void testSetWidth(double width) {
		PuzzleStructure structure = Puzzle.P0.getStructure();
		
		structure.setWidth(width);
		assertEquals(width,structure.getWidth());
	}

	/**
	 * Check height in  test puzzles
	 * 
	 * @param puzzle with test data
	 * @param height expected
	 */
	@ParameterizedTest
	@CsvSource({ "P0, 80", "P1, 100", "P2, 300"})
	void testGetHeight(Puzzle puzzle, double height) {
		assertEquals(height,puzzle.getStructure().getHeight());
	}

	/**
	 * Check height property setter and getter
	 * 
	 * @param height values provided by method
	 */
	@ParameterizedTest
	@MethodSource("intProvider")
	void testSetHeight(double height) {
		PuzzleStructure structure = Puzzle.P0.getStructure();
		
		structure.setHeight(height);
		assertEquals(height,structure.getHeight());
	}

	/**
	 * Check piece width in  test puzzles
	 * 
	 * @param puzzle with test data
	 * @param pieceWidth expected
	 */
	@ParameterizedTest
	@CsvSource({ "P0, 10", "P1, 20", "P2, 30"})
	void testGetPieceWidth(Puzzle puzzle, double pieceWidth) {
		assertEquals(pieceWidth,puzzle.getStructure().getPieceWidth());
	}

	/**
	 * Check pieceHeight property setter and getter
	 * 
	 * @param pieceHeight values provided by method
	 */
	@ParameterizedTest
	@CsvSource({ "P0, 10", "P1, 12.5", "P2, 30"})
	void testGetPieceHeight(Puzzle puzzle, double pieceHeight) {
		assertEquals(pieceHeight,puzzle.getStructure().getPieceHeight());
	}
	
	/**
	 * Check piece facing in test puzzles
	 * 
	 * @param puzzle with test data
	 * @param id of piece
	 * @param dirName direction name
	 * @param face expected id of piece facing in given direction
	 */
	@ParameterizedTest
	@CsvSource({ 
		"P0, 0,EAST, 1", "P0, 0,SOUTH,10", "P0, 0,WEST,-1", "P0, 0,NORTH,-1", 
		"P0, 1,EAST, 2", "P0, 1,SOUTH,11", "P0, 1,WEST, 0", "P0, 1,NORTH,-1",
		"P0, 9,EAST,-1", "P0, 9,SOUTH,19", "P0, 9,WEST, 8", "P0, 9,NORTH,-1",
		"P0,10,EAST,11", "P0,10,SOUTH,20", "P0,10,WEST,-1", "P0,10,NORTH, 0",
		"P0,70,EAST,71", "P0,70,SOUTH,-1", "P0,70,WEST,-1", "P0,70,NORTH,60",
		"P0,79,EAST,-1", "P0,79,SOUTH,-1", "P0,79,WEST,78", "P0,79,NORTH,69",
		
		"P2, 0,EAST, 1", "P2, 0,SOUTH,10", "P2, 0,WEST,-1", "P2, 0,NORTH,-1", 
		"P2, 1,EAST, 2", "P2, 1,SOUTH,11", "P2, 1,WEST, 0", "P2, 1,NORTH,-1",
		"P2, 9,EAST,-1", "P2, 9,SOUTH,19", "P2, 9,WEST, 8", "P2, 9,NORTH,-1",
		"P2,10,EAST,11", "P2,10,SOUTH,20", "P2,10,WEST,-1", "P2,10,NORTH, 0",
		"P2,90,EAST,91", "P2,90,SOUTH,-1", "P2,90,WEST,-1", "P2,90,NORTH,80",
		"P2,99,EAST,-1", "P2,99,SOUTH,-1", "P2,99,WEST,98", "P2,99,NORTH,89",
	
	})
	void testGetPieceFacing(Puzzle puzzle, int id, String dirName, int face) throws MPJPException {
		Direction direction = Direction.valueOf(dirName);
		Integer facing = (face < 0 ? null: face);
		
		assertEquals(facing,puzzle.getStructure().getPieceFacing(direction, id));
	}

	/**
	 * Check piece "center" in pieces facing given position in givem diretion
	 * 
	 * @param puzzle with test data
	 * @param dirName direction name
	 * @param bx base x coordinate
	 * @param by base y coordinate
	 * @param fx facing x coordinate
	 * @param fy facing y coordinate
	 */
	@ParameterizedTest
	@CsvSource({ 
		"P0, EAST,  100,100, 110,100",
		"P0, SOUTH, 100,100, 100,110",
		"P0, WEST,  100,100,  90,100",
		"P0, NORTH, 100,100, 100, 90",
		
		"P1, EAST,  100,100, 120,100",
		"P1, SOUTH, 100,100, 100,112.5",
		"P1, WEST,  100,100,  80,100",
		"P1, NORTH, 100,100, 100, 87.5",
		
		"P2, EAST,  100,100, 130,100",
		"P2, SOUTH, 100,100, 100,130",
		"P2, WEST,  100,100,  70,100",
		"P2, NORTH, 100,100, 100, 70",
	})
	void testGetPieceCenterFacing(Puzzle puzzle, String dirName, 
			double bx, double by, // base point coordinates
			double fx, double fy  // face point coordinates 
			) {
		Direction direction = Direction.valueOf(dirName);
		Point base = new Point(bx,by);
		Point face = new Point(fx,fy);
		
		assertEquals(face,	
				puzzle.getStructure().getPieceCenterFacing(direction, base));
	}

	/**
	 * Check row of given piece in test puzzles, including invalid IDs
	 * 
	 * @param puzzle with test data
	 * @param id of piece
	 * @param row expected
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@CsvSource({ 
		"P0, 0, 0", "P0, 1, 0", "P0, 8, 0", "P0, 9, 0",
		"P0,10, 1", "P0,11, 1", "P0,18, 1", "P0,19, 1",
		"P0,60, 6", "P0,61, 6", "P0,68, 6", "P0,69, 6",
		"P0,70, 7", "P0,71, 7", "P0,78, 7", "P0,79, 7",
		
		"P0,80,-1", "P0,81,-1", "P0,-1,-1", "P0,-2,-1",
		
		"P2, 0, 0", "P2, 1, 0", "P2, 8, 0", "P2, 9, 0",
		"P2,10, 1", "P2,11, 1", "P2,18, 1", "P2,19, 1",
		"P2,80, 8", "P2,81, 8", "P2,88, 8", "P2,89, 8",
		"P2,90, 9", "P2,91, 9", "P2,98, 9", "P2,99, 9",

		"P2,100,-1",  	"P2,-1,-1"
	})
	void testGetPieceRow(Puzzle puzzle,int id, int row) throws MPJPException {
		
		if(row == -1)	
			assertThrows(MPJPException.class, 
					() -> puzzle.getStructure().getPieceRow(id));
		else
			assertEquals(row,puzzle.getStructure().getPieceRow(id));			
	}

	/**
	 * Check column of given piece in test puzzles, including invalid IDs
	 * 
	 * @param puzzle with test data
	 * @param id of piece
	 * @param row expected
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@CsvSource({ 
		"P0, 0, 0", "P0, 1, 1", "P0, 8, 8", "P0, 9, 9",
		"P0,10, 0", "P0,11, 1", "P0,18, 8", "P0,19, 9",
		"P0,60, 0", "P0,61, 1", "P0,68, 8", "P0,69, 9",
		"P0,70, 0", "P0,71, 1", "P0,78, 8", "P0,79, 9",
		
		"P0,80,-1", "P0,81,-1", "P0,-1,-1", "P0,-2,-1",
		
		"P2, 0, 0", "P2, 1, 1", "P2, 8, 8", "P2, 9, 9",
		"P2,10, 0", "P2,11, 1", "P2,18, 8", "P2,19, 9",
		"P2,80, 0", "P2,81, 1", "P2,88, 8", "P2,89, 9",
		"P2,90, 0", "P2,91, 1", "P2,98, 8", "P2,99, 9",

		"P2,100,-1",  	"P2,-1,-1"
	})
	void testGetPieceColumn(Puzzle puzzle,int id, int column)
			throws MPJPException {
		
		if(column == -1)	
			assertThrows(MPJPException.class, 
					() -> puzzle.getStructure().getPieceColumn(id));
		else
			assertEquals(column,puzzle.getStructure().getPieceColumn(id));
			
	}
	
	/**
	 * Check locations of pieces in solved puzzle
	 * 
	 * @param puzzle with test data
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testGetLocations(Puzzle puzzle) throws MPJPException {
		PuzzleStructure structure = puzzle.getStructure();
		Map<Integer, Point> locations = structure.getStandardLocations();
		int pieceCount = structure.getPieceCount();
		double width = structure.getPieceWidth();
		double height = structure.getPieceHeight();
		
		assertEquals(pieceCount,locations.size(),
				"# locations should equsl the # of pieces");
		for(int id=0; id < pieceCount; id++) {
			assertTrue(locations.containsKey(id),"location for id:"+id);
			
			int row = structure.getPieceRow(id);
			int column = structure.getPieceColumn(id);
			
			double x = ((double) column + 0.5D) * width;
			double y = ((double) row    + 0.5D) * height;
			
			Point expected = new Point(x,y);
			assertEquals(expected,locations.get(id));
		}
	}
	
	/**
	 * Check that puzzles structures are iterable on the piece IDs,
	 * are produced in ascending order and for all pieces.
	 *  
	 * @param puzzle with test data
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testIterable(Puzzle puzzle) {
		PuzzleStructure structure = puzzle.getStructure();
		int last = -1;
		
		for(int id: structure) {
			assertEquals(last+1,id,"IDs expected in ascending order");
			last = id;
		}
		
		assertEquals(structure.getPieceCount()-1, last,
				"Number of IDs should be equal to piece count");
	}
	
	@ParameterizedTest
	@CsvSource({ 
		"P0, 0,  5, 5", "P0, 1, 15, 5", "P0, 9, 95, 5",
		"P0,10,  5,15", "P0,11, 15,15", "P0,19, 95,15",
		"P0,70,  5,75", "P0,71, 15,75", "P0,79, 95,75",
		"P0, 80, -1,-1",
		
		"P1, 0, 10, 6.25", "P1, 1, 30, 6.25", "P1, 9,190, 6.25",
		"P1,10, 10,18.75", "P1,11, 30,18.75", "P1,19,190,18.75",
		"P1,70, 10,93.75", "P1,71, 30,93.75", "P1,79,190,93.75",
		
		"P2, 0, 15, 15", "P2, 1, 45, 15", "P2, 9,285, 15",
		"P2,10, 15, 45", "P2,11, 45, 45", "P2,19,285, 45",
		"P2,90, 15,285", "P2,91, 45,285", "P2,99,285,285"
	})
	void testGetPieceStandardCenter(Puzzle puzzle, int id, double x, double y) 
			throws MPJPException {
		PuzzleStructure structure = puzzle.getStructure();
		if(x <0)
			assertThrows(MPJPException.class, 
					() -> structure.getPieceStandardCenter(id));
		else
			assertEquals(new Point(x,y), structure.getPieceStandardCenter(id));
	}

	@ParameterizedTest
	@CsvSource({ 
		"P0,  0, 0, 0;1;10;11",
		"P0,  5, 4, 0;1;10;11",
		"P0, 10, 4, 0;1;2;10;11;12",
		"P0, 85, 4, 7;8;9;17;18;19",
		"P0, 95, 4, 8;9;18;19",
		
		"P0,  5, 12, 0;1;10;11;20;21",
		"P0, 15, 12, 0;1;2;10;11;12;20;21;22",
		"P0, 85, 12, 7;8;9;17;18;19;27;28;29",
		"P0, 95, 12, 8;9;18;19;28;29",
		
		"P0,  5, 76, 60;61;70;71",
		"P0, 15, 76, 60;61;62;70;71;72",
		"P0, 85, 76, 67;68;69;77;78;79",
		"P0, 95, 76, 68;69;78;79",
		"P0, 99, 79, 68;69;78;79",
	})
	void testGetPossiblePiecesFor(Puzzle puzzle, double x, double y, String ids) throws MPJPException {
		PuzzleStructure structure = puzzle.getStructure();
		Point point 			= new Point(x,y);
		Set<Integer> expected 	= new HashSet<>();
		
		for(String id: ids.split(";"))
			expected.add(Integer.parseInt(id));
		
		assertEquals(expected,structure.getPossiblePiecesInStandarFor(point));
	}

	
	@EnumSource(Puzzle.class)
	void testGetRandomPointInPuzzle(Puzzle puzzle) {
		PuzzleStructure structure = puzzle.getStructure();
		for(int c=0; c< REPETIONS; c++) {
			Point point = structure.getRandomPointInStandardPuzzle();
		
			assertTrue(point.getX() >= 0);
			assertTrue(point.getX() <  structure.getWidth());
		
			assertTrue(point.getY() >= 0);
			assertTrue(point.getY() <  structure.getHeight());
		}
	}

}
