package mpjp.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;

/**
 * Test the WorspacePool class. Special care with serialization files,
 * to clean them up after use.
 * 
 *  @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
class WorkspacePoolTest extends PuzzleData {
	static final String SERIALIAZTION_SUFFIX = ".ser";
	static final String MY_POOL_DIRECTORY = "."; //working directory must exist!  

	
	WorkspacePool pool;
	List<String> ids = new ArrayList<>();
	
	/**
	 * Set pool directory and clear IDs that may need to be cleared afterwards  
	 */
	@BeforeEach
	void setUp() {
		WorkspacePool.setPoolDiretory(MY_POOL_DIRECTORY); 	
		pool = new WorkspacePool();
		
		ids.clear();
	}
	
	
	/**
	 * Remove serialization files created during tests
	 */
	@AfterEach
	void cleanUp() {
		File dir = WorkspacePool.getPoolDirectory();
		
		ids.forEach((id) -> {
			File file = new File(dir,id+SERIALIAZTION_SUFFIX);
			
			if(file.exists())
				file.delete();
		});		
	}
	
	/**
	 * Just to be sure, remove files with serialization suffix
	 * both before and after all tests 
	 */
	@BeforeAll
	@AfterAll
	static void doubleCheck() {
		 	
		for(File file: new File(MY_POOL_DIRECTORY).listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				
				return pathname.getName().endsWith(SERIALIAZTION_SUFFIX);
			}
		})) file.delete();
	}

	/**
	 * Test static property poolDiretory an its setter and getter
	 */
	@Test
	void test_poolDirectory_setter_and_getter() {
		WorkspacePool.setPoolDiretory(new File("/usr/tmp"));
		assertEquals(new File("/usr/tmp"), WorkspacePool.getPoolDirectory());
		
		WorkspacePool.setPoolDiretory("/tmp");
		assertEquals(new File("/tmp"), WorkspacePool.getPoolDirectory());
	}
		
	/**
	 * Check that a string id is returned on creation and
	 * that a corresponding serialization file is created
	 * 
	 * @param puzzle with test data
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)		
	void testCreateWorkspace(Puzzle puzzle) throws MPJPException {
		String id = pool.createWorkspace(puzzle.getPuzzleInfo());
		
		ids.add(id);
		
		assertNotNull(id,"Id expected");
		
		String filename = id+SERIALIAZTION_SUFFIX;
		File file = new File(WorkspacePool.getPoolDirectory(),filename);
	
		assertTrue(file.exists(),"serialization file expected");
		
	}

	@ParameterizedTest
	@EnumSource(Puzzle.class)		
	void testGetWorkspace(Puzzle puzzle) throws MPJPException {
		PuzzleInfo info = puzzle.getPuzzleInfo();
		String id = pool.createWorkspace(info);
		
		ids.add(id);
		
		Workspace workspace = pool.getWorkspace(id);
		PuzzleView view = workspace.getPuzzleView();
		
		assertEquals(info.getWidth(),view.getPuzzleWidth());
		assertEquals(info.getHeight(),view.getPuzzleHeight());
	}

	/**
	 * Check if available workspace reflect those that where
	 * created so far, and map IDs to the correct type.   
	 * 
	 * @throws MPJPException if something unexpected happens
	 */
	@Test
	void testGetAvailableWorkspaces() throws MPJPException {
		Map<String, PuzzleSelectInfo> map = null;
		Set<String> keys = null;
		
		for(Puzzle puzzle: Puzzle.values()) {
			String id = pool.createWorkspace(puzzle.getPuzzleInfo());
			ids.add(id);
			
			map = pool.getAvailableWorkspaces();
			
			assertNotNull(map,"instance expected");
			
			keys = map.keySet();
		
			assertEquals(ids.size(),keys.size(),"Invalid size");
			assertEquals(new HashSet<>(ids),keys,"Unexpected keys");
		
			PuzzleSelectInfo info = map.get(id);
			
			assertNotNull(info,"PuzzleSelectInfo expected");
		}
		
	}

	/**
	 * Check generated file objects for each workspace 
	 * 
	 * @param name for file
	 */
	@ParameterizedTest
	@MethodSource("stringProvider")
	void testGetFile(String name) {

		File file  = pool.getFile(name);
		File other = pool.getFile(name);
		
		assertNotNull(file,"file expected");
		assertNotNull(other,"file expected");
		assertTrue(file == other,"Same file if same name");
		
		assertTrue(file.getName().endsWith(SERIALIAZTION_SUFFIX),
				"serialization suffix expected");
		assertEquals(WorkspacePool.getPoolDirectory(),file.getParentFile(),
				"poolDirectory expected as parent");
	}

	/**
	 * Check is backup file is actually created (doesn't exit before)
	 * 
	 * @param puzzle with test data
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)		
	void testBackup(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		String id = workspace.getId();
		File file = pool.getFile(id);
		
		assertFalse(file.exists(),"serialization file not yet expected");
		
		pool.backup(id, workspace);
		ids.add(id);
		
		assertTrue(file.exists(),"serialization file expected");
		
	}

	/**
	 * Check that recovered serialization files contain expected data
	 * 
	 * @param puzzle with test data
	 * @throws MPJPException if something unexpected happens
	 */
	@ParameterizedTest
	@EnumSource(Puzzle.class)		
	void testRecover(Puzzle puzzle) throws MPJPException {
		Workspace workspace = new Workspace(puzzle.getPuzzleInfo());
		PuzzleView view = workspace.getPuzzleView();
		String id = workspace.getId();

		pool.backup(id, workspace);
		ids.add(id);

		Workspace copy = pool.recover(id);
		PuzzleView viewCopy = copy.getPuzzleView();
		
		assertEquals(workspace.getId(),copy.getId());
		assertEquals(workspace.getPercentageSolved(),copy.getPercentageSolved());
		assertEquals(view.getPuzzleWidth(),viewCopy.getPuzzleWidth());
		assertEquals(view.getPuzzleHeight(),viewCopy.getPuzzleHeight());
	}

}
