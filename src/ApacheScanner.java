import java.io.File;

public class ApacheScanner {
	public static void main(String args[]){

		/* Check to see if input file is given in command line arguments */
		if (args.length != 1){
			System.out.println("Wrong number of inputs. Please give exactly one input argument: the name of the Apache"
								+ " log file");
			System.exit(0);
		}
		else{

			/* Create file if in current directory, instantiate extractor object, scan file, and print Apache stats to 
			 * command line or */
			File apacheLog = new File(args[0]);
			if (apacheLog.exists()){
				ApacheDataExtractor dataExtractor = new ApacheDataExtractor(apacheLog);
				dataExtractor.scanFile();
				dataExtractor.outputStatsToCommandLine();
				dataExtractor.outputStatsToFile("stats.txt");
			}

			/* If file not in current directory, let user know and exit program */
			else{
				System.out.println("FILE NOT FOUND.  MAKE SURE IN CORRECT DIRECTORY!");
				System.exit(0);
			}
		}
	}
}