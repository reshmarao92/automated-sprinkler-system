package com.scu.sprinklersystem;

import java.awt.BorderLayout;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Scheduler {
	
	//Data Members
	private String[] savedStartTime;
	private String[] savedEndTime;
	private String[] savedWaterAmount;
	ScheduleTable newContentPane;
	static String fileRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\data\\");
	static String imageRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\images\\");

	private static final String scheduleFile = fileRoot.concat("Schedule.txt");
	private static final String statusFile = fileRoot.concat("Status.csv");
	
	
	public ScheduleTable getNewContentPane() {
		return newContentPane;
	}

	public void setNewContentPane(ScheduleTable newContentPane) {
		this.newContentPane = newContentPane;
	}

	
	public JPanel displayScreen() {
		
		//Creating a frame
		JFrame frame = new JFrame("Sprinkler Schedule");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		//Creating a Main Panel
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		//Creating a Menu Panel
		JPanel menuPanel = new JPanel(new FlowLayout());
		
		//Creating a Menu Label
		JLabel title = new JLabel("Schedule Configurations");
		title.setFont(new Font("Segoe UI", 1, 25));
		
		//Adding components to Menu Panel
		menuPanel.add(title);
		
		//Creating a Submit Label
		JPanel submitPanel = new JPanel(new FlowLayout());
		JButton resetButton = new JButton("Reset");
		JButton saveButton = new JButton("Save");
		submitPanel.add(resetButton, FlowLayout.LEFT);
		submitPanel.add(saveButton);
		
		//Creating Schedule Table
		newContentPane = new ScheduleTable();
        newContentPane.setOpaque(true); //content panes must be opaque
        
        
        //Action for the buttons
      	resetButton.addActionListener(new ActionListener(){
      			
      			public void actionPerformed(ActionEvent ae){
      				
      			
      			}
      		});	
      		
      		//Action Listener for Save Button
      		saveButton.addActionListener(new ActionListener(){
      			
      			public void actionPerformed(ActionEvent ae){
      				
      				JTable table = newContentPane.getTable();
      				savedStartTime = new String[table.getRowCount()];
      				savedEndTime = new String[table.getRowCount()];
      				savedWaterAmount = new String[table.getRowCount()];
      				for (int i=0; i<table.getRowCount(); i++ ){
      					savedStartTime[i]=table.getValueAt(i,1).toString();
      				}
      				for (int i=0; i<table.getRowCount(); i++ ){
      					savedEndTime[i]=table.getValueAt(i,2).toString();
      				}
      				for (int i=0; i<table.getRowCount(); i++ ){
      					savedWaterAmount[i]=table.getValueAt(i,3).toString();
      				}
      				
      				
      				Scheduler schedulerObj = new Scheduler();
      				schedulerObj.setNewContentPane(newContentPane);
      				
                    //Writes in a file
                    String[] Days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
            		try (BufferedWriter bw = new BufferedWriter(new FileWriter(scheduleFile))) {
            			for(int i=0; i<savedStartTime.length; i++)
                        {
            				try {
								bw.write(Days[i]+","+savedStartTime[i]+","+savedEndTime[i]+","+savedWaterAmount[i]+"\n");
							} catch (IOException e) {
								e.printStackTrace();
							}
                        	bw.newLine();
                        }
            			
            			
            			
      			} catch (IOException e) {
					e.printStackTrace();
				}
            		
            		//Write to Status file as well.
            		writeScheduleToFile();
      			}
		
      		});
		
		//Adding components to the Main Panel	
		mainPanel.add(menuPanel, BorderLayout.NORTH);
		mainPanel.add(newContentPane, BorderLayout.CENTER);
		mainPanel.add(submitPanel,BorderLayout.SOUTH);
		
		return mainPanel;
		
	}
	
	public List<Double> getChartValues(ScheduleTable newContentPane2){
		
			
			JTable table = newContentPane2.getTable();
			List<Double> waterUsage = new ArrayList<>();
			savedStartTime = new String[table.getRowCount()];
			savedEndTime = new String[table.getRowCount()];
			savedWaterAmount = new String[table.getRowCount()];
			for (int i=1; i<table.getRowCount(); i++ ){
				savedStartTime[i-1]=table.getValueAt(i,1).toString();
			}
			for (int i=1; i<table.getRowCount(); i++ ){
				savedEndTime[i-1]=table.getValueAt(i,2).toString();
			}
			for (int i=1; i<table.getRowCount(); i++ ){
				savedWaterAmount[i-1]=table.getValueAt(i,3).toString();
			}
			
			for(int i = 0;i<table.getRowCount()-1;i++)
			{
				
				int startTime = Integer.parseInt(savedStartTime[i].split(":")[0]);
				int endTime = Integer.parseInt(savedEndTime[i].split(":")[0]);
				int waterUsed = Integer.parseInt(savedWaterAmount[i].split("L")[0]);
				waterUsage.add((double) ((Math.abs(endTime - startTime)) * waterUsed) * 30);
				
				
			}	
			return waterUsage;
	}
	
	public void writeScheduleToFile(){
		
		Scanner input = null;
		try {
			input = new Scanner(new File(statusFile));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		   String currentDayOfWeek = new DateCheck().getCurrentDay();
	       int currentHour = new DateCheck().getCurrentHour();
	        
			File scheduleDataFile = new File(scheduleFile); 
	        Scanner in = null;
	        try {
	            in = new Scanner(scheduleDataFile);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        String scheduleLine = "";
	        
	        while(in.hasNextLine()){
	        	String[] tempDataHolder = in.nextLine().split(",");
	            if(currentDayOfWeek.equalsIgnoreCase(tempDataHolder[0]))
	            {
	            	for(String s : tempDataHolder)
	            	{
	            		scheduleLine=scheduleLine + " " + s;
	            	}
	            	
	            }
	        }
	        
	        int startHour = Integer.parseInt(scheduleLine.split(" ")[2].split(":")[0]);
	        int endHour = Integer.parseInt(scheduleLine.split(" ")[3].split(":")[0]);
	        
	        String tempHolder = "";
	        
	        if((currentHour >= startHour) && (currentHour < endHour))
	        {
	        	//Write to Status file
	        	while(input.hasNextLine()) {
	        		String tempLine = input.nextLine();
	        		if(tempLine.contains("false")) {
	        			tempLine = tempLine.replace("false","true");
	                		tempHolder = tempHolder+tempLine;
	        		}
	                else
	                {
	                		tempHolder = tempHolder+tempLine;
	                }
	        		tempHolder=tempHolder+"\n";
	        		
	        	}
	        }
	        else
	        {
	        	while(input.hasNextLine()) {
	        		 String tempLine = input.nextLine();
	        		 if(tempLine.contains("true")){
	        			 tempLine = tempLine.replace("true","false");
	        			 tempHolder = tempHolder+tempLine;
	        		 }
	        		 else
	        		 {
	        			 tempHolder = tempHolder+tempLine;
	        		 }
                
	        		 tempHolder=tempHolder+"\n";
	        }
          }
            input.close();
            FileWriter fw;
			try {
				fw = new FileWriter(new File(statusFile));
				fw.write(tempHolder);
	            fw.close();
			} catch (IOException e) {

				e.printStackTrace();
			
			}
		}
	
	
}
