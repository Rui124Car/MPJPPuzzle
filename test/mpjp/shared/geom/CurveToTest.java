package mpjp.shared.geom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test on CurveTo segments
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
class CurveToTest extends GeomTestData {
	CurveTo curveTo;
	Point[] points;
	
	@BeforeEach
	void setUp() throws Exception {
		points = getPoints();
		curveTo = new CurveTo(points[0],points[1],points[2]);
	}

	/**
	 * Check empty constructor
	 */
	@Test
	void testCurveTo() {
		assertNotNull(new CurveTo());
	}

	/**
	 * Check regular constructor
	 */
	@Test
	void testCurveToPointPointPoint() {
		assertNotNull(curveTo);
	}

	/**
	 * Check 1st control point getter 
	 */
	@Test
	void testGetControlPoint1() {
		assertEquals(points[0],curveTo.getControlPoint1());
	}
	
	/**
	 * Check 1st control point setter 
	 */
	@ParameterizedTest
	@MethodSource("pointProvider")
	void testSetControlPoint1(Point point) {
		curveTo.setControlPoint1(point);
		assertEquals(point,curveTo.getControlPoint1());
	}
	
	/**
	 * Check 2nd control point getter
	 */
	@Test
	void testGetControlPoint2() {
		assertEquals(points[1],curveTo.getControlPoint2());
	}

	/**
	 * Check 2nd control point setter
	 */
	@ParameterizedTest
	@MethodSource("pointProvider")
	void testSetControlPoint2(Point point) {
		curveTo.setControlPoint1(point);
		assertEquals(point,curveTo.getControlPoint1());
	}
	
	/**
	 * Check end point getter
	 */
	@Test
	void testGetEndPoint() {
		assertEquals(points[2],curveTo.getEndPoint());
	}

	/**
	 * Check end point setter
	 */
	@ParameterizedTest
	@MethodSource("pointProvider")
	void testSetEndPoint(Point point) {
		curveTo.setEndPoint(point);
		assertEquals(point,curveTo.getEndPoint());

	}

}
