package mpjp;

import static java.lang.Math.PI;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

/**
 * Class for converting method names in labels to be displayed 
 * when in test cases are run.
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
class DisplayNames implements DisplayNameGenerator {

	@Override
	public String generateDisplayNameForClass(Class<?> arg0) {
		return arg0.getName();
	}

	@Override
	public String generateDisplayNameForMethod(Class<?> arg0, Method method) {
		String name = method.getName();
		
		if(name.startsWith("test")) {
			name = name.replaceAll("_", " ");
			
			int pos = name.indexOf(" ");
			String methodName = Character.toLowerCase(name.charAt(4))+
					(pos == -1 || pos < 5 ? 
							name.substring(5): 
								name.substring(5, pos) );
			String rest = (pos == -1 || pos < 5 ? "" : name.substring(pos));
			
			name = "Test "+methodName+"() "+rest;
		}
			
		return name;
	}

	@Override
	public String generateDisplayNameForNestedClass(Class<?> arg0) {
		
		return arg0.getName();
	}
	
}


@DisplayNameGeneration(DisplayNames.class)
public class TestData {
	

	protected final static int ID = 0;

	protected final static double X = 111;
	protected final double Y = 222;

	protected final static double WIDTH = 300;
	protected final static double HEIGHT = 300;
	
	protected final static int ROWS = 8;
	protected final static int COLUMNS = 10;
	
	protected final static String IMAGE_NAME = "<image name>";
	protected final static String CUTTING_NAME = "<cutting name>";
	
	protected final static Date START = new Date();
	
	protected final static String[] TEST_IMAGES = { 
			"exterior2.jpg", 
			"exterior3.jpg",
			"exterior7.jpg"
	};
	
	protected final static int REPETIONS = 10;
	
	protected static Stream<Integer> intProvider() {
        return Stream.of(0,1,23,10,11,20,30,99,100);
	}
	
	protected static Stream<String> stringProvider() {
        return Stream.of("", "Hello world!");
	}

	protected static Stream<Double> radianProvider() {
		return Stream.of(0D,PI/4,PI/2,PI,3/2*PI,2*PI);
	}
	
	protected static Stream<Double> doubleProvider() {
		return Stream.of(0D,1e-8,1.1D,-1.3D,5.56,123.0D);
	}
	
	protected static Stream<Date> dateProvider() {
		Date now = new Date();
		
		return Stream.of(now, new Date(0), new Date(now.getTime()+1000*1000));
	}

	protected static final Random RANDOM = new Random();
	
	protected double getDelta(double radius) {
		return RANDOM.nextDouble()*radius;
	}
	
	
}
