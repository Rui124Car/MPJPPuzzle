package mpjp.shared.geom;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test on CQuadTo segments
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
class QuadToTest extends GeomTestData {
	QuadTo quadTo;
	Point[] points;
	
	@BeforeEach
	void setUp() throws Exception {
		points = getPoints();
		quadTo = new QuadTo(points[0],points[1]);

	}
	
	/**
	 * Check empty constructor
	 */
	@Test
	void testQuadTo() {
		assertNotNull(new QuadTo());
	}
	
	/**
	 * Check regular constructor
	 */
	@Test
	void testQuadToPointPoint() {
		assertNotNull(quadTo);
	}

	/**
	 * Check control control point getter
	 */
	@Test
	void testGetControlPoint() {
		assertEquals(points[0],quadTo.getControlPoint());
	}

	/**
	 * Check control point setter
	 */
	@ParameterizedTest
	@MethodSource("pointProvider")
	void testSetControlPoint(Point point) {
		quadTo.setControlPoint(point);
		assertEquals(point,quadTo.getControlPoint());
	}

	/**
	 * Check end point getter
	 */
	@Test
	void testGetEndPoint() {
		assertEquals(points[1],quadTo.getEndPoint());
	}

	/**
	 * Check control end point setter
	 */
	@ParameterizedTest
	@MethodSource("pointProvider")
	void testSetEndPoint(Point point) {
		quadTo.setEndPoint(point);
		assertEquals(point,quadTo.getEndPoint());
	}

}
