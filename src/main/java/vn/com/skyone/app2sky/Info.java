package vn.com.skyone.app2sky;

import java.io.IOException;

public class Info extends CopyBase {

	public Info(Integer nodeId) {
		super(nodeId);
	}

	public Info(String[] args) {
		super(args);
	}

	/**
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void sysInfo() {
		System.out.println("################################# INFO #################################");
		System.out.println("# METRO: " + urlMetro);
		System.out.println("# PACS: " + urlPacs);
		System.out.println("# SKYONE: " + urlSkyOne);
		System.out.println("# NODE: " + nodeId);
		System.out.println("# COMPANY: " + companyId);
		System.out.println("# BRANCH: " + branchId);
		System.out.println("# CREATOR: " + createdId);
		System.out.println("# DIR IMAGE PATH: " + dirPath);
		System.out.println("# DIR IMAGE ID: " + dirId);
		System.out.println("########################################################################");
	}

}
