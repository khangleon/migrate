package vn.com.skyone.metro2sky;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AddExport {

    public AddExport() {
        super();
    }

    public static void main(String[] args) {
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

            Info info = new Info(args);
            info.sysInfo();

            if (cl.hasOption("info")) {
                return;
            }

            // export file pdf
            if (!cl.hasOption("nx")) {
                System.out.println("------------------------------- Export file pdf -------------------------------");
                Export exportPdf = new Export(args);
                exportPdf.exportPDF();
            }

            // copy files
            if (!cl.hasOption("nf")) {
                System.out.println("------------------------------- Copy files -------------------------------");
                Export exportPdf = new Export(args);
                exportPdf.copyFiles();
            }

        } catch (ParseException e) {
            System.err.println("metro2skyone1: " + e.getMessage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println("metro2skyone1: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
        System.out.println("Done. " + Lib.elapsedTime(t1, System.currentTimeMillis()));
    }

}