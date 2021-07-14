package mpjp.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import mpjp.TestData;

class PuzzleInfoTest extends TestData {
	PuzzleInfo puzzleInfo;
	
	@BeforeEach
	void setUp() {
		puzzleInfo = new PuzzleInfo(
				IMAGE_NAME,CUTTING_NAME, 
				ROWS, COLUMNS,
				WIDTH, HEIGHT 
				);
	}
	
	@Test
	void testPuzzleInfo() {
		assertNotNull(new PuzzleInfo());
	}

	@Test
	void testPuzzle_with_arguments() {

		assertNotNull(puzzleInfo);
	}

	@Test
	void testGetImageName_from_constructor() {
		assertEquals(IMAGE_NAME,puzzleInfo.getImageName());
	}
		
	@ParameterizedTest
	@MethodSource("stringProvider")
	void test_imageName_setter_and_getter(String string) {
		puzzleInfo.setImageName(string);
		
		assertEquals(string,puzzleInfo.getImageName());
	}
	
	@Test
	void testGetCuttingName_from_constructor() {
		assertEquals(CUTTING_NAME,puzzleInfo.getCuttingName());
	}

	@ParameterizedTest
	@MethodSource("stringProvider")
	void test_CuttingName_setter_and_getter(String string) {
		puzzleInfo.setCuttingName(string);
		
		assertEquals(string,puzzleInfo.getCuttingName());
	}

	@Test
	void testGetWidth_from_constructor() {
		assertEquals(WIDTH,puzzleInfo.getWidth());
	}

	@ParameterizedTest
	@MethodSource("doubleProvider")
	void test_Width_setter_and_getter(double value) {
		puzzleInfo.setWidth(value);
		
		assertEquals(value,puzzleInfo.getWidth());
	}

	@Test
	void testGetHeight_from_constructor() {
		assertEquals(HEIGHT,puzzleInfo.getHeight());
	}

	@ParameterizedTest
	@MethodSource("doubleProvider")
	void test_Height_setter_and_getter(double value) {
		puzzleInfo.setHeight(value);
		
		assertEquals(value,puzzleInfo.getHeight());
	}

	@Test
	void testGetRow_from_constructor() {
		assertEquals(ROWS,puzzleInfo.getRows());
	}

	@ParameterizedTest
	@MethodSource("intProvider")
	void test_Rows_setter_and_getter(int value) {
		puzzleInfo.setRows(value);
		
		assertEquals(value,puzzleInfo.getRows());
	}


	@Test
	void testGetColumns_from_constructor() {
		assertEquals(COLUMNS,puzzleInfo.getColumns());
	}

	@ParameterizedTest
	@MethodSource("intProvider")
	void test_Columns_setter_and_getter(int value) {
		puzzleInfo.setColumns(value);
		
		assertEquals(value,puzzleInfo.getColumns());
	}

}
