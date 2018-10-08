package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class CarAgent extends Agent implements CarAgentInterface{
	
	private int currentCapacity = 2;
	
	public CarAgent() {
		registerO2AInterface(CarAgentInterface.class, this);
	}
	
	//Create car agent on init and then send message through to MSA 
	
	protected void setup() {
		currentCapacity = 1;
		
		ACLMessage aMsg = new ACLMessage(ACLMessage.INFORM);
		aMsg.setContent(String.valueOf(getCapacity()));
		aMsg.addReceiver(new AID("msa_agent",AID.ISLOCALNAME));
		send(aMsg);
	}
	
	public int getCapacity(){
		return currentCapacity;
	}

	@Override
	public int getSuccess() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
