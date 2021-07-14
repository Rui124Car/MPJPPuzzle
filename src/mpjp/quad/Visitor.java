package mpjp.quad;

import mpjp.shared.HasPoint;

public interface Visitor<T extends HasPoint> {

	void visit(LeafTrie<T> visitor);

	void visit(NodeTrie<T> visitor);

}
