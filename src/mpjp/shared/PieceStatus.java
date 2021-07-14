package mpjp.shared;

import java.io.Serializable;

import mpjp.shared.geom.Point;

public class PieceStatus implements HasPoint, Serializable {
	private static final long serialVersionUID = 1L;
	int block;
	int id;
	Point position;
	double rotation;

	public PieceStatus() {
		super();
	}

	public PieceStatus(int id, Point position) {
		super();
		this.id = id;
		this.position = position;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public int getId() {
		return id;
	}

	public double getX() {
		return position.getX();
	}

	public double getY() {
		return position.getY();
	}
}
