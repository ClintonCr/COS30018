package Models;


import Enums.*;

public class Pump {
	// Fields
	final private String _uniqueId;
	final private PumpType _pumpType;
	// Operating fields
	private boolean _isConnected;
	
	// Constructors
	public Pump(String uniqueId, PumpType pumpType) {
		_uniqueId = uniqueId;
		_pumpType = pumpType;
	}

	// Properties
	public String getId() {	return _uniqueId; }
	public PumpType getPumpType() { return _pumpType; }
}
