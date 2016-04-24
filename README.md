# Apache Log Scanner

Submitted a runnable jar (for simple command line running of the program, compiled
with java version 1.7.0_45) and an archived jar to allow viewing of the
implementation details of each class.  In order to run the file from the jar,
simply type in the following in the command line of a unix machine:

```shell
java -jar apache_scanner_runnable.jar filename
```

where filename represents the name of the apache logs that the user wishes to
search.  If for any reason the jar does not work, simply unarchive the non runnable
jar, change into the directory where the Java class files are stores, and run the
following in the command line of a Unix machine:

java ApacheScanner filename

where filename represents the name of the apache logs the user wishes to search.
The specified file must be in the same directory as the program files.  If it is
not, the program will send you an error message and quit:

java -jar apache_scanner_runnable.jar badfilename

Output:

FILE NOT FOUND.  MAKE SURE IN CORRECT DIRECTORY!

If no file is specified, the program will print out an error message as well:

java -jar apache_scanner_runnable.jar

Output:

No input apache access log file given.  Please specify input file!

If a correct file name is given and the file contains apache access logs, then the
program will output the appropriate statistics to the command line.

java -jar apache_scanner_runnable.jar access_log.txt

Output:

APACHE ACCESS LOG STATS

Earliest Request: Jul 2, 1996 at 11:51:31
Latest Request: Dec 4, 1996 at 11:56:31
Number of Get Requests: 11496
Number of Put Requests: 0
Number of Post Requests: 194
Number of Head Requests: 14
Number of Unknown Request Type: 5

For NCSA Combined Format logs
Number of Mozilla Based Browser Requests: 9751
Number of Internet Explorer Based Browser Requests: 72
Number of Other Browser Based Requests: 1886
