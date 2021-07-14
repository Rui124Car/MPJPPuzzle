package mpjp.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

/**
 * Template for a test class on Manager - YOU NEED TO IMPLEMENTS THESE TESTS!
 * 
 */
class ManagerTest extends PuzzleData {
	static Manager manager;

	/**
	 * Get the singleton instance for tests
	 */
	@BeforeAll
	static void firstSetUp() {
		manager = Manager.getInstance();
	}

	/**
	 * Reset the singleton to its previous state
	 */
	@BeforeEach
	void setUp() {
		manager.reset();
	}

	/**
	 * GetIntance should return always the same instance
	 */
	@RepeatedTest(value = 10)
	void testGetInstance() {
		Manager instance = Manager.getInstance();
		
		for(int i = 0; i<10; i++) {
			Manager testing = Manager.getInstance();
			
			assertEquals(instance.getClass(), testing.getClass());
		}
	}

	/**
	 * Check if a set of cuttings with is, al least one, is available.
	 */
	@Test
	void testGetAvailableCuttings() {
		Set<String> set = manager.getAvailableCuttings();
		
		assertNotNull(set, "Cuttings set expected");
		assertTrue(set.size() > 1);
	}

	/**
	 * Check if images in test resources are available
	 */
	@Test
	void testGetAvailableImages() {
		Set<String> set = manager.getAvailableImages();
		
		
		for(String image :  TEST_IMAGES) 
			assertTrue(set.contains(image));
		
	}

	/**
	 * Check if available workspace reflect those that where created so far, and map
	 * IDs to the correct type.
	 * 
	 * @throws MPJPException if something unexpected happens
	 */
	@Test
	void testGetAvailableWorkspaces() throws MPJPException {
		fail();
	}

	/**
	 * Check if workspaces created from an info return an ID and that IDs change
	 * even after a minimal delay
	 * 
	 * @param puzzle with data for testing
	 * @throws InterruptedException on error during sleep
	 * @throws MPJPException        if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testCreateWorkspace(Puzzle puzzle) throws InterruptedException, MPJPException {
		PuzzleInfo pi = puzzle.getPuzzleInfo();
		
		String ws1 = manager.createWorkspace(pi);
		String ws2 = manager.createWorkspace(pi);
		
		assertNotEquals(ws1, ws2);
	}

	/**
	 * Check if piece selection using current layout. Using their locations it
	 * should retrieve either the same piece or one with a higher block id, if two
	 * or more pieces overlap
	 * 
	 * @param puzzle to test
	 * @throws MPJPException if an unexpected exception occurs
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testSelectPiece(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleLayout layout = manager.getCurrentLayout(workspace.getId());
		Map<Integer, PieceStatus> pieces = layout.getPieces();
		double radius = workspace.getSelectRadius();
		double delta = Math.sqrt(radius);
		
		for(Integer id: pieces.keySet()) {
			PieceStatus piece = pieces.get(id);
			Point point = piece.getPosition();
			Point near = new Point(
					point.getX()+getDelta(delta),
					point.getY()+getDelta(delta));
			Integer block = piece.getBlock();
			
			Integer selected = manager.selectPiece(workspace.getId(), near);
			
			Integer selectedBlock = pieces.get(selected).getBlock();
			
			assertNotNull(selected,"Some piece selected");
			
			assertTrue(id == selected || selectedBlock > block,
					"At least a higher block expected");
		}
		
	}

	/**
	 * Check if placing the first piece (0) at the center raises no exception, but
	 * trying place a non existing piece does raise an exception.
	 * 
	 * @param puzzle to test
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testConnect(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleLayout puzzleLayout = workspace.getCurrentLayout();
		PuzzleView puzzleView = workspace.getPuzzleView();
		int nPieces = puzzleLayout.getPieces().size();
		Point center = new Point(
				puzzleView.getWorkspaceWidth()/2,
				puzzleView.getWorkspaceHeight()/2);
		
		//está a faltar confirmar se a peça 0 mexer-se faz excepções
		assertThrows(MPJPException.class, () -> manager.connect(nPieces, center));
	}

	/**
	 * Check if puzzle view corresponds to given puzzle info
	 * 
	 * @param puzzle to test
	 * @throws MPJPException if unexpected exceptions occurs
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testGetPuzzleView(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleView pw = manager.getPuzzleView(workspace.getId());
		PuzzleInfo info =  puzzle.getPuzzleInfo();
		
		
		assertNotNull(pw, "PuzzleView expected");
		assertEquals(info.getWidth(),pw.getPuzzleWidth(),"wrong width");
		assertEquals(info.getHeight(),pw.getPuzzleHeight(),"wrong height");
		assertEquals(info.getImageName(),pw.getImage(),"wrong image");
		
	}

	/**
	 * Check if puzzle layout corresponds to given puzzle info, particularly in the
	 * number of pieces, and the initial layout should be unsolved.
	 * 
	 * @param puzzle to test
	 * @throws MPJPException if unexpected exceptions occurs
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)
	void testGetCurrentLayout(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleLayout layout = manager.getCurrentLayout(workspace.getId());
		PuzzleInfo info = puzzle.getPuzzleInfo();
		int pieceCount = info.getRows()*info.getColumns();
		
		assertNotNull(layout, "PuzzleLayout expected");
		assertAll(
				() -> assertEquals(pieceCount,layout.getPieces().size(),
						"unexpected #pieces"),
				() -> assertEquals(pieceCount,layout.getBlocks().size(),
						"unexpected #blocks (initialy equal to #pieces)")	
				);
	}

}
