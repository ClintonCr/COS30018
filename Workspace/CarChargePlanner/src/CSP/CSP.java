package CSP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Models.Car;
import Models.Pump;

public class CSP {
	
	private List<Car> _cars;
	private List<Pump> _pumps;
	private Map<Pump, Car> _map;
	
	public CSP() {
		_cars = new ArrayList<>();
		_pumps = new ArrayList<>();
		_map = new HashMap<>();
	}
	
	public Map<Car, Pump> process(List<Car> cars, List<Pump> pumps){
		if (cars.size() == 0)
		{
			return new HashMap<>();
		}
		
		_cars.addAll(cars);
		_pumps.addAll(pumps);
		
		backtrack(_cars.get(0), _pumps.get(0));
		return invertMap(_map);
	}
	
	public void backtrack(Car currentCar, Pump currentPump){
		
		// Exit clause
		if (currentCar == null || currentPump == null) {
			return; 
		}
		
		Car nextCar = getNextCar(currentCar);
		Pump nextPump = getNextPump(currentPump);
		
		_map.put(currentPump, currentCar);
		
		// If valid then iterate pump
		if (isSafe()){
			backtrack(nextCar, nextPump);
		}
		else{ // If in-valid then iterate car
			_map.remove(currentPump);
			backtrack(nextCar, currentPump);
		}
	}
	
	private boolean isSafe(){
		List<Car> cars = new ArrayList<>(_map.values().stream().collect(Collectors.toList()));
		
		Car tempCar = cars.get(cars.size()-1);
		cars.remove(cars.size()-1); //TODO: Validate this
		
		return cars.contains(tempCar)==false;
	}

	private Car getNextCar(Car car){
		int currentPosition= _cars.indexOf(car);
		
		if (currentPosition >= _cars.size()-1) {
			return null;
		}
		
		return _cars.get(currentPosition+1);
	}

	private Pump getNextPump(Pump pump){
		int currentPosition = _pumps.indexOf(pump);
		
		if (currentPosition >= _pumps.size()-1) {
			return null;
		}
		
		return _pumps.get(currentPosition+1);
	}
	
	private Map<Car, Pump> invertMap(Map<Pump, Car> map){
		Map<Car,Pump> tempMap = new HashMap<>();
		
		List<Pump> pumps = map.keySet().stream().collect(Collectors.toList());
		List<Car> cars = map.values().stream().collect(Collectors.toList());
		
		for (Pump pump : pumps) {
			int position = pumps.indexOf(pump);
			
			tempMap.put(cars.get(position),pumps.get(position));
		}
		
		return tempMap;
	}
}
