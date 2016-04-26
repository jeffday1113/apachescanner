import java.io.File;

public class ApacheScanner {
	public static void main(String args[]){
		
		/* Check to see if input file given on command line */
		if (args.length == 0){
			System.out.println("No input apache access log file given.  Please specify input file!");
			System.exit(0);
		}
		
		/* Create file if in current directory, instantiate extractor object, scan file, 
		  and print Apache stats to command line*/
		File apacheLog = new File(args[0]);
		if (apacheLog.exists()){
			ApacheDataExtractor dataExtractor = new ApacheDataExtractor(apacheLog);
			dataExtractor.scanFile();
			dataExtractor.outputStatsToFile("jeff.txt");
		}
		
		/* If file not in current directory, let user know and exit program */
		else{
			System.out.println("FILE NOT FOUND.  MAKE SURE IN CORRECT DIRECTORY!");
			System.exit(0);
		}
	}
}