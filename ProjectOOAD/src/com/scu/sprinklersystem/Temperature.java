package com.scu.sprinklersystem;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Temperature {
	
	//Data Members
	private int temperature;
	private int StatusCheck;
	static String fileRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\data\\");
	static String imageRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\images\\");

	String temperatureDataFile = fileRoot.concat("Temperature.txt");
	String statusFile = fileRoot.concat("Status.csv");
	
	
	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	//Method for checking the temperature
	public int checkTemperature(){
		
		BufferedReader br = null;
		FileReader fr = null;

		//Reading the Input Temperature File
		try {

			fr = new FileReader(temperatureDataFile);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(temperatureDataFile));
			
			while ((sCurrentLine = br.readLine()) != null) {
				temperature = Integer.parseInt(sCurrentLine);
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		
		
		if (temperature > 90)
			return(1);
		else if (temperature < 55)
			return(0);	
		else 
			return(2);
		
	}
	
	public JPanel displayTempChecker(){
		
		JPanel tempPane = new JPanel(new BorderLayout());
		JPanel subPane = new JPanel (new FlowLayout(5,5,5));
		JPanel buttonPane = new JPanel(new FlowLayout());
		
		JButton checkTemp = new JButton("Check the Temperature");
		buttonPane.add(checkTemp);
		JTextField tempValue = new JTextField(10);
		//Gets the Temperature value of the surrounding from the user
		subPane.add(new JLabel("Enter the temperature value :"));		
		checkTemp.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae){
				
				 try (BufferedWriter bw = new BufferedWriter(new FileWriter(temperatureDataFile))) {
					 
					 bw.write(tempValue.getText());
				 
				 } 
				 
				 catch (IOException e) {
				
					 e.printStackTrace();
				}
				 
				//Temperature Checker is invoked
				StatusCheck = checkTemperature();
				 
				//According to the Temperature the Sprinkler Status is modified
				Scanner input = null;
				try {
					input = new Scanner(new File(statusFile));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
		            String tempHolder = "";
		            while(input.hasNextLine()){
		            	
		                String tempLine = input.nextLine();
		                if(StatusCheck == 0 )
		                {
		                	if(tempLine.contains("true")){
		                		tempLine = tempLine.replace("true","false");
		                		tempHolder = tempHolder+tempLine;
		                	}
		                	else
		                	{
		                		tempHolder = tempHolder+tempLine;
		                	}
		               }
		               else if(StatusCheck == 1 )
		               {
		     
		                	if(tempLine.contains("false"))
		                	{
		              
		                		tempLine = tempLine.replace("false","true");
		                		tempHolder = tempHolder+tempLine;
		                	}
		                	else
		                	{
		                		tempHolder = tempHolder+tempLine;
		                	}
		                
		                
		               }
		               else if(StatusCheck == 2)
		               {
		            	   tempHolder = tempHolder+tempLine;
		            	   
		               }
		                
		                tempHolder=tempHolder+"\n";
		            }
		            input.close();
		            
		            //Status file is Updated
		            FileWriter fw;
					try {
						fw = new FileWriter(new File(statusFile));
						fw.write(tempHolder);
			            fw.close();
					} catch (IOException e) {

						e.printStackTrace();
					
					}
					
					if(StatusCheck == 2)
					{
						new Scheduler().writeScheduleToFile();
					}
		}
	});
		subPane.add(tempValue, FlowLayout.CENTER);
		tempPane.add(subPane, BorderLayout.CENTER);
		tempPane.add(buttonPane, BorderLayout.SOUTH);
		return tempPane;
	 

	}	
		
}