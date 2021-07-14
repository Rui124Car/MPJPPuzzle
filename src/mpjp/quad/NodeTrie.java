package mpjp.quad;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mpjp.shared.HasPoint;

public class NodeTrie<T extends HasPoint> extends Trie<T> implements Element<T> {

	Map<Trie.Quadrant, Trie<T>> tries;
	boolean visited;

	NodeTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super(topLeftX, topLeftY, bottomRightX, bottomRightY);
		
		Trie<T> SW = new LeafTrie<T>(topLeftX, (topLeftY+bottomRightY)/2, 
				(bottomRightX+topLeftX)/2, bottomRightY);
		
		Trie<T> NW = new LeafTrie<T>(topLeftX, topLeftY, 
				(bottomRightX+topLeftX)/2, (bottomRightY+topLeftY)/2);
		
		Trie<T> SE = new LeafTrie<T>((bottomRightX+topLeftX)/2, (topLeftY+bottomRightY)/2, 
				bottomRightX, bottomRightY);
		
		Trie<T> NE = new LeafTrie<T>((topLeftX+bottomRightX)/2, topLeftY, 
				bottomRightX, (bottomRightY+topLeftY)/2);
		
		this.tries = initializateQuadrants(SW, NW, SE, NE);
		visited = false;
	}
	
	Map<Trie.Quadrant, Trie<T>> initializateQuadrants(Trie<T> SW, Trie<T> NW, 
			Trie<T> SE, Trie<T> NE) {
		
		Map<Trie.Quadrant, Trie<T>> newNode = new HashMap<Trie.Quadrant, Trie<T>>();
			
		newNode.put(Quadrant.SE, SE);
		newNode.put(Quadrant.NE, NE);
		newNode.put(Quadrant.SW, SW);
		newNode.put(Quadrant.NW, NW);
		
		return newNode;
	}

	public void accept(Visitor<T> visitor) {
		visitor.visit(this);
	}

	void collectAll(Set<T> set) {
		for(Quadrant q : Quadrant.values()) {
				tries.get(q).collectAll(set);
		}
	}

	void collectNear(double x, double y, double radius, Set<T> nodes) {
		for(Quadrant q : Quadrant.values()) {
			if(tries.get(q).overlaps(x, y, radius)) 
				this.tries.get(q).collectNear(x, y, radius, nodes);
		}
	}

	void delete(T point) {
		this.tries.get(quadrantOf(point)).delete(point);
	}

	T find(T point) {
		T p = this.tries.get(quadrantOf(point)).find(point);
		return p;
	}

	Collection<Trie<T>> getTries() {
		Collection<Trie<T>> retVal = new ArrayList<Trie<T>>();
		
		for(Quadrant q : Quadrant.values()) {
			retVal.add(tries.get(q));
		}
		
		return retVal;
	}

	Trie<T> insert(T point) {
		Quadrant quad = quadrantOf(point);
		Trie<T> trie = tries.get(quad).insert(point);
		
		tries.put(quad, trie);
		return this;
	}

	Trie<T> insertReplace(T point) {
		Quadrant quad = quadrantOf(point);
		Trie<T> trie = tries.get(quad).insertReplace(point);
		
		tries.put(quad, trie);
		return this;
	}
	
	Quadrant quadrantOf(T point) {
		if (point.getX() < ((topLeftX+bottomRightX)/2)) {
			if (point.getY() < ((topLeftY+bottomRightY)/2))
				return Quadrant.SW;
			else
				return Quadrant.NW;
		} 
		else {
			if (point.getY() < ((topLeftY+bottomRightY)/2))
				return Quadrant.SE;
			else
				return Quadrant.NE;
		}
	}

	@Override
	public String toString() {
		return "NodeTrie [tries=" + tries + "]";
	}

}