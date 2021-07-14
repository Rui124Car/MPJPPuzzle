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
 * Test on a LeafTrie, the leaves of a quadtree
 * Test the implemented methods from the abstract
 * type {@code Trie} 
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
public class LeafTrieTest {
	
	private static int CAPACITY = 10;
	
	private static final int BOTTOM_RIGHT_Y = 10;
	private static final int BOTTOM_RIGHT_X = 20;
	private static final int TOP_LEFT_Y = 20;
	private static final int TOP_LEFT_X = 10;
	
	private static final int CENTER_X = (TOP_LEFT_X + BOTTOM_RIGHT_X)/2;
	private static final int CENTER_Y = (TOP_LEFT_Y + BOTTOM_RIGHT_Y)/2;
	
	private static final int LARGE_RADIUS = 100;
	private static final int SMALL_RADIUS = 1;
	
	private static final Location CENTER = new Location("", CENTER_X, CENTER_Y);
		
	LeafTrie<Location> leaf;
	Set<Location> points;
	
	@BeforeAll
	public static void init() {
		Trie.setCapacity(CAPACITY);
	}
	
	/**
	 * Create a leaf and a set for collecting points
	 */
	@BeforeEach
	public void setUp() {
		leaf = new LeafTrie<Location>(TOP_LEFT_X,TOP_LEFT_Y,BOTTOM_RIGHT_X,BOTTOM_RIGHT_Y);
		points = new HashSet<>();
	}
	
	/**
	 * No points before insertion, either with collect or find
	 */
	@Test
	public void testEmptyOnCreation() {
		leaf.collectNear(TOP_LEFT_X, TOP_LEFT_Y, LARGE_RADIUS, points);
		
		assertAll(
				() -> assertEquals(0,points.size()),
		
				() -> assertNull(leaf.find(CENTER))
				);
	}
	
	/**
	 * Test a single location using insertion and find
	 */
	@Test  
	public void testInsertAndFind() {
		leaf.insert(CENTER);
		
		assertEquals(CENTER,leaf.find(CENTER));
	}
	
	
	/**
	 * Test that a duplicate insert/replace desn't raise an exception
	 */
	@Test  
	public void testDuplicateInsertReplace() {
		leaf.insert(CENTER);

		leaf.insertReplace(CENTER);
	}
	
	/**
	 * Check that locations aren't there after delete
	 */
	@Test
	public void testDelete() {
		assertAll(
				() -> {
					leaf.insert(CENTER);
					assertEquals(CENTER,leaf.find(CENTER));
				},
				() -> {
					leaf.delete(CENTER);
					assertNull(leaf.find(CENTER));
				});
	}
	
	/**
	 * Check delete of non existing point
	 */
	@Test
	public void testDeleteMissingPoint() {
		
		leaf.delete(CENTER);
		assertNull(leaf.find(CENTER));
	}
	
	private static final Random random = new Random();

	/**
	 * Insert several locations in bucket 
	 */
	@Test  
	public void testInsertRandom() {
		Set<Location> locations = new HashSet<Location>();
		
		for(short count =0; count < CAPACITY-1; count++) {
			Location location = null;
				
			do {
				double x = CENTER_X + 2*random.nextDouble()*SMALL_RADIUS - SMALL_RADIUS;
				double y = CENTER_Y + 2*random.nextDouble()*SMALL_RADIUS - SMALL_RADIUS;
				location = new Location("", x, y);
			} while(locations.contains(location));
			
			leaf.insert(location);
			leaf.collectNear(TOP_LEFT_X, TOP_LEFT_Y, LARGE_RADIUS, points);

			assertEquals(count+1,points.size());
		}

	}

	
	/**
	 * Find near points on a QuadTree having a single leaf
	 */
	@Test
	public void testFindNearLeaf() {
		generateLocationsInGrid();
		
		leaf.collectNear(CENTER_X, CENTER_Y, SMALL_RADIUS, points);
		
		assertEquals(5,points.size());
	}

	
	/**
	 * Find all points on a QuadTree having a single leaf
	 */
	@Test
	public void testFindAllLeaf() {
		generateLocationsInGrid();
		
		leaf.collectAll( points);
		
		assertEquals(9,points.size());
	}
	
	private void generateLocationsInGrid() {
		
		for(int x=CENTER_X-1; x <= CENTER_X+1; x++)
			for(int y=CENTER_Y-1; y <= CENTER_Y+1; y++)
				leaf.insert(new Location("",x,y));
	}
}
