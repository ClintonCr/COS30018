package Agents;

import jade.core.Agent;
import jade.core.behaviours.*;

public class TesterAgent extends Agent implements TestAgentInterface{
	private int _testerInt = 0;
	public TesterAgent() {
		registerO2AInterface(TestAgentInterface.class, this);
	}
	protected void setup() {
		addBehaviour(new TickerBehaviour(this, 1000) {
			protected void onTick() {
				_testerInt++;
			}
		});
	}
	@Override
	public int getCount() {
		return _testerInt;
	}
}
