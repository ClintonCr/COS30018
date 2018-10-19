package Models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Enums.*;
import Helpers.CarSpecification;
import Helpers.CarTypeTranslator;
import Helpers.PumpSpecification;
import Helpers.PumpTypeTranslator;

public class Car implements Serializable{
	// Fields
	final private String _uniqueAgentName;
	final private CarType _carType;
	final private double _minChargeCapacity;
	final private double _maxChargeCapacity;
	final private Date _earliestStartDate;
	final private Date _latestFinishDate;	
	// Operating fields
	private double _priority; //updated on refresh
	private double _currentCapacity; //updated on refresh
	private boolean _isConnected;
	private PumpType _currentPumpType;
	
	// Constructors
	public Car(String uniqueAgentName, CarType carType, double minChargeCapacity, double maxChargeCapacity, Date earliestStartDate, Date latestFinishDate) {
		
		_uniqueAgentName = uniqueAgentName;
		_carType = carType;
		_minChargeCapacity = minChargeCapacity;
		_maxChargeCapacity = maxChargeCapacity;
		_earliestStartDate = earliestStartDate;
		_latestFinishDate = latestFinishDate;
		_currentCapacity = 0;
		_isConnected = false;
	}
	
	public Car(String uniqueAgentName, Object[] args) throws ParseException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
		
		_uniqueAgentName = uniqueAgentName;
		_carType = (CarType)args[0];
		_minChargeCapacity = (double)args[1];
		_maxChargeCapacity = (double)args[2];
		_earliestStartDate = format.parse((String)args[3]);
		_latestFinishDate = format.parse((String)args[4]);
	}
	
	// Methods
	public void refresh() {
		CarSpecification carSpec = CarTypeTranslator.getCarFromType(_carType);
		
		if (_isConnected) {
			PumpSpecification pumpSpec = PumpTypeTranslator.getPumpFromType(_currentPumpType);
			double additionalCharge = 0.5 * pumpSpec.getOutputChargeRate();
			_currentCapacity += additionalCharge;
		}
		// Update priority
		double remainingCharge = _maxChargeCapacity - _currentCapacity;
		
		//set isConnected as false and currentPumpType to null
	}
	
	// Properties
	public double getMinChargeCapacity() {return _minChargeCapacity;}
	public double getMaxChargeCapacity() {return _maxChargeCapacity;}
	public Date getEarliestStartDate() {return _earliestStartDate;}
	public Date getLatestFinishDate() {return _latestFinishDate;}
	public PumpType getCarPumpType() {return _currentPumpType;}
	public String getId() {	return _uniqueAgentName; }
	public CarType getType() { return _carType; }
	public double getCurrentCapacity() { return _currentCapacity; }
	public boolean getIsConnected() { return _isConnected; }
}
