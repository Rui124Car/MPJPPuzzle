package mpjp.shared.geom;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mpjp.TestData;

/**
 * Tests on Point
 *
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
class PointTest extends TestData {
	Point point;
	
	@BeforeEach
	public void before() {
		point = new Point(X,Y);
	}

	/**
	 * Check an instance is created without arguments
	 */
	@Test
    @DisplayName("Test constructor Point()")
	void testPoint() {
		assertNotNull(new Point());
	}

	/**
	 * Check an instance is created with teh right arguments
	 */
	@Test
    @DisplayName("Test constructor Point(double,double)")
	void testPointDoubleDouble() {
		assertNotNull(point);
	}

	/**
	 * Check the horizontal
	 */
	@Test
	void testGetX() {
		
		assertEquals(X,point.getX());
	}

	/**
	 * Check the vertical coordinate
	 */
	@Test
	void testGetY() {
		assertEquals(Y,point.getY());
	}

	@Test
    @DisplayName("Test equals() [generated method]")
	void testequals() {
		assertAll(
				() ->	assertEquals(point, new Point(X,Y)),
				() -> 	assertNotEquals(point, new Point(X+1,X-1))
				);
	}
}
