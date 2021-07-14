package mpjp.game.cuttings;

import java.util.HashSet;
import java.util.Set;

import mpjp.shared.MPJPException;

public class CuttingFactoryImplementation implements CuttingFactory {

	private Set<String> cuttings;

	public CuttingFactoryImplementation() {
		super();
		cuttings = new HashSet<String>();
		cuttings.add("RoundCutting");
		cuttings.add("StandardCutting");
		cuttings.add("StraightCutting");
		cuttings.add("TriangulosCutting");
	}

	@Override
	public Cutting createCutting(String name) throws MPJPException {
		switch(name) {
		case "RoundCutting": return new RoundCutting();
		case "StandardCutting": return new StandardCutting();
		case "StraightCutting": return new StraightCutting();
		case "TriangulosCutting": return new TriangularCutting();
		default: throw new MPJPException("Invalid Cutting");
		}
	}

	@Override
	public Set<String> getAvaliableCuttings() {
		return this.cuttings;
	}

}
