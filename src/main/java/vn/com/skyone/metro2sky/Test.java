package vn.com.skyone.metro2sky;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class Test {

    public Test() {
        super();
    }

    public static void main2(String[] args) throws IOException, ParseException {
        long t1 = System.currentTimeMillis();

        System.out.println("Done. " + Lib.elapsedTime(t1, System.currentTimeMillis()));

    }

    public static void main(String[] args) throws IOException, ParseException {
        long t1 = System.currentTimeMillis();
        try {
            Options opts = new Options();
            CopyBase.addConnectOption(opts);
            opts.addOption("h", "help", false, "Try `batchlab --help' for more information.");

            CommandLineParser parser = new DefaultParser();
            CommandLine cl = parser.parse(opts, args);

            if (cl.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(" ", opts);
                System.exit(0);
            }

            // TEST
            System.out.println("########### Start copy test ###########");
            Common copyItem = new Common(args);
           // copyItem.exportImagingResult(8858110212020L);

        } catch (Exception e) {
            System.err.println("metro2skyone: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
        System.out.println("Done. " + Lib.elapsedTime(t1, System.currentTimeMillis()));

    }

    public static int getWorkingDaysBetweenTwoDates(Date startDate, Date startTime, Date endDate, Date endTime) {
        Calendar startCal;
        Calendar endCal;
        startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int workDays = 0;

        // Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (!isOffDay(startCal)) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

        return workDays;
    }

    public static boolean isOffDay(Calendar date) {
        if (date != null) {
            return Calendar.SATURDAY == date.get(Calendar.DAY_OF_WEEK)
                    || Calendar.SUNDAY == date.get(Calendar.DAY_OF_WEEK);
        }
        return true;
    }

    public static boolean isOffHour(Calendar date) {
        int[] hours = new int[] { 8, 9, 10, 11, 12 };
        if (date != null) {
            return Calendar.SATURDAY == date.get(Calendar.DAY_OF_WEEK)
                    || Calendar.SUNDAY == date.get(Calendar.DAY_OF_WEEK);
        }
        return true;
    }

    public static String sentenceCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length()).toLowerCase();
    }

}
