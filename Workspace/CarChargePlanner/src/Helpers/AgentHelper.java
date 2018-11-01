package Helpers;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import CSP.CSP;
import Enums.CarType;
import Enums.PumpType;
import GA.GeneticAlgorithm;
import Models.Car;
import Models.Pump;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class AgentHelper {
	// ******************************
	// Public methods
	// ******************************
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
	
	public static Map<Car, Pump> generateSchedule(List<Car> cars, List<Pump> pumps){
		
		List<Car> tempCars = new ArrayList<>(cars);
		Map<Car, Pump> tempMap = new HashMap<>();
		GeneticAlgorithm algorithm = new GeneticAlgorithm();
		CSP csp = new CSP();
		
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
		Map<Car, Pump> largeMapCSP = csp.process(largeCars, largePumps);
		Map<Car, Pump> largeMap = algorithm.process(largeCars, largePumps);
		bulkUpdateCars(largeMap, largeCars);
		
		// Trigger algorithm for medium pump
		mediumCars.addAll(largeCars);
		removeImpossibleCarsForAlgo(mediumCars, PumpType.Medium);
		Map<Car, Pump> mediumMapCSP = csp.process(mediumCars, mediumPumps);
		Map<Car, Pump> mediumMap = algorithm.process(mediumCars, mediumPumps);
		bulkUpdateCars(mediumMap, mediumCars);
		
		// Trigger algorithm for small pump
		smallCars.addAll(mediumCars);
		removeImpossibleCarsForAlgo(smallCars, PumpType.Small);
		Map<Car, Pump> smallMapCSP = csp.process(smallCars, smallPumps);
		Map<Car, Pump> smallMap = algorithm.process(smallCars, smallPumps);
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
		// Stores list of completed cars
		List<Car> successfulCars = cars.stream()
				.filter(car -> car.getCurrentCapacity() >= car.getMinChargeCapacity())
				.collect(Collectors.toList());
		String output = "~~Agent helper message generator~~";
		
		// Remove completed cars from car list and schedule
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
		List<Car> carsToRemove = new ArrayList<>();
		for (Car car: cars) {
			if (map.containsKey(car) == false) {
				if (cantMeetRequirement(car, null)) {//todo calculate if its possible to complete
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(new AID(car.getId(), AID.ISLOCALNAME));
					msg.setContent("Requirements cannot be met for: " + car.getId());
					result.add(msg);
					output += System.lineSeparator() + "	Creating failed message for " + car.getId() + ". Requirement was "+ car.getMinChargeCapacity() + " and final was " + car.getCurrentCapacity() + ".";
					map.remove(car);
					carsToRemove.add(car);
				}
			}
		}
		
		cars.removeAll(carsToRemove);		
		if (output != "~~Agent helper message generator~~") {
			System.out.println(output);			
		}
		
		return result;
	}
	
	// ******************************
	// Private methods
	// ******************************
	/// This method is used to generate n amount of pumps for a given pump type
	private static List<Pump> batchGenerate(PumpType pumpType, int batchSize){
		List<Pump> batchResult = new ArrayList<>();
		 for (int i = 0; i < batchSize; i++) {
			 String uniqueId = pumpType.toString() + "_" + i; // safe as we only add on start-up
			 batchResult.add(new Pump(uniqueId, pumpType));
		 }
		 
		 return batchResult;
	}
	
	/// Determines whether its possible for a car to meet its requirements with a given pump type
	private static boolean cantMeetRequirement(Car car, PumpType pumpType) {
		PumpType idealPump = pumpType == null ? PumpType.valueOf(car.getType().toString()) : pumpType;
		PumpSpecification pumpSpec = PumpTypeTranslator.getPumpFromType(idealPump);
		double minimumChargecapacity = car.getMinChargeCapacity();
		double currentCapacity = car.getCurrentCapacity();
		double remainingCapacity = minimumChargecapacity - currentCapacity;
		double hoursToComplete = remainingCapacity / pumpSpec.getOutputChargeRate();
		
		Calendar projectedCompletion = Calendar.getInstance();
		projectedCompletion.add(Calendar.MINUTE, (int) (hoursToComplete * 60));
		
		return (projectedCompletion.after(car.getLatestFinishDate()));
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
	
	/// Removes cars from a given list that are not 
	private static void removeImpossibleCarsForAlgo(List<Car> cars, PumpType pumpType) {
		// Remove cars that projected finish date is greater then requirement
		List<Car> carsToRemove = new ArrayList<>();
		
		for (Car car : cars) {
			double minimumChargecapacity = car.getMinChargeCapacity();
			double currentCapacity = car.getCurrentCapacity();
			double remainingCapacity = minimumChargecapacity - currentCapacity;
			
			if (remainingCapacity <= 0 || cantMeetRequirement(car, pumpType)) {
				carsToRemove.add(car);
			}
		}
		cars.removeAll(carsToRemove);
	}
	
	/// Updates the operating fields on each of the cars
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
	
	private static int calculateFitness(Map<Car,Pump> map) {
		int fitness = 0;
		
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()) {
			
			Map.Entry aPair = (Map.Entry)it.next(); 
			
			Car car = (Car)aPair.getKey();
			Pump pump = (Pump)aPair.getValue();
			
			fitness += totalDeadlineTimeBuffer(car, pump);
		}
		
		return fitness;
	}
	
	private static int totalDeadlineTimeBuffer(Car car, Pump pump) {
		double remainingCharge = 0;
		double hoursTillMin = 0;
		double chargeRate = PumpTypeTranslator.getPumpFromType(pump.getPumpType()).getOutputChargeRate();
		Date estimatedCompletionTime = new Date();
		
		//need to account for when above min
		remainingCharge = car.getMaxChargeCapacity() - car.getCurrentCapacity();
		Date minRequiredTime = new Date();
		
		hoursTillMin = remainingCharge/chargeRate;
		
		estimatedCompletionTime = addHoursToJavaUtilDate(minRequiredTime, (Math.round(hoursTillMin * 2)/2.0));
		return Math.abs((int)differenceBetweenDatesInMinutes(car.getLatestFinishDate(),estimatedCompletionTime));
	}
	
	private static Date addHoursToJavaUtilDate(Date date, double hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int minutes;
		minutes = (int) (hours * 60);
		calendar.add(Calendar.MINUTE,minutes);
		return calendar.getTime();
	}
	
	private static long differenceBetweenDatesInMinutes(Date upper, Date lower) {
		return ChronoUnit.MINUTES.between(upper.toInstant(), lower.toInstant()); 
	}
}
