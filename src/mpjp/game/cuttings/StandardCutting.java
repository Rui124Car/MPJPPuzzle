package mpjp.game.cuttings;

import java.util.Map;

import mpjp.game.Direction;
import mpjp.game.PuzzleStructure;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

public class StandardCutting implements Cutting {

	public StandardCutting() {
	}

	@Override
	public Map<Integer, PieceShape> getShapes(PuzzleStructure structure) {
		return null;
	}

	Point getEndControlPoint1(int id, Direction direction) {
		return null;
	}

	Point getEndControlPoint2(int id, Direction direction) {
		return null;
	}

	Point getMiddlePoint(int id, Direction direction) {
		return null;
	}

	Point getStartControlPoint1(int id, Direction direction) {
		return null;
	}

	Point getStartControlPoint2(int id, Direction direction) {
		return null;
	}
}
