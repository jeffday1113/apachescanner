import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;



public class Test {

	public static void main(String[] args){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MMM/yyyy:HH:mm:ss Z");

		DateTime date1 = dtf.parseDateTime("12/Jul/2012:12:20:54 -0400");
		DateTime date2 = dtf.parseDateTime("12/Jul/2012:12:20:54 -0200");
		System.out.println(date1.isAfter(date2));

	}

}
