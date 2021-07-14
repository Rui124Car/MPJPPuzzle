package mpjp.shared;

import java.util.Date;
import java.util.Map;

import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;

public class PuzzleView {
	String image;
	Map<Integer, Point> locations;
	Map<Integer, PieceShape> shapes;
	Date start;
	double pieceHeight;
	double pieceWidth;
	double puzzleHeight;
	double puzzleWidth;
	double workspaceHeight;
	double workspaceWidth;

	public PuzzleView(Date start, double workspaceWidth, double workspaceHeight, double puzzleWidth,
			double puzzleHeight, double pieceWidth, double pieceHeight, String image, Map<Integer, PieceShape> shapes,
			Map<Integer, Point> locations) {
		super();
		this.image = image;
		this.locations = locations;
		this.shapes = shapes;
		this.start = start;
		this.pieceHeight = pieceHeight;
		this.pieceWidth = pieceWidth;
		this.puzzleHeight = puzzleHeight;
		this.puzzleWidth = puzzleWidth;
		this.workspaceHeight = workspaceHeight;
		this.workspaceWidth = workspaceWidth;
	}

	public Date getStart() {
		return start;
	}

	public String getImage() {
		return image;
	}

	public double getPieceHeight() {
		return pieceHeight;
	}

	public double getPieceWidth() {
		return pieceWidth;
	}

	public PieceShape getPieceShape(int id) {
		return shapes.get(id);
	}

	public double getPuzzleHeight() {
		return puzzleHeight;
	}

	public double getPuzzleWidth() {
		return puzzleWidth;
	}

	public Point getStandardPieceLocation(int id) {
		return locations.get(id);
	}

	public double getWorkspaceHeight() {
		return workspaceHeight;
	}

	public double getWorkspaceWidth() {
		return workspaceWidth;
	}

}
