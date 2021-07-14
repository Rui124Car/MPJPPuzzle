package mpjp.shared.geom;

import java.io.Serializable;

public class CurveTo implements Serializable, Segment {
	private static final long serialVersionUID = 1L;

	private Point controlPoint1;
	private Point controlPoint2;
	private Point endPoint;

	public CurveTo() {
		super();
	}

	public CurveTo(Point controlPoint1, Point controlPoint2, Point endPoint) {
		super();
		this.controlPoint1 = controlPoint1;
		this.controlPoint2 = controlPoint2;
		this.endPoint = endPoint;
	}

	public Point getControlPoint1() {
		return controlPoint1;
	}

	public void setControlPoint1(Point controlPoint1) {
		this.controlPoint1 = controlPoint1;
	}

	public Point getControlPoint2() {
		return controlPoint2;
	}

	public void setControlPoint2(Point controlPoint2) {
		this.controlPoint2 = controlPoint2;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	
}
