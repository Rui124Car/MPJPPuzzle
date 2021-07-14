package mpjp.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mpjp.game.cuttings.CuttingFactoryImplementation;
import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleSelectInfo;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

public class Workspace implements Serializable {
	private static final long serialVersionUID = 1L;
	static double widthFactor, heightFactor, radius;

	PuzzleInfo info;
	Date start;
	PuzzleLayout layout;
	PuzzleStructure structure;
	PuzzleView view;
	CuttingFactoryImplementation cuttings;
	
	// PointQuadtree<> qtree;

	Workspace(PuzzleInfo info) throws MPJPException {
		this.info = info;
		start = new Date();
		structure = new PuzzleStructure(info);
		initializePuzzleLayout();
		initializePuzzleView();
		// qtree = new PointQuadtree<>(info.getWidth(), info.getHeight());
		scatter();
	}
	
	void initializePuzzleLayout() throws MPJPException {
		Map<Integer,List<Integer>> blocks = new HashMap<>();
		Map<Integer, PieceStatus> pieces = new HashMap<>();
		int percentageSolved = 0;
		
		for(int i=0; i<structure.getPieceCount(); i++) {
			LinkedList<Integer> pieceList = new LinkedList<>();
			pieceList.add(i);
			blocks.put(i, pieceList);
			
			PieceStatus piece = new PieceStatus(i, structure.getPieceStandardCenter(i));
			piece.setBlock(i);
			pieces.put(i, piece);
		}
		
		layout = new PuzzleLayout(pieces, blocks, percentageSolved);
	}
	
	void initializePuzzleView() {
		double workspaceWidth = info.getWidth()*widthFactor;
		double workspaceHeight = info.getHeight()*heightFactor;
		
		double puzzleWidth = info.getWidth();
		double puzzleHeight = info.getHeight();
		
		double pieceWidth = structure.getPieceWidth();
		double pieceHeight = structure.getPieceHeight();
		String image = info.getImageName();
		
		Map<Integer, PieceShape> shapes = new HashMap<>();
		Map<Integer, Point> locations = new HashMap<>();
		
		
		view = new PuzzleView(start, workspaceWidth, workspaceHeight, 
				puzzleWidth, puzzleHeight, pieceWidth, pieceHeight, 
				image, shapes, locations);
	}
	
	private boolean checkConnect(PieceStatus p,Map<Integer, PieceStatus> pieces) {
		for(int i=0; i<structure.getPieceCount(); i++) {
			if((p.getY()-pieces.get(i).getY()) < getRadius() && ((Math.abs(p.getPosition().getX())-pieces.get(i).getX()) - structure.getPieceWidth()) < getRadius()) {
				p.setBlock(pieces.get(i).getBlock());
				return true;
			}
			if((p.getX() - pieces.get(i).getX())< getRadius() && ((Math.abs(p.getPosition().getY())-pieces.get(i).getY())- structure.getPieceHeight()) < getRadius()) {
				p.setBlock(pieces.get(i).getBlock());
				return true;
			}
		}
		return false;
	}

	PuzzleLayout connect(int id, Point point) throws MPJPException{
		if((id<0 || id >= structure.getPieceCount()) || 
				(point.getX() <= 0 || point.getY() <= 0
				|| point.getX() > info.getWidth() || 
				point.getY() > info.getHeight()))

			throw new MPJPException();
		
		Map<Integer, PieceStatus> pieces = layout.getPieces();
		
		PieceStatus ps = pieces.get(id);
		int blockId = ps.getBlock();
		ps.setPosition(point);
		pieces.replace(id, ps);
		
		
		
		if(checkConnect(ps, pieces)) {
			pieces.replace(id, ps);
			List<Integer> blocks = layout.getBlocks().get(ps.getBlock());
			blocks.add(ps.getId());
			layout.getBlocks().put(ps.getBlock(), blocks);
			layout.getBlocks().remove(blockId);
		}
		
		// fazer update da percentageSolved
		// formula:
		// 100 * (p - b) / (p - 1)
		// onde 
		//p is the number of pieces and
		//b is the number of blocks
		
		layout = new PuzzleLayout(pieces, layout.getBlocks(), 
				layout.getPercentageSolved());
		return layout;
	}

	PuzzleLayout getCurrentLayout() {
		return layout;
	}

	public static double getHeightFactor() {
		return heightFactor;
	}

	String getId() {
		return info.getImageName()+start+Math.random();
	}

	int getPercentageSolved() {
		return layout.getPercentageSolved();
	}

	PuzzleSelectInfo getPuzzleSelectInfo() {
		return new PuzzleSelectInfo(info, getPercentageSolved(), start);
	}

	PuzzleStructure getPuzzleStructure() {
		return this.structure;
	}

	PuzzleView getPuzzleView() {
		return this.view;
	}

	public static double getRadius() {
		return radius;
	}

	double getSelectRadius() {
		return Math.max(structure.getPieceWidth(), structure.getPieceHeight());
	}

	public static double getWidthFactor() {
		return widthFactor;
	}

	void restore() throws MPJPException {
		
		for(int i=0; i<structure.getPieceCount(); i++) {
			LinkedList<Integer> pieceList = new LinkedList<>();
			pieceList.add(i);
			layout.getBlocks().replace(i, pieceList);
			
			PieceStatus piece = new PieceStatus(i, structure.getPieceStandardCenter(i));
			piece.setBlock(i);
			layout.getPieces().replace(i, piece);
		}
		layout.getPercentageSolved();
		
	}

	void scatter() {
		for(int i=0; i<structure.getPieceCount(); i++) {
			PieceStatus piece = layout.getPieces().get(i);
			piece.setPosition(new Point(Math.random()*structure.getWidth(), Math.random()*structure.getHeight()));
			layout.getPieces().replace(i, piece);
		}
		layout.getPercentageSolved();
	}
	
	boolean isPointInPiece(PieceStatus piece, Point point) {
		if(point.getX() >= piece.getX()-structure.getPieceWidth()
				&& point.getX() <= piece.getX()+structure.getPieceWidth())
			if(point.getY() >= piece.getY()-structure.getHeight()
			&& point.getY() <= piece.getY()+structure.getHeight())
				return true;
		return false;
	}

	Integer selectPiece(Point point) {
		PuzzleLayout layout = getCurrentLayout();
		LinkedList<Integer> possiblePieces = new LinkedList<>();
		
		for(int i=0; i<structure.getPieceCount(); i++) {
			if(isPointInPiece(layout.getPieces().get(i), point)) {
				possiblePieces.add(i);
			}
		}
		
		
		Collections.sort(possiblePieces, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return layout.getPieces().get((int)o2).getBlock() - layout.getPieces().get((int)o1).getBlock();
			}
		});
		
		for(int i : possiblePieces)
			System.out.println(i);
		
		return possiblePieces.getFirst();
	}

	public static void setHeightFactor(double heightFactor) {
		Workspace.heightFactor = heightFactor;
	}

	public static void setRadius(double radius) {
		Workspace.radius = radius;
	}

	public static void setWidthFactor(double widthFactor) {
		Workspace.widthFactor = widthFactor;
	}
}
