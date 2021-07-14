package mpjp.game.cuttings;

import java.util.HashMap;
import java.util.Map;

import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.LineTo;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

public class TriangularCutting implements Cutting {

	public TriangularCutting() {
		super();
	}

	@Override
	public Map<Integer, PieceShape> getShapes(PuzzleStructure structure) {
		Map<Integer, PieceShape> map = new HashMap<Integer, PieceShape>();
		
		int numberOfPieces = structure.getPieceCount();
		double pieceWidth = structure.getWidth() / structure.getColumns();
		double pieceHeight = structure.getHeight() / structure.getRows();
	
		for(int i = 0; i<numberOfPieces; i++) {
			
			PieceShape pc = new PieceShape(new Point(0,0))
					.addSegment(new LineTo(new Point(pieceWidth/2,pieceHeight/2)))
					.addSegment(new LineTo(new Point(pieceWidth,0)))
					.addSegment(new LineTo(new Point(pieceWidth + pieceWidth/2,pieceHeight/2)))
					.addSegment(new LineTo(new Point(pieceWidth,pieceHeight)))
					.addSegment(new LineTo(new Point(pieceWidth/2,pieceHeight/2)))
					.addSegment(new LineTo(new Point(0,pieceHeight)))
					.addSegment(new LineTo(new Point(-pieceWidth/2,pieceHeight/2)))
					.addSegment(new LineTo(new Point(0,0)));

			map.put(i, pc);
		}
		return map;
	}

}
