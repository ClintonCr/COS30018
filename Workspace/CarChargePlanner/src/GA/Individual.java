package GA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Models.Car;
import Models.Pump;

public class Individual {
	private Map<Car, Pump> _map;
	
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
		}
	}
	
	public Map<Car, Pump> ATestGetterSHit(){
		return _map;
	}
}
