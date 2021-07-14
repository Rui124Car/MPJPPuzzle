package mpjp.shared.geom;

import java.io.Serializable;

public class QuadTo implements Serializable, Segment {
	private static final long serialVersionUID = 1L;
	private Point controlPoint;
	private Point endPoint;

	public QuadTo() {
	}

	public QuadTo(Point controlPoint, Point endPoint) {
		this.controlPoint = controlPoint;
		this.endPoint = endPoint;
	}

	public Point getControlPoint() {
		return this.controlPoint;
	}

	public Point getEndPoint() {
		return this.endPoint;
	}

	public void setControlPoint(Point controlPoint) {
		this.controlPoint = controlPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}
}