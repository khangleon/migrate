package vn.com.skyone.metro2sky;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

public class CopyCompany extends CopyBase {

	public CopyCompany(Integer nodeId) {
		super(nodeId);
	}

	public CopyCompany(String[] args) {
		super(args);
	}

	public void copyCompany() {
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connSkyOne = getSkyOneConnection();
			log.info("CopyCompany connect SKY ok");
		} catch (SQLException e) {
			log.error("CopyCompany Connect database error", e);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("CopyCompany start: " + (new Date()));
		log.info("CopyCompany from: " + startDate);
		System.out.println("CopyCompany start: " + (new Date()));

		copyCompany(connSkyOne, sysDate);

		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("CopyCompany end in " + (t2 - t1) + " ms");
	}

	public void copyCompany(Connection connSkyOne, Long startDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyPatient start copy: " + startDate);

		String dir = System.getProperty("user.dir");
		Properties prop = new Properties();
		try {
			InputStream fileInput = new FileInputStream(dir + "/conf/info.sky1");
			if (fileInput != null) {
				prop.load(new InputStreamReader(fileInput, Charset.forName("UTF-8")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Long companyId = Lib.toLong(prop.getProperty("COMPANY_ID"), 0);
		String companyCode = prop.getProperty("COMPANY_CODE");
		String companyName = prop.getProperty("COMPANY_NAME");

		Long branchId = Lib.toLong(prop.getProperty("BRANCH_ID"), 0);
		String branchCode = prop.getProperty("BRANCH_CODE");
		String branchName = prop.getProperty("BRANCH_NAME");

		String local = prop.getProperty("LOCALE");
		String localEn = prop.getProperty("LOCALE_FOREIGN");
		String timeZone = prop.getProperty("TIME_ZONE");

		try {
			// copy company
			companyId = copyCompany(connSkyOne, companyId, companyCode, companyName, local, localEn, timeZone);
			// coppy branch
			copyBranch(connSkyOne, branchId, branchCode, branchName, local, localEn, timeZone, companyId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		long t2 = System.currentTimeMillis();
		log.info("copyPatient end in " + (t2 - t1) + " ms");
		System.out.println("CopyCompany end in " + (t2 - t1) + " ms");
	}

	public Long copyCompany(Connection connSkyOne, Long id, String code, String name, String local, String localEn,
			String timeZone) throws SQLException {
		String sqlSelect = "select id from company where id = ? ";
		String sqlInsert = "insert into company(id, code, name, locale, locale_foreign, time_zone) VALUES(?,?,?,?,?,?)";
		String sqlUpdate = "update company set id = ?, code = ?, name = ?, locale = ?, locale_foreign = ?, time_zone = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		Long skyCompanyId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setLong(1, id);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyCompanyId = rsSelect.getLong("id");
		}
		if (skyCompanyId == null) {
			skyCompanyId = id; // getId("Company", connSkyOne);
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyCompanyId);
			stmtInsert.setString(2, code);
			stmtInsert.setString(3, name);
			stmtInsert.setString(4, local);
			stmtInsert.setString(5, localEn);
			stmtInsert.setString(6, timeZone);
			stmtInsert.executeUpdate();
			System.out.println("inserted company.. " + skyCompanyId);
		} else {
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyCompanyId);
			stmtUpdate.setString(2, code);
			stmtUpdate.setString(3, name);
			stmtUpdate.setString(4, local);
			stmtUpdate.setString(5, localEn);
			stmtUpdate.setString(6, timeZone);
			stmtUpdate.setLong(7, skyCompanyId);
			stmtUpdate.executeUpdate();
			System.out.println("updated company.. " + skyCompanyId);
		}

		return skyCompanyId;
	}

	public void copyBranch(Connection connSkyOne, Long id, String code, String name, String local, String localEn,
			String timeZone, Long companyId) throws SQLException {
		String sqlSelect = "select id from branch where id = ? ";
		String sqlInsert = "insert into branch(id, code, name, locale, locale_foreign, time_zone, company_id) VALUES(?,?,?,?,?,?,?)";
		String sqlUpdate = "update branch set id = ?, code = ?, name = ?, locale = ?, locale_foreign = ?, time_zone = ?, company_id = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		Long skyBranchId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setLong(1, id);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyBranchId = rsSelect.getLong("id");
		}
		if (skyBranchId == null) {
			skyBranchId = id; // getId("Branch", connSkyOne);
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyBranchId);
			stmtInsert.setString(2, code);
			stmtInsert.setString(3, name);
			stmtInsert.setString(4, local);
			stmtInsert.setString(5, localEn);
			stmtInsert.setString(6, timeZone);
			stmtInsert.setLong(7, companyId);
			stmtInsert.executeUpdate();
			System.out.println("inserted branch.. " + skyBranchId);
		} else {
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyBranchId);
			stmtUpdate.setString(2, code);
			stmtUpdate.setString(3, name);
			stmtUpdate.setString(4, local);
			stmtUpdate.setString(5, localEn);
			stmtUpdate.setString(6, timeZone);
			stmtUpdate.setLong(7, companyId);
			stmtUpdate.setLong(8, skyBranchId);
			stmtUpdate.executeUpdate();
			System.out.println("updated branch.. " + skyBranchId);
		}
	}

}
