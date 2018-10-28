package GA;

import java.util.List;

import Models.Car;
import Models.Pump;

public class Population {
	final private Individual[] _individuals;
	final private int PopulationSize = 50;
	
	public Population(List<Car> cars, List<Pump> pumps, boolean isInitilise) {
		_individuals = new Individual[PopulationSize];
		if (isInitilise) {
			// Loop and create individuals
            for (int i = 0; i < _individuals.length; i++) {
                Individual newIndividual = new Individual();
                newIndividual.generateIndividual(cars, pumps);
                _individuals[i] = newIndividual;
            }
		}
	}
	
	public Individual testShit() {
		return _individuals[1];
	}
}
