package com.scu.sprinklersystem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCheck {
	
	//Get Current Day
	public String getCurrentDay(){
		Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String currentDayOfWeek = "";
        switch(dayOfWeek){
        	case 1:
        		currentDayOfWeek = "Sunday";
        		break;
        	case 2:
        		currentDayOfWeek = "Monday";
        		break;
        	case 3:
        		currentDayOfWeek = "Tuesday";
        		break;
        	case 4:
        		currentDayOfWeek = "Wednesday";
        		break;
        	case 5:
        		currentDayOfWeek = "Thursday";
        		break;
        	case 6:
        		currentDayOfWeek = "Friday";
        		break;
        	case 7:
        		currentDayOfWeek = "Saturday";
        		break;
        	default:
        		currentDayOfWeek = "Sunday";
        		break;
        }
        
        return currentDayOfWeek;
	}
	
	//Get Current Hour
	public int getCurrentHour(){
		return Integer.parseInt(new SimpleDateFormat("HH:mm:ss").format(new Date()).split(":")[0]);
	}

}
