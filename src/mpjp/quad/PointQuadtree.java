package mpjp.quad;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import mpjp.quad.Trie.Quadrant;
import mpjp.shared.HasPoint;

public class PointQuadtree<T extends HasPoint> implements Iterable<T> {
	
	Trie<T> top;

	public class PointIterator implements Iterator<T>, Runnable, Visitor<T> {
		T nextPoint;
		boolean terminated;
		Thread thread;

		PointIterator() {
			thread = new Thread(this, "Point Iterator");
			thread.start();
		}

		public boolean hasNext() {
			synchronized (this) {
				if(!terminated)
					handshake();
			}

			return nextPoint != null;
		}

		public T next() {
			T value = nextPoint;
			
			synchronized (this) {
				nextPoint = null;
			}
			return value;
		}

		public void run() {
			terminated = false;
			top.accept(this);
			
			synchronized(this) {
				terminated = true;
				handshake();
			}
		}

		public void visit(LeafTrie<T> leaf) {
			if(leaf != null) {
				synchronized(this) {
					for(T point : leaf.getPoints()) {
						if(nextPoint != null)
							handshake();
						nextPoint = point;
						handshake();
					}
				}
			}
		}

		public void visit(NodeTrie<T> node) {
			if(node != null) {
				for(Quadrant q : Quadrant.values()) {
					synchronized(this) {
						node.tries.get(q).accept(this);
					}
				}
			}
		}
		
		private void handshake() {
			notify();
			try {
				wait();
			}catch (InterruptedException cause) {
				throw new RuntimeException("Unexpected Interruption while waiting");
			}
		}
	}

	public PointQuadtree(double width, double height) {
		this.top = new NodeTrie<T>(0, height, width, 0);
	}

	public PointQuadtree(double width, double height, double margin) {
		this.top = new NodeTrie<T>(-margin, height+margin, width+margin, -margin);
	}

	public PointQuadtree(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		this.top = new NodeTrie<T>(topLeftX, topLeftY, bottomRightX, bottomRightY);
	}
	
	public void delete(T point) {
		top.delete(point);
	}

	public T find(T point) {
		return top.find(point);
	}

	public Set<T> findNear(double x, double y, double radius) {
		Set<T> set = new HashSet<T>();
		top.collectNear(x, y, radius, set);
		return set;
	}

	public Set<T> getAll() {
		Set<T> set = new HashSet<T>();
		top.collectAll(set);
		return set;
	}

	public void insert(T point) {
		double x = point.getX();
		double y = point.getY();
		
		if((x >= top.topLeftX && y <= top.topLeftY) && (x <= top.bottomRightX && y >= top.bottomRightY))
			top = top.insert(point);
		else 
			throw new PointOutOfBoundException();
	}

	public void insertReplace(T point) {
		top = top.insertReplace(point);
	}

	public Iterator<T> iterator() {
		PointIterator it = new PointIterator();

		return it;
	}

}
