package mpjp.quad;



import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test on a NodesTrie, the nodes of a quadtree.
 * Test the implemented methods from the abstract
 * type {@code Trie} 
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
public class NodeTrieTest {

	private static int CAPACITY = 10;
	
	private static final int BOTTOM_RIGHT_Y = 10;
	private static final int BOTTOM_RIGHT_X = 20;
	private static final int TOP_LEFT_Y = 20;
	private static final int TOP_LEFT_X = 10;
	
	private static final int CENTER_X = (TOP_LEFT_X + BOTTOM_RIGHT_X)/2;
	private static final int CENTER_Y = (TOP_LEFT_Y + BOTTOM_RIGHT_Y)/2;
	
	private static final int REPETITIONS = 1000;
	
	private static final int LARGE_RADIUS = 100;
	private static final int SMALL_RADIUS = 1;
	
	private static final Location CENTER = new Location("", CENTER_X, CENTER_Y);
	
	
	
	private NodeTrie<Location> node;
	Set<Location> points;
	
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		Trie.setCapacity(CAPACITY);
	}

	@BeforeEach
	public void setUp() throws Exception {
		 node = new NodeTrie<Location>(TOP_LEFT_X,TOP_LEFT_Y,BOTTOM_RIGHT_X,BOTTOM_RIGHT_Y);
		 points = new HashSet<>();
	}


	
	/**
	 * No nodes before insertion, either with collect or find
	 */
	@Test
	public void testEmptyOnCreation() {
		node.collectNear(TOP_LEFT_X, TOP_LEFT_Y, LARGE_RADIUS, points);
		
		assertAll(
				() -> 	assertEquals(0,points.size()),
				() -> 	assertNull(node.find(CENTER))
				);
	}
	
	
	/**
	 * Test that a duplicate insert/replace desn't raise an exception
	 */
	@Test  
	public void testDuplicateInsertReplace() {
		node.insert(CENTER);

		node.insertReplace(CENTER);
	}
	
	/**
	 * Check that locations aren't there after delete
	 */
	@Test
	public void testDelete() {
		
		assertAll(
				() -> {
					node.insert(CENTER);
					assertEquals(CENTER,node.find(CENTER));
				},
				() -> {
					node.delete(CENTER);
					assertNull(node.find(CENTER));
				}
			);		
	}

	private static final Random random = new Random();

	/**
	 * Insert several locations in bucket 
	 */
	@Test  
	public void testInsertRandom() {
		Set<Location> locations = new HashSet<Location>();
		
		for(short count =0; count < REPETITIONS; count++) {
			Location location = null;
				
			do {
				double x = CENTER_X + 2*random.nextDouble()*SMALL_RADIUS - SMALL_RADIUS;
				double y = CENTER_Y + 2*random.nextDouble()*SMALL_RADIUS - SMALL_RADIUS;
				location = new Location("", x, y);
			} while(locations.contains(location));
			
			node.insert(location);
			node.collectNear(TOP_LEFT_X, TOP_LEFT_Y, LARGE_RADIUS, points);

			assertEquals(count+1,points.size());
		}
	}
}
