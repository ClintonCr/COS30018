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
		tempMap.put(CarType.Small, new CarSpecification(1.0, 1.0));
		tempMap.put(CarType.Medium, new CarSpecification(2.0, 2.0));
		tempMap.put(CarType.Large, new CarSpecification(3.0, 3.0));
		
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
