package Controllers;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;

import jade.wrapper.*;

public class JadeController {
	// Fields
	private final CarGateway _carGateway;
	private final MsaGateway _msaGateway;
	private final ContainerController _mainContainer;
	private final ContainerController _carContainer;
	
	// Constructors
	public JadeController() {
		_carGateway = new CarGateway();
		_msaGateway = new MsaGateway();
		_mainContainer = createContainer(null, true, true);
		_carContainer = createContainer("car_container", false, false);
	}
	
	// Methods
	public void createCarAgent() {
		_carContainer.createNewAgent(arg0, arg1, arg2)
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
