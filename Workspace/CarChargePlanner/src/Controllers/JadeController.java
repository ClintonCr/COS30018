package Controllers;

import jade.core.Runtime;
import Agents.CarAgent;
import Agents.CarAgentInterface;
import Agents.TestAgentInterface;
import Agents.TesterAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;

import jade.wrapper.*;

public class JadeController {
	// Fields
	private final CarGateway _carGateway;
	private final MsaGateway _msaGateway;
	private final ContainerController _mainContainer;
	private final ContainerController _carContainer;
	private int _numOfCarAgents = 0;
	
	// Constructors
	public JadeController() {
		_carGateway = new CarGateway();
		_msaGateway = new MsaGateway();
		_mainContainer = createContainer(null, true, true);
		_carContainer = createContainer("car_container", false, false);
	}
	
	// Methods
	public CarAgentInterface createCarAgent() throws StaleProxyException { // todo make this return interface of new agent
		AgentController agentController = _carContainer
				.createNewAgent("car_agent" + String.valueOf(_numOfCarAgents++), CarAgent.class.getName(), new Object[0]);
		
		agentController.start();
		return agentController.getO2AInterface(CarAgentInterface.class);
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
