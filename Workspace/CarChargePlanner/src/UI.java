import java.awt.EventQueue;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

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
	private JFrame frame;
	private JadeController _jadeController;
	private MsaAgentInterface _msaAgent;
	private List<CarAgentInterface> _carAgents;
	final private JTable carOverviewTable;
	private Properties _properties = new Properties();
	private DefaultTableModel carOverviewModel = new DefaultTableModel();
	private Timer aTimer = new Timer();
	final private JTable carScheduleTable;
	private DefaultTableModel carScheduleModel = new DefaultTableModel();
	public static final long HOUR = 3600*1000;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window.frame.setVisible(true);
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
		frame = new JFrame();
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
		
		carOverviewTable = new JTable();
		carScheduleTable = new JTable();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setBounds(100, 100, 600, 450);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Car Type
		
		JComboBox cmbCarType = new JComboBox();
		cmbCarType.setBounds(471, 232, 90, 16);
		cmbCarType.setModel(new DefaultComboBoxModel<>(CarType.values()));
		frame.getContentPane().add(cmbCarType);
		
		JLabel lblChargetype = new JLabel("Car Type:");
		lblChargetype.setBounds(377, 232, 90, 16);
		frame.getContentPane().add(lblChargetype);
		
		//Minimum Expected Charge
		
		JLabel lblMinCharge = new JLabel("Minimum Charge");
		lblMinCharge.setBounds(377, 262, 90, 16);
		frame.getContentPane().add(lblMinCharge);
		
		JTextField txtMinCharge = new JTextField("Min Charge");
		txtMinCharge.setBounds(471, 262,  90,  16);
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
		frame.getContentPane().add(txtMinCharge);
		
		//Maximum Expected Charge
		
		JLabel lblMaxCharge = new JLabel("Maximum Charge");
		lblMaxCharge.setBounds(377, 292, 90, 16);
		frame.getContentPane().add(lblMaxCharge);
		
		JTextField txtMaxCharge = new JTextField("Max Charge");
		txtMaxCharge.setBounds(471, 292,  90,  16);
		txtMaxCharge.addFocusListener(new FocusListener() {
			
			@Override //Refactor focus listeners into one.
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
		frame.getContentPane().add(txtMaxCharge);
		
		//Start Time
		
		JLabel lblStartTime = new JLabel("Start Time");
		lblStartTime.setBounds(377, 322, 90, 16);
		frame.getContentPane().add(lblStartTime);
		
		JTextField txtStartTime = new JTextField("Enter Start Time");
		txtStartTime.setBounds(471, 322,  90,  16);
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
					txtStartTime.setText("");
				}
			}
		});
		frame.getContentPane().add(txtStartTime);
		
		//Deadline
		
		JLabel lblDeadlineTime = new JLabel("Deadline");
		lblDeadlineTime.setBounds(377, 352, 90, 16);
		frame.getContentPane().add(lblDeadlineTime);
		
		JTextField txtDeadlineTime = new JTextField("Enter Deadline");
		txtDeadlineTime.setBounds(471, 352,  90,  16);
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
					txtDeadlineTime.setText("");
				}
			}
		});
		frame.getContentPane().add(txtDeadlineTime);
		
		//Add Agent Button		
		
		JButton btnAddButton = new JButton("Add Car");
		btnAddButton.setBounds(424, 377, 90, 28);
		frame.getContentPane().add(btnAddButton);
		
		//Add Agent table

		String[] columnNames = new String[7];
		columnNames[0] = "Agent Id";
		columnNames[1] = "Min Expected Charge";
		columnNames[2] = "Max Expected Charge";
		columnNames[3] = "Start Time";
		columnNames[4] = "Deadline";
		columnNames[5] = "Current Charge";
		columnNames[6] = "Car Type";
		
		carOverviewModel.setColumnIdentifiers(columnNames);
		carOverviewTable.setModel(carOverviewModel);
		
		JScrollPane scrollTableOverview = new JScrollPane(carOverviewTable);
		scrollTableOverview.setBounds(10,10,551,211);
		scrollTableOverview.setVisible(true);
		
		frame.getContentPane().add(scrollTableOverview);
		
		//Add in schedule table
		
		JScrollPane scrollTableSchedule = new JScrollPane(carScheduleTable);
		
		String[] scheduleColumnNames = new String[4];
		scheduleColumnNames[0] = "Car Id";
		scheduleColumnNames[1] = "Pump Id";
		scheduleColumnNames[2] = "Current Charge";
		scheduleColumnNames[3] = "Projected Finish Time";
		
		carScheduleModel.setColumnIdentifiers(scheduleColumnNames);
		carScheduleTable.setModel(carScheduleModel);
		
		scrollTableSchedule.setBounds(10,233,355,135);
		scrollTableSchedule.setVisible(true);
		
		frame.getContentPane().add(scrollTableSchedule);
		
		TimerTask aTask = new TimerTask() {
			
			@Override
			public void run() {
				Map<Car,Pump> tempSchedule = _msaAgent.getMap();
				List<Car> tempCarList = _msaAgent.getCars();
				refreshCarOverviewTable(tempCarList);
				refreshCarSchedule(tempCarList, tempSchedule);
			}
		};
		aTimer.scheduleAtFixedRate(aTask, 0, 5000);
		
		btnAddButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CarType carType;
				boolean validDeadline;
				boolean validStartTime;
				double minChargeCapacity, maxChargeCapacity;
				String earliestStartTime, deadline ;
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
				
				carType = (CarType)cmbCarType.getSelectedItem();
				minChargeCapacity = Double.valueOf(txtMinCharge.getText());
				maxChargeCapacity = Double.valueOf(txtMaxCharge.getText());
				
				earliestStartTime = txtStartTime.getText();
				deadline = txtDeadlineTime.getText();
				
				validStartTime = isThisDateValid(earliestStartTime, format);
				validDeadline = isThisDateValid(deadline, format);

				if (!(validStartTime || validDeadline)) {
					System.out.println("Invalid Date");
				}
				createAgent(carType, minChargeCapacity, maxChargeCapacity, earliestStartTime, deadline);
			}
		});
	}
	
	public boolean isThisDateValid(String dateToValidate, SimpleDateFormat sdf) {
		
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
		carOverviewModel.setRowCount(0);
		
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
		String[] rowData = new String[7];
		
		for (int i = 0; i < tempCarList.size(); i++) {
			rowData[0] = tempCarList.get(i).getId();
			rowData[1] = String.valueOf(tempCarList.get(i).getMinChargeCapacity());
			rowData[2] = String.valueOf(tempCarList.get(i).getMaxChargeCapacity());
			rowData[3] = format.format(tempCarList.get(i).getEarliestStartDate());
			rowData[4] = format.format(tempCarList.get(i).getLatestFinishDate());
			rowData[5] = String.valueOf(tempCarList.get(i).getCurrentCapacity());
			rowData[6] = String.valueOf(tempCarList.get(i).getType());
			carOverviewModel.addRow(rowData);
		}
	}
	
	private void refreshCarSchedule(List<Car> tempCarList, Map<Car,Pump> tempSchedule) {
		carScheduleModel.setRowCount(0);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
		String[] rowData = new String[4];
		
		
		Iterator it = tempSchedule.entrySet().iterator();
		while(it.hasNext()) {
			
			Map.Entry aPair = (Map.Entry)it.next(); 
			
			Car aCar = (Car)aPair.getKey();
			Pump aPump = (Pump)aPair.getValue();
			
			rowData[0] = aCar.getId();
			rowData[1] = aPump.getId();
			rowData[2] = String.valueOf(aCar.getCurrentCapacity());
			rowData[3] = format.format(calculateExpectedCompletionTime(aCar.getCurrentCapacity(), aCar.getMinChargeCapacity(), 
					CarTypeTranslator.getCarFromType(aCar.getType())));
			it.remove();

			carScheduleModel.addRow(rowData);
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
