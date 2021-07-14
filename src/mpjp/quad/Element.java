package mpjp.quad;

import mpjp.shared.HasPoint;

public interface Element<T extends HasPoint> {
	void accept(Visitor<T> visitor);
}