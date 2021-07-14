package mpjp.game.cuttings;

import java.util.Set;

import mpjp.shared.MPJPException;

public interface CuttingFactory {
	Cutting createCutting(String name) throws MPJPException;

	Set<String> getAvaliableCuttings();
}
