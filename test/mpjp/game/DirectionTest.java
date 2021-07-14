package mpjp.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests on Direction enumeration 
 * 
 *  @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
class DirectionTest {

	/**
	 * Check enumeration has 4 values 
	 */
	@Test
	@DisplayName("Test number of directions")
	void testDirection() {
		assertEquals(4,Direction.values().length,"4 directions expected");
	}

	/**
	 * Check each direction horizontal signal
	 * 
	 * @param dir    direction
	 * @param signal  expected signal
	 */
	@ParameterizedTest
	@CsvSource({ "EAST,1", "SOUTH,0", "WEST,-1", "NORTH,0"})
	void testGetSignalX(String dir, String signal) {
		assertEquals(Integer.parseInt(signal),Direction.valueOf(dir).getSignalX());
	}

	/**
	 * Check each direction vertical signal
	 * 
	 * @param dir    direction
	 * @param signal  expected signal
	 */
	@ParameterizedTest
	@DisplayName("Test getSignalY()")
	@CsvSource({ "EAST,0", "SOUTH,1", "WEST,0", "NORTH,-1"})
	void testGetDeltaY(String dir, String delta) {
		assertEquals(Integer.parseInt(delta),Direction.valueOf(dir).getSignalY());
	}
}
