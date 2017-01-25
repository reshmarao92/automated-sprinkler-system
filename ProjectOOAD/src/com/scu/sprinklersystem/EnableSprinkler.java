package com.scu.sprinklersystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EnableSprinkler extends MouseAdapter implements  MouseListener{
	
	//Data Members
	private JPanel enablePanel,buttonPanel;
	private JButton saveButton;
	//Relative file naming
	
	static String fileRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\data\\");
	static String imageRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\images\\");
	public Boolean firstRun = false;
	private JButton updateButton;
	BufferedImage gardenImg = null;
	
	//Checkboxes
	public JCheckBox northEnable, southEnable, eastEnable, westEnable;
	
	//Status File
	private static final String FILENAME = fileRoot.concat("Status.csv");
	
	//Sprinkler Array lists
	public ArrayList<Sprinkler> sprinklerNorth;
	public ArrayList<Sprinkler> sprinklerSouth;
	public ArrayList<Sprinkler> sprinklerEast;
	public ArrayList<Sprinkler> sprinklerWest;
	public ArrayList<Sprinkler> allSprinklers;
	
	public JPanel displayMap() {
				
		try {

				gardenImg = ImageIO.read(new File(imageRoot.concat("Garden.jpg")));
						
		    } 
		catch (Exception ex) {
		      System.out.println(ex);
		    }
		
		enablePanel = new JPanel(new BorderLayout()) {

			private static final long serialVersionUID = -273275818555372268L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(gardenImg, 0, 0, getWidth(), getHeight(), this);
		    }
		};
				
		addSprinklersToPanel();
		
		buttonPanel = new JPanel(new FlowLayout());
		
		saveButton = new JButton("Save Status");
		updateButton = new JButton("Update Status");
		
		//Action Listener  for Update Button
		updateButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae){
				Scanner input = null;
				try {
					input = new Scanner(new File(FILENAME));
				}
				catch (Exception e) {
		            e.printStackTrace();
		        }
				ArrayList<String> sprinklerID = new ArrayList<String>();
				ArrayList<String> sprinklerStat = new ArrayList<String>();
				
				while(input.hasNextLine()){
					
		            String[] tempData = input.nextLine().split(",");
		            sprinklerID.add(tempData[0]);
		            sprinklerStat.add(tempData[1]);
		           
		           
				}
				
				input.close();
				
				Scanner inputForStatus = null;
				try {
					inputForStatus = new Scanner(new File(FILENAME));
				}
				catch (Exception e) {
		            e.printStackTrace();
		        }
				
				for(Sprinkler s : allSprinklers)
        		{
					if(inputForStatus.hasNextLine()){
						String tempLine = inputForStatus.nextLine();
						
						if(tempLine.contains("true")){
							s.setStatus(true);
							s.getSprinkler().setForeground(Color.blue);
						}
						else{
							s.setStatus(false);
							s.getSprinkler().setForeground(Color.red);
						}
					for(int i=0; i<sprinklerID.size();i++){
						if(s.getSprinklerId()==sprinklerID.get(i))
						{
							if(sprinklerStat.get(i).equalsIgnoreCase("true")){
								s.setStatus(true);
								s.getSprinkler().setForeground(Color.blue);
							}
							else
							{
								s.setStatus(false);
								s.getSprinkler().setForeground(Color.red);
							}
						}
        			}
				  }
        		}
				
				inputForStatus.close();
				
				
			}
		});
		
		//Action Listener for Save Button
		saveButton.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent ae){
            	
            	for(Sprinkler s : allSprinklers)
        		{
        			if(s.getSprinkler().getForeground().equals(Color.red))
        			{
        				s.setStatus(false);
        			}
        			else
        			{
        				s.setStatus(true);
        			}
        		}
            	
            	try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {
	            	for(Sprinkler s : allSprinklers)
	            	{
	            		
	            		bw.write((s.getSprinklerId() + "," + s.getStatus() +"\n"));
	            	}
	            }
            	
            	catch (Exception ex)
            	{
            		ex.printStackTrace();
            	}
            	
            }
            
            
		});
		
		//Adding the buttons to the panel
		buttonPanel.add(saveButton);
		buttonPanel.add(updateButton);
		buttonPanel.setOpaque(false);
			
		enablePanel.add(buttonPanel);
		return enablePanel;
	}
	
	//Adding Sprinklers to the Panel
	public void addSprinklersToPanel()
	{
		sprinklerNorth = new ArrayList<Sprinkler>();
		sprinklerSouth = new ArrayList<Sprinkler>();
		sprinklerEast = new ArrayList<Sprinkler>();
		sprinklerWest = new ArrayList<Sprinkler>();
		allSprinklers = new ArrayList<Sprinkler>();
		ArrayList<JLabel> sprinklerNorthLabels = new ArrayList<JLabel>();
		
		for(int i = 1;i<4;i++)
		{
			Sprinkler sprinklerObjNorth = new Sprinkler();
			sprinklerObjNorth.setLocation("North");
			sprinklerObjNorth.setSprinklerId("N"+i);
			sprinklerObjNorth.setSprinkler(sprinklerObjNorth.createSprinkler());
			sprinklerNorthLabels.add(sprinklerObjNorth.getSprinkler());
			sprinklerNorth.add(sprinklerObjNorth);
			allSprinklers.add(sprinklerObjNorth);
		}
		
		JPanel northPanel = new JPanel(new GridLayout(1,4));
		northPanel.setOpaque(false);
		
		northEnable = new JCheckBox("Enable/Disable");
		northEnable.setOpaque(false);
		northEnable.setSelected(false);
		northPanel.add(northEnable);
		
		for(JLabel jl : sprinklerNorthLabels)
		{
			northPanel.add(jl);
		}
		enablePanel.add(northPanel, BorderLayout.NORTH);
			
		ArrayList<JLabel> sprinklerEastLabels = new ArrayList<JLabel>();
		
		for(int i = 1;i<4;i++)
		{
			Sprinkler sprinklerObjEast = new Sprinkler();
			sprinklerObjEast.setLocation("East");
			sprinklerObjEast.setSprinklerId("E"+i);
			sprinklerObjEast.setSprinkler(sprinklerObjEast.createSprinkler());
			sprinklerEastLabels.add(sprinklerObjEast.getSprinkler());
			sprinklerEast.add(sprinklerObjEast);
			allSprinklers.add(sprinklerObjEast);
		}

		JPanel eastPanel = new JPanel(new GridLayout(4,1));
		eastPanel.setOpaque(false);
		
		eastEnable = new JCheckBox("Enable/Disable");
		eastEnable.setOpaque(false);
		eastEnable.setSelected(false);
		eastPanel.add(eastEnable);
		
		for(JLabel jl : sprinklerEastLabels)
		{
			eastPanel.add(jl);
		}
		enablePanel.add(eastPanel, BorderLayout.EAST);
		
		ArrayList<JLabel> sprinklerSouthLabels = new ArrayList<JLabel>();
		
		for(int i = 1;i<4;i++)
		{
			Sprinkler sprinklerObjSouth = new Sprinkler();
			sprinklerObjSouth.setLocation("South");
			sprinklerObjSouth.setSprinklerId("S"+i);
			sprinklerObjSouth.setSprinkler(sprinklerObjSouth.createSprinkler());
			sprinklerSouthLabels.add(sprinklerObjSouth.getSprinkler());
			sprinklerSouth.add(sprinklerObjSouth);
			allSprinklers.add(sprinklerObjSouth);
		}
		
		
		JPanel southPanel = new JPanel(new GridLayout(1,4));
		southPanel.setOpaque(false);
		//Using Checkbox to enable a group of Sprinklers
		southEnable = new JCheckBox("Enable/Disable");
		southEnable.setOpaque(false);
		southEnable.setSelected(false);
		southPanel.add(southEnable);
		
		for(JLabel jl : sprinklerSouthLabels)
		{
			southPanel.add(jl);
		}
		enablePanel.add(southPanel, BorderLayout.SOUTH);
		
		ArrayList<JLabel> sprinklerWestLabels = new ArrayList<JLabel>();
		
		for(int i = 1;i<4;i++)
		{
			Sprinkler sprinklerObjWest = new Sprinkler();
			sprinklerObjWest.setLocation("West");
			sprinklerObjWest.setSprinklerId("W"+i);
			sprinklerObjWest.setSprinkler(sprinklerObjWest.createSprinkler());
			sprinklerWestLabels.add(sprinklerObjWest.getSprinkler());
			sprinklerEast.add(sprinklerObjWest);
			allSprinklers.add(sprinklerObjWest);
		}
		
		JPanel westPanel = new JPanel(new GridLayout(4,1));
		westPanel.setOpaque(false);
		//Using Checkbox to enable a group of Sprinklers
		westEnable = new JCheckBox("Enable/Disable");
		westEnable.setOpaque(false);
		westEnable.setSelected(false);
		westPanel.add(westEnable);
		
		for(JLabel jl : sprinklerWestLabels)
		{
			westPanel.add(jl);
		}
		enablePanel.add(westPanel, BorderLayout.WEST);
		
		
		for(JLabel jl : sprinklerNorthLabels)
		{
			jl.addMouseListener(new EnableSprinkler()
		      {
		    	  public void mouseClicked(MouseEvent e)
		    	  {
		    		  if(jl.getForeground() == Color.blue)
		    		  {
		    			  jl.setForeground(Color.red);
		    		  }
		    		  else
		    		  {
		    			  jl.setForeground(Color.blue);
		    		  }
		    	  }
		      });
		}
		
		northEnable.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {
	        	 for(JLabel jl : sprinklerNorthLabels){
	        	 if (northEnable.isSelected()) {
	        		 
	        		 jl.setForeground(Color.blue);
	        		 
	        		} else {
	        		 
	        			jl.setForeground(Color.red);
	        		 
	        		}
	        	 }
	         }
		});
				
		for(JLabel jl : sprinklerSouthLabels)
		{
			jl.addMouseListener(new EnableSprinkler()
		      {
		    	  public void mouseClicked(MouseEvent e)
		    	  {
		    		  if(jl.getForeground() == Color.blue)
		    		  {
		    			  jl.setForeground(Color.red);
		    		  }
		    		  else
		    		  {
		    			  jl.setForeground(Color.blue);
		    		  }
		    	  }
		      });
		}
		
		southEnable.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {
	        	 for(JLabel jl : sprinklerSouthLabels){
	        	 if (southEnable.isSelected()) {
	        		 
	        		 jl.setForeground(Color.blue);
	        		 
	        		} else {
	        		 
	        			jl.setForeground(Color.red);
	        		 
	        		}
	        	 }
	         }
		});
		
		
		for(JLabel jl : sprinklerEastLabels)
		{
			jl.addMouseListener(new EnableSprinkler()
		      {
		    	  public void mouseClicked(MouseEvent e)
		    	  {
		    		  if(jl.getForeground() == Color.blue)
		    		  {
		    			  jl.setForeground(Color.red);
		    		  }
		    		  else
		    		  {
		    			  jl.setForeground(Color.blue);
		    		  }
		    	  }
		      });
		}
		
		eastEnable.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {
	        	 for(JLabel jl : sprinklerEastLabels){
	        	 if (eastEnable.isSelected()) {
	        		 
	        		 jl.setForeground(Color.blue);
	        		 
	        		} else {
	        		 
	        			jl.setForeground(Color.red);
	        		 
	        		}
	        	 }
	         }
		});
		
		
		for(JLabel jl : sprinklerWestLabels)
		{
			jl.addMouseListener(new EnableSprinkler()
		      {
		    	  public void mouseClicked(MouseEvent e)
		    	  {
		    		  if(jl.getForeground() == Color.blue)
		    		  {
		    			  jl.setForeground(Color.red);
		    		  }
		    		  else
		    		  {
		    			  jl.setForeground(Color.blue);
		    		  }
		    	  }
		      });
		}
		
		westEnable.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {
	        	 for(JLabel jl : sprinklerWestLabels){
	        	 if (westEnable.isSelected()) {
	        		 
	        		 jl.setForeground(Color.blue);
	        		 
	        		} else {
	        		 
	        			jl.setForeground(Color.red);
	        		 
	        		}
	        	 }
	         }
		});
		
		
		 
	        String currentDayOfWeek = new DateCheck().getCurrentDay();
	        int currentHour = new DateCheck().getCurrentHour();
	        
			File scheduleFile = new File(fileRoot.concat("Schedule.txt")); 
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
	            	for(String s : tempDataHolder)
	            	{
	            		scheduleLine=scheduleLine + " " + s;
	            	}
	            	
	            }
	            else
	            {
	            	
	            }
	        }
	        
	        int startHour = Integer.parseInt(scheduleLine.split(" ")[2].split(":")[0]);
	        int endHour = Integer.parseInt(scheduleLine.split(" ")[3].split(":")[0]);
	        
	        
	        if((currentHour >= startHour) && (currentHour < endHour))
	        {
	        	for(Sprinkler s : allSprinklers)
	        	{
	        		s.setStatus(true);
	        		s.getSprinkler().setForeground(Color.blue);
	        	}
	        }
	        else
	        {
	        	for(Sprinkler s : allSprinklers)
	        	{
	        		s.setStatus(false);
	        		s.getSprinkler().setForeground(Color.red);
	        	}
	        }
	        

	        	
	        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {
	        		for(Sprinkler s : allSprinklers)
	        		{
	        			
	        			bw.write((s.getSprinklerId() + "," + s.getStatus() +"\n"));
	        		}
	        	}
        	
	        	catch (Exception ex)
	        	{
	        		ex.printStackTrace();
	        	}
	       
	        
	}
	
}
