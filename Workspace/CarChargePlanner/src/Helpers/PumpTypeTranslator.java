package Helpers;

import Models.*;
import Enums.*;
import java.util.*;

public class PumpTypeTranslator {
	private final static Map<PumpType, PumpSpecification> _pumpTypeSpecificationMap;
	static {
		Map<PumpType, PumpSpecification> tempMap = new HashMap<>();
		tempMap.put(PumpType.Small, new PumpSpecification(1.0));
		tempMap.put(PumpType.Medium, new PumpSpecification(2.0));
		tempMap.put(PumpType.Large, new PumpSpecification(3.0));
		
		_pumpTypeSpecificationMap = Collections.unmodifiableMap(tempMap);
	}
	
	/**
	 * Returns static pump details for a given pump type.
	 * 
	 * @Returns constant pump details.
	 * */
	public static PumpSpecification getCarFromType(PumpType pumpType) {
		return _pumpTypeSpecificationMap.get(pumpType);
	}
}
