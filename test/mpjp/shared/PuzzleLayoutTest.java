package mpjp.shared;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
* Tests on Point
*
* @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
*/
class PuzzleLayoutTest {
	private static final int N_PIECES = 10;

	PuzzleLayout puzzleLayout; 

	@BeforeEach
	void setUp() throws Exception {
		Map<Integer,PieceStatus> pieces = makePieces(N_PIECES);
		Map<Integer,List<Integer>> blocks = makeBlocks(N_PIECES);
		
		puzzleLayout = new PuzzleLayout(pieces,blocks,0);

	}

	/**
	 * Check instance without arguments
	 */
	@Test
	void testLayout() {
		assertNotNull(new PuzzleLayout());
	}

	/**
	 * Check instance with expected arguments
	 */
	@Test
	@DisplayName("test Layout constructor")
	void testLayoutMapOfIntegerPieceStatusMapOfIntegerListOfInteger() {
		assertNotNull(puzzleLayout);
	}

	/**
	 * Check pieces map
	 */
	@Test
	void testGetPieces() {
		Map<Integer, PieceStatus> pieces = puzzleLayout.getPieces();
		assertAll(
				() -> assertNotNull(pieces),
				() -> assertEquals(N_PIECES, pieces.size()) 
			);
	}

	/**
	 * Check blocks map
	 */
	@Test
	void testGetBlocks() {
		Map<Integer, List<Integer>> blocks = puzzleLayout.getBlocks();
		assertAll(
				() -> assertNotNull(blocks),
				() -> assertEquals(N_PIECES, blocks.size())
				);
	}

	/**
	 * Check is solved as  
	 */
	@Test
	void testIsSolved() {
		Map<Integer,PieceStatus> pieces = makePieces(N_PIECES);

		assertAll(
				() -> {
					for(int b=N_PIECES; b>1; b--) {
						Map<Integer,List<Integer>> blocks = makeBlocks(b);
						puzzleLayout = new PuzzleLayout(pieces,blocks,b);
						assertFalse(puzzleLayout.isSolved(),
								b+" blocks, shouldn't solved");
					}
				},
				() -> {
					Map<Integer,List<Integer>> blocks = makeBlocks(1);
					puzzleLayout = new PuzzleLayout(pieces,blocks,1);
					assertTrue(puzzleLayout.isSolved(),
							"Only one block, should be solved");
				});
	}
	
	/**
	 * Check percentage complete
	 */
	@Test
	void testPercentageComplete() {
		Map<Integer,PieceStatus> pieces = makePieces(N_PIECES);
		Map<Integer,List<Integer>> blocks;
		
		int p = N_PIECES;
		for(int b=1; b<=N_PIECES; b++) {
			int expected = 100*(p-b)/(p-1);
			
			blocks = makeBlocks(b);
			puzzleLayout = new PuzzleLayout(pieces,blocks,expected);
		
			assertEquals(expected,puzzleLayout.getPercentageSolved());
		}
	}
	
	
	private Map<Integer,PieceStatus> makePieces(int n) {
		Map<Integer,PieceStatus> pieces = new HashMap<>();
		
		for(int c=0; c<n; c++)
			pieces.put(c,new PieceStatus());
		
		return pieces;
	}
	
	private Map<Integer,List<Integer>> makeBlocks(int n) {
		Map<Integer,List<Integer>> blocks = new HashMap<>();
		
		for(int c=0; c<n; c++)
			blocks.put(c,new ArrayList<Integer>());
		
		return blocks;
	}

}
