package com.scu.sprinklersystem;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Status {
	
	//Data Members
	static String fileRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\data\\");

	private File statusFile = new File(fileRoot.concat("Status.csv"));
	private File scheduleFile = new File(fileRoot.concat("Schedule.txt"));
	public JPanel statusPanel,viewPanel,schedulePanel;
	public JLabel statusLabel,scheduleLabel;
	public String fileData;
	
public JPanel viewStatus() {
		
		//Read the Status File
		Scanner input = null;
		try {
			input = new Scanner(statusFile);
		}
		catch (FileNotFoundException e) {
            e.printStackTrace();
        }

		statusLabel = new JLabel(fileData);
		viewPanel = new JPanel();
        while(input.hasNextLine()){
            String[] tempData = input.nextLine().split(",");
            ArrayList<String> sprinklerNames = new ArrayList<String>();
            ArrayList<String> sprinklerStatus = new ArrayList<String>();
            for(int i=0;i<tempData.length;){                         
            	sprinklerNames.add(tempData[i]);
            	i+=2;
            }
            //Update the Sprinklers
            for(int i=1;i<tempData.length;){
            	if(tempData[i].equalsIgnoreCase("true"))
            	{
            		sprinklerStatus.add("active");
            	}
            	else
            	{
            		sprinklerStatus.add("inactive");
            	}
            	
            	i+=2;
            }
            
            for(int i=0;i<sprinklerNames.size();i++){
            	
            	statusLabel = new JLabel("Status of the sprinkler " + sprinklerNames.get(i) + " is " + sprinklerStatus.get(i) + ". \n");
            	statusLabel.setFont(statusLabel.getFont().deriveFont(25.0f));
                viewPanel.add(statusLabel);
            }
        }
        
        statusPanel = new JPanel(new GridLayout(1,2)); 
               
        TitledBorder statusTitle = BorderFactory.createTitledBorder("Sprinkler Status");
        statusTitle.setTitleJustification(TitledBorder.CENTER);
        TitledBorder scheduleTitle = BorderFactory.createTitledBorder("Current Schedule");
        scheduleTitle.setTitleJustification(TitledBorder.CENTER);
                    
        viewPanel.setBorder(statusTitle);
        
        schedulePanel = new JPanel();
        schedulePanel.setBorder(scheduleTitle);
        String currentDayOfWeek = new DateCheck().getCurrentDay();
        
        //Read the Schedule File
        Scanner in = null;
        try {
            in = new Scanner(scheduleFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String scheduleLine = "";
        
        while(in.hasNextLine()){
        	String[] tempDataHolder = in.nextLine().split(",");
            if(currentDayOfWeek.equalsIgnoreCase(tempDataHolder[0]))
            {	
            	scheduleLine = tempDataHolder[1]+" to "+tempDataHolder[2];
            }
            else
            {}
        }
        
        //Display Current Day Schedule
        scheduleLabel = new JLabel("Schedule for "+ currentDayOfWeek +" is : " + scheduleLine); 
        scheduleLabel.setFont(statusLabel.getFont().deriveFont(25.0f));
        schedulePanel.add(scheduleLabel);
        statusPanel.add(viewPanel);
        statusPanel.add(schedulePanel);
        
        return statusPanel;
	}

}