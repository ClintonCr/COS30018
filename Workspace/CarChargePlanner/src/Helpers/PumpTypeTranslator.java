package Helpers;

import Enums.*;
import java.util.*;

public class PumpTypeTranslator {
	// ******************************
	// Fields
	// ******************************
	private final static Map<PumpType, PumpSpecification> _pumpTypeSpecificationMap;
	static {
		Map<PumpType, PumpSpecification> tempMap = new HashMap<>();
		tempMap.put(PumpType.Small, new PumpSpecification(10));
		tempMap.put(PumpType.Medium, new PumpSpecification(50));
		tempMap.put(PumpType.Large, new PumpSpecification(120));
		
		_pumpTypeSpecificationMap = Collections.unmodifiableMap(tempMap);
	}
	
	// ******************************
	// Private methods
	// ******************************
	/**
	 * Returns static pump details for a given pump type.
	 * 
	 * @Returns constant pump details.
	 * */
	public static PumpSpecification getPumpFromType(PumpType pumpType) {
		return _pumpTypeSpecificationMap.get(pumpType);
	}
}
