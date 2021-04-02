package vn.com.skyone.app2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class CopyPatients extends CopyBase {

	public CopyPatients(Integer nodeId) {
		super(nodeId);
	}

	public CopyPatients(String[] args) {
		super(args);
	}

	/**
	 * 
	 * @param path
	 */
	public void copyPatients() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("copyPatients connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("copyPatients connect SKY ok");
		} catch (SQLException e) {
			log.error("copyPatients Connect database error", e);
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("copyPatients start: " + (new Date()));
		log.info("copyPatients from: " + startDate);
		System.out.println("copyPatients start: " + (new Date()));

		copyPatients(connMetro, connSkyOne, sysDate);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("copyPatients end in " + (t2 - t1) + " ms");
	}
	
	public void copyPatientsRange() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("copyPatients connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("copyPatients connect SKY ok");
		} catch (SQLException e) {
			log.error("copyPatients Connect database error", e);
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("copyPatients start: " + (new Date()));
		log.info("copyPatients from: " + startDate);
		System.out.println("copyPatients start: " + (new Date()));

		copyPatientsRange(connMetro, connSkyOne, sysDate, fromDate, toDate);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("copyPatients end in " + (t2 - t1) + " ms");
	}

	public void copyPatients(Connection connMetro, Connection connSkyOne, Long startDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyPatient start copy: " + startDate);
		String sqlMetro = "select patients_id,code,fullname, address, phone, email, sex, age, dob, date_created from \"Patients\" where deleted = 0 ";
		if (!Lib.isEmpty(Const.PATIENT_SORT)) {
			sqlMetro += Const.PATIENT_SORT;
		}
		if (Const.PATIENT_LIMIT > 0) {
			sqlMetro += " limit " + Const.PATIENT_LIMIT;
		}
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
					String patients_id = rsMetro.getString("patients_id");
					String code = rsMetro.getString("code");
					String fullname = rsMetro.getString("fullname");
					String address = rsMetro.getString("address");
					String phone = rsMetro.getString("phone");
					String email = rsMetro.getString("email");
					Integer sex = rsMetro.getInt("sex");
					Integer age = rsMetro.getInt("age");
					Date dob = rsMetro.getDate("dob");
					Date dateCreated = rsMetro.getDate("date_created");

					updatePartner(connSkyOne, patients_id, code, fullname, address, phone, email, sex, age, dob,
							dateCreated);
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
		System.out.println("copyPatients end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}
	
	public void copyPatientsRange(Connection connMetro, Connection connSkyOne, Long startDate, Date fromDate, Date toDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyPatient start copy: " + startDate);
		String sqlMetro = "select patients_id,code,fullname, address, phone, email, sex, age, dob, date_created from \"Patients\" where deleted = 0 ";

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
					String patients_id = rsMetro.getString("patients_id");
					String code = rsMetro.getString("code");
					String fullname = rsMetro.getString("fullname");
					String address = rsMetro.getString("address");
					String phone = rsMetro.getString("phone");
					String email = rsMetro.getString("email");
					Integer sex = rsMetro.getInt("sex");
					Integer age = rsMetro.getInt("age");
					Date dob = rsMetro.getDate("dob");
					Date dateCreated = rsMetro.getDate("date_created");

					updatePartner(connSkyOne, patients_id, code, fullname, address, phone, email, sex, age, dob,
							dateCreated);
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
		System.out.println("copyPatients end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public Long copyPatientsByExternalId(Connection connMetro, Connection connSkyOne, String externalId) {
		long t1 = System.currentTimeMillis();
		log.info("copyPatient start copy: " + startDate);
		String sqlMetro = "select patients_id,code,fullname, address, phone, email, sex, age, dob, date_created from \"Patients\" where deleted = 0 and patients_id = ?::uuid";

		PreparedStatement metroSelect = null;
		ResultSet rsMetro = null;

		rowInsert = 0;
		rowUpdate = 0;
		Long id = 0L;
		try {
			metroSelect = connMetro.prepareStatement(sqlMetro);
			metroSelect.setString(1, externalId);
			rsMetro = metroSelect.executeQuery();
			while (rsMetro.next()) {
				Long patientId = null;
				try {
					String patients_id = rsMetro.getString("patients_id");
					String code = rsMetro.getString("code");
					String fullname = rsMetro.getString("fullname");
					String address = rsMetro.getString("address");
					String phone = rsMetro.getString("phone");
					String email = rsMetro.getString("email");
					Integer sex = rsMetro.getInt("sex");
					Integer age = rsMetro.getInt("age");
					Date dob = rsMetro.getDate("dob");
					Date dateCreated = rsMetro.getDate("date_created");

					id = updatePartner(connSkyOne, patients_id, code, fullname, address, phone, email, sex, age, dob,
							dateCreated);
					System.out.println("Create date: "+ Lib.dt2Str(dateCreated, "yyyyMMdd"));
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
		System.out.println("copyPatients end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
		return id;
	}

	public Long updatePartner(Connection connSkyOne, String patients_id, String code, String fullname, String address,
			String phone, String email, Integer sex, Integer age, Date dob, Date dateCreated) throws SQLException {
		long t1 = System.currentTimeMillis();
		log.info("updatePatient start copy: " + startDate);

		String sqlSelect = "select id from partner where deleted_by is null and external_uuid = ? ";
		String sqlInsert = "insert into partner(id, company_id, type_patient,search_text,code,first_name,middle_name,last_name,nick_name, name,title,address,phone_mobile,email,gender,birth_date,created_by,created_date,updated_by,updated_date,version,external_uuid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update partner set id = ?, company_id = ?, type_patient = ?, search_text = ?, code = ?,first_name = ?, middle_name = ?, last_name = ?, nick_name = ?,name = ?,title = ?,address = ?,phone_mobile = ?,email = ?,gender = ?,birth_date = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ?,external_uuid  = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		// check partner exist in table
		Long skyPatientId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setString(1, patients_id);
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
		String codeName = code;
		String nickName = Lib.viLatin(Lib.getName(fullname, Const.TYPE_NAME.ACCOUNT)).toLowerCase();
		String searchText = Lib.viLatin(codeName + " " + fullname + " " + address).toUpperCase().replace("NULL", "")
				.trim();

		Long createdDate = getMilliSecond(dateCreated);
		Long updatedBy = Const.CREATED_ID;
		Long updatedDate = getSysDate();
		Integer version = 1;
		Integer typePatient = 1; // Patient
		sex = (sex != null && sex == 0) ? 2 : sex;

		if (skyPatientId == null) {
			skyPatientId = getId("Partner", connSkyOne);
			log.info("Insert (UUID: " + patients_id + ", CODE: " + code + ", ID: " + skyPatientId + ")");
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyPatientId);
			stmtInsert.setLong(2, companyId);
			stmtInsert.setInt(3, typePatient);
			stmtInsert.setString(4, searchText);
			stmtInsert.setString(5, codeName);
			stmtInsert.setString(6, firsName);
			stmtInsert.setString(7, middleName);
			stmtInsert.setString(8, lastName);
			stmtInsert.setString(9, nickName);
			stmtInsert.setString(10, fullname.toUpperCase());
			stmtInsert.setString(11, "");
			stmtInsert.setString(12, address);
			stmtInsert.setString(13, phone);
			stmtInsert.setString(14, email);
			stmtInsert.setInt(15, sex);
			stmtInsert.setLong(16, getMilliSecond(dob));
			stmtInsert.setLong(17, createdId);
			stmtInsert.setLong(18, createdDate);
			stmtInsert.setLong(19, updatedBy);
			stmtInsert.setLong(20, updatedDate);
			stmtInsert.setInt(21, version);
			stmtInsert.setString(22, patients_id);

			stmtInsert.executeUpdate();
			rowInsert++;
			System.out.println("inserted patient.. (" + code + ")");
		} else {
			log.info("Update (UUID: " + patients_id + ", CODE: " + code + ", ID: " + skyPatientId + ")");
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyPatientId);
			stmtUpdate.setLong(2, companyId);
			stmtUpdate.setInt(3, typePatient);
			stmtUpdate.setString(4, searchText);
			stmtUpdate.setString(5, codeName);
			stmtUpdate.setString(6, firsName);
			stmtUpdate.setString(7, middleName);
			stmtUpdate.setString(8, lastName);
			stmtUpdate.setString(9, nickName);
			stmtUpdate.setString(10, fullname);
			stmtUpdate.setString(11, "");
			stmtUpdate.setString(12, address);
			stmtUpdate.setString(13, phone);
			stmtUpdate.setString(14, email);
			stmtUpdate.setInt(15, sex);
			stmtUpdate.setLong(16, getMilliSecond(dob));
			stmtUpdate.setLong(17, createdId);
			stmtUpdate.setLong(18, createdDate);
			stmtUpdate.setLong(19, updatedBy);
			stmtUpdate.setLong(20, updatedDate);
			stmtUpdate.setInt(21, version);
			stmtUpdate.setString(22, patients_id);
			stmtUpdate.setLong(23, skyPatientId);

			stmtUpdate.executeUpdate();
			rowUpdate++;
			System.out.println("updated patient.. (" + code + ")");
		}

		long t2 = System.currentTimeMillis();
		log.info("updatePatient end in " + (t2 - t1) + " ms");

		// UPDATE PATIENT
		System.out.println("==Patient==");
		updatePatient(connSkyOne, skyPatientId, code, createdId, createdDate);
		System.out.println("==Patient(ID: " + skyPatientId + ")==");

		return skyPatientId;
	}

	public void updatePatient(Connection connSkyOne, Long partnerId, String code, Long createId, Long createDate)
			throws SQLException {
		String sqlSelect = "select id from patient where id = ? ";
		String sqlInsert = "insert into patient(id, code, first_visit_date, created_by,created_date,updated_by,updated_date,version) VALUES(?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update patient set id = ?, code = ?, first_visit_date = ?, created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		Long skyPatientId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setLong(1, partnerId);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyPatientId = rsSelect.getLong("id");
		}

		Long updatedBy = 0L;
		Long updatedDate = new Date().getTime();
		Integer version = 1;

		if (skyPatientId == null) {
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, partnerId);
			stmtInsert.setString(2, code);
			stmtInsert.setLong(3, createDate);

			stmtInsert.setLong(4, createId);
			stmtInsert.setLong(5, createDate);
			stmtInsert.setLong(6, updatedBy);
			stmtInsert.setLong(7, updatedDate);
			stmtInsert.setInt(8, version);

			stmtInsert.executeUpdate();
			rowInsert++;
			System.out.println("--insert patient.. ");
		} else {
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, partnerId);
			stmtUpdate.setString(2, code);
			stmtUpdate.setLong(3, createDate);

			stmtUpdate.setLong(4, createId);
			stmtUpdate.setLong(5, createDate);
			stmtUpdate.setLong(6, updatedBy);
			stmtUpdate.setLong(7, updatedDate);
			stmtUpdate.setInt(8, version);
			stmtUpdate.setLong(9, partnerId);

			stmtUpdate.executeUpdate();
			rowInsert++;
			System.out.println("--update patient.. ");
		}

	}

}
