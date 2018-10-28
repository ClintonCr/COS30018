package CSP;

import java.util.List;

import Enums.CarType;
import Models.Car;
import Models.Pump;

public class PumpsCSP extends CSP {
	
    public static final String smallCar = CarType.Small.toString();
    public static final String mediumCar = CarType.Medium.toString();
    public static final String largeCar = CarType.Large.toString();
    
	public PumpsCSP(List<Pump> pumps, List<Car> cars) {
		//add N number of pump variables.
		for (Pump pump : pumps) {
			final Variable var = new Variable(pump.getId());
			addVariable(var);
		}
		
		// Define domain as a set of cars which can be attached to each pump size
		Domain carsDomain = new Domain(new Object[] {cars});
		
		for (Variable var : getVariables()) {
			setDomain(var, carsDomain);
		}
				
		//Define the Arbitrary constraints on each pump Variable i.e. car size must be within subset of pump accepted cars
		
		
	}

}
