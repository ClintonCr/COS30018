package Agents;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Enums.CarType;
import Enums.PumpType;
import Helpers.AgentHelper;
import Models.Car;
import Models.Pump;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class MsaAgent extends Agent implements MsaAgentInterface {
	// Fields
	final private List<Car> _cars;
	final private List<Pump> _pumps;
	final private Map<Car,Pump> _currentCarPump;
	
	public MsaAgent(){
		registerO2AInterface(MsaAgentInterface.class, this);
		_cars = new ArrayList<>();
		_pumps = new ArrayList<>();
		_currentCarPump = new HashMap<>();
	}
	
	protected void setup(){
		Object[] args =  getArguments();
		
		// Instantiate pumps
		_pumps.addAll(AgentHelper.generatePumps((int)args[0], (int)args[1], (int)args[2]));
		
		// Read in message
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				ACLMessage aMsg = myAgent.receive();
				
				if (aMsg != null) {
					try {
						_cars.add((Car)aMsg.getContentObject());
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
				} 
				else {
					block();
				}
			}
		} );
		addBehaviour(new TickerBehaviour(this, 30000) { // 30 seconds

			@Override
			protected void onTick() {
				// Re-calculate priorities
				for(Car car: _cars) {
					car.refresh();
				}
				
				// Update schedule and notify
				//_currentCarPump.clear();
				//List<ACLMessage> result = CustomAlgorithm.process(_currentCarPump, _cars, _pumps);
				//bulkInform(result);
			}
		});
	}
	
	public void end() {
		doDelete();
	}
	
	protected void takeDown() {
		System.out.println("MSA agent " + getName() + " is terminating.");
	}
	
	private void bulkInform(List<ACLMessage> messages) {
		try {
			for(ACLMessage message : messages) {
				send(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Car> getCars(){
		return _cars;
	}
	
	public Map<Car,Pump> getMap(){
		Map<Car,Pump> randomshit = new HashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
		Date earliestStartDate = new Date();
		Date deadline = new Date();
		
		try {
			earliestStartDate = format.parse("20/10/2018 09:00");
			deadline = format.parse("20/10/2018 09:30");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Car aCar = new Car("haha",CarType.Medium,10,20,earliestStartDate,deadline);
		Pump aPump = new Pump("haha", PumpType.Medium);
		
		randomshit.put(aCar, aPump);
		
		return randomshit;
	}
}
