package mpjp.game.cuttings;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mpjp.shared.MPJPException;

/**
 * Test Factory implementation
 * 
 *  @author Jos&eacute; paulo Leal {@code zp@dcc.fc.up.pt}
 * 
 */
class CuttingFactoryImplementationTest {
	CuttingFactoryImplementation factory;
	
	@BeforeEach
	void setUp() {
		factory = new CuttingFactoryImplementation();
	}
	
	/**
	 * Check that and instance is created with the expected type
	 */
	@Test
	void testCuttingFactoryImplementation() {
		assertNotNull(factory,"inatnce expected");
		
		assertTrue(CuttingFactory.class.isInstance(factory),
				"should implement CuttingFactory");
	}
	
	/**
	 * Check the is a list of available cuttings with at lest one element
	 */
	@Test
	void testGetAvaliableCuttings() {		
		Set<String> set = factory.getAvaliableCuttings();
				
		assertNotNull(set,"expected a set of strings");
		
		assertTrue(set.size() > 1);
	}
	
	/**
	 * Test instantiation of available cuttings 
	 * @throws MPJPException
	 */
	@Test
	void testCreateCutting() throws MPJPException {
		assertAll(
				() -> {
					for(String name: factory.getAvaliableCuttings()) {
						Cutting cutting = factory.createCutting(name);
			
						assertNotNull(cutting,"cutting expected");
					}
				},
				() -> {
					assertThrows(MPJPException.class,
							() -> factory.createCutting("#!@?*"),
							"Exception expected with this name");
				}
			);
	}	

}
