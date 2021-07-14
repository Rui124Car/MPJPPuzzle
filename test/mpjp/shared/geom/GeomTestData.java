package mpjp.shared.geom;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import mpjp.TestData;

/**
 * Test data for types in this package, in particular points.
 * Avoid using it for testing the Point class
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
public class GeomTestData extends TestData {
	
	/**
	 * Standard test points
	 * @return array with 3 points
	 */
	protected Point[] getPoints() {
		return new Point[] {
			new Point(160,140),
			new Point(220,270),
			new Point(350,350) 
		};
	};
	
	
	protected static Stream<Point> pointProvider() {

		Builder<Point> builder = Stream.builder();
		
		for(int c=0; c< REPETIONS; c++)
			builder.accept(new Point(RANDOM.nextDouble(),RANDOM.nextDouble()));
			
		return builder.build();
		
	}
	
}
