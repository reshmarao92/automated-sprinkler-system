package com.scu.sprinklersystem;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
 
public class ScheduleTable extends JPanel {
    
	//Data Members
	static String fileRoot =  new File("").getAbsolutePath().concat("\\src\\com\\scu\\sprinklersystem\\data\\");
	static String statFile = fileRoot.concat("Status.csv");
	private static final long serialVersionUID = 1L;
	private boolean DEBUG = false;
    private JTable table = new JTable(new MyTableModel());
    
    public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

    public ScheduleTable() {
        super(new GridLayout(1,0));
 
        
        table.setPreferredScrollableViewportSize(new Dimension(600, 115));
        table.setFillsViewportHeight(true);
 
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
 
        //Set up column sizes.
        initColumnSizes(table);
        
        //Creating values for Combo Box
        String[] time = {"01:00","02:00","03:00" ,"04:00" ,"05:00" ,"06:00" ,"07:00" ,"08:00" ,"09:00" ,"10:00" 
    			,"11:00" ,"12:00" ,"13:00" ,"14:00" ,"15:00" ,"16:00" ,"17:00" ,"18:00" ,"19:00" ,"20:00" 
    			,"21:00" ,"22:00" ,"23:00" ,"24:00"};
    	String[] waterAmount = {"1L/s","2L/s","3L/s","4L/s","6L/s","7L/s","8L/s","9L/s","10L/s"};
    
    	setUpColumn(table, table.getColumnModel().getColumn(1),time);
    	setUpColumn(table, table.getColumnModel().getColumn(2),time);   
        setUpColumn(table, table.getColumnModel().getColumn(3),waterAmount);
 
        //Add the scroll pane to this panel.
        add(scrollPane);
    }
 
  
    private void initColumnSizes(JTable table) {
        MyTableModel model = (MyTableModel)table.getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();
 
        for (int i = 0; i < 4; i++) {
            column = table.getColumnModel().getColumn(i);
 
            comp = headerRenderer.getTableCellRendererComponent(
                                 null, column.getHeaderValue(),
                                 false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;
 
            comp = table.getDefaultRenderer(model.getColumnClass(i)).
                             getTableCellRendererComponent(
                                 table, longValues[i],
                                 false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;
 
            if (DEBUG) {
                System.out.println("Initializing width of column "
                                   + i + ". "
                                   + "headerWidth = " + headerWidth
                                   + "; cellWidth = " + cellWidth);
            }
 
            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }
 
    public void setUpColumn(JTable table,
                                 TableColumn aColumn, String[] data) {
        
    	
    	//Create combo boxes with the data.
    	
		@SuppressWarnings("unchecked")
		JComboBox aMenu = new JComboBox(data);
		
		aColumn.setCellEditor(new DefaultCellEditor(aMenu));
 
        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click to select a value!");
        aColumn.setCellRenderer(renderer);
    }
 
    class MyTableModel extends AbstractTableModel {
        
		private static final long serialVersionUID = -6771444761964039904L;

		private String[] columnNames = {"Days",
                                        "Start Time",
                                        "End Time",
                                        "Amount of water "};
 	
        private Object[][] data = getData();
                
        public final Object[] longValues = {"Saturday", "03:00","4:00","5L/s"};
 
        public int getColumnCount() {
            return columnNames.length;
        }
 
        public int getRowCount() {
            return data.length;
        }
        
        //Reading the Schedule File
        public Object[][] getData(){
        	 File file = new File(fileRoot.concat("Schedule.txt"));
             Scanner in = null;
			try {
				in = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        	ArrayList<String> fileData = new ArrayList<String>();
        	
        	while(in.hasNextLine()){
        		String[] tempData = (in.nextLine().split(","));
        		for(String s : tempData){
        			if(s.equalsIgnoreCase(""))
        			{
        				
        			}
        			else
        			{
        				fileData.add(s);
        			}
        		}
        	}
        	
        	//Populating Table from the file
        	Object[][] dataObject = {
        	        {fileData.get(0),fileData.get(1),fileData.get(2),fileData.get(3)},
        	        {fileData.get(4),fileData.get(5),fileData.get(6),fileData.get(7)},
        	        {fileData.get(8),fileData.get(9),fileData.get(10),fileData.get(11)},
        	        {fileData.get(12),fileData.get(13),fileData.get(14),fileData.get(15)},
        	        {fileData.get(16),fileData.get(17),fileData.get(18),fileData.get(19)},
        	        {fileData.get(20),fileData.get(21),fileData.get(22),fileData.get(23)},
        	        {"Saturday", "03:00","04:00","5L/s"}
        	        };
        	
        	
        	return dataObject;
        }
 
        public String getColumnName(int col) {
            return columnNames[col];
        }
 
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
 
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
       
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 1) {
                return false;
            } else {
                return true;
            }
        }
 
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }
 
            data[row][col] = value;
            fireTableCellUpdated(row, col);
 
            if (DEBUG) {
                printDebugData();
            }
        }
 
        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
 
            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }
    
}