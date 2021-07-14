package mpjp.game.cuttings;

import java.util.HashMap;
import java.util.Map;

import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.LineTo;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

public class StraightCutting implements Cutting {
	
	public StraightCutting() {
		super();
	}

	@Override
	public Map<Integer, PieceShape> getShapes(PuzzleStructure structure) {
		Map<Integer, PieceShape> map = new HashMap<Integer, PieceShape>();
	
		double pieceWidth = structure.getPieceWidth();
		double pieceHeight = structure.getPieceHeight();
		
		for(int i = 0; i<structure.getRows(); i++) {
			for(int j = 0; j<structure.getColumns(); j++) {
			PieceShape pc = new PieceShape(new Point(pieceWidth/2,pieceHeight/2))
					.addSegment(new LineTo(new Point(pieceWidth/2,-pieceHeight/2)))
					.addSegment(new LineTo(new Point(-pieceWidth/2,-pieceHeight/2)))
					.addSegment(new LineTo(new Point(-pieceWidth/2,pieceHeight/2)))
					.addSegment(new LineTo(new Point(pieceWidth/2,pieceHeight/2)));
				
			map.put((i * structure.getColumns()) + j, pc);
		}}
		return map;
	}
}
