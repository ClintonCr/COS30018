package Helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Enums.CarType;
import Enums.PumpType;
import Models.Car;
import Models.Pump;

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
	
	//(String uniqueAgentName, CarType carType, double minChargeCapacity, double maxChargeCapacity, Date earliestStartDate, Date latestFinishDate)
	public static Car toCar(String csv) {
		return new Car(csv, CarType.Large, 100, 200, new Date(), new Date());
	}
}
