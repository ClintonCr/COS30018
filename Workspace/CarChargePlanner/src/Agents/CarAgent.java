package Agents;

import Models.Car;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CarAgent extends Agent implements CarAgentInterface{
	
	private Car _car;
	
	public CarAgent() {
		registerO2AInterface(CarAgentInterface.class, this);
	}
	
	//Create car agent on init and then send message through to MSA 
	
	protected void setup() {
		try {
			_car = new Car(this.getAID().getLocalName(), this.getArguments());
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setContentObject(_car);
			msg.addReceiver(new AID("msa_agent",AID.ISLOCALNAME));
			send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		addBehaviour(new CyclicBehaviour() { //todo get this messaging interactions up to FIPA standards..
			@Override
			public void action() {
				ACLMessage msg = myAgent.receive();
				
				if (msg != null) {
					System.out.println(msg.getContent());
					
					if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
						return;
					}
					doDelete();
				} 
				else {
					block();
				}
			}
		} );
	}
	
	public void end() {
		// TODO - on delete inform MSA
		doDelete();
	}	
	protected void takeDown() {
		System.out.println("Car agent "+ getName() + " is terminating.");
	}
}
