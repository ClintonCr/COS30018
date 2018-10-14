package Controllers;

import jade.core.Runtime;
import Agents.CarAgent;
import Agents.CarAgentInterface;
import Agents.MsaAgent;
import Agents.MsaAgentInterface;
import jade.core.Profile;
import jade.core.ProfileImpl;

import jade.wrapper.*;

public class JadeController {
	// Fields
	private final ContainerController _mainContainer;
	private final ContainerController _carContainer;
	private int _numOfCarAgents = 0;
	
	// Constructors
	public JadeController() {
		_mainContainer = createContainer(null, true, true);
		_carContainer = createContainer("car_container", false, false);
	}
	
	// Methods
	public CarAgentInterface createCarAgent() throws StaleProxyException {
		AgentController agentController = _carContainer
				.createNewAgent("car_agent" + String.valueOf(_numOfCarAgents++), CarAgent.class.getName(), new Object[] { "Small", 10.0, 20.0, "15/10/2018","15/10/2018" });
		
		agentController.start();
		return agentController.getO2AInterface(CarAgentInterface.class);
	}
	
	public MsaAgentInterface createMsaAgent() throws StaleProxyException{
		AgentController agentController = _mainContainer
				.createNewAgent("msa_agent", MsaAgent.class.getName(),new Object[] {100,100,100}); //TODO replace this with config values
		
		agentController.start();
		return agentController.getO2AInterface(MsaAgentInterface.class);		
	}
	
	// Helpers
	private static ContainerController createContainer(String name, boolean showGui, boolean isMainContainer) {
		Runtime rt = Runtime.instance();
		Profile profile = new ProfileImpl(null, 8888, null);
		
		if (name != null && name != "")
			profile.setParameter(Profile.CONTAINER_NAME, name);
		
		if (showGui)
			profile.setParameter(Profile.GUI, "true");
		
		if (isMainContainer)
			return rt.createMainContainer(profile);
		
		return rt.createAgentContainer(profile);
	}
}
