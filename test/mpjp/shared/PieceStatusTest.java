package mpjp.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import mpjp.TestData;
import mpjp.shared.geom.Point;

/**
 *  Tests PieceStatus
 *  
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
class PieceStatusTest extends TestData {
	Point point;
	PieceStatus piece;

	@BeforeEach
	void setUp() throws Exception {
		point = new Point(X,Y);
		piece = new PieceStatus(ID,point);
	}

	/**
	 * Check an instance is created without arguments
	 */
	@Test
    @DisplayName("Test constructor PieceStatus()")
	void testPieceStatus() {
		assertNotNull(new PieceStatus());
	}

	/**
	 * Check an instance is created from the right arguments
	 */
	@Test
    @DisplayName("Test constructor PieceStatus(int,Point)")
	void testPieceStatusIntPoint() {
		assertNotNull(piece);
	}

	/**
	 * Check expected iD
	 */
	@Test
	void testGetId() {
		assertEquals(ID,piece.getId());
	}

	/**
	 * Check horizontal coordinate
	 */
	@Test
	void testGetX() {
		assertEquals(X,piece.getX());
	}

	/**
	 * Check vertical coordinate
	 */
	@Test
	void testGetY() {
		assertEquals(Y,piece.getY());
	}

	/**
	 * Check position  set on instantiation
	 */
	@Test
	void testGetPosition() {
		assertEquals(point,piece.getPosition());
	}

	/**
	 * Check setting a different position
	 * @param x coordinate
	 * @param y coordinate
	 */
	@ParameterizedTest
	@CsvSource({ "10,20", "30,12", "100,12" })	
	void testSetPosition(int x,int y) {
		Point point = new Point(x,y);
		
		piece.setPosition(point);
		assertEquals(point,piece.getPosition());
	}

	/**
	 * Check block set on instantiation
	 */
	@Test
	void testGetBlock() {
		assertEquals(ID,piece.getBlock(),"Initial block should be the ID");
	}

	/**
	 * Check setting block
	 * 
	 * @param block  for testing provided by method
	 */
	@ParameterizedTest
	@MethodSource("intProvider")
	void testSetBlock(int block) {
		piece.setBlock(block);
		
		assertEquals(block,piece.getBlock());
	}

	/**
	 * Check default rotation (rotation won't be used in this prototype)
	 */
	@Test
	void testGetRotation() {
		assertEquals(0,piece.getRotation(),"Default rotaion should be 0");
	}

	/**
	 * Check setting rotation (rotation won't be used in this prototype)
	 * 
	 * @param rotation for testing provided by method
	 */
	@ParameterizedTest
	@MethodSource("radianProvider")
	void testSetRotation(double rotation) {
		piece.setRotation(rotation);
		
		assertEquals(rotation,piece.getRotation());
		
	}

}
