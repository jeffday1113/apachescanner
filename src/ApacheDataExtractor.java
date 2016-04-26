import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ApacheDataExtractor {
	/* Program Strings */
	private static final String dateFormat = "dd/MMM/yyyy:HH:mm:ss Z";
	private static final String monthOutputFormat = "MMM";
	private static final String quoteRegex = "\"";
	private static final String dateRegex = "\\[(.*?)\\]";
	private static final String requestRegex = "\\\"(.*?)\\/";
	private static final String browserRegex = "\\\" \"(.*?)\\\"$";

	/* Extractor fields that represent data to output */
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
		accessFile = logFile;
		earliestRequest = null;
		latestRequest = null;
		getRequestCount = 0;
		putRequestCount = 0;
		postRequestCount = 0;
		headRequestCount = 0;
		unknownRequestCount = 0;
		mozillaRequestCount = 0;
		ieRequestCount = 0;
		otherBrowserRequestCount = 0;
	}
	
	public DateTime getEarliestRequest(){
		return earliestRequest;
	}
	
	public DateTime getLatestRequest(){
		return latestRequest;
	}
	
	public int getGetRequestCount(){
		return getRequestCount;
	}
	
	public int getPutRequestCount(){
		return putRequestCount;
	}
	
	public int getPostRequestCount(){
		return postRequestCount;
	}
	
	public int getHeadRequestCount(){
		return headRequestCount;
	}
	
	public int getUnknownRequestCount(){
		return unknownRequestCount;
	}
	
	public int getMozillaRequestCount(){
		return mozillaRequestCount;
	}
	
	public int getIeRequestCount(){
		return ieRequestCount;
	}
	
	public int getOtherBrowserRequestCount(){
		return otherBrowserRequestCount;
	}
	
	public void setLogFile(File logFile){
		this.accessFile = logFile;
	}

	public void resetStats(){
		earliestRequest = null;
		latestRequest = null;
		getRequestCount = 0;
		putRequestCount = 0;
		postRequestCount = 0;
		headRequestCount = 0;
		unknownRequestCount = 0;
		mozillaRequestCount = 0;
		ieRequestCount = 0;
		otherBrowserRequestCount = 0;
	}

	public void scanFile(){
		Scanner scan = null;
		try {
			scan = new Scanner(accessFile);
		} catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND.  MAKE SURE IN CORRECT DIRECTORY!");
			System.exit(0);
		}

		/* Declare variables to be used */
		String currentLine;
		DateTime currentDate;
		DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

		/* Create pattern objects to find date, request type, and associated browser of each apache access log */
		Pattern dateAndTimePattern = Pattern.compile(dateRegex);
		Pattern requestPattern = Pattern.compile(requestRegex);
		Pattern browserPattern = Pattern.compile(browserRegex);
		Matcher datMatcher, requestMatcher, browserMatcher = null;
		while (scan.hasNextLine()){
			currentLine = scan.nextLine();

			/* Initialize matcher objects */
			datMatcher = dateAndTimePattern.matcher(currentLine);
			requestMatcher = requestPattern.matcher(currentLine);
			browserMatcher = browserPattern.matcher(currentLine);

			/* Find Request type of access log and edit global data */
			extractAndEditRequestStats(currentLine, requestMatcher);

			/* Find browser type of access log if it is a combined format and edit global data */
			if (currentLine.endsWith(quoteRegex))
				extractAndEditBrowserStats(currentLine, browserMatcher);

			/* Extract date of access log, update currentDate object, and see if it is the earliest or latest log seen
			 * 
			 * NOTE: Trivial solution to this is if log files are always in order by date and time then first one is 
			 * earliest request and last one is most recent.  However wanted to make my program handle the case where 
			 * logs are for some reason out of order. */
			String dateAndTime = extractDateAndTime(currentLine, datMatcher);
			if (dateAndTime != null){
				if (earliestRequest == null && latestRequest == null){
					earliestRequest=dtf.parseDateTime(dateAndTime);
					latestRequest=dtf.parseDateTime(dateAndTime);
					continue;
				}
				else{
					currentDate = dtf.parseDateTime(dateAndTime);
					if (currentDate.isBefore(earliestRequest))
						earliestRequest = currentDate;
					if (currentDate.isAfter(latestRequest))
						latestRequest = currentDate;
				}
			}
		}
	}

	public void outputStatsToCommandLine(){
		System.out.println("\nAPACHE ACCESS LOG STATS");
		System.out.println("=======================");
		System.out.println("Earliest Request: " + buildDateOutputString(earliestRequest));
		System.out.println("Latest Request: " + buildDateOutputString(latestRequest));
		System.out.println("Number of Get Requests: " + getRequestCount);
		System.out.println("Number of Put Requests: " + putRequestCount);
		System.out.println("Number of Post Requests: " + postRequestCount);
		System.out.println("Number of Head Requests: " + headRequestCount);
		System.out.println("Number of Unknown Request Type: " + unknownRequestCount);
		System.out.println("\nFor NCSA Combined Format logs\nNumber of Mozilla Based Browser Requests: " + 
				mozillaRequestCount);
		System.out.println("Number of Internet Explorer Based Browser Requests: " + ieRequestCount);
		System.out.println("Number of Other Browser Based Requests: " + otherBrowserRequestCount);
	}

	public void outputStatsToFile(String fileName){
		File statText = new File(fileName);
		FileOutputStream is;
		try {
			is = new FileOutputStream(statText);
			OutputStreamWriter osw = new OutputStreamWriter(is);    
			BufferedWriter w = new BufferedWriter(osw);
			writeLineAndNewLine(w, "APACHE ACCESS LOG STATS");
			writeLineAndNewLine(w, "=======================");
			writeLineAndNewLine(w, "Earliest Request: " + buildDateOutputString(earliestRequest));
			writeLineAndNewLine(w, "Latest Request: " + buildDateOutputString(latestRequest));
			writeLineAndNewLine(w, "Number of Get Requests: " + getRequestCount);
			writeLineAndNewLine(w, "Number of Put Requests: " + putRequestCount);
			writeLineAndNewLine(w, "Number of Post Requests: " + postRequestCount);
			writeLineAndNewLine(w, "Number of Head Requests: " + headRequestCount);
			writeLineAndNewLine(w, "Number of Unknown Request Type: " + unknownRequestCount);
			writeLineAndNewLine(w, "\nFor NCSA Combined Format logs\nNumber of Mozilla Based Browser Requests: " + mozillaRequestCount);
			writeLineAndNewLine(w, "Number of Internet Explorer Based Browser Requests: " + ieRequestCount);
			writeLineAndNewLine(w, "Number of Other Browser Based Requests: " + otherBrowserRequestCount);
			w.close();
		} catch (IOException e) {
			System.out.println("Problem writing stats to file! See stack trace for details");
			e.printStackTrace();
		}
	}

	private void writeLineAndNewLine(BufferedWriter bw, String line) throws IOException{
		bw.write(line);
		bw.newLine();
	}

	private String buildDateOutputString(DateTime date){
		return date.toString(monthOutputFormat) + " " + date.getDayOfMonth()+ ", " + date.getYear() + " at " + 
				date.getHourOfDay() + ":" + date.getMinuteOfHour() + ":"+ date.getSecondOfMinute();
	}

	private void extractAndEditBrowserStats(String line, Matcher m){
		if (m.find()){
			String browser=m.group(1).toLowerCase();
			if (browser.contains("mozilla"))
				mozillaRequestCount++;
			else if (browser.contains("internet explorer"))
				ieRequestCount++;
			else
				otherBrowserRequestCount++;
		}
	}

	private String extractDateAndTime(String line, Matcher m){
		String dateAndTime = null;
		if (m.find()){
			String current_string=m.group(1);
			if (current_string.contains("/"))
				dateAndTime=current_string;
		}
		return dateAndTime;
	}

	private void extractAndEditRequestStats(String line, Matcher m){
		if (m.find()){
			String request=m.group(1);
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
}
