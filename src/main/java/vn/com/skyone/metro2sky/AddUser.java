package vn.com.skyone.metro2sky;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AddUser {

	public AddUser() {
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

			if (cl.hasOption("info")) {
				return;
			}

			// Update user
			System.out.println("########### Start create user ###########");
			CopyUsers users = new CopyUsers(args);
			if (cl.hasOption("account") && cl.hasOption("name")) {
				users.newUser(cl.getOptionValue("name"), cl.getOptionValue("account"));
			} else {
				users.newUser("Administrator", "admin");
			}

		} catch (ParseException e) {
			System.err.println("metro2skyone: " + e.getMessage());
			System.exit(2);
		} catch (Exception e) {
			System.err.println("metro2skyone: " + e.getMessage());
			e.printStackTrace();
			System.exit(2);
		}
		System.out.println("Done. " + Lib.elapsedTime(t1, System.currentTimeMillis()));
	}

}
