package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class MsaAgent extends Agent {
	
	private String capacity;

	public MsaAgent(){
		
	}
	
	protected void setup(){
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				ACLMessage aMsg = myAgent.receive();
				
				if (aMsg != null) {
					capacity = aMsg.getContent();
					System.out.print("The capacity is: ");
					System.out.print(capacity);
				} 
				else {
					block();
				}
			}
		} );
	}
}
