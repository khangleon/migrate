package vn.com.skyone.app2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class CopyImagingSub extends Common {

	public CopyImagingSub(Integer nodeId) {
		super(nodeId);
	}

	public CopyImagingSub(String[] args) {
		super(args);
	}

	/**
	 * 
	 * @param path
	 */
	public void copyImagingSub() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("CopyImagingSub connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("copyImagingSub connect SKY ok");
		} catch (SQLException e) {
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("copyImagingSub start: " + (new Date()));
		log.info("copyImagingSub from: " + startDate);
		System.out.println("copyImagingSub start: " + (new Date()));

		copyImagingSub(connMetro, connSkyOne, sysDate);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("copyImagingSub end in " + (t2 - t1) + " ms");
	}

	public void copyImagingSub(Connection connMetro, Connection connSkyOne, Long startDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyImagingSub start copy: " + startDate);
		String sqlSkyOne = "select * from imaging_result where deleted_by is null ";
		if (!Lib.isEmpty(Const.IMAGING_RESULT_SORT)) {
			sqlSkyOne += Const.IMAGING_RESULT_SORT;
		}
		if (limit > 0) {
			sqlSkyOne += " limit " + limit;
		}

		PreparedStatement stmtSelect = null;
		ResultSet rsSkyOne = null;

		rowInsert = 0;
		rowUpdate = 0;
		try {
			stmtSelect = connSkyOne.prepareStatement(sqlSkyOne);
			rsSkyOne = stmtSelect.executeQuery();
			while (rsSkyOne.next()) {
				try {
					Long id = rsSkyOne.getLong("id");
					String code = rsSkyOne.getString("code");
					String externalUUID = rsSkyOne.getString("external_uuid");
					updateImagingSub(connMetro, connSkyOne, id, code, externalUUID);
				} catch (Exception e) {
					log.error("Error update: ", e);
				}
			}
		} catch (Exception e) {
			log.error("Error copyImagingSub: " + startDate, e);
		} finally {
			close(rsSkyOne);
			close(stmtSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyImagingSub end in " + (t2 - t1) + " ms");
		System.out.println("copyImagingSub end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void copyImagingSubByImagingResult(Connection connMetro, Connection connSkyOne,  Long imagingResultId) {
		long t1 = System.currentTimeMillis();
		log.info("copyImagingSub start copy: " + startDate);
		String sqlSkyOne = "select * from imaging_result where deleted_by is null and id = ? ";

		PreparedStatement stmtSelect = null;
		ResultSet rsSkyOne = null;

		rowInsert = 0;
		rowUpdate = 0;
		try {
			stmtSelect = connSkyOne.prepareStatement(sqlSkyOne);
			stmtSelect.setLong(1, imagingResultId);
			rsSkyOne = stmtSelect.executeQuery();
			while (rsSkyOne.next()) {
				try {
					Long id = rsSkyOne.getLong("id");
					String code = rsSkyOne.getString("code");
					String externalUUID = rsSkyOne.getString("external_uuid");
					updateImagingSub(connMetro, connSkyOne, id, code, externalUUID);
				} catch (Exception e) {
					log.error("Error update: ", e);
				}
			}
		} catch (Exception e) {
			log.error("Error copyImagingSub: " + startDate, e);
		} finally {
			close(rsSkyOne);
			close(stmtSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyImagingSub end in " + (t2 - t1) + " ms");
		System.out.println("copyImagingSub end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void updateImagingSub(Connection connMetro, Connection connSkyOne, Long id, String code, String externalUUID)
			throws SQLException {
		long t1 = System.currentTimeMillis();
		log.info("updateImagingSub start copy: " + startDate);

		String sqlMetroSelect = "select * from \"EndoscopicMedicalRecordItem\" where deleted = 0 and endoscopicmedicalrecord_id = ?::uuid";
		PreparedStatement stmtSelect = null;
		ResultSet rsSelect = null;

		stmtSelect = connMetro.prepareStatement(sqlMetroSelect);
		stmtSelect.setString(1, externalUUID);
		rsSelect = stmtSelect.executeQuery();
		int row = 0;
		while (rsSelect.next()) {
			try {
				row++;
				String endoscopicmedicalrecordId = rsSelect.getString("endoscopicmedicalrecord_id");
				String endoscopicmedicalrecorditemId = rsSelect.getString("endoscopicmedicalrecorditem_id");
				String procedurecode = rsSelect.getString("procedurecode");
				String procedurename = rsSelect.getString("procedurename");
				Timestamp dateCreated = rsSelect.getTimestamp("date_created");
				updateImagingSub(connSkyOne, id, endoscopicmedicalrecorditemId, procedurecode, procedurename,
						dateCreated, endoscopicmedicalrecordId);
			} catch (Exception e) {
				log.error("Error update ", e);
			}
		}

		long t2 = System.currentTimeMillis();
		log.info("updateImaginSub(" + row + ") end in " + (t2 - t1) + " ms");
	}

	public void updateImagingSub(Connection connSkyOne, Long id, String endoscopicmedicalrecorditemId,
			String procedurecode, String procedurename, Date dateCreated, String endoscopicmedicalrecordId)
			throws Exception {
		long t1 = System.currentTimeMillis();
		log.info("updateImagingSub start copy: " + startDate);
		String sqlSelect = "select id from imaging_sub where deleted_by is null and external_uuid = ? ";
		String sqlInsert = "insert into imaging_sub(id, imaging_result_id, imaging_label_id, label, notes, label_foreign, notes_foreign, sub_result, abnormal, tagged, created_by, created_date, updated_by, updated_date, version, external_uuid, remind_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update imaging_sub set id = ?, imaging_result_id = ?, imaging_label_id = ?, label = ?, notes = ?, label_foreign = ?, notes_foreign = ?, sub_result = ?, abnormal = ?, tagged = ?, created_by = ?, created_date = ?, updated_by = ?, updated_date = ?, version = ?, external_uuid = ?, remind_date = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		String notes = "";
		Long remindDate = 0L;
		String labelForeign = "";
		String notesForeign = "";

		Long imagingLabelId = findImagingSub(procedurecode);
		Integer subResult = 0;
		Integer abnormal = 0;
		Integer tagged = 1;
		Long createdDate = getMilliSecond(dateCreated);
		Long updatedDate = getSysDate();

		Long skyImagingSubId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setString(1, endoscopicmedicalrecorditemId);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyImagingSubId = rsSelect.getLong("id");
		}

		EndoMedical endoMedical = findEndoMedicalById(endoscopicmedicalrecordId);
		if (endoMedical != null) {
			if (Const.CLO_TEST_CODE.equals(procedurecode)) {
				if (endoMedical.getCloTestResult() != null) {
					notes = getCloTestItems(endoMedical.getCloTestResult());
					remindDate = getMilliSecond(endoMedical.getCloTestRemindTime());
				}
			} else if (Const.BIOPSY_CODE.equals(procedurecode.trim())) {
				if (endoMedical.getBiosyResult() != null) {
					notes = endoMedical.getBiosyResult();
					remindDate = getMilliSecond(endoMedical.getBiopsyRemindTime());
				}
			} else if (Const.HP_CODE.equals(procedurecode.trim())) {
				if (endoMedical.getHpResult() != null) {
					notes = endoMedical.getHpResult();
					remindDate = getMilliSecond(endoMedical.getHpRemindTime());
				}
			}
		}

		if (endoMedical != null) {
			System.out.print("clo: " + endoMedical.getCloTestResult());
			System.out.println("; date: " + endoMedical.getCloTestRemindTime());
		}

		if (skyImagingSubId == null) {
			skyImagingSubId = getId("ImagingSub", connSkyOne);
			log.info("Insert (Code procedure: " + procedurecode + ")");
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyImagingSubId);
			stmtInsert.setLong(2, id);
			stmtInsert.setLong(3, imagingLabelId);

			stmtInsert.setString(4, procedurename);
			stmtInsert.setString(5, notes);
			stmtInsert.setString(6, labelForeign);
			stmtInsert.setString(7, notesForeign);

			stmtInsert.setInt(8, subResult);
			stmtInsert.setInt(9, abnormal);
			stmtInsert.setInt(10, tagged);

			stmtInsert.setLong(11, createdId);
			stmtInsert.setLong(12, createdDate);
			stmtInsert.setLong(13, createdId);
			stmtInsert.setLong(14, updatedDate);
			stmtInsert.setInt(15, version);
			stmtInsert.setString(16, endoscopicmedicalrecorditemId);
			stmtInsert.setLong(17, remindDate);

			stmtInsert.executeUpdate();
			rowInsert++;
			System.out.println("Insert (Code procedure: " + procedurecode + ")");
		} else {
			log.info("Update (Code procedure: " + procedurecode + ")");
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyImagingSubId);
			stmtUpdate.setLong(2, id);
			stmtUpdate.setLong(3, imagingLabelId);

			stmtUpdate.setString(4, procedurename);
			stmtUpdate.setString(5, notes);
			stmtUpdate.setString(6, labelForeign);
			stmtUpdate.setString(7, notesForeign);

			stmtUpdate.setInt(8, subResult);
			stmtUpdate.setInt(9, abnormal);
			stmtUpdate.setInt(10, tagged);

			stmtUpdate.setLong(11, createdId);
			stmtUpdate.setLong(12, createdDate);
			stmtUpdate.setLong(13, createdId);
			stmtUpdate.setLong(14, updatedDate);
			stmtUpdate.setInt(15, version);
			stmtUpdate.setString(16, endoscopicmedicalrecorditemId);
			stmtUpdate.setLong(17, remindDate);

			stmtUpdate.setLong(18, skyImagingSubId);
			stmtUpdate.executeUpdate();
			rowUpdate++;
			System.out.println("Update (Code procedure: " + procedurecode + ")");
		}

		long t2 = System.currentTimeMillis();
		log.info("updateImagingSub end in " + (t2 - t1) + " ms");

	}

}
