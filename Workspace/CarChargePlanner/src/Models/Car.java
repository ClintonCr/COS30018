package Models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Enums.*;

public class Car implements Serializable{
	// Fields
	final private String _uniqueAgentName;
	final private CarType _carType;
	final private double _minChargeCapacity;
	final private double _maxChargeCapacity;
	final private Date _earliestStartDate;
	final private Date _latestFinishDate;	
	// Operating fields
	private boolean _isConnected;
	private PumpType _currentPumpType;
	private Date _projectedEndDate;	
	
	// Constructors
	public Car(String uniqueAgentName, CarType carType, double minChargeCapacity, double maxChargeCapacity, Date earliestStartDate, Date latestFinishDate) {
		_uniqueAgentName = uniqueAgentName;
		_carType = carType;
		_minChargeCapacity = minChargeCapacity;
		_maxChargeCapacity = maxChargeCapacity;
		_earliestStartDate = earliestStartDate;
		_latestFinishDate = latestFinishDate;
	}
	
	public Car(String uniqueAgentName, Object[] args) throws ParseException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		
		_uniqueAgentName = uniqueAgentName;
		_carType = CarType.valueOf((String)args[0]);
		_minChargeCapacity = (double)args[1];
		_maxChargeCapacity = (double)args[2];
		_earliestStartDate = format.parse((String)args[3]);
		_latestFinishDate = format.parse((String)args[4]);
	}
	
	// Properties
	public String getId() {
		return _uniqueAgentName;
	}
}
