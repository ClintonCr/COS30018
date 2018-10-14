package Agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Algorithms.CustomAlgorithm;
import Helpers.AgentHelper;
import Models.Car;
import Models.Pump;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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
		
		for(Pump pump:_pumps) {
			System.out.println(pump.getId());
		}
	}
}
