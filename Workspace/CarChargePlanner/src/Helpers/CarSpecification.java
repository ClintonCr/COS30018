package Helpers;

public class CarSpecification {
	// Fields
	final private double _rateOfCharge;
	final private double _capacity;
	
	// Constructors
	public CarSpecification(double rateOfCharge, double capacity) {
		_rateOfCharge = rateOfCharge;
		_capacity = capacity;
	}
	
	// Properties
	public double getRateOfCharge() {
		return _rateOfCharge;
	}
	
	public double getCapacity() {
		return _capacity;
	}
}
