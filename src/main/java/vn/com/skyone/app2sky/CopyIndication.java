package vn.com.skyone.app2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class CopyIndication extends Common {

	public CopyIndication(Integer nodeId) {
		super(nodeId);
	}

	public CopyIndication(String[] args) {
		super(args);
	}

	/**
	 * 
	 * @param path
	 */
	public void copyIndication() {
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connSkyOne = getSkyOneConnection();
			log.info("copyIndication connect SKY ok");
		} catch (SQLException e) {
			log.error("copyIndication Connect database error", e);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("copyIndication start: " + (new Date()));
		log.info("copyIndication from: " + startDate);
		System.out.println("copyIndication start: " + (new Date()));

		copyIndication(connSkyOne, sysDate);

		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("copyIndication end in " + (t2 - t1) + " ms");
	}

	public void copyIndication(Connection connSkyOne, Long startDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyIndication start copy: " + startDate);
		String sqlSkyOne = "select * from imaging_result where deleted_by is null order by result_date desc ";
		PreparedStatement skyOneSelect = null;
		ResultSet rsSkyOne = null;

		rowInsert = 0;
		rowUpdate = 0;
		try {
			skyOneSelect = connSkyOne.prepareStatement(sqlSkyOne);
			rsSkyOne = skyOneSelect.executeQuery();
			while (rsSkyOne.next()) {
				try {
					Long id = rsSkyOne.getLong("id");
					Long indicationId = rsSkyOne.getLong("indication_id");
					String code = rsSkyOne.getString("code");
					Long patientId = rsSkyOne.getLong("patient_id");
					Long indicatorId = rsSkyOne.getLong("indicator_id");
					Long performanceDate = rsSkyOne.getLong("result_date");
					String diagnosis = rsSkyOne.getString("diagnosis");
					Long itemId = rsSkyOne.getLong("item_id");
					updateIndication(connSkyOne, id, indicationId, code, patientId, indicatorId, performanceDate,
							diagnosis, itemId);
				} catch (Exception e) {
					log.error("Error update indicationId: ", e);
				}
			}

		} catch (Exception e) {
			log.error("Error copyIndication: " + startDate, e);
		} finally {
			close(rsSkyOne);
			close(skyOneSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyIndication end in " + (t2 - t1) + " ms");
		System.out.println("copyIndication end in " + (t2 - t1) + " ms");
		System.out.println("Complete Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void copyIndicationByImagingResult(Connection connSkyOne, Long imagingResultId) {
		long t1 = System.currentTimeMillis();
		log.info("copyIndication start copy: " + startDate);
		String sqlSkyOne = "select * from imaging_result where deleted_by is null and id = ? ";
		PreparedStatement skyOneSelect = null;
		ResultSet rsSkyOne = null;

		rowInsert = 0;
		rowUpdate = 0;
		try {
			skyOneSelect = connSkyOne.prepareStatement(sqlSkyOne);
			skyOneSelect.setLong(1, imagingResultId);
			rsSkyOne = skyOneSelect.executeQuery();
			while (rsSkyOne.next()) {
				try {
					Long id = rsSkyOne.getLong("id");
					Long indicationId = rsSkyOne.getLong("indication_id");
					String code = rsSkyOne.getString("code");
					Long patientId = rsSkyOne.getLong("patient_id");
					Long indicatorId = rsSkyOne.getLong("indicator_id");
					Long performanceDate = rsSkyOne.getLong("result_date");
					String diagnosis = rsSkyOne.getString("diagnosis");
					Long itemId = rsSkyOne.getLong("item_id");
					updateIndication(connSkyOne, id, indicationId, code, patientId, indicatorId, performanceDate,
							diagnosis, itemId);
				} catch (Exception e) {
					log.error("Error update indicationId: ", e);
				}
			}

		} catch (Exception e) {
			log.error("Error copyIndication: " + startDate, e);
		} finally {
			close(rsSkyOne);
			close(skyOneSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyIndication end in " + (t2 - t1) + " ms");
		System.out.println("copyIndication end in " + (t2 - t1) + " ms");
		System.out.println("Complete Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void updateIndication(Connection connSkyOne, Long id, Long indicationId, String code, Long patientId,
			Long indicatorId, Long performanceDate, String diagnosis, Long itemId) throws SQLException {
		long t1 = System.currentTimeMillis();
		log.info("updateIndication start copy: " + startDate);

		String sqlSelect = "select id from indication where id = ? ";
		String sqlInsert = "insert into indication(id, company_id, branch_id, department_id, search_text, code, patient_id, indicator_id, indication_date, appt_date, diagnosis, created_by, created_date, updated_by, updated_date, version) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update indication set id = ?, company_id = ?, branch_id = ?, department_id = ?, search_text = ?, code = ?, patient_id = ?, indicator_id = ?, indication_date = ?, appt_date = ?, diagnosis = ?, created_by = ?, created_date = ?, updated_by = ?, updated_date = ?, version = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		String searchText = "";

		Long indicationDepartmentId = endoDepartmentId;

		Long skyIndicationId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setLong(1, indicationId);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyIndicationId = rsSelect.getLong("id");
		}

		Long createdId = Const.CREATED_ID;
		Long updatedDate = getSysDate();

		if (skyIndicationId == null) {
			skyIndicationId = getId("Indication", connSkyOne);
			log.info("Insert (Code imaging_result: " + code + ")");
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyIndicationId);
			stmtInsert.setLong(2, companyId);
			stmtInsert.setLong(3, branchId);
			stmtInsert.setLong(4, indicationDepartmentId);
			stmtInsert.setString(5, searchText);
			stmtInsert.setString(6, code);
			stmtInsert.setLong(7, patientId);
			stmtInsert.setLong(8, indicatorId);
			stmtInsert.setLong(9, performanceDate);
			stmtInsert.setLong(10, performanceDate);
			stmtInsert.setString(11, diagnosis);
			stmtInsert.setLong(12, createdId);
			stmtInsert.setLong(13, performanceDate);
			stmtInsert.setLong(14, createdId);
			stmtInsert.setLong(15, updatedDate);
			stmtInsert.setInt(16, version);

			stmtInsert.executeUpdate();
			rowInsert++;
			System.out.println("--insert Indication..");
		} else {
			log.info("Update (Code imaging_result: " + code + ")");
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyIndicationId);
			stmtUpdate.setLong(2, companyId);
			stmtUpdate.setLong(3, branchId);
			stmtUpdate.setLong(4, indicationDepartmentId);
			stmtUpdate.setString(5, searchText);
			stmtUpdate.setString(6, code);
			stmtUpdate.setLong(7, patientId);
			stmtUpdate.setLong(8, indicatorId);
			stmtUpdate.setLong(9, performanceDate);
			stmtUpdate.setLong(10, performanceDate);
			stmtUpdate.setString(11, diagnosis);
			stmtUpdate.setLong(12, createdId);
			stmtUpdate.setLong(13, performanceDate);
			stmtUpdate.setLong(14, createdId);
			stmtUpdate.setLong(15, updatedDate);
			stmtUpdate.setInt(16, version);
			stmtUpdate.setLong(17, skyIndicationId);

			stmtUpdate.executeUpdate();
			rowUpdate++;
			System.out.println("--update Indication..");
		}

		Long indicationItemId = updateIndicationItem(connSkyOne, indicationId, itemId);

		updateImagingResult(connSkyOne, id, skyIndicationId, indicationItemId, performanceDate, indicationDepartmentId);

		long t2 = System.currentTimeMillis();
		log.info("updateIndication end in " + (t2 - t1) + " ms");

	}

	public Long updateIndicationItem(Connection connSkyOne, Long indicationId, Long itemId) throws SQLException {
		long t1 = System.currentTimeMillis();
		log.info("updateIndicationItem start copy: " + startDate);

		String sqlSelect = "select id from indication_item where indication_id = ? ";
		String sqlInsert = "insert into indication_item(id, indication_id, item_id, item_code, item_name, screen, unit_id, qty, notes, category_id, created_by, created_date, updated_by, updated_date, version) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update indication_item set id = ?, indication_id = ?, item_id = ?, item_code = ?, item_name = ?, screen = ?, unit_id = ?, qty = ?, notes = ?, category_id = ?, created_by = ?, created_date = ?, updated_by = ?, updated_date = ?, version = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		Long skyIndicationItemId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setLong(1, indicationId);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyIndicationItemId = rsSelect.getLong("id");
		}

		Long createdId = Const.CREATED_ID;
		Long updatedDate = getSysDate();

		Double qty = 1D;
		String notes = "";

		String itemCode = null;
		String itemName = null;
		String screen = null;
		Long categoryId = null;
		Long unitId = null;
		Item item = findItemById(itemId);
		if (item != null) {
			itemId = item.getId();
			itemCode = item.getCode();
			itemName = item.getName();
			screen = item.getScreen();
			categoryId = item.getCategoryId();
			unitId = item.getUnitId();
		}

		if (skyIndicationItemId == null) {
			skyIndicationItemId = getId("IndicationItem", connSkyOne);
			log.info("Insert (IndicationItem ID: " + skyIndicationItemId + ")");
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyIndicationItemId);
			stmtInsert.setLong(2, indicationId);
			stmtInsert.setLong(3, itemId);
			stmtInsert.setString(4, itemCode);
			stmtInsert.setString(5, itemName);
			stmtInsert.setString(6, screen);
			stmtInsert.setLong(7, unitId);
			stmtInsert.setDouble(8, qty);
			stmtInsert.setString(9, notes);
			stmtInsert.setLong(10, categoryId);

			stmtInsert.setLong(11, createdId);
			stmtInsert.setLong(12, updatedDate);
			stmtInsert.setLong(13, createdId);
			stmtInsert.setLong(14, updatedDate);
			stmtInsert.setInt(15, version);

			stmtInsert.executeUpdate();
			rowInsert++;
			System.out.println("---insert IndicationItem..");
		} else {
			log.info("Update (IndicationItem ID: " + skyIndicationItemId + ")");
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyIndicationItemId);
			stmtUpdate.setLong(2, indicationId);
			stmtUpdate.setLong(3, itemId);
			stmtUpdate.setString(4, itemCode);
			stmtUpdate.setString(5, itemName);
			stmtUpdate.setString(6, screen);
			stmtUpdate.setLong(7, unitId);
			stmtUpdate.setDouble(8, qty);
			stmtUpdate.setString(9, notes);
			stmtUpdate.setLong(10, categoryId);

			stmtUpdate.setLong(11, createdId);
			stmtUpdate.setLong(12, updatedDate);
			stmtUpdate.setLong(13, createdId);
			stmtUpdate.setLong(14, updatedDate);
			stmtUpdate.setInt(15, version);
			stmtUpdate.setLong(16, skyIndicationItemId);

			stmtUpdate.executeUpdate();
			rowUpdate++;
			System.out.println("---update IndicationItem..");
		}
		long t2 = System.currentTimeMillis();
		log.info("updateIndicationItem end in " + (t2 - t1) + " ms");
		return skyIndicationItemId;
	}

	public Long updateImagingResult(Connection connSkyOne, Long imagingResultId, Long indicationId,
			Long indicationItemId, Long performanceDate, Long indicationDepartmentId) throws SQLException {
		long t1 = System.currentTimeMillis();
		log.info("updateImagingResult by Indication start copy: " + startDate);

		//, indication_department_id = ?
		String sqlUpdate = "update imaging_result set indication_id = ?, indication_item_id = ?, appt_date = ?, indication_date = ?  where id = ?";

		PreparedStatement stmtUpdate = null;
		stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
		stmtUpdate.setLong(1, indicationId);
		stmtUpdate.setLong(2, indicationItemId);
		stmtUpdate.setLong(3, performanceDate);
		stmtUpdate.setLong(4, performanceDate);
		//stmtUpdate.setLong(5, indicationDepartmentId);
		stmtUpdate.setLong(5, imagingResultId);
		int update = stmtUpdate.executeUpdate();

		System.out.println("update: " + update);

		long t2 = System.currentTimeMillis();
		log.info("updateImagingResult by Indication end in " + (t2 - t1) + " ms");
		return imagingResultId;
	}

}
