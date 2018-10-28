package GA;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Helpers.PumpTypeTranslator;
import Models.Car;
import Models.Pump;

public class Individual {
	private Map<Car, Pump> _map;
	private int _fitness;
	
	public void generateIndividual(List<Car> cars, List<Pump> pumps) {
		_map = new HashMap<>();
		List<Car> tempCars = new ArrayList<>(cars);
		
		Random r = new Random();
		
		for (Pump pump : pumps) {
			int high = tempCars.size();
			
			if (high <= 0) {
				break;
			}
			
			int randPos = r.nextInt(high);
			Car car = tempCars.remove(randPos);
			
			_map.put(car, pump);
			_fitness = calculateFitness(_map);
		}
	}
	
	private int calculateFitness(Map<Car,Pump> map) {
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
	
	/*
	 * Deadline Time Buffer is defined as the sum of:
	 * Deadline - Projected finished time in minutes for each car in schedule.  
	 */
	private int totalDeadlineTimeBuffer(Car car, Pump pump) {
		double remainingCharge = 0;
		double hoursTillMin = 0;
		double chargeRate = PumpTypeTranslator.getPumpFromType(pump.getPumpType()).getOutputChargeRate();
		Date estimatedCompletionTime = new Date();
		
		//need to account for when above min
		remainingCharge = car.getMaxChargeCapacity() - car.getCurrentCapacity();
		Date minRequiredTime = new Date();
		
		hoursTillMin = remainingCharge/chargeRate;
		
		estimatedCompletionTime = addHoursToJavaUtilDate(minRequiredTime, (Math.round(hoursTillMin * 2)/2.0));
		return (int)differenceBetweenDatesInMinutes(car.getLatestFinishDate(),estimatedCompletionTime);
	}
	
	private Date addHoursToJavaUtilDate(Date date, double hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int minutes;
		minutes = (int) (hours * 60);
		calendar.add(Calendar.MINUTE,minutes);
		return calendar.getTime();
	}
	
	private long differenceBetweenDatesInMinutes(Date upper, Date lower) {
		return ChronoUnit.MINUTES.between(upper.toInstant(), lower.toInstant()); 
	}
	
	public Map<Car, Pump> getSchedule(){return _map;}	
	public int getFitness() {return _fitness;}
}
