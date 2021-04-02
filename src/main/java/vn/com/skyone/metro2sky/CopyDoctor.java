package vn.com.skyone.metro2sky;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class CopyDoctor extends CopyBase {

	public CopyDoctor(Integer nodeId) {
		super(nodeId);
	}

	public CopyDoctor(String[] args) {
		super(args);
	}

	/**
	 * 
	 * @param path
	 */
	public void copyDoctor2() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("CopyDoctor connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("CopyDoctor connect SKY ok");
		} catch (SQLException e) {
			log.error("CopyDoctor Connect database error", e);
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("CopyDoctor start: " + (new Date()));
		log.info("CopyDoctor from: " + startDate);
		System.out.println("CopyDoctor start: " + (new Date()));

		System.out.println("Url metro: " + urlMetro);
		System.out.println("Url sky: " + urlSkyOne);
		System.out.println("startDate: " + startDate);
		System.out.println("Node: " + nodeId);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("CopyDoctor end in " + (t2 - t1) + " ms");
	}

	public void copyDoctor() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("CopyDoctor connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("CopyDoctor connect SKY ok");
		} catch (SQLException e) {
			log.error("CopyDoctor Connect database error", e);
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("CopyDoctor start: " + (new Date()));
		log.info("CopyDoctor from: " + startDate);
		System.out.println("CopyDoctor start: " + (new Date()));

		copyDoctor(connMetro, connSkyOne, sysDate);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("CopyDoctor end in " + (t2 - t1) + " ms");
	}

	public void copyDoctorRange() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("CopyDoctor connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("CopyDoctor connect SKY ok");
		} catch (SQLException e) {
			log.error("CopyDoctor Connect database error", e);
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("CopyDoctor start: " + (new Date()));
		log.info("CopyDoctor from: " + startDate);
		System.out.println("CopyDoctor start: " + (new Date()));

		copyDoctorRange(connMetro, connSkyOne, sysDate, fromDate, toDate);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("CopyDoctor end in " + (t2 - t1) + " ms");
	}

	public void copyDoctor(Connection connMetro, Connection connSkyOne, Long startDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyPatient start copy: " + startDate);
		String sqlMetro = "select doctor_id, fullname, title, address, phone, email, sex, age, digitalsignature from \"Doctor\" where deleted = 0; ";
		PreparedStatement metroSelect = null;
		ResultSet rsMetro = null;

		rowInsert = 0;
		rowUpdate = 0;
		try {
			metroSelect = connMetro.prepareStatement(sqlMetro);
			rsMetro = metroSelect.executeQuery();
			while (rsMetro.next()) {
				Long patientId = null;
				try {
					String doctor_id = rsMetro.getString("doctor_id");
					String fullname = rsMetro.getString("fullname");
					String title = rsMetro.getString("title");
					String address = rsMetro.getString("address");
					String phone = rsMetro.getString("phone");
					String email = rsMetro.getString("email");
					Integer sex = rsMetro.getInt("sex");
					Integer age = rsMetro.getInt("age");
					byte[] digitalsignature = rsMetro.getBytes("digitalsignature");

					updatePartner(connSkyOne, doctor_id, fullname, title, address, phone, email, sex, age,
							digitalsignature);
				} catch (Exception e) {
					log.error("Error update patientId: " + patientId, e);
				}
			}

		} catch (Exception e) {
			log.error("Error copyPatient: " + startDate, e);
		} finally {
			close(rsMetro);
			close(metroSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyPatient end in " + (t2 - t1) + " ms");
		System.out.println("CopyDoctor end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void copyDoctorRange(Connection connMetro, Connection connSkyOne, Long startDate, Date fromDate,
			Date toDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyPatient start copy: " + startDate);
		String sqlMetro = "select doctor_id, fullname, title, address, phone, email, sex, age, digitalsignature, date_created from \"Doctor\" where deleted = 0 ";

		sqlMetro += " and date_created between '" + fromDate + "' and '" + toDate + "'";
		sqlMetro += " order by date_created " + sortType;

		PreparedStatement metroSelect = null;
		ResultSet rsMetro = null;

		rowInsert = 0;
		rowUpdate = 0;
		try {
			metroSelect = connMetro.prepareStatement(sqlMetro);
			rsMetro = metroSelect.executeQuery();
			while (rsMetro.next()) {
				Long patientId = null;
				try {
					String doctor_id = rsMetro.getString("doctor_id");
					String fullname = rsMetro.getString("fullname");
					String title = rsMetro.getString("title");
					String address = rsMetro.getString("address");
					String phone = rsMetro.getString("phone");
					String email = rsMetro.getString("email");
					Integer sex = rsMetro.getInt("sex");
					Integer age = rsMetro.getInt("age");
					byte[] digitalsignature = rsMetro.getBytes("digitalsignature");
					Date dateCreated = rsMetro.getDate("date_created");

					updatePartner(connSkyOne, doctor_id, fullname, title, address, phone, email, sex, age,
							digitalsignature);
					System.out.println("Create date doctor: "+ Lib.dt2Str(dateCreated, "dd/MM/yyyy"));
				} catch (Exception e) {
					log.error("Error update patientId: " + patientId, e);
				}
			}

		} catch (Exception e) {
			log.error("Error copyPatient: " + startDate, e);
		} finally {
			close(rsMetro);
			close(metroSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyPatient end in " + (t2 - t1) + " ms");
		System.out.println("CopyDoctor end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public Long copyDoctorByExternalId(Connection connMetro, Connection connSkyOne, String externalId) {
		long t1 = System.currentTimeMillis();
		log.info("copyPatient start copy: " + startDate);
		String sqlMetro = "select doctor_id, fullname, title, address, phone, email, sex, age, digitalsignature from \"Doctor\" where deleted = 0 and doctor_id = ?::uuid ";
		PreparedStatement metroSelect = null;
		ResultSet rsMetro = null;

		rowInsert = 0;
		rowUpdate = 0;
		Long id = 0L;
		try {
			metroSelect = connMetro.prepareStatement(sqlMetro);
			metroSelect.setString(1, externalId);
			rsMetro = metroSelect.executeQuery();
			if (rsMetro.next()) {
				Long patientId = null;
				try {
					String doctor_id = rsMetro.getString("doctor_id");
					String fullname = rsMetro.getString("fullname");
					String title = rsMetro.getString("title");
					String address = rsMetro.getString("address");
					String phone = rsMetro.getString("phone");
					String email = rsMetro.getString("email");
					Integer sex = rsMetro.getInt("sex");
					Integer age = rsMetro.getInt("age");
					byte[] digitalsignature = rsMetro.getBytes("digitalsignature");

					id = updatePartner(connSkyOne, doctor_id, fullname, title, address, phone, email, sex, age,
							digitalsignature);
				} catch (Exception e) {
					log.error("Error update doctorId: " + patientId, e);
				}
			}

		} catch (Exception e) {
			log.error("Error copyDoctor: " + startDate, e);
		} finally {
			close(rsMetro);
			close(metroSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyDoctor end in " + (t2 - t1) + " ms");
		System.out.println("CopyDoctor end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
		return id;
	}

	public Long updatePartner(Connection connSkyOne, String doctor_id, String fullname, String title, String address,
			String phone, String email, Integer sex, Integer age, byte[] digitalsignature) throws SQLException {
		long t1 = System.currentTimeMillis();
		log.info("updateDoctor start copy: " + startDate);

		String sqlSelect = "select id from partner where deleted_by is null and external_uuid = ? ";
		String sqlInsert = "insert into partner(id, company_id, type_provider,search_text,code,first_name,middle_name,last_name,nick_name, name,title,address,phone_mobile,email,gender,birth_date,signature_data,signature_type,created_by,created_date,updated_by,updated_date,version,external_uuid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update partner set id = ?, company_id = ?, type_provider = ?, search_text = ?, code = ?,first_name = ?, middle_name = ?, last_name = ?, nick_name = ?,name = ?,title = ?,address = ?,phone_mobile = ?,email = ?,gender = ?,birth_date = ?,signature_data = ?,signature_type = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ?,external_uuid  = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		// check partner exist in table
		Long skyPatientId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setString(1, doctor_id);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyPatientId = rsSelect.getLong("id");
		}

		address = address != null ? address : "";
		phone = phone != null ? phone : "";
		email = email != null ? email : "";

		fullname = fullname.toUpperCase();
		String firsName = Lib.getName(fullname, Const.TYPE_NAME.FIRST_NAME);
		String middleName = Lib.getName(fullname, Const.TYPE_NAME.MIDDLE_NAME);
		String lastName = Lib.getName(fullname, Const.TYPE_NAME.LAST_NAME);
		String codeName = Lib.viLatin(Lib.getName(fullname, Const.TYPE_NAME.CODE));
		String nickName = Lib.viLatin(Lib.getName(fullname, Const.TYPE_NAME.ACCOUNT)).toLowerCase();
		String searchText = Lib.viLatin(codeName + " " + fullname + " " + address).toUpperCase().replace("NULL", "")
				.trim();

		Long updatedBy = 0L;
		Long updatedDate = new Date().getTime();
		Integer version = 1;
		Integer typeMedic = 1; // doctor

		if (skyPatientId == null) {
			skyPatientId = getId("Partner", connSkyOne);
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyPatientId);
			stmtInsert.setLong(2, companyId);
			stmtInsert.setInt(3, typeMedic);
			stmtInsert.setString(4, searchText);
			stmtInsert.setString(5, codeName);
			stmtInsert.setString(6, firsName);
			stmtInsert.setString(7, middleName);
			stmtInsert.setString(8, lastName);
			stmtInsert.setString(9, nickName);
			stmtInsert.setString(10, fullname.toUpperCase());
			stmtInsert.setString(11, title);
			stmtInsert.setString(12, address);
			stmtInsert.setString(13, phone);
			stmtInsert.setString(14, email);
			stmtInsert.setInt(15, sex);
			stmtInsert.setLong(16, new Date(age).getTime());
			stmtInsert.setBytes(17, digitalsignature);
			stmtInsert.setString(18, digitalsignature != null ? "png" : null);
			stmtInsert.setLong(19, updatedBy);
			stmtInsert.setLong(20, updatedDate);
			stmtInsert.setLong(21, updatedBy);
			stmtInsert.setLong(22, updatedDate);
			stmtInsert.setInt(23, version);
			stmtInsert.setString(24, doctor_id);

			stmtInsert.executeUpdate();
			rowInsert++;
			System.out.println("inserted doctor.. (" + codeName + ")");
		} else {
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyPatientId);
			stmtUpdate.setLong(2, companyId);
			stmtUpdate.setInt(3, typeMedic);
			stmtUpdate.setString(4, searchText);
			stmtUpdate.setString(5, codeName);
			stmtUpdate.setString(6, firsName);
			stmtUpdate.setString(7, middleName);
			stmtUpdate.setString(8, lastName);
			stmtUpdate.setString(9, nickName);
			stmtUpdate.setString(10, fullname);
			stmtUpdate.setString(11, title);
			stmtUpdate.setString(12, address);
			stmtUpdate.setString(13, phone);
			stmtUpdate.setString(14, email);
			stmtUpdate.setInt(15, sex);
			stmtUpdate.setLong(16, new Date(age).getTime());
			stmtUpdate.setBytes(17, digitalsignature);
			stmtUpdate.setString(18, digitalsignature != null ? "png" : null);
			stmtUpdate.setLong(19, updatedBy);
			stmtUpdate.setLong(20, updatedDate);
			stmtUpdate.setLong(21, updatedBy);
			stmtUpdate.setLong(22, updatedDate);
			stmtUpdate.setInt(23, version);
			stmtUpdate.setString(24, doctor_id);
			stmtUpdate.setLong(25, skyPatientId);

			stmtUpdate.executeUpdate();
			rowUpdate++;
			System.out.println("updated doctor.. (" + codeName + ")");
		}

		long t2 = System.currentTimeMillis();
		log.info("updateDoctor end in " + (t2 - t1) + " ms");
		return skyPatientId;
	}

	public void copySignature() {
		long t1 = System.currentTimeMillis();
		log.info("copyDoctor start copy: " + startDate);
		String sqlMetro = "select doctor_id, fullname, title, address, phone, email, sex, age, digitalsignature from \"Doctor\" where deleted = 0 and digitalsignature is not null ";
		PreparedStatement metroSelect = null;
		ResultSet rsMetro = null;

		rowInsert = 0;
		rowUpdate = 0;
		try {
			Connection connMetro = getMetroConnection();
			metroSelect = connMetro.prepareStatement(sqlMetro);
			rsMetro = metroSelect.executeQuery();
			while (rsMetro.next()) {
				Long patientId = null;
				try {
					String doctor_id = rsMetro.getString("doctor_id");
					String fullname = Lib.viLatin(rsMetro.getString("fullname"));
					byte[] digitalsignature = rsMetro.getBytes("digitalsignature");

					File file = new File("/signature/" + fullname + ".png");
					if (!file.exists()) {
						// file.mkdirs();
					}

					OutputStream os = new FileOutputStream(file);
					os.write(digitalsignature);
					System.out.println("Successfully" + " byte inserted");

					// Close the file
					os.close();

				} catch (Exception e) {
					log.error("Error update doctorId: " + patientId, e);
				}
			}

		} catch (Exception e) {
			log.error("Error copyDoctor: " + startDate, e);
		} finally {
			close(rsMetro);
			close(metroSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyDoctor end in " + (t2 - t1) + " ms");
		System.out.println("CopyDoctor end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

}
