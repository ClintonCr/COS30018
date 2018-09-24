import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class MainScreen {

	private JFrame frame;
	private JTextField txtMinimumexpectedCharge;
	private JTextField textDeadline;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen();
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
	public MainScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblMainTitle = new JLabel("Electric Car Charger Scheduler");
		lblMainTitle.setBounds(136, 11, 169, 14);
		frame.getContentPane().add(lblMainTitle);
		
		txtMinimumexpectedCharge = new JTextField();
		txtMinimumexpectedCharge.setBounds(338, 153, 86, 20);
		frame.getContentPane().add(txtMinimumexpectedCharge);
		txtMinimumexpectedCharge.setColumns(10);
		
		JLabel lblExpectedMinimumCharge = new JLabel("Expected Minimum Charge");
		lblExpectedMinimumCharge.setBounds(180, 156, 148, 14);
		frame.getContentPane().add(lblExpectedMinimumCharge);
		
		JComboBox cmbVehicleType = new JComboBox();
		cmbVehicleType.setBounds(338, 182, 86, 22);
		frame.getContentPane().add(cmbVehicleType);
		
		JLabel lblTypeOfVehicle = new JLabel("Type of Vehicle");
		lblTypeOfVehicle.setBounds(219, 186, 86, 14);
		frame.getContentPane().add(lblTypeOfVehicle);
		
		JLabel lblDeadline = new JLabel("Deadline");
		lblDeadline.setBounds(241, 211, 46, 14);
		frame.getContentPane().add(lblDeadline);
		
		textDeadline = new JTextField();
		textDeadline.setBounds(338, 208, 86, 20);
		frame.getContentPane().add(textDeadline);
		textDeadline.setColumns(10);
	}
}
