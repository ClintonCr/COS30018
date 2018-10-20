package Agents;

import Models.Car;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class CarAgent extends Agent implements CarAgentInterface{
	
	private Car _car;
	
	public CarAgent() {
		registerO2AInterface(CarAgentInterface.class, this);
	}
	
	//Create car agent on init and then send message through to MSA 
	
	protected void setup() {
		// todo add receive message functionality (from MSA)
		try {
			_car = new Car(this.getName(), this.getArguments());
			ACLMessage aMsg = new ACLMessage(ACLMessage.INFORM);
			aMsg.setContentObject(_car);
			aMsg.addReceiver(new AID("msa_agent",AID.ISLOCALNAME));
			send(aMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void end() {
		// TODO - on delete inform MSA
		doDelete();
	}	
	protected void takeDown() {
		System.out.println("Car agent "+ getName() + " is terminating.");
	}
}
