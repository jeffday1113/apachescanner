import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ApacheDataExtractor {
	/*Extractor fields that represent data to output*/
	private static final String dateFormat = "dd/MMM/yyyy:HH:mm:ss Z";
	private static final String monthOutputFormat = "MMM";
	private DateTime earliestRequest;
	private DateTime latestRequest;
	private File accessFile;
	private int getRequestCount;
	private int putRequestCount;
	private int postRequestCount;
	private int headRequestCount;
	private int unknownRequestCount;
	private int mozillaRequestCount;
	private int ieRequestCount;
	private int otherBrowserRequestCount;

	public ApacheDataExtractor(File logFile){
		accessFile=logFile;
		earliestRequest=null;
		latestRequest=null;
		getRequestCount=0;
		putRequestCount=0;
		postRequestCount=0;
		headRequestCount=0;
		unknownRequestCount=0;
		mozillaRequestCount=0;
		ieRequestCount=0;
		otherBrowserRequestCount=0;
	}

	public void scanFile(){
		Scanner scan = null;
		try {
			scan = new Scanner(accessFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String currentLine;
		DateTime currentDate;
		DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);
		
		/*Create pattern objects to find date, request type, and associated
		 * browser of each apache access log*/
		Pattern dateAndTimePattern = Pattern.compile("\\[(.*?)\\]");
		Pattern requestPattern = Pattern.compile("\\\"(.*?)\\/");
		Pattern browserPattern = Pattern.compile("\\\" \"(.*?)\\\"$");
		
		Matcher datMatcher, requestMatcher, browserMatcher = null;
		while (scan.hasNextLine()){
			currentLine = scan.nextLine();
			/*Initialize matcher objects*/
			datMatcher = dateAndTimePattern.matcher(currentLine);
			requestMatcher = requestPattern.matcher(currentLine);
			browserMatcher = browserPattern.matcher(currentLine);
			
			/*Find Request type of access log and edit global data */
			extractAndEditRequestStats(currentLine, requestMatcher);
			
			/*Find browser type of access log if it is a combined format 
			 * and edit global data*/
			if (currentLine.endsWith("\""))
				extractAndEditBrowserStats(currentLine, browserMatcher);
			
			/*Extract date of access log, update currentDate object, and
			 * see if it is the earliest or latest log seen
			 * 
			 * NOTE: Trivial solution to this is if log files are always in 
			 * order by date and time then first one is earliest request and
			 * last one is most recent.  However wanted to make my program 
			 * handle the case where logs are for some reason out of order*/
			String dateAndTime = extractDateAndTime(currentLine, datMatcher);
			if (earliestRequest == null && latestRequest==null){
				earliestRequest=dtf.parseDateTime(dateAndTime);
				latestRequest=dtf.parseDateTime(dateAndTime);
				continue;
			}
			currentDate = dtf.parseDateTime(dateAndTime);
			//currentDate.setDateAndTimeString(dateAndTime);
			if (currentDate.isBefore(earliestRequest))
				earliestRequest = currentDate;
			else if (currentDate.isAfter(latestRequest))
				latestRequest = currentDate;
//			if (isEarliest(currentDate)){
//				earliestRequest.setDateAndTimeString(currentDate);
//			}
//			if (isLatest(currentDate)){
//				latestRequest.setDateAndTimeString(currentDate);
//			}
		}
	}
	
	public void outputStats(){
		System.out.println("\nAPACHE ACCESS LOG STATS");
		System.out.println("=======================");
		System.out.println("Earliest Request: " + buildDateOutputString(earliestRequest));
		System.out.println("Latest Request: " + buildDateOutputString(latestRequest));
		System.out.println("Number of Get Requests: " + getRequestCount);
		System.out.println("Number of Put Requests: " + putRequestCount);
		System.out.println("Number of Post Requests: " + postRequestCount);
		System.out.println("Number of Head Requests: " + headRequestCount);
		System.out.println("Number of Unknown Request Type: " + unknownRequestCount);
		System.out.println("\nFor NCSA Combined Format logs\nNumber of Mozilla Based Browser Requests: " + mozillaRequestCount);
		System.out.println("Number of Internet Explorer Based Browser Requests: " + ieRequestCount);
		System.out.println("Number of Other Browser Based Requests: " + otherBrowserRequestCount);
	}

	private String buildDateOutputString(DateTime date){
		return date.toString(monthOutputFormat) + " " + date.getDayOfMonth()+ ", " + date.getYear() + " at " + 
				date.getHourOfDay() + ":" + date.getMinuteOfHour() + ":"+ date.getSecondOfMinute();
	}

	private void extractAndEditBrowserStats(String line, Matcher m){
		String browser="";
		if (m.find()){
			browser=m.group(1).toLowerCase();
			if (browser.contains("mozilla"))
				mozillaRequestCount++;
			else if (browser.contains("internet explorer"))
				ieRequestCount++;
			else
				otherBrowserRequestCount++;
		}
	}

	private String extractDateAndTime(String line, Matcher m){
		String dateAndTime="";
		if (m.find()){
			String current_string=m.group(1);
			CharSequence dateIdentifiers = "/";
			if (!current_string.contains(dateIdentifiers))
				return "NO DATE IN LOG.  CHECK FOR ERRORS";
			dateAndTime=current_string;
		}
		return dateAndTime;
	}

	private void extractAndEditRequestStats(String line, Matcher m){
		String request="";
		if (m.find()){
			request=m.group(1);
			if (request.contains("GET"))
				getRequestCount++;
			else if (request.contains("POST"))
				postRequestCount++;
			else if (request.contains("PUT"))
				putRequestCount++;
			else if (request.contains("HEAD"))
				headRequestCount++;
			else
				unknownRequestCount++;
		}
		else
			unknownRequestCount++;

	}

//	private boolean isLatest(DateAndTime dat){
//		if (dat.getYear()>latestRequest.getYear())
//			return true;
//		else if (dat.getYear()<latestRequest.getYear())
//			return false;
//		if (dat.getMonthNum()>latestRequest.getMonthNum())
//			return true;
//		else if (dat.getMonthNum()<latestRequest.getMonthNum())
//			return false;
//		if (dat.getDay()>latestRequest.getDay())
//			return true;
//		else if (dat.getDay()<latestRequest.getDay())
//			return false;
//		if (dat.getHourInt()>latestRequest.getHourInt())
//			return true;
//		else if (dat.getHourInt()<latestRequest.getHourInt())
//			return false;
//		if (dat.getMinuteInt()>latestRequest.getMinuteInt())
//			return true;
//		else if (dat.getMinuteInt()<latestRequest.getMinuteInt())
//			return false;
//		if (dat.getSecondInt()>latestRequest.getSecondInt())
//			return true;
//		else if (dat.getSecondInt()<latestRequest.getSecondInt())
//			return false;
//		return false;
//	}
//
//	private boolean isEarliest(DateAndTime dat){
//		if (dat.getYear()<earliestRequest.getYear())
//			return true;
//		else if (dat.getYear()>earliestRequest.getYear())
//			return false;
//		if (dat.getMonthNum()<earliestRequest.getMonthNum())
//			return true;
//		else if (dat.getMonthNum()>earliestRequest.getMonthNum())
//			return false;
//		if (dat.getDay()<earliestRequest.getDay())
//			return true;
//		else if (dat.getDay()>earliestRequest.getDay())
//			return false;
//		if (dat.getHourInt()<earliestRequest.getHourInt())
//			return true;
//		else if (dat.getHourInt()>earliestRequest.getHourInt())
//			return false;
//		if (dat.getMinuteInt()<earliestRequest.getMinuteInt())
//			return true;
//		else if (dat.getMinuteInt()>earliestRequest.getMinuteInt())
//			return false;
//		if (dat.getSecondInt()<earliestRequest.getSecondInt())
//			return true;
//		else if (dat.getSecondInt()>earliestRequest.getSecondInt())
//			return false;
//		return false;
//	}

}