package Helpers;

import Enums.*;
import java.util.*;

public class CarTypeTranslator {
	// ******************************
	// Fields
	// ******************************
	private final static Map<CarType, CarSpecification> _carTypeSpecificationMap;
	static {
		Map<CarType, CarSpecification> tempMap = new HashMap<>();
		tempMap.put(CarType.Small, new CarSpecification(10, 50));
		tempMap.put(CarType.Medium, new CarSpecification(50, 70));
		tempMap.put(CarType.Large, new CarSpecification(120, 100));
		
		_carTypeSpecificationMap = Collections.unmodifiableMap(tempMap);
	}
	
	// ******************************
	// Public methods
	// ******************************
	/**
	 * Returns static car details for a given car type.
	 * 
	 * @Returns constant car details.
	 * */
	public static CarSpecification getCarFromType(CarType carType) {
		return _carTypeSpecificationMap.get(carType);
	}
}
