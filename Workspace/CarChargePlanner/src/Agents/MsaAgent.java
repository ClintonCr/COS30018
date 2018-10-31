package Agents;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Enums.CarType;
import Enums.PumpType;
import Helpers.AgentHelper;
import Helpers.CarSpecification;
import Helpers.CarTypeTranslator;
import Helpers.PumpSpecification;
import Helpers.PumpTypeTranslator;
import Models.Car;
import Models.Pump;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class MsaAgent extends Agent implements MsaAgentInterface {
	// Fields
	final private List<Car> _cars;
	final private List<Pump> _pumps;
	private Map<Car,Pump> _map;
	private Map<Integer, String> _logHistory;
	private int _currentLogId;
	
	public MsaAgent(){
		registerO2AInterface(MsaAgentInterface.class, this);
		_cars = new ArrayList<>();
		_pumps = new ArrayList<>();
		_map = new HashMap<>();
		_logHistory = new HashMap<>();
		_currentLogId = 0;
	}
	
	protected void setup(){
		Object[] args =  getArguments();
		
		// Instantiate pumps
		_pumps.addAll(AgentHelper.generatePumps((int)args[0], (int)args[1], (int)args[2]));
		
		// Read in message
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				ACLMessage msg = myAgent.receive();
				
				if (msg != null) {
					try {
						Car newCar = (Car)msg.getContentObject();
						logMessage(msg, "Received",newCar);
						ACLMessage response = new ACLMessage();
						response.addReceiver(new AID(newCar.getId(),AID.ISLOCALNAME));
						
						if (canAccomidateCar(newCar)) {
							_cars.add(newCar);
							response.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
							response.setContent("Proposal Accepted.");
						}
						else {
							response.setPerformative(ACLMessage.REJECT_PROPOSAL);
							response.setContent("Proposal Rejected.");
						}
						logMessage(response, "Sent", null);
						send(response);
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
				if (_cars.isEmpty()) {
					return;
				}
				
				// Re-calculate capacity
				for(Car car: _cars) {
					car.refresh();
				}
				
				// Update schedule and notify
				_map = AgentHelper.generateSchedule(_cars, _pumps);
				List<ACLMessage> result = AgentHelper.getMessages(_map, _cars);
				bulkInform(result);
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
				logMessage(message, "Sent", null);
				send(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean canAccomidateCar(Car car) {
		Calendar projectedCompletion = Calendar.getInstance();
		CarSpecification carSpec = CarTypeTranslator.getCarFromType(car.getType());
		PumpSpecification idealPump = null;
		
		if (car.getType() == CarType.Small)
			idealPump = PumpTypeTranslator.getPumpFromType(PumpType.Small);
		
		if (car.getType() == CarType.Medium)
			idealPump = PumpTypeTranslator.getPumpFromType(PumpType.Medium);
		
		if (car.getType() == CarType.Large)
			idealPump = PumpTypeTranslator.getPumpFromType(PumpType.Large);
		
		double totalChargeTillFull = car.getMinChargeCapacity(); 
		double minimumMinutesToComplete = (totalChargeTillFull / idealPump.getOutputChargeRate()) * 60;
		
		projectedCompletion.add(Calendar.MINUTE, (int)minimumMinutesToComplete);
		
		return car.getLatestFinishDate().after(projectedCompletion.getTime());
	}
	
	private void logMessage(ACLMessage msg, String messageType, Car car) {
		String log = messageType + "--" + ACLMessage.getPerformative(msg.getPerformative());
		
		if (messageType != "Received") {
			log += ": " + msg.getContent();
		}
		else {
			log += ": Added car " + car.getId();
		} 
		System.out.println(log);
		_logHistory.put(_currentLogId++, log);
	}
	
	public List<Car> getCars(){
		return new ArrayList<>(_cars);
	}
	
	public Map<Car,Pump> getMap(){
		return new HashMap<>(_map);
	}
}
