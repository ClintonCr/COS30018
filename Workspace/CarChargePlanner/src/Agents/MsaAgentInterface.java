package Agents;

import java.util.List;
import java.util.Map;

import Models.Car;
import Models.Pump;

public interface MsaAgentInterface {
	//todo should be used as a means for the UI to get information that it wants....
	List<Car> getCars();
	Map<Car,Pump> getMap();
	void end();
}
