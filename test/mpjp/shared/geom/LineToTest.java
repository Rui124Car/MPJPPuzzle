package mpjp.shared.geom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
* Tests on LineTo segments
*
* @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
*/
class LineToTest extends GeomTestData {
	LineTo lineTo;
	Point point;
	
	@BeforeEach
	void setUp() throws Exception {
		lineTo = new LineTo();
		point = new Point(X,Y);
	}

	@Test
	void testLineTo() {
		assertNotNull(lineTo);
	}

	@Test
	void testLineToPoint() {
		assertNotNull(new LineTo(point));
	}

	@Test
	void testGetEndPoint() {
		lineTo = new LineTo(point);
		assertEquals(point,lineTo.getEndPoint());
	}
	
	/**
	 * Check end point setter
	 */
	@ParameterizedTest
	@MethodSource("pointProvider")
	void testSetEndPoint(Point point) {
		lineTo.setEndPoint(point);
		assertEquals(point,lineTo.getEndPoint());
	}

}
