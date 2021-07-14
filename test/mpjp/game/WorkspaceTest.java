package mpjp.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

/**
 * Tests Workspace class
 *
 * @author Jos&eacute; paulo Leal {@code zp@dcc.fc.up.pt}
 */
class WorkspaceTest extends PuzzleData {

	/**
	 * Check if workspaces are instantiated
	 *  
	 * @param puzzle with data for testing
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testWorkspace(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		
		assertNotNull(workspace,"workspace expected");
	}

	/**
	 * Check static property radius and its setter and getter
	 * @param value to test
	 */
	@ParameterizedTest
	@MethodSource("doubleProvider")
	void test_radius(double value) {
		Workspace.setRadius(value);
		assertEquals(value,Workspace.getRadius());
	}

	/**
	 * Check static property heightFactor and its setter and getter
	 * @param value to test
	 */
	@ParameterizedTest
	@MethodSource("doubleProvider")
	void test_heightFactor(double value) {
		Workspace.setHeightFactor(value);
		assertEquals(value,Workspace.getHeightFactor());
	}

	/**
	 * Check static property heightFactor and its setter and getter
	 * @param value to test
	 */
	@ParameterizedTest
	@MethodSource("doubleProvider")
	void test_widthFactor(double value) {
		Workspace.setWidthFactor(value);
		assertEquals(value,Workspace.getWidthFactor());
	}
	
	
	/**
	 * Check that Id is a string and that a new ID is produced even if
	 * workspaces are created within a millisecond   
	 * 
	 * @param puzzle with data for testing
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testGetId(Puzzle puzzle) throws MPJPException {
		PuzzleInfo info = puzzle.getPuzzleInfo();
		Set<String> ids = new HashSet<>();
		
		for(int c=0; c < REPETIONS; c++) {
			Workspace workspace = new Workspace(info);
			String id = workspace.getId();
			
			assertNotNull(id,"String expected");
			
			assertFalse(ids.contains(id),"different IDs expected");
			ids.add(id);
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}
		
	}

	/**
	 * Check if puzzle views match with provided info
	 * 
	 * @param puzzle with data for testing
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testGetPuzzleView(Puzzle puzzle) throws MPJPException {
		PuzzleInfo info = puzzle.getPuzzleInfo();
		Workspace workspace = new Workspace(info);
		PuzzleView puzzleView = workspace.getPuzzleView();
		
		assertNotNull(puzzleView,"puzzle view expected");
		assertEquals(info.getWidth(),puzzleView.getPuzzleWidth(),"wrong width");
		assertEquals(info.getHeight(),puzzleView.getPuzzleHeight(),"wrong height");
		assertEquals(info.getImageName(),puzzleView.getImage(),"wrong image");
	}

	/**
	 * Check if initial layout matches with provided info
	 * 
	 * @param puzzle with data for testing
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testGetCurrentLayout(Puzzle puzzle) throws MPJPException {
		PuzzleInfo info = puzzle.getPuzzleInfo();
		Workspace workspace = new Workspace(info);
		PuzzleLayout puzzleLayout = workspace.getCurrentLayout();
		int pieceCount = info.getRows() * info.getColumns();
		
		assertNotNull(puzzleLayout,"puzzleLayout expected");
		assertAll(
				() -> assertEquals(pieceCount,puzzleLayout.getPieces().size(),
						"unexpected #pieces"),
				() -> assertEquals(pieceCount,puzzleLayout.getBlocks().size(),
						"unexpected #blocks (initialy equal to #pieces)")
				);
	}

	/**
	 * Check that pieces are selected when retrieved by their point,
	 * of if a different piece is selected (it may be overlapping it)
	 * is belongs to a block with a higher ID (lower ID block tend
	 * to be larger, higher ID blocks then to be disconnected pieces).     
	 * 
	 * @param puzzle to test
	 * @throws MPJPException if something unexpected occurs
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testSelectPiece(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleLayout puzzleLayout = workspace.getCurrentLayout();
		Map<Integer, PieceStatus> pieces = puzzleLayout.getPieces();
		double radius = workspace.getSelectRadius();
		double delta = Math.sqrt(radius);
		
		for(Integer id: pieces.keySet()) {
			PieceStatus piece = pieces.get(id);
			Point point = piece.getPosition();
			Point near = new Point(
					point.getX()+getDelta(delta),
					point.getY()+getDelta(delta));
			Integer block = piece.getBlock();
			
			Integer selected = workspace.selectPiece(near);
			
			Integer selectedBlock = pieces.get(selected).getBlock();
			
			assertNotNull(selected,"Some piece selected");
			
			assertTrue(id == selected || selectedBlock > block,
					"At least a higher block expected");
		}
	}

	/**
	 * Connect 2 pieces in the expected position
	 * 
	 * @param puzzle with data for testing
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testConnect_exact(Puzzle puzzle) throws MPJPException {		
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleStructure structure = workspace.getPuzzleStructure();
		PuzzleLayout puzzleLayout = workspace.getCurrentLayout();
		Map<Integer, PieceStatus> pieces = puzzleLayout.getPieces();
		int nBlocks = puzzleLayout.getBlocks().size();
		
		double pieceWidth = structure.getPieceWidth();
		
		Point p0 = pieces.get(0).getPosition();
		Point p1 = new Point(p0.getX()+pieceWidth,p0.getY());
		
		PuzzleLayout newLayout = workspace.connect(1, p1);
		
		assertTrue(newLayout.getBlocks().size()<nBlocks,"less blocks expected");
	}
	
	/**
	 * Connect 2 pieces with a random small difference in the expected position
	 * 
	 * @param puzzle with data for testing
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testConnect_delta(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleStructure structure = workspace.getPuzzleStructure();
		PuzzleLayout puzzleLayout = workspace.getCurrentLayout();
		Map<Integer, PieceStatus> pieces = puzzleLayout.getPieces();
		int nBlocks = puzzleLayout.getBlocks().size();
		
		double pieceWidth = structure.getPieceWidth();
		double delta = Math.sqrt(Workspace.getRadius());
		
		Point p0 = pieces.get(0).getPosition();
		Point p1 = new Point(
				p0.getX()+pieceWidth+getDelta(delta),
				p0.getY()+getDelta(delta));
		
		
		PuzzleLayout newLayout = workspace.connect(1, p1);
			
		assertTrue(newLayout.getBlocks().size() < nBlocks,
	 				"one less blocks expected");
			
	}

	/**
	 * Connecting a piece that moved out of the workspace should raise
	 * an exception, and the pieces should be reverted to their original 
	 * positions:
	 * 
	 * @param puzzle with data for testing
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testConnect_out_of_workspace(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleLayout puzzleLayout = workspace.getCurrentLayout();
		PuzzleView puzzleView = workspace.getPuzzleView();
		Map<Integer, PieceStatus> pieces = puzzleLayout.getPieces();
		int nBlocks = puzzleLayout.getBlocks().size();
		
		Point p0 = pieces.get(0).getPosition();
		Point p1 = pieces.get(0).getPosition();
		Point out = new Point(
				2*puzzleView.getWorkspaceWidth(),
				2*puzzleView.getWorkspaceHeight());
		
		 		
 		assertThrows(MPJPException.class, () -> workspace.connect(1, out));
		 		
 		PuzzleLayout newLayout = workspace.getCurrentLayout();
 		Map<Integer, PieceStatus> newPieces = newLayout.getPieces();
 		
 		assertEquals(p0,newPieces.get(0).getPosition(),"should not have changed");
 		assertEquals(p1,newPieces.get(0).getPosition(),"should not have changed");
		assertEquals(nBlocks,newLayout.getBlocks().size(),"same blocks expected");
 		
	}	
	
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testConnect_invalid_ids(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleLayout puzzleLayout = workspace.getCurrentLayout();
		PuzzleView puzzleView = workspace.getPuzzleView();
		int nPoints = puzzleLayout.getPieces().size();
		Point center = new Point(
				puzzleView.getWorkspaceWidth()/2,
				puzzleView.getWorkspaceHeight()/2);
		
		assertAll(
				() -> assertThrows(MPJPException.class,
						() -> workspace.connect(nPoints, center),
						"exception expected with an non existing piece id"),
				() -> assertThrows(MPJPException.class,
						() -> workspace.connect(nPoints, center),
						"exception expected with a negative piece id")
				);
	}
	
	/**
	 * Connect pieces in order and check that the percentage solved evolves 
	 * as expected.
	 * 
	 * @param puzzle with data for testing
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testPercentageSolved(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleView view     = workspace.getPuzzleView();
		double puzzleWidth  = view.getPuzzleWidth();
		double puzzleHeight = view.getPuzzleHeight();
		double pieceWidth   = view.getPieceWidth();
		double pieceHeight  = view.getPieceHeight();
		int    rows         = (int) (puzzleHeight / pieceHeight);
		int    columns      = (int) (puzzleWidth / pieceWidth);
		int    total        = rows * columns;
		PuzzleLayout layout = null;
		int    blockCount   = total;
		int    id           = 0;

		for(int row=0; row < rows; row++) 
			for(int column=0; column < columns; column++) {
				double x = ( column + 0.5D) * pieceWidth;
				double y = (    row + 0.5D) * pieceHeight;

				layout = workspace.connect(id++, new Point(x,y));
				
				Map<Integer, PieceStatus> pieces = layout.getPieces();
				Map<Integer, List<Integer>> blocks = layout.getBlocks();
				int b = blocks.size();
				int p = pieces.size();
				
				assertTrue(blockCount-- >= b,"Unexpected block count");
				
				int percentageSolved = 100*(p - b)/(p - 1);
				
				assertEquals(percentageSolved,workspace.getPercentageSolved());
				assertEquals(percentageSolved,layout.getPercentageSolved());
			}
		assertTrue(layout.isSolved());
	}

}
