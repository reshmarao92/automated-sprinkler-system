package com.scu.sprinklersystem;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class SprinklerOptions extends JPanel {

	private static final long serialVersionUID = 2081429390523810017L;
	public JPanel panel2,enablePanel;
	Scheduler schedulerObject = new Scheduler();
	static String imageRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\images\\");
	EnableSprinkler enableSprinklerObject = new EnableSprinkler();
	
	//Creating Tabs
	public JComponent displayTabs() {
		
		UIManager.put("TabbedPane.contentOpaque", false);
		//Main Page
		JTabbedPane tabbedPane = new JTabbedPane();
		ImageIcon icon = new ImageIcon(new ImageIcon(imageRoot.concat(
				"favicon.jpg")).getImage().getScaledInstance(10, 10, Image.SCALE_DEFAULT));
        
		//Tab1
		schedulerObject = new Scheduler();
        tabbedPane.addTab("Configure Schedule", icon, schedulerObject.displayScreen(),"Click to configure the schedule");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        //Tab2
        tabbedPane.addTab("Configure Sprinklers", icon, enableSprinklerObject.displayMap(),"Enable the sprinklers");
        enableSprinklerObject.firstRun = true;
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        //Tab3
        tabbedPane.addTab("Temperature Control", icon, new Temperature().displayTempChecker(),"Temperature based controls");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        
        //Tab4
        JButton updateStatus = new JButton("View latest status");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(updateStatus);
        updateStatus.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				JPanel statusPanelInTab = new Status().viewStatus();
				tabbedPane.removeTabAt(3);
				statusPanelInTab.add(buttonPanel);
				tabbedPane.insertTab("View Status", icon, statusPanelInTab,"View the current status of the sprinklers",3);
				tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
				tabbedPane.setSelectedIndex(3);
				tabbedPane.revalidate();
				tabbedPane.repaint();	
		}	
	});
     
        Status statusObj = new Status();
        JPanel statusPanel = statusObj.viewStatus();
        statusPanel.add(buttonPanel);
        tabbedPane.addTab("View Status", icon, statusPanel,"View the current status of the sprinklers");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
        
        //Graph Panel
        List<Double> scores = new ArrayList<>();
        int maxDataPoints = 40;
        double value = 0;
        for (int i = 0; i < maxDataPoints; i++) {
            scores.add(value);
        }
      
        final GraphPanel gp = new GraphPanel(scores);
        JLabel title = new JLabel("Water Consumption Chart");
        title.setForeground(Color.black);
        gp.setOpaque(false);
        gp.add(title);
        
        JButton displayGraph = new JButton("Refresh graph");
        displayGraph.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				
				List<Double> scores = schedulerObject.getChartValues(schedulerObject.getNewContentPane());
				JLabel title = new JLabel("Water Consumption Chart");
				title.setForeground(Color.black);
				final GraphPanel gp1 = new GraphPanel(scores);
				gp1.setOpaque(false);
				gp1.add(displayGraph);
				gp1.add(title);
				tabbedPane.removeTabAt(4);
				tabbedPane.addTab("Display Graph", icon, gp1,"View water consumption.");
				tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);
				tabbedPane.setSelectedIndex(4);
				tabbedPane.revalidate();
				tabbedPane.repaint();	
		}	
	});
        
        gp.add(displayGraph);
        tabbedPane.addTab("Display Graph", icon, gp,"View water consumption.");
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);
             
        //Add the tabbed pane to this panel.
        panel2 = new JPanel(new GridLayout(1,1));
        tabbedPane.setOpaque(false);
        panel2.add(tabbedPane);
        panel2.setOpaque(false);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        return panel2;
	}
	
	protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
}