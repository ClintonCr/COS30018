package GA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Models.Car;
import Models.Pump;

public class Population {
	private Individual[] _individuals;
	final private int PopulationSize = 50;
	final private double MutationRate = 0.01; // x/100 e.g. 1/100
	
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
	
	public void evolve(Population population) {
		//first take top bloke and put him in position. Elitism
		Individual[] parentIndividuals = new Individual[PopulationSize];
		Individual[] childrenIndividuals = new Individual[PopulationSize];
		
		parentIndividuals[0] = getFittest();
		
		for (int i = 1; i < parentIndividuals.length; i++) {
			// Do tournaments to fill new population with random fittest individuals
			int randomIndOne = (int) (Math.random() * parentIndividuals.length);
			int randomIndTwo = (int) (Math.random() * parentIndividuals.length);
			
			parentIndividuals[i] = tournament(randomIndOne, randomIndTwo);
		}
		
		for (int i = 0; i < parentIndividuals.length; i++) {
			int randomIndOne = (int) (Math.random() * parentIndividuals.length);
			int randomIndTwo = (int) (Math.random() * parentIndividuals.length);

			childrenIndividuals[i] = crossover(parentIndividuals[randomIndOne], parentIndividuals[randomIndTwo]);
			
			if (Math.random() <= MutationRate) {
				mutate(childrenIndividuals[i]);
			}
		}
		
		_individuals = childrenIndividuals;
	}
	
	private Individual mutate(Individual individual) {
		Map<Car, Pump> map = new HashMap<>();
		map = individual.getSchedule();
		Individual individualResult = new Individual();
		
		int randomIndOne = (int) (Math.random() * (map.size()));
		int randomIndTwo = (int) (Math.random() * (map.size()));
			
		Car carOneKey = (Car) map.keySet().toArray()[randomIndOne];
		Car carTwoKey = (Car) map.keySet().toArray()[randomIndTwo];
		
		Pump pump = map.get(carOneKey);
		map.put(carOneKey, map.get(carTwoKey));
		map.put(carTwoKey, pump);
		
		individualResult.generateIndividual(map);
		
		return individualResult;		
	}
	
	private Individual crossover(Individual indiOne, Individual indiTwo) {
		Individual newIndividual = new Individual();
		
		List<Car> cars = new ArrayList<>();
		
		cars.addAll(indiOne.getSchedule().keySet().stream().collect(Collectors.toList()));
		cars.addAll(indiTwo.getSchedule().keySet().stream().collect(Collectors.toList()));
		List<Car> distinctCars = cars.stream().distinct().collect(Collectors.toList());
		
		List<Pump> pumps = new ArrayList<>();
		
		pumps.addAll(indiOne.getSchedule().values().stream().collect(Collectors.toList()));
		pumps.addAll(indiOne.getSchedule().values().stream().collect(Collectors.toList()));
		List<Pump> distinctPumps = pumps.stream().distinct().collect(Collectors.toList());
		
		Map<Car, Pump> map = new HashMap<>();
		
		for (Pump pump : distinctPumps) {
			if (distinctCars.size() <= 0) {
				break;
			}
			
			Car car = distinctCars.get(distinctCars.size()-1);
			map.put(car, pump);
			distinctCars.remove(car);
		}
		
		newIndividual.generateIndividual(map);
		return newIndividual;
	}
	
	private Individual tournament(int one, int two) {
		int fitnessOne = _individuals[one].getFitness();
		int fitnessTwo = _individuals[two].getFitness();
		
		if (!(fitnessOne > fitnessTwo)) {
			return _individuals[two];
		}
		
		return _individuals[one];
	}
	
	public Individual getFittest() {
		Individual fittest = _individuals[0];
		for (int i = 1; i < _individuals.length; i++) {
			
			if (fittest.getFitness() < _individuals[i].getFitness()) {
				fittest = _individuals[i];
			}
		}
		
		return fittest;
	}
}
