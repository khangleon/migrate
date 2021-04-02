package vn.com.skyone.metro2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class CopyImgingTemplate extends Common {

	public CopyImgingTemplate(Integer nodeId) {
		super(nodeId);
	}

	public CopyImgingTemplate(String[] args) {
		super(args);
	}

	/**
	 * 
	 * @param path
	 */
	public void copyImagingTemplate() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("CopyDoctor connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("copyImagingTemplate connect SKY ok");
		} catch (SQLException e) {
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("copyImagingTemplate start: " + (new Date()));
		log.info("copyImagingTemplate from: " + startDate);
		System.out.println("copyImagingTemplate start: " + (new Date()));

		copyImagingTemplate(connMetro, connSkyOne, sysDate);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("copyImagingTemplate end in " + (t2 - t1) + " ms");
	}

	public void copyImagingTemplate(Connection connMetro, Connection connSkyOne, Long startDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyImagingTemplate start copy: " + startDate);
		String sqlSkyOne = "select * from item where deleted_by is null and sub_included = 1 and code like 'NS%'";

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
					String nameEn = rsSkyOne.getString("cpt_name");
					updateImagingTemplate(connMetro, connSkyOne, id, code, externalUUID, nameEn);
				} catch (Exception e) {
					log.error("Error update patientId: ", e);
				}
			}
		} catch (Exception e) {
			log.error("Error copyImagingTemplate: " + startDate, e);
		} finally {
			close(rsSkyOne);
			close(stmtSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyImagingTemplate end in " + (t2 - t1) + " ms");
		System.out.println("copyImagingTemplate end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void updateImagingTemplate(Connection connMetro, Connection connSkyOne, Long id, String code,
			String externalUUID, String nameEn) throws SQLException {
		long t1 = System.currentTimeMillis();
		log.info("updateImagingTemplate start copy: " + startDate);

		String sqlMetroSelect = "select * from \"TemplateContent\" where deleted = 0 ";

		if (typeOfContent > -1) {
			sqlMetroSelect += " and typeofcontent = " + typeOfContent + " ";
		}

		if (!Lib.isEmpty(Const.METRO_REPORT_TEMPLATE)) {
			sqlMetroSelect += Const.METRO_REPORT_TEMPLATE;
		}

		PreparedStatement stmtSelect = null;
		ResultSet rsSelect = null;

		stmtSelect = connMetro.prepareStatement(sqlMetroSelect);
		rsSelect = stmtSelect.executeQuery();
		int row = 0;
		while (rsSelect.next()) {
			try {
				row++;
				String templatecontentId = rsSelect.getString("templatecontent_id");
				String title = rsSelect.getString("title");
				String content = rsSelect.getString("content");
				Integer typeOfContent = rsSelect.getInt("typeofcontent");
				Timestamp dateCreated = rsSelect.getTimestamp("date_created");
				updateImagingTemplate(connSkyOne, id, code, templatecontentId, title, content, typeOfContent,
						dateCreated, row, nameEn);
			} catch (Exception e) {
				log.error("Error update patientId: ", e);
			}
		}

		long t2 = System.currentTimeMillis();
		log.info("updateImaginImage(" + row + ") end in " + (t2 - t1) + " ms");
	}

	public void updateImagingTemplate(Connection connSkyOne, Long id, String itemCode, String templatecontentId,
			String title, String content, Integer typeOfContent, Date dateCreated, Integer row, String nameEn) throws Exception {
		long t1 = System.currentTimeMillis();
		log.info("updateImagingTemplate start copy: " + startDate);
		String sqlSelect = "select id from imaging_template where deleted_by is null and external_uuid = ? and item_id = ? ";
		String sqlInsert = "insert into imaging_template(id, company_id, branch_id, screen, item_id, code, name, technique, findings, impression, histopathology, recommendation, note, default_template, report_template_code, medic_private, medic_id, abnormal, male, female, template_type, template_id, template_code,template_foreign, sort, created_by, created_date, updated_by, updated_date, version, external_uuid, title) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update imaging_template set id = ?, company_id = ?, branch_id = ?, screen = ?, item_id = ?, code = ?, name = ?, technique = ?, findings = ?, impression = ?, histopathology = ?, recommendation = ?, note = ?, default_template = ?, report_template_code = ?, medic_private = ?, medic_id = ?, abnormal = ?, male = ?, female = ?, template_type = ?, template_id = ?, template_code = ?,template_foreign = ?, sort = ?, created_by = ?, created_date = ?, updated_by = ?, updated_date = ?, version = ?, external_uuid = ?, title = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		String screen = "img211";
		String code = Lib.genCode(itemCode, row);
		String impression = "";
		String histopathology = "";
		String recommendation = "";
		String note = "";
		Integer defaultTemplate = row > 1 ? 0 : 1;
		String reportCode = "es";
		Integer medicPrivate = 0;
		Long medicId = 0L;
		Integer abNormal = 0;
		Integer male = 1;
		Integer female = 1;
		Integer templateType = 1;
		// Long templateId = templateId123;
		String templateCode = "es";
		Integer templateForeign = 0;
		Integer sort = row;
		Long createdDate = getMilliSecond(dateCreated);
		Long updatedDate = getSysDate();
		
		String technique = title;
		String titleCustom = title + "\r\n" + nameEn;

		Long skyImagingTemplateId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setString(1, templatecontentId);
		stmtSelect.setLong(2, id);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyImagingTemplateId = rsSelect.getLong("id");
		}

		if (skyImagingTemplateId == null) {
			skyImagingTemplateId = getId("ImagingTemplate", connSkyOne);
			log.info("Insert (Code file name: " + code + ")");
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyImagingTemplateId);
			stmtInsert.setLong(2, companyId);
			stmtInsert.setLong(3, branchId);
			stmtInsert.setString(4, screen);
			stmtInsert.setLong(5, id);
			stmtInsert.setString(6, code);
			stmtInsert.setString(7, title);
			stmtInsert.setString(8, technique);
			stmtInsert.setString(9, content);
			stmtInsert.setString(10, impression);
			stmtInsert.setString(11, histopathology);
			stmtInsert.setString(12, recommendation);
			stmtInsert.setString(13, note);
			stmtInsert.setInt(14, defaultTemplate);
			stmtInsert.setString(15, reportCode);
			stmtInsert.setInt(16, medicPrivate);
			stmtInsert.setLong(17, medicId);
			stmtInsert.setInt(18, abNormal);
			stmtInsert.setInt(19, male);
			stmtInsert.setInt(20, female);
			stmtInsert.setInt(21, templateType);
			stmtInsert.setLong(22, templateId);
			stmtInsert.setString(23, templateCode);
			stmtInsert.setInt(24, templateForeign);
			stmtInsert.setInt(25, sort);

			stmtInsert.setLong(26, createdDate);
			stmtInsert.setLong(27, createdId);
			stmtInsert.setLong(28, updatedDate);
			stmtInsert.setLong(29, createdId);
			stmtInsert.setInt(30, version);
			stmtInsert.setString(31, templatecontentId);
			stmtInsert.setString(32, titleCustom);

			stmtInsert.executeUpdate();
			rowInsert++;
			System.out.println("Insert (Code file name: " + code + ")");
		} else {
			log.info("Update (Code file name: " + code + ")");
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyImagingTemplateId);
			stmtUpdate.setLong(2, companyId);
			stmtUpdate.setLong(3, branchId);
			stmtUpdate.setString(4, screen);
			stmtUpdate.setLong(5, id);
			stmtUpdate.setString(6, code);
			stmtUpdate.setString(7, title);
			stmtUpdate.setString(8, technique);
			stmtUpdate.setString(9, content);
			stmtUpdate.setString(10, impression);
			stmtUpdate.setString(11, histopathology);
			stmtUpdate.setString(12, recommendation);
			stmtUpdate.setString(13, note);
			stmtUpdate.setInt(14, defaultTemplate);
			stmtUpdate.setString(15, reportCode);
			stmtUpdate.setInt(16, medicPrivate);
			stmtUpdate.setLong(17, medicId);
			stmtUpdate.setInt(18, abNormal);
			stmtUpdate.setInt(19, male);
			stmtUpdate.setInt(20, female);
			stmtUpdate.setInt(21, templateType);
			stmtUpdate.setLong(22, templateId);
			stmtUpdate.setString(23, templateCode);
			stmtUpdate.setInt(24, templateForeign);
			stmtUpdate.setInt(25, sort);

			stmtUpdate.setLong(26, createdDate);
			stmtUpdate.setLong(27, createdId);
			stmtUpdate.setLong(28, updatedDate);
			stmtUpdate.setLong(29, createdId);
			stmtUpdate.setInt(30, version);
			stmtUpdate.setString(31, templatecontentId);
			stmtUpdate.setString(32, titleCustom);

			stmtUpdate.setLong(33, skyImagingTemplateId);
			stmtUpdate.executeUpdate();
			rowUpdate++;
			System.out.println("Update (Code file name: " + code + ")");
		}

		long t2 = System.currentTimeMillis();
		log.info("updateImagingTemplate end in " + (t2 - t1) + " ms");

	}

}
