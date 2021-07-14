package mpjp.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleSelectInfo;

public class WorkspacePool {
	static String SERIALIZATION_SUFIX = ".ser";
	static File poolDirectory;
	Map<String, Workspace> workspaces;

	WorkspacePool() {
		workspaces = new HashMap<>();
	}

	void backup(String workspaceId, Workspace workspace){
		try(
			FileOutputStream stream = new FileOutputStream(this.getFile(workspaceId));
			ObjectOutputStream serializer = new ObjectOutputStream(stream);
				) {
				serializer.writeObject(workspace);
		} catch(IOException cause) {
			cause.printStackTrace();
		}
	}

	String createWorkspace(PuzzleInfo info) throws MPJPException {

	}

	Map<String, PuzzleSelectInfo> getAvailableWorkspaces() {

	}

	File getFile(String workspaceId){
		//serializar para não criar repetidos
		File file = new File(workspaceId);
		return file;
	}

	public static File getPoolDirectory() {
		return poolDirectory;
	}

	Workspace getWorkspace(String id) {
		return workspaces.get(id);
	}

	Workspace recover(String workspaceId) {
		FileInputStream stream = new FileInputStream(this.getFile(workspaceId));
		ObjectInputStream deserializer = new ObjectInputStream(stream);
		Workspace workspace = null;
		try {
			workspace = (Workspace) deserializer.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workspace;
	}

	void reset() {
	}
	
	Set<String> getAvailableImages() {
		Set<String> set = new HashSet<>();
		
		return set;
	}

	public static void setPoolDiretory(File poolDirectory) {
		WorkspacePool.poolDirectory = poolDirectory;
	}

	public static void setPoolDiretory(String pathname) {
		WorkspacePool.poolDirectory = new File(pathname);
	}
}
