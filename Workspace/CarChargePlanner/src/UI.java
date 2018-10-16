import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import Agents.CarAgentInterface;
import Agents.MsaAgentInterface;
import Controllers.JadeController;
import jade.wrapper.StaleProxyException;
import Enums.CarType;
import Models.Car;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;

public class UI {
	private JFrame frame;
	private JadeController _jadeController;
	private MsaAgentInterface _msaAgent;
	private List<CarAgentInterface> _carAgents;
	private JTable table;
	private DefaultTableModel model = new DefaultTableModel();
	
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
		
		// Create MSA Agent
		try {
			createMsa();
		} catch(Exception e) {
			e.getStackTrace();
		}
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setBounds(100, 100, 600, 450);
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
		
		table = new JTable();
		
		
		Object[] columnNames = new Object[7];
		columnNames[0] = "Agent Id";
		columnNames[1] = "Min Expected Charge";
		columnNames[2] = "Max Expected Charge";
		columnNames[3] = "Start Time";
		columnNames[4] = "Deadline";
		columnNames[5] = "Deadline";
		columnNames[6] = "Deadline"; //, "Current Charge", "Projected min charge"};
		
		model.setColumnIdentifiers(columnNames);
		
		table.setModel(model);
		table.setBounds(10,10,551,211);
		frame.getContentPane().add(table);
		
		if (_carAgents.size() != 0) {
			refreshTable();
		}
		
		btnAddButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CarType carType;
				double minChargeCapacity, maxChargeCapacity;
				String earliestStartTime, deadline ;
				
				carType = (CarType)cmbCarType.getSelectedItem();
				minChargeCapacity = Double.valueOf(txtMinCharge.getText());
				maxChargeCapacity = Double.valueOf(txtMaxCharge.getText());
				earliestStartTime = txtStartTime.getText();
				deadline = txtDeadlineTime.getText();

				
				createAgent(carType, minChargeCapacity, maxChargeCapacity, earliestStartTime, deadline);
			}
		});
	}
	
	private void createAgent(CarType carType,double minChargeCapacity, double maxChargeCapacity, String earliestStartTime, String deadline) {
		try {
			_carAgents.add(_jadeController.createCarAgent(carType, minChargeCapacity, maxChargeCapacity, earliestStartTime, deadline));
		}
		catch (Exception e) {
			// TODO - log this correctly?
			e.printStackTrace();
		}
		if (_carAgents.size() != 0) {
			refreshTable();
		}
		
	}
	
	private void createMsa() throws StaleProxyException {
		_msaAgent = _jadeController.createMsaAgent();
	}
	
	private void refreshTable() {
		
		List<Car> tempCarList = _msaAgent.getCars();
		Object[] rowData = new Object[5];
		
		for (int i = 0; i < tempCarList.size(); i++) {
			rowData[0] = tempCarList.get(i).getId();
			rowData[1] = tempCarList.get(i).getMinChargeCapacity();
			rowData[2] = tempCarList.get(i).getMaxChargeCapacity();
			rowData[3] = tempCarList.get(i).getEarliestStartDate();
			rowData[4] = tempCarList.get(i).getLatestFinishDate();
			
			model.addRow(rowData);
		}
		
		
		
		
	}
}
