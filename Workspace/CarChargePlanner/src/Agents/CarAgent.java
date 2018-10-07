package Agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.introspection.ACLMessage;
import jade.lang.acl.ACLMessage.*;

public class CarAgent extends Agent implements CarAgentInterface{
	
	private int currentCapacity = 0;
	
	public CarAgent() {
		registerO2AInterface(CarAgentInterface.class, this);
	}
	
	//Create car agent on init and then send message through to MSA 
	
	protected void setup() {
		currentCapacity = 1;
		
		ACLMessage aMsg = new ACLMessage(ACLMessage.INFORM);
		aMsg.setContent(String.valueOf(getCapacity()));
	}
	
	public int getCapacity(){
		return currentCapacity;
	}
	
}
