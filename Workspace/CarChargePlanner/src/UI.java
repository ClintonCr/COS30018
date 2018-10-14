import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import Agents.CarAgentInterface;
import Controllers.JadeController;
import jade.wrapper.StaleProxyException;

import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class UI {
	private JFrame frame;
	private JadeController _jadeController;
	private List<CarAgentInterface> _carAgents;
	
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		_jadeController = new JadeController();
		_carAgents = new ArrayList<>();
		try {
			_jadeController.createMsaAgent();
		} catch (StaleProxyException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
				
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Car Type
		
		JComboBox cmbCarType = new JComboBox();
		cmbCarType.setBounds(471, 232, 90, 16);
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
		
		btnAddButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//testerShit();
				try {
					
					_carAgents.add(_jadeController.createCarAgent());
					
					
				//register this interface in a list...
				} catch (StaleProxyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		
		
	}
	
	private void testerShit() {
		/*
		try {
			//TestAgentInterface asd =_jadeController.createCarAgent();
			try {
				System.out.println("FIRST TESTER" + asd.getCount());
				Thread.sleep(1000);
				System.out.println(asd.getCount());
				Thread.sleep(1000);
				System.out.println(asd.getCount());
				Thread.sleep(1000);
				System.out.println(asd.getCount());
				Thread.sleep(1000);
				System.out.println(asd.getCount());
			}
			catch(Exception ex) {
				
			}
		} catch (StaleProxyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
	}
}
