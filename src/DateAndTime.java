
public class DateAndTime {
	/*Class to conveniently store and express data regarding date and time of 
	 * access logs.  Instead of having to constantly call substring with the 
	 * associated indices, can simply call getYear or getDay etc.*/
	
	private String myDateAndTime;

	public DateAndTime(String dat){
		myDateAndTime=dat;
	}
	
	public void setDateAndTimeString(DateAndTime dat){
		myDateAndTime=dat.getDateAndTimeString();
	}
	
	public void setDateAndTimeString(String str){
		myDateAndTime=str;
	}
	
	public String getDateAndTimeString(){
		return myDateAndTime;
	}

	public int getYear(){
		return Integer.parseInt(myDateAndTime.substring(7,11));
	}

	public int getMonthNum(){
		return convertMonthNameToNum(myDateAndTime.substring(3,6));
	}
	
	public String getMonth(){
		return myDateAndTime.substring(3,6);
	}
	
	public int getDay(){
		return Integer.parseInt(myDateAndTime.substring(0,2));
	}
	
	public int getHourInt(){
		return Integer.parseInt(myDateAndTime.substring(12,14));
	}
	
	public int getMinuteInt(){
		return Integer.parseInt(myDateAndTime.substring(15,17));
	}
	
	public int getSecondInt(){
		return Integer.parseInt(myDateAndTime.substring(18,20));
	}
	
	public String getHourString(){
		return myDateAndTime.substring(12,14);
	}
	
	public String getMinuteString(){
		return myDateAndTime.substring(15,17);
	}
	
	public String getSecondString(){
		return myDateAndTime.substring(18,20);
	}

	private int convertMonthNameToNum(String month){
		String lowerCaseMonth = month.toLowerCase();
		switch (lowerCaseMonth) {
		case "jan":  
			return (int) 1;
		case "feb":  
			return (int) 2;
		case "mar":  
			return (int) 3;
		case "apr":  
			return (int) 4;
		case "may":  
			return (int) 5;
		case "jun":  
			return (int) 6;
		case "jul":  
			return (int) 7;
		case "aug":  
			return (int) 8;
		case "sep":  
			return (int) 9;
		case "oct":  
			return (int) 10;
		case "nov":  
			return (int) 11;
		case "dec":  
			return (int) 12;

		default:
			return (int) -1;
		}

	}	
}