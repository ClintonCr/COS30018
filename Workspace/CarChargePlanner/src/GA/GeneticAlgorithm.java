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
		// Create population
		Population population = new Population(cars, pumps, true);
		
		Map<Car, Pump> fittest = population.getFittest().getSchedule();
		result.putAll(fittest);
		return result;
	}
}

//TO DO

//Calculate fitness of individuals
//
