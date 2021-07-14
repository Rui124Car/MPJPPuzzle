package mpjp.quad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import mpjp.shared.HasPoint;

public class LeafTrie<T extends HasPoint> extends Trie<T> implements Element<T> {
	
	private ArrayList<T> points; 
	boolean visited;

	LeafTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super(topLeftX, topLeftY, bottomRightX, bottomRightY);
		points = new ArrayList<T>();
		visited = false;
	}
	
	public void accept(Visitor<T> visitor) {
		visitor.visit(this);
	}

	void collectAll(Set<T> set) {
		for(T point : this.points) {
			set.add(point);
		}
	}
	
	void collectNear(double x, double y, double radius, Set<T> nodes) {
		for(T point : this.points) {
			if(isNear(x, y, radius, point)) {
				nodes.add(point);
			}
		}
	}
	
	private boolean isNear(double x, double y, double radius, T point) {
		
		double dist = super.getDistance(x, y, point.getX(), point.getY());
		if(dist <= radius)
			return true;

		return false;
	}

	void delete(T point) {
		for(int i = 0; i<this.points.size(); i++) {
			if(point.getX() == this.points.get(i).getX() 
				&& point.getY() == this.points.get(i).getY()) {
				this.points.remove(i);
				return;
			}
		}
	}

	T find(T point) {
		for(T pointInList : this.points) {
			if(point.getX() == pointInList.getX() && point.getY() == pointInList.getY())
				return pointInList;
		}	
		return null;
	}

	Collection<T> getPoints() {
		Collection<T> coll = new ArrayList<T>(super.getCapacity());
		
		for(T pointInList : this.points) {
			coll.add(pointInList);
		}
		return coll;
	}

	Trie<T> insert(T point) {
		if(this.points.size() + 1 > Trie.getCapacity()) {
			Trie<T> trie = new NodeTrie<T>
				(topLeftX, topLeftY, bottomRightX, bottomRightY);
			
			trie.insert(point);
			for(T p : this.points) 
				trie.insert(p);

			this.points.clear();
			return trie;
		}
		
		this.points.add(point);
		return this;
	}

	Trie<T> insertReplace(T point) {
		this.points.removeAll(points);
		this.points.add(point);
		return this;
	}

	@Override
	public String toString() {
		return "LeafTrie []";
	}

}