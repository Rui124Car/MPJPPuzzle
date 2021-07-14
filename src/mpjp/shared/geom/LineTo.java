package mpjp.shared.geom;

import java.io.Serializable;

public class LineTo implements Serializable, Segment {
	private Point endPoint;
	private static final long serialVersionUID = 1L;

	public LineTo() {
	}

	public LineTo(Point endPoint) {
		this.endPoint = endPoint;
	}

	public Point getEndPoint() {
		return this.endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}
}