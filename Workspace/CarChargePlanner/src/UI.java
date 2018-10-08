import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import Agents.CarAgentInterface;
import Controllers.JadeController;
import jade.wrapper.StaleProxyException;

import javax.swing.JButton;
import java.awt.BorderLayout;

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
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnAddButton = new JButton("New button");
		frame.getContentPane().add(btnAddButton, BorderLayout.WEST);
		btnAddButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//testerShit();
				try {
					_carAgents.add(_jadeController.createCarAgent());//register this interface in a list...
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
