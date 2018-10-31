package Agents;

import Models.Car;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CarAgent extends Agent implements CarAgentInterface{
	// ******************************
	// Fields
	// ******************************
	private Car _car;
	
	// ******************************
	// Constructors
	// ******************************
	public CarAgent() {
		registerO2AInterface(CarAgentInterface.class, this);
	}
	
	// ******************************
	// Public  methods
	// ******************************
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
		
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				ACLMessage msg = myAgent.receive();
				
				if (msg != null) {
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
		doDelete();
	}	
	protected void takeDown() {
		System.out.println("Car agent "+ getName() + " is terminating.");
	}
}
