package mpjp.quad;

import java.util.Set;
import java.lang.Math;

import mpjp.shared.HasPoint;

public abstract class Trie<T extends HasPoint> implements Element<T> {

	protected double bottomRightX;
	protected double bottomRightY;
	protected double topLeftX;
	protected double topLeftY;
	static int capacity;

	static enum Quadrant {
		SW, NW, SE, NE;
	}

	protected Trie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		this.bottomRightX = bottomRightX;
		this.bottomRightY = bottomRightY;
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;

	}

	abstract void collectAll(Set<T> points);

	abstract void collectNear(double x, double y, double radius, Set<T> points);

	abstract void delete(T point);

	abstract T find(T point);

	abstract Trie<T> insert(T point);

	abstract Trie<T> insertReplace(T point);

	static int getCapacity() {
		return Trie.capacity;
	}

	static void setCapacity(int capacity) {
		Trie.capacity = capacity;
	}

	static double getDistance(double x1, double y1, double x2, double y2) {
		double distX = Math.pow((x2-x1), 2);
		double distY = Math.pow((y2-y1), 2);
		
		double distance = Math.sqrt(distY+distX);
		return distance;
	}


	boolean overlaps(double x, double y, double radius) {
		
		if((x >= this.topLeftX && x <= this.bottomRightX) 
				&& (y <= this.topLeftY && y >= this.bottomRightY))
			return true;

		
		double x1 = Math.max(this.topLeftX, 
				Math.min(x, this.bottomRightX));
		
		double y1 = Math.max(this.bottomRightY, 
				Math.min(y, this.topLeftY));
		
		double distance = getDistance(x1, y1, x, y);
		
		return distance <= radius;
	}

	@Override
	public String toString() {
		return null;
	}

}
