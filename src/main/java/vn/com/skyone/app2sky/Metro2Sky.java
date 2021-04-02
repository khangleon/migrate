package vn.com.skyone.app2sky;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Metro2Sky {

	public Metro2Sky() {
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
			System.out.println("########### Start copy doctor ###########");
			CopyDoctor copy = new CopyDoctor(args);
			//copy.copyDoctor();

			// Copy Nurse to Partner
			System.out.println("########### Start copy nurse ###########");
			CopyNurse nurse = new CopyNurse(args);
			nurse.copyNurse();

			// Copy Patients to Partner
			System.out.println("########### Start copy patients ###########");
			CopyPatients patients = new CopyPatients(args);
			patients.copyPatients();

			// Update user
			System.out.println("########### Start copy users ###########");
			CopyUsers users = new CopyUsers(args);
			users.copyUsers();

			// Update item
			System.out.println("########### Start copy item ###########");
			CopyItem copyItem = new CopyItem(args);
			copyItem.copyItem();
			copyItem.copyItemSub();

			// Update user
			System.out.println("########### Start copy result ###########");
			CopyResult copyResult = new CopyResult(args);
			//copyResult.copyResult();

			// Update CopyImagingLable
			System.out.println("########### Start copy imaging lable ###########");
			CopyImagingLable copyImagingLable = new CopyImagingLable(args);
			// copyImagingLable.copyImagingLabel();

			// Update Indication
			System.out.println("########### Start copy item ###########");
			CopyIndication copyIndication = new CopyIndication(args);
			copyIndication.copyIndication();

			// Update imaging sub
			System.out.println("########### Start copy item ###########");
			CopyImagingSub copyImagingSub = new CopyImagingSub(args);
			copyImagingSub.copyImagingSub();

			// Update imaging image
			System.out.println("########### Start copy imaging image ###########");
			CopyImagingImage copyImagingImage = new CopyImagingImage(args);
			copyImagingImage.copyImagingImage();

			// Update imaging template
			System.out.println("########### Start copy imaging template ###########");
			CopyImgingTemplate copyImgingTemplate = new CopyImgingTemplate(args);
			copyImgingTemplate.copyImagingTemplate();

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
