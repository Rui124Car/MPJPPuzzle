package mpjp.shared.geom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PieceShape implements Serializable {
	private static final long serialVersionUID = 1L;
	List<Segment> segments;
	Point startPoint;

	public PieceShape() {
	}

	public PieceShape(Point startPoint) {
		this.segments = new ArrayList<Segment>();
		this.startPoint = startPoint;
	}

	public PieceShape(Point startPoint, List<Segment> segments) {
		this.startPoint = startPoint;
		this.segments = segments;
	}

	public PieceShape addSegment(Segment segment) {
		this.segments.add(segment);
		return this;
	}

	public Point getStartPoint() {
		return this.startPoint;
	}

	public List<Segment> getSegments() {
		return this.segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}
}
