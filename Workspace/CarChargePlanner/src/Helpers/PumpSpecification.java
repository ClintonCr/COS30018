package Helpers;

public class PumpSpecification {
	// Fields
	final private double _outputChargeRate;

	// Constructors
	PumpSpecification(double outputChargeRate){
		_outputChargeRate = outputChargeRate;
	}

	//todo properties
	public double getOutputChargeRate() { return _outputChargeRate; }
}
