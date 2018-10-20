package Agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Helpers.AgentHelper;
import Models.Car;
import Models.Pump;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class MsaAgent extends Agent {
	// Fields
	final private List<Car> _cars;
	final private List<Pump> _pumps;
	final private Map<Car,Pump> _currentCarPump;
	
	public MsaAgent(){
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
				// Re-calculate capacity
				for(Car car: _cars) {
					car.refresh();
				}
				
				// Update schedule and notify
				List<ACLMessage> result = AgentHelper.generateSchedule(_cars, _pumps, _currentCarPump);
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
}
