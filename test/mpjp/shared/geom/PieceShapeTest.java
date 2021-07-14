package mpjp.shared.geom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class PieceShapeTest extends GeomTestData {
	PieceShape pieceShape;
	Point[] points;
	
	@BeforeEach
	void setUp() throws Exception {
		points = getPoints();
		pieceShape = new PieceShape(points[0]);
	}
	
	/**
	 * Check empty constructor
	 */
	@Test
	void testPieceShape() {
		assertNotNull(new PieceShape());
	}

	/**
	 * Check constructor with just start point
	 */
	@Test
	void testPieceShapePoint() {
		assertNotNull(pieceShape);
	}

	/**
	 * Check constructor with start point and segments
	 */
	@Test
	void testPieceShapePointListOfSegment() {
		List<Segment> segments = Arrays.asList(new LineTo(points[1]));
		assertNotNull(new PieceShape(points[0],segments));
	}

	/**
	 * Check start point getter
	 */
	@Test
	void testGetStartPoint() {
		assertEquals(points[0],pieceShape.getStartPoint());
	}

	/**
	 * Check start point getter
	 */
	@ParameterizedTest
	@MethodSource("pointProvider")
	void testSetStartPoint(Point point) {
		pieceShape.setStartPoint(point);
		assertEquals(point,pieceShape.getStartPoint());
	}

	@Test
	void testGetSegments() {
		List<Segment> segments = pieceShape.getSegments();
		
		assertNotNull(segments);
		assertEquals(0,segments.size());
	}

	@Test
	void testSetSegments() {
		List<Segment> segments = Arrays.asList(new LineTo(points[1]));
		
		pieceShape.setSegments(segments);
		assertNotNull(pieceShape.getSegments());
		assertEquals(1,pieceShape.getSegments().size());
	}

	@Test
	void testAddSegment() {
		int count = 0;
		for(Point point: points) {
			Segment segment = new LineTo(point);
			PieceShape chain = pieceShape.addSegment(segment);
			List<Segment> segments = pieceShape.getSegments();
			
			assertTrue(chain == pieceShape,"expected same shape for chaining");
			assertTrue(++ count == segments.size(),"wrong number of segments");
			assertTrue(segments.contains(segment),"segment expected");
		}		
	}

}
