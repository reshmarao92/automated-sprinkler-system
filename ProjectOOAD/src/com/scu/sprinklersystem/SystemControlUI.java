package com.scu.sprinklersystem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SystemControlUI {
	
	private JFrame frame;
	private JLabel label1;
	private JPanel panel1;
	private JButton manageButton,exitButton;
	static String fileRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\data\\");
	static String imageRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\images\\");


	public void displayScreen() {
		
		//Creating frame for Welcome screen
		frame = new JFrame("HummingBee Sprinkler System");
		
		ImageIcon imageIcon = new ImageIcon(new ImageIcon(imageRoot.concat(
				"sprinkler-system.jpg")).getImage().getScaledInstance(2250, 1506, Image.SCALE_DEFAULT));
		frame.setContentPane(new JLabel(imageIcon));
		
		Container cp = frame.getContentPane();
		cp.setLayout(new BorderLayout());
		
		//Creating labels to display text
		label1 = new JLabel("Welcome to HummingBee Sprinkler System",JLabel.CENTER);
		label1.setFont(label1.getFont().deriveFont(50.0f));
						
		// Creating panels and adding buttons 
		panel1 = new JPanel(new FlowLayout());
		manageButton = new JButton("Manage");
		exitButton = new JButton("Exit");
		
		cp.add(label1, BorderLayout.CENTER);
		panel1.add(manageButton, FlowLayout.LEFT);
		panel1.add(exitButton, FlowLayout.CENTER);
		panel1.setOpaque(false);
		cp.add(panel1, BorderLayout.SOUTH);
				
		//Action on button click
		manageButton.addActionListener(new ActionListener()
		{	
				@Override
				public void actionPerformed(ActionEvent ae) {
	
					frame.remove(label1);
					frame.remove(panel1);
					
			        frame.add(new SprinklerOptions().displayTabs(), BorderLayout.CENTER);
			        frame.pack();
					
			}	
		});
	
		exitButton.addActionListener(new ActionListener()
			{	
				@Override
				public void actionPerformed(ActionEvent ae) {
					frame.dispose();
					
			}	
		});
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		
	}
	
	public static void main(String args[]) {
	
		SystemControlUI obj = new SystemControlUI();
		obj.displayScreen();
	}
	
}