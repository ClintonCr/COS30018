package Helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import Enums.CarType;
import Enums.PumpType;
import GA.GeneticAlgorithm;
import Models.Car;
import Models.Pump;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class AgentHelper {
	public static List<Pump> generatePumps(int small, int medium, int large) {
		List<Pump> result = new ArrayList<>();
		
		if (small >= 0)
			result.addAll(batchGenerate(PumpType.Small, small));
		if (medium >= 0)
			result.addAll(batchGenerate(PumpType.Medium, medium));
		if (large >= 0)
			result.addAll(batchGenerate(PumpType.Large, large));
		return result;
	}
	
	private static List<Pump> batchGenerate(PumpType pumpType, int batchSize){
		List<Pump> batchResult = new ArrayList<>();
		 for (int i = 0; i < batchSize; i++) {
			 String uniqueId = pumpType.toString() + "_" + i; // safe as we only add on start-up
			 batchResult.add(new Pump(uniqueId, pumpType));
		 }
		 
		 return batchResult;
	}
	
	public static List<ACLMessage> generateSchedule(List<Car> cars, List<Pump> pumps, Map<Car,Pump> map){
		GeneticAlgorithm algorithm = new GeneticAlgorithm();
		
		// Create temporary list of cars
		List<Car> largeCars = getCarsByType(cars, CarType.Large);
		List<Car> mediumCars = getCarsByType(cars, CarType.Medium);
		List<Car> smallCars = getCarsByType(cars, CarType.Small);
		
		// Create temporary list of pumps
		List<Pump> largePumps = getPumpsByType(pumps, PumpType.Large);
		List<Pump> mediumPumps = getPumpsByType(pumps, PumpType.Medium);
		List<Pump> smallPumps = getPumpsByType(pumps, PumpType.Small);
		
		// Trigger algorithm for large pump
		removeImpossibleCarsForAlgo(largeCars, PumpType.Large); // filter out cars that we can accommodate
		Map<Car, Pump> largeMap = algorithm.process(largeCars, largePumps, PumpType.Large);
		bulkUpdateCars(largeMap, largeCars);
		
		// Trigger algorithm for medium pump
		mediumCars.addAll(largeCars);
		removeImpossibleCarsForAlgo(mediumCars, PumpType.Medium);
		Map<Car, Pump> mediumMap = algorithm.process(mediumCars, mediumPumps, PumpType.Medium);
		bulkUpdateCars(mediumMap, mediumCars);
		
		// Trigger algorithm for small pump
		smallCars.addAll(mediumCars);
		removeImpossibleCarsForAlgo(smallCars, PumpType.Small);
		Map<Car, Pump> smallMap = algorithm.process(smallCars, smallPumps, PumpType.Small);
		bulkUpdateCars(smallMap, smallCars);
		
		// Update map
		map.clear();
		map.putAll(largeMap);
		map.putAll(mediumMap);
		map.putAll(smallMap);
		
		return getMessages(map, cars);
	}
	
	private static List<Car> getCarsByType(List<Car> cars, CarType cartype){
		return cars.stream()
				.filter(car -> car.getCurrentCapacity() <= CarTypeTranslator.getCarFromType(car.getType()).getCapacity()) //remove cars that are finished
				.filter(car -> car.getStartDate().before(new Date())) //remove when its before a cars start date
				.filter(car -> car.getType() == cartype).collect(Collectors.toList()); //only add relevant car types
	}
	
	private static List<Pump> getPumpsByType(List<Pump> pumps, PumpType pumpType){
		return pumps.stream().filter(pump -> pump.getPumpType() == pumpType).collect(Collectors.toList());
	}
	
	private static void removeImpossibleCarsForAlgo(List<Car> cars, PumpType pumpType) {
		// Remove cars that projected finish date is greater then requirement
		List<Car> carsToRemove = new ArrayList<>();
		PumpSpecification pumpSpec = PumpTypeTranslator.getPumpFromType(pumpType);
		double chargeRate = pumpSpec.getOutputChargeRate();
		
		for (Car car : cars) {
			double minimumChargecapacity = car.getMinChargeCapacity();
			double currentCapacity = car.getCurrentCapacity();
			double remainingCapacity = minimumChargecapacity - currentCapacity;
			double hoursToComplete = minimumChargecapacity / chargeRate;
			
			Calendar projectedCompletion = Calendar.getInstance();
			projectedCompletion.add(Calendar.MINUTE, (int) (hoursToComplete * 60));
			
			if (projectedCompletion.after(car.getEndDate())) {
				carsToRemove.add(car);
			}
		}
		cars.removeAll(carsToRemove);
	}
	
	private static void bulkUpdateCars(Map<Car, Pump> map, List<Car> cars) {
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry kvp = (Map.Entry)it.next();
			Car currentCar = (Car) kvp.getKey();
			currentCar.setIsConnected();
			currentCar.setPump((Pump)kvp.getValue());
			cars.remove(currentCar);
		}
	}
	
	// This needs to tell cars when they are done. Either finished, or canceled.
	private static List<ACLMessage> getMessages(Map<Car, Pump> map, List<Car> cars) {
		List<ACLMessage> result = new ArrayList<>();
		List<Car> successfulCars = cars.stream()
				.filter(car -> car.getCurrentCapacity() >= CarTypeTranslator.getCarFromType(car.getType()).getCapacity())
				.collect(Collectors.toList());
		
		// Remove cars that have completed requirements
		for(Car successfulCar : successfulCars) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID(successfulCar.getId(), AID.ISLOCALNAME));
			result.add(msg);
			cars.remove(successfulCar);
		}
		
		// Remove cars that we can't accommodate
		for (Car car: cars) {
			if (map.containsKey(car) == false) {
				if (true) {//todo calculate if its possible to complete
					//ACLMessage msg = new ACLMessage(ACLMessage.);
				}
			}
		}
		return result;
	}
}
