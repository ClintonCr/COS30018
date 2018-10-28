import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import Agents.CarAgentInterface;
import Agents.MsaAgentInterface;
import Controllers.JadeController;
import jade.wrapper.StaleProxyException;
import Enums.CarType;
import Helpers.CarSpecification;
import Helpers.CarTypeTranslator;
import Models.Car;
import Models.Pump;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class UI {
	private JFrame _frame;
	private JadeController _jadeController;
	private MsaAgentInterface _msaAgent;
	private List<CarAgentInterface> _carAgents;
	private List<Pump> _pumps;
	final private JTable _carOverviewTable;
	private Properties _properties = new Properties();
	private DefaultTableModel _carOverviewModel = new DefaultTableModel();
	private Timer _aTimer = new Timer();
	final private JTable _carScheduleTable;
	private DefaultTableModel _carScheduleModel = new DefaultTableModel();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window._frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI() {
		// Initialise fields
		_frame = new JFrame();
		_jadeController = new JadeController();
		_carAgents = new ArrayList<>();
		
		// Load in config
		try {
			String projPath = System.getProperty("user.dir");
			String configPath = projPath + "//res//appConfig.txt";
			_properties.load(new FileInputStream(configPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Create MSA Agent
		try {
			int smallPumps = Integer.parseInt(_properties.getProperty("SMALL_PUMP"));
			int mediumPumps = Integer.parseInt(_properties.getProperty("MEDIUM_PUMP"));
			int largePumps = Integer.parseInt(_properties.getProperty("LARGE_PUMP"));
			createMsa(smallPumps, mediumPumps, largePumps);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		_carOverviewTable = new JTable();
		_carScheduleTable = new JTable();
		initialize();
	}

	/**
	 * Initialize the contents of the _frame.
	 */
	private void initialize() {
		/// Add frame
		_frame.setBounds(0, 0, 800, 950);
		_frame.setResizable(false);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.getContentPane().setLayout(null);
		
		/// Add car controls
		// Car Type
		JLabel lblCartype = new JLabel("Car Type:");
		lblCartype.setBounds(507, 252, 90, 16);
		_frame.getContentPane().add(lblCartype);
		
		JComboBox cmbCarType = new JComboBox();
		cmbCarType.setBounds(621, 252, 110, 16);
		cmbCarType.setModel(new DefaultComboBoxModel<>(CarType.values()));
		_frame.getContentPane().add(cmbCarType);
		
		// Minimum Expected Charge		
		JLabel lblMinCharge = new JLabel("Minimum Charge");
		lblMinCharge.setBounds(507, 282, 110, 16);
		_frame.getContentPane().add(lblMinCharge);
		
		JTextField txtMinCharge = new JTextField("Min Charge");
		txtMinCharge.setBounds(621, 282, 110, 16);
		txtMinCharge.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent fe) {
				
				if(txtMinCharge.getText().isEmpty()) {
					txtMinCharge.setText("Min Charge");
				}
			}
			
			@Override
			public void focusGained(FocusEvent fe) {
				
				if (txtMinCharge.getText().equals("Min Charge")) {
					txtMinCharge.setText("");
				}
			}
		});
		_frame.getContentPane().add(txtMinCharge);
		
		// Maximum Expected Charge		
		JLabel lblMaxCharge = new JLabel("Maximum Charge");
		lblMaxCharge.setBounds(507, 312, 110, 16);
		_frame.getContentPane().add(lblMaxCharge);
		
		JTextField txtMaxCharge = new JTextField("Max Charge");
		txtMaxCharge.setBounds(621, 312, 110, 16);
		txtMaxCharge.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent fe) {
				
				if(txtMaxCharge.getText().isEmpty()) {
					txtMaxCharge.setText("Max Charge");
				}
			}
			
			@Override
			public void focusGained(FocusEvent fe) {
				
				if (txtMaxCharge.getText().equals("Max Charge")) {
					txtMaxCharge.setText("");
				}
			}
		});
		_frame.getContentPane().add(txtMaxCharge);
		
		// Start Time		
		JLabel lblStartTime = new JLabel("Start Time");
		lblStartTime.setBounds(507, 342, 90, 16);
		_frame.getContentPane().add(lblStartTime);
		
		JTextField txtStartTime = new JTextField("Enter Start Time");
		txtStartTime.setBounds(621, 342, 110, 16);
		txtStartTime.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent fe) {
				
				if(txtStartTime.getText().isEmpty()) {
					txtStartTime.setText("Enter Start Time");
				}
			}
			
			@Override
			public void focusGained(FocusEvent fe) {
				
				if (txtStartTime.getText().equals("Enter Start Time")) {
					txtStartTime.setText("28/10/2018 11:00");
				}
			}
		});
		_frame.getContentPane().add(txtStartTime);
		
		// Deadline		
		JLabel lblDeadlineTime = new JLabel("Deadline");
		lblDeadlineTime.setBounds(507, 372, 90, 16);
		_frame.getContentPane().add(lblDeadlineTime);
		
		JTextField txtDeadlineTime = new JTextField("Enter Deadline");
		txtDeadlineTime.setBounds(621, 372, 110, 16);
		txtDeadlineTime.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent fe) {
				
				if(txtDeadlineTime.getText().isEmpty()) {
					txtDeadlineTime.setText("Enter Deadline");
				}
			}
			
			@Override
			public void focusGained(FocusEvent fe) {
				
				if (txtDeadlineTime.getText().equals("Enter Deadline")) {
					txtDeadlineTime.setText("28/10/2018 16:00");
				}
			}
		});
		_frame.getContentPane().add(txtDeadlineTime);
		
		// Add Agent Button		
		JButton btnAddButton = new JButton("Add Car");
		btnAddButton.setBounds(524, 427, 150, 28);
		_frame.getContentPane().add(btnAddButton);
		btnAddButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CarType carType;
				boolean validDeadline;
				boolean validStartTime;
				double minChargeCapacity, maxChargeCapacity;
				String earliestStartTime, deadline ;
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
				
				try {					
					carType = (CarType)cmbCarType.getSelectedItem();
					minChargeCapacity = Double.valueOf(txtMinCharge.getText());
					maxChargeCapacity = Double.valueOf(txtMaxCharge.getText());
					
					earliestStartTime = txtStartTime.getText();
					deadline = txtDeadlineTime.getText();
					
					validStartTime = isThisDateValid(earliestStartTime, format);
					validDeadline = isThisDateValid(deadline, format);

					if (!(validStartTime || validDeadline)) {
						System.out.println("Invalid Date");
						return;
					}
					createAgent(carType, minChargeCapacity, maxChargeCapacity, earliestStartTime, deadline);
				}
				catch (Exception exception) {
					System.out.println("Parsing error.");
				}
			}
		});
		
		/// Car Agent table		
		String[] columnNames = new String[7];
		columnNames[0] = "Agent Id";
		columnNames[1] = "Min Expected Charge";
		columnNames[2] = "Max Expected Charge";
		columnNames[3] = "Start Time";
		columnNames[4] = "Deadline";
		columnNames[5] = "Current Charge";
		columnNames[6] = "Car Type";
		
		_carOverviewModel.setColumnIdentifiers(columnNames);
		_carOverviewTable.setModel(_carOverviewModel);
		
		JScrollPane scrollTableOverview = new JScrollPane(_carOverviewTable);
		scrollTableOverview.setBounds(10,10,765,211);
		scrollTableOverview.setVisible(true);
		
		_frame.getContentPane().add(scrollTableOverview);
		
		/// Schedule table
		JLabel lblScheduleTable = new JLabel("Current Schedule");
		lblScheduleTable.setBounds(10,-20,450,543);
		lblScheduleTable.setFont(new Font("asd",Font.PLAIN, 30));
		_frame.getContentPane().add(lblScheduleTable);
		JScrollPane scrollTableSchedule = new JScrollPane(_carScheduleTable);
		
		String[] scheduleColumnNames = new String[4];
		scheduleColumnNames[0] = "Pump Id";
		scheduleColumnNames[1] = "Car Id";
		scheduleColumnNames[2] = "Current Charge";
		scheduleColumnNames[3] = "Projected Finish Time";
		
		_carScheduleModel.setColumnIdentifiers(scheduleColumnNames);
		_carScheduleTable.setModel(_carScheduleModel);
		
		scrollTableSchedule.setBounds(10,283,450,543);
		scrollTableSchedule.setVisible(true);
		
		_frame.getContentPane().add(scrollTableSchedule);
		
		// Preset Configurations Button
		JButton btnConfig1 = new JButton("Configuration 1");
		btnConfig1.setBounds(524, 590, 150, 28);
		_frame.getContentPane().add(btnConfig1);
		btnConfig1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
				Calendar deadline = Calendar.getInstance();
				deadline.add(Calendar.HOUR, 12);
				
				// Add small cars
				createBatchCars(20, CarType.Small, 12, 12, df.format(new Date()), df.format(deadline.getTime()));
				// Add medium cars
				createBatchCars(20, CarType.Medium, 12, 12, df.format(new Date()), df.format(deadline.getTime()));
				// Add large cars
				createBatchCars(20, CarType.Large, 12, 12, df.format(new Date()), df.format(deadline.getTime()));
			}
		});
		JButton btnConfig2 = new JButton("Configuration 2");
		btnConfig2.setBounds(524, 690, 150, 28);
		_frame.getContentPane().add(btnConfig2);
		btnConfig2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
				Calendar deadline = Calendar.getInstance();
				deadline.add(Calendar.HOUR, 12);
				
				// Add small cars
				createBatchCars(20, CarType.Small, 12, 12, df.format(new Date()), df.format(deadline.getTime()));
				// Add medium cars
				createBatchCars(20, CarType.Medium, 12, 12, df.format(new Date()), df.format(deadline.getTime()));
				// Add large cars
				createBatchCars(20, CarType.Large, 12, 12, df.format(new Date()), df.format(deadline.getTime()));
			}
		});
		JButton btnConfig3 = new JButton("Configuration 3");
		btnConfig3.setBounds(524, 790, 150, 28);
		_frame.getContentPane().add(btnConfig3);
		btnConfig3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
				Calendar deadline = Calendar.getInstance();
				deadline.add(Calendar.HOUR, 12);
				
				// Add small cars
				createBatchCars(20, CarType.Small, 12, 12, df.format(new Date()), df.format(deadline.getTime()));
				// Add medium cars
				createBatchCars(20, CarType.Medium, 12, 12, df.format(new Date()), df.format(deadline.getTime()));
				// Add large cars
				createBatchCars(20, CarType.Large, 12, 12, df.format(new Date()), df.format(deadline.getTime()));
			}
		});
		
		// Dashboard timer		
		TimerTask aTask = new TimerTask() {
			
			@Override
			public void run() {				
				Map<Car,Pump> tempSchedule = _msaAgent.getMap();
				List<Car> tempCarList = _msaAgent.getCars();
				refreshCarOverviewTable(tempCarList);
				refreshCarSchedule(tempCarList, tempSchedule);
			}
		};
		_aTimer.scheduleAtFixedRate(aTask, 0, 5000);
	}
	
	private void createBatchCars(int amount, CarType carType, double minChargeCapacity, double maxChargeCapacity, String earliestStartTime, String deadline) {
		for (int i = 0; i<amount; i++) {
			createAgent(carType, minChargeCapacity, maxChargeCapacity, earliestStartTime, deadline);
		}
	}
	
	private boolean isThisDateValid(String dateToValidate, SimpleDateFormat sdf) {
		
		if(dateToValidate == null) {
			return false;
		}
		
		sdf.setLenient(false);
		
		try {
			Date date = sdf.parse(dateToValidate);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void createAgent(CarType carType,double minChargeCapacity, double maxChargeCapacity, String earliestStartTime, String deadline) {
		try {
			_carAgents.add(_jadeController.createCarAgent(carType, minChargeCapacity, maxChargeCapacity, earliestStartTime, deadline));
		}
		catch (Exception e) {
			// TODO - log this correctly?
			e.printStackTrace();
		}
	}
	
	private void createMsa(int smallPumps, int mediumPumps, int largePumps) throws StaleProxyException {
		_msaAgent = _jadeController.createMsaAgent(smallPumps, mediumPumps, largePumps);
	}
	
	private void refreshCarOverviewTable(List<Car> tempCarList) {
		_carOverviewModel.setRowCount(0);
		
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
		String[] rowData = new String[7];
		
		for (int i = 0; i < tempCarList.size(); i++) {
			rowData[0] = tempCarList.get(i).getId();
			rowData[1] = String.valueOf(tempCarList.get(i).getMinChargeCapacity()) + "kW";
			rowData[2] = String.valueOf(tempCarList.get(i).getMaxChargeCapacity()) + "kW";
			rowData[3] = format.format(tempCarList.get(i).getEarliestStartDate());
			rowData[4] = format.format(tempCarList.get(i).getLatestFinishDate());
			rowData[5] = String.valueOf(tempCarList.get(i).getCurrentCapacity()) + "kW";
			rowData[6] = String.valueOf(tempCarList.get(i).getType());
			_carOverviewModel.addRow(rowData);
		}
	}
	
	private void refreshCarSchedule(List<Car> tempCarList, Map<Car,Pump> tempSchedule) {
		if (_pumps == null || _pumps.isEmpty()) {
			_pumps = tempSchedule.values()
			.stream()
			.sorted((base,target) -> base.getId().compareTo(target.getId()))
			.collect(Collectors.toList());
		}
		
		_carScheduleModel.setRowCount(0);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
		String[] rowData = new String[4];
		for (Pump pump : _pumps) {
			Car connectedCar = tempSchedule.entrySet().stream()
					.filter(kvp -> kvp.getValue().equals(pump))
					.findFirst()
					.map(m->m.getKey())
					.orElse(null);
			
			rowData[0] = pump.getId();
			rowData[1] = connectedCar.getId();
			rowData[2] = String.valueOf(connectedCar.getCurrentCapacity()) + "kW";
			rowData[3] = format.format(calculateExpectedCompletionTime(connectedCar.getCurrentCapacity(), connectedCar.getMinChargeCapacity(), 
					CarTypeTranslator.getCarFromType(connectedCar.getType())));
			_carScheduleModel.addRow(rowData);
		}
	}
	
	private Date calculateExpectedCompletionTime(double currentCapacity, double minCapacity, CarSpecification carSpec) {
		double remainingCharge = 0;
		double hoursTillMin = 0;
		double chargeRate = carSpec.getRateOfCharge();
		Date estimatedCompletionTime = new Date();
		
		//need to account for when above min
		remainingCharge = minCapacity - currentCapacity;
		Date minRequiredTime = new Date();
		
		hoursTillMin = remainingCharge/chargeRate;
		
		estimatedCompletionTime = addHoursToJavaUtilDate(minRequiredTime, (Math.round(hoursTillMin * 2)/2.0));
		return estimatedCompletionTime;
	}
	
	public Date addHoursToJavaUtilDate(Date date, double hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int minutes;
		minutes = (int) (hours * 60);
		calendar.add(Calendar.MINUTE,minutes);
		return calendar.getTime();
		
	}
}
