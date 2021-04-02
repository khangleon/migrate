package vn.com.skyone.app2sky;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AddPartners {

	public AddPartners() {
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

			// Copy Doctor to Partner
			System.out.println("########### Start copy doctor range ###########");
			CopyDoctor copy = new CopyDoctor(args);
			copy.copyDoctorRange();

			// Copy Nurse to Partner
			System.out.println("########### Start copy nurse range ###########");
			CopyNurse nurse = new CopyNurse(args);
			//nurse.copyNurseRange();

			// Copy Patients to Partner
			System.out.println("########### Start copy patients range ###########");
			CopyPatients patients = new CopyPatients(args);
			//patients.copyPatientsRange();

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
