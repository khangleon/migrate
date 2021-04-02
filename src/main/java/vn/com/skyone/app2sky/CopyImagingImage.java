package vn.com.skyone.app2sky;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CopyImagingImage extends Common {

	public CopyImagingImage(Integer nodeId) {
		super(nodeId);
	}

	public CopyImagingImage(String[] args) {
		super(args);
	}

	/**
	 * 
	 * @param path
	 */
	public void copyImagingImage() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("CopyDoctor connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("copyImagingImage connect SKY ok");
		} catch (SQLException e) {
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("copyImagingImage start: " + (new Date()));
		log.info("copyImagingImage from: " + startDate);
		System.out.println("copyImagingImage start: " + (new Date()));

		copyImagingImage(connMetro, connSkyOne, sysDate);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("copyImagingImage end in " + (t2 - t1) + " ms");
	}

	public void copyImagingImage(Connection connMetro, Connection connSkyOne, Long startDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyImagingImage start copy: " + startDate);
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
					updateImagingImage(connMetro, connSkyOne, id, code, externalUUID);
				} catch (Exception e) {
					log.error("Error update patientId: ", e);
				}
			}
		} catch (Exception e) {
			log.error("Error copyImagingImage: " + startDate, e);
		} finally {
			close(rsSkyOne);
			close(stmtSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyImagingImage end in " + (t2 - t1) + " ms");
		System.out.println("copyImagingImage end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void copyImagingImageByImagingResult(Connection connMetro, Connection connSkyOne, Long imagingResultId) {
		long t1 = System.currentTimeMillis();
		log.info("copyImagingImage start copy: " + startDate);
		String sqlSkyOne = "select * from imaging_result where deleted_by is null and id = ?";

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
					updateImagingImage(connMetro, connSkyOne, id, code, externalUUID);
				} catch (Exception e) {
					log.error("Error update patientId: ", e);
				}
			}
		} catch (Exception e) {
			log.error("Error copyImagingImage: " + startDate, e);
		} finally {
			close(rsSkyOne);
			close(stmtSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyImagingImage end in " + (t2 - t1) + " ms");
		System.out.println(
				"copyImagingImage end in " + (t2 - t1) + " ms; " + "Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void updateImagingImage(Connection connMetro, Connection connSkyOne, Long id, String code,
			String externalUUID) throws SQLException {
		long t1 = System.currentTimeMillis();
		log.info("updateImagingImage start copy: " + startDate);

		String sqlMetroSelect = "select * from \"MediaInfo\" where deleted = 0 and endoscopicmedicalrecord_id = ?::uuid";
		PreparedStatement stmtSelect = null;
		ResultSet rsSelect = null;

		stmtSelect = connMetro.prepareStatement(sqlMetroSelect);
		stmtSelect.setString(1, externalUUID);
		rsSelect = stmtSelect.executeQuery();
		int row = 0;
		while (rsSelect.next()) {
			try {
				row++;
				byte[] data = rsSelect.getBytes("mediablob");
				Timestamp dateCreated = rsSelect.getTimestamp("date_created");
				Timestamp dateModified = rsSelect.getTimestamp("date_modified");
				String mediainfoId = rsSelect.getString("mediainfo_id");
				updateImagingImage(connSkyOne, id, code, data, dateCreated, dateModified, mediainfoId, 1, row);
			} catch (Exception e) {
				log.error("Error update patientId: ", e);
			}
		}

		long t2 = System.currentTimeMillis();
		log.info("updateImaginImage(" + row + ") end in " + (t2 - t1) + " ms");
	}

	public void updateImagingImage(Connection connSkyOne, Long id, String code, byte[] data, Date dateCreated,
			Date dateModified, String mediainfoId, Integer keyImage, Integer keyIndex) throws Exception {
		long t1 = System.currentTimeMillis();
		log.info("updateImagingImage start copy: " + startDate);
		String sqlSelect = "select id from imaging_image where deleted_by is null and external_uuid = ? ";
		String sqlInsert = "insert into imaging_image(id, imaging_result_id, filesystem_id, filepath, filename, filetype, file_md5, file_size, created_by, created_date, updated_by, updated_date, version, external_uuid, key_image, key_index) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update imaging_image set id = ?, imaging_result_id = ?, filesystem_id = ?, filepath = ?, filename = ?, filetype = ?, file_md5 = ?, file_size = ?, created_by = ?, created_date = ?, updated_by = ?, updated_date = ?, version = ?, external_uuid = ?,  key_image = ?, key_index = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		// Long dirId = Const.DIR_PATH_ID;
		// String dirPath = Const.DIR_PATH;
		String filePath = "";
		String fileName = "";
		String fileType = "png";
		String fileMd5 = Lib.getMD5Checksum(data);
		Long fileSize = (long) data.length;
		Long createdDate = getMilliSecond(dateCreated);
		Long updatedDate = getMilliSecond(dateModified);

		// create file
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		filePath = sdf.format(new Date(createdDate)) + "/" + code;
		File path = new File(dirPath, filePath);
		if (!path.exists()) {
			path.mkdirs();
		}

		Long skyImagingImageId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setString(1, mediainfoId);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyImagingImageId = rsSelect.getLong("id");
		}

		if (skyImagingImageId == null) {
			skyImagingImageId = getId("ImagingDocument", connSkyOne);
			log.info("Insert (Code file name: " + fileName + ")");
			fileName = code + "_" + skyImagingImageId + "." + fileType;
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyImagingImageId);
			stmtInsert.setLong(2, id);
			stmtInsert.setLong(3, dirId);

			stmtInsert.setString(4, filePath);
			stmtInsert.setString(5, fileName);
			stmtInsert.setString(6, fileType);
			stmtInsert.setString(7, fileMd5);
			stmtInsert.setLong(8, fileSize);

			stmtInsert.setLong(9, createdId);
			stmtInsert.setLong(10, createdDate);
			stmtInsert.setLong(11, createdId);
			stmtInsert.setLong(12, updatedDate);
			stmtInsert.setInt(13, version);
			stmtInsert.setString(14, mediainfoId);
			stmtInsert.setInt(15, keyImage);
			stmtInsert.setInt(16, keyIndex);

			stmtInsert.executeUpdate();
			rowInsert++;
			System.out.println("Insert (Code file name: " + fileName + ")");
		} else {
			log.info("Update (Code file name: " + fileName + ")");
			fileName = code + "_" + skyImagingImageId + "." + fileType;
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyImagingImageId);
			stmtUpdate.setLong(2, id);
			stmtUpdate.setLong(3, dirId);

			stmtUpdate.setString(4, filePath);
			stmtUpdate.setString(5, fileName);
			stmtUpdate.setString(6, fileType);
			stmtUpdate.setString(7, fileMd5);
			stmtUpdate.setLong(8, fileSize);

			stmtUpdate.setLong(9, createdId);
			stmtUpdate.setLong(10, createdDate);
			stmtUpdate.setLong(11, createdId);
			stmtUpdate.setLong(12, updatedDate);
			stmtUpdate.setInt(13, version);
			stmtUpdate.setString(14, mediainfoId);
			stmtUpdate.setInt(15, keyImage);
			stmtUpdate.setInt(16, keyIndex);

			stmtUpdate.setLong(17, skyImagingImageId);
			stmtUpdate.executeUpdate();
			rowUpdate++;
			System.out.println("Update (Code file name: " + fileName + ")");
		}

		File file = new File(path, fileName);
		Files.write(file.toPath(), data);

		long t2 = System.currentTimeMillis();
		log.info("updateImagingImage end in " + (t2 - t1) + " ms");

	}

}
