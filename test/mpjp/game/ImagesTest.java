package mpjp.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import mpjp.TestData;

class ImagesTest extends TestData {
	static File initialImageDirectory;
	
	@BeforeEach
	void setUp() {
		initialImageDirectory = Images.getImageDirectory();
	}
	
	@AfterEach
	void cleanUp() {
		Images.setImageDirectory(initialImageDirectory);
	}
	
	
	/**
	 * Check image directory property
	 * 
	 * @param name to test
	 */
	@ParameterizedTest
	@MethodSource("stringProvider")
	void test_imageDirectory_property(String name) {
		File dir = new File(name);
		
		Images.setImageDirectory(dir);
		assertEquals(dir,Images.getImageDirectory());
	}

	/**
	 * Check that {@code jpg} is a default extension
	 */
	@Test
	void testGetExtensions() {
		assertTrue(Images.getExtensions().contains("jpg"),
				"'jpg' should be an accpeted extension");
	}

	/**
	 * Use a strange extension, check that its is not accepted,
	 * add it and then check that it is accepted
	 */
	@Test
	void testAddExtension() {
		final String extension = ".xpto";
		
		assertFalse(Images.getExtensions().contains(extension),
				extension+" not expected as extension");
		
		Images.addExtension(extension);
		
		assertTrue(Images.getExtensions().contains(extension),
				extension+" now expected as extension");
	}

	/**
	 * Check if the images from test resources are available 
	 */
	@Test
	void testGetAvailableImages() {
		Set<String> images = Images.getAvailableImages();

		for(String image: TEST_IMAGES)
			assertTrue(images.contains(image),
					"expected image from test resources: "+image);
	}

}
