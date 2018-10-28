package Helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
	
	public static Map<Car, Pump> generateSchedule(List<Car> cars, List<Pump> pumps){
		
		
		//bkbkb
		List<Car> tempCars = new ArrayList<>(cars);
		Map<Car, Pump> tempMap = new HashMap<>();
		GeneticAlgorithm algorithm = new GeneticAlgorithm();
		
		// Create temporary list of cars
		List<Car> largeCars = getCarsByType(tempCars, CarType.Large);
		List<Car> mediumCars = getCarsByType(tempCars, CarType.Medium);
		List<Car> smallCars = getCarsByType(tempCars, CarType.Small);
		
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
		tempMap.clear();
		tempMap.putAll(largeMap);
		tempMap.putAll(mediumMap);
		tempMap.putAll(smallMap);
		
		// Update cars
		cars.clear();
		cars.addAll(tempCars);
		
		return tempMap;
	}
	
	// This needs to tell cars when they are done. Either finished, or canceled.
	public static List<ACLMessage> getMessages(Map<Car, Pump> map, List<Car> cars) {
		List<ACLMessage> result = new ArrayList<>();
		List<Car> successfulCars = cars.stream()
				.filter(car -> car.getCurrentCapacity() >= car.getMaxChargeCapacity())
				.collect(Collectors.toList());
		String output = "~~Agent helper message generator~~";
		
		// Remove cars that have completed requirements
		for(Car successfulCar : successfulCars) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID(successfulCar.getId(), AID.ISLOCALNAME));
			msg.setContent("Requirements have been met for: " + successfulCar.getId());
			result.add(msg);
			output += System.lineSeparator() + "	Creating success message for " + successfulCar.getId() + ". Requirement was "+ successfulCar.getMinChargeCapacity() + " and final was " + successfulCar.getCurrentCapacity() + ".";
			map.remove(successfulCar);
			cars.remove(successfulCar);
		}
		
		// Remove cars that we can't accommodate
		for (Car car: cars) {
			if (map.containsKey(car) == false) {
				if (false) {//todo calculate if its possible to complete
					//ACLMessage msg = new ACLMessage(ACLMessage.);
					output += System.lineSeparator() + "	Creating failed message for " + car.getId() + ". Requirement was "+ car.getMinChargeCapacity() + " and final was " + car.getCurrentCapacity() + ".";
					System.out.println("Removing Car " + car.getId() +" because we can't meet deadline.");
				}
			}
		}
		
		if (output != "~~Agent helper message generator~~") {
			System.out.println(output);			
		}
		
		return result;
	}
	
	private static List<Car> getCarsByType(List<Car> cars, CarType cartype){
		return new ArrayList<> (cars.stream()
				.filter(car -> car.getCurrentCapacity() <= car.getMinChargeCapacity()) //remove cars that are finished
				.filter(car -> car.getEarliestStartDate().before(new Date())) //remove when its before a cars start date
				.filter(car -> car.getType() == cartype).collect(Collectors.toList())); //only add relevant car types
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
			
			if (projectedCompletion.after(car.getLatestFinishDate())) {
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
}
