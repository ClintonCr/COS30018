package GA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Enums.PumpType;
import Models.Car;
import Models.Pump;

public class GeneticAlgorithm {
	public Map<Car, Pump> process(List<Car> cars, List<Pump> pumps, PumpType pumpType){
		Map<Car, Pump> result = new HashMap<Car, Pump>();
		int _generations = 0;
		
		if (cars.size() == 0)
		{
			return new HashMap<>();
		}
		// Create population
		Population population = new Population(cars, pumps, true);
		
		while ((population.getFittest().getFitness() < 2700)) {
			if(_generations > 10) {
				break;
			}
			population.evolve(population);
			_generations++;
		}
		
		Map<Car, Pump> fittest = population.getFittest().getSchedule();
		result.putAll(fittest);
		return result;
	}
	
}