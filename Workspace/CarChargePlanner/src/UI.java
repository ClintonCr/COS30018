import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import Agents.CarAgentInterface;
import Agents.MsaAgentInterface;
import Controllers.JadeController;
import jade.wrapper.StaleProxyException;

import javax.swing.JButton;
import java.awt.BorderLayout;

public class UI {
	private JFrame frame;
	private JadeController _jadeController;
	private MsaAgentInterface _msaAgent;
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
		// Initilise fields
		frame = new JFrame();
		_jadeController = new JadeController();
		_carAgents = new ArrayList<>();
		
		// Create MSA Agent
		createAgent(false); // createCarAgent = false
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setBounds(100, 100, 600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnAddButton = new JButton("New button");
		frame.getContentPane().add(btnAddButton, BorderLayout.WEST);
		btnAddButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createAgent(true); // createCarAgent = true
			}
		});
	}
	
	private void createAgent(boolean createCarAgent) {
		try {
			if (createCarAgent) {
				_carAgents.add(_jadeController.createCarAgent());
			}
			else {
				// This will through an error if the MSA already exists
				_msaAgent = _jadeController.createMsaAgent();
			}
		}
		catch (Exception e) {
			// TODO - log this correctly?
			e.printStackTrace();
		}
	}
}
