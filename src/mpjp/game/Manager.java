package mpjp.game;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mpjp.game.cuttings.Cutting;
import mpjp.game.cuttings.CuttingFactoryImplementation;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

public class Manager {
	
	private WorkspacePool wp;
	private static Manager instance = null;
	
	private Manager() {
		wp = new WorkspacePool();
	}

	public PuzzleLayout connect(String workspaceId, int pieceId, Point point) throws MPJPException {
		return wp.getWorkspace(workspaceId).connect(pieceId, point);
	}

	public String createWorkspace(PuzzleInfo info) throws MPJPException {
		return wp.createWorkspace(info);
	}

	public Set<String> getAvailableCuttings() {
		CuttingFactoryImplementation cuttings = new CuttingFactoryImplementation();
		
		return cuttings.getAvaliableCuttings();
	}

	public Set<String> getAvailableImages() {
		return wp.getAvailableImages();
	}

	public Map<String, PuzzleSelectInfo> getAvailableWorkspaces() {
		return wp.getAvailableWorkspaces();
	}

	public PuzzleLayout getCurrentLayout(String workspaceId) {
		return wp.getWorkspace(workspaceId).getCurrentLayout();
	}

	public static Manager getInstance() {
		
		if(instance==null) {
			instance = new Manager();
		}
		return instance;
	}

	public PuzzleView getPuzzleView(String workspaceId) {
		return wp.getWorkspace(workspaceId).getPuzzleView();
	}

	void reset() {
		if(instance != null)
			instance = null;
	}

	public Integer selectPiece(String workspaceId, Point point) {
		return wp.getWorkspace(workspaceId).selectPiece(point);
	}

}
