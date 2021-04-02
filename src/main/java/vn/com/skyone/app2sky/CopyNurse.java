package vn.com.skyone.app2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class CopyNurse extends CopyBase {

	public CopyNurse(Integer nodeId) {
		super(nodeId);
	}

	public CopyNurse(String[] args) {
		super(args);
	}

	/**
	 * 
	 * @param path
	 */
	public void copyNurse() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("copyNurse connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("copyNurse connect SKY ok");
		} catch (SQLException e) {
			log.error("copyNurse Connect database error", e);
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("copyNurse start: " + (new Date()));
		log.info("copyNurse from: " + startDate);
		System.out.println("copyNurse start: " + (new Date()));

		copyNurse(connMetro, connSkyOne, sysDate);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("copyNurse end in " + (t2 - t1) + " ms");
	}

	public void copyNurseRange() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("copyNurse connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("copyNurse connect SKY ok");
		} catch (SQLException e) {
			log.error("copyNurse Connect database error", e);
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("copyNurse start: " + (new Date()));
		log.info("copyNurse from: " + startDate);
		System.out.println("copyNurse start: " + (new Date()));

		copyNurseRange(connMetro, connSkyOne, sysDate, fromDate, toDate);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("copyNurse end in " + (t2 - t1) + " ms");
	}

	public void copyNurse(Connection connMetro, Connection connSkyOne, Long startDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyPatient start copy: " + startDate);
		String sqlMetro = "select nurse_id, fullname, title, address, phone, email, sex, age from \"Nurse\" where deleted = 0; ";
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
					String nurse_id = rsMetro.getString("nurse_id");
					String fullname = rsMetro.getString("fullname");
					String title = rsMetro.getString("title");
					String address = rsMetro.getString("address");
					String phone = rsMetro.getString("phone");
					String email = rsMetro.getString("email");
					Integer sex = rsMetro.getInt("sex");
					Integer age = rsMetro.getInt("age");

					updatePartner(connSkyOne, nurse_id, fullname, title, address, phone, email, sex, age);
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
		System.out.println("copyNurse end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void copyNurseRange(Connection connMetro, Connection connSkyOne, Long startDate, Date fromDate,
			Date toDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyPatient start copy: " + startDate);
		String sqlMetro = "select nurse_id, fullname, title, address, phone, email, sex, age, date_created from \"Nurse\" where deleted = 0 ";

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
					String nurse_id = rsMetro.getString("nurse_id");
					String fullname = rsMetro.getString("fullname");
					String title = rsMetro.getString("title");
					String address = rsMetro.getString("address");
					String phone = rsMetro.getString("phone");
					String email = rsMetro.getString("email");
					Integer sex = rsMetro.getInt("sex");
					Integer age = rsMetro.getInt("age");
					Date dateCreated = rsMetro.getDate("date_created");

					updatePartner(connSkyOne, nurse_id, fullname, title, address, phone, email, sex, age);
					System.out.println("Create date: " + Lib.dt2Str(dateCreated, "yyyyMMdd"));
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
		System.out.println("copyNurse end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public Long copyNurseByExternalId(Connection connMetro, Connection connSkyOne, String externalId) {
		long t1 = System.currentTimeMillis();
		log.info("copyPatient start copy: " + startDate);
		String sqlMetro = "select nurse_id, fullname, title, address, phone, email, sex, age from \"Nurse\" where deleted = 0 and nurse_id = ?::uuid ";
		PreparedStatement metroSelect = null;
		ResultSet rsMetro = null;

		rowInsert = 0;
		rowUpdate = 0;
		Long id = 0L;
		try {
			metroSelect = connMetro.prepareStatement(sqlMetro);
			rsMetro = metroSelect.executeQuery();
			while (rsMetro.next()) {
				Long patientId = null;
				try {
					String nurse_id = rsMetro.getString("nurse_id");
					String fullname = rsMetro.getString("fullname");
					String title = rsMetro.getString("title");
					String address = rsMetro.getString("address");
					String phone = rsMetro.getString("phone");
					String email = rsMetro.getString("email");
					Integer sex = rsMetro.getInt("sex");
					Integer age = rsMetro.getInt("age");

					id = updatePartner(connSkyOne, nurse_id, fullname, title, address, phone, email, sex, age);
				} catch (Exception e) {
					log.error("Error update nurseId: " + patientId, e);
				}
			}

		} catch (Exception e) {
			log.error("Error copyNurse: " + startDate, e);
		} finally {
			close(rsMetro);
			close(metroSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyNurse end in " + (t2 - t1) + " ms");
		System.out.println("copyNurse end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
		return id;
	}

	public Long updatePartner(Connection connSkyOne, String nurse_id, String fullname, String title, String address,
			String phone, String email, Integer sex, Integer age) throws SQLException {
		long t1 = System.currentTimeMillis();
		log.info("updateNurse start copy: " + startDate);

		String sqlSelect = "select id from partner where deleted_by is null and external_uuid = ? ";
		String sqlInsert = "insert into partner(id, company_id, type_medic,search_text,code,first_name,middle_name,last_name,nick_name, name,title,address,phone_mobile,email,gender,birth_date,created_by,created_date,updated_by,updated_date,version,external_uuid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update partner set id = ?, company_id = ?, type_medic = ?, search_text = ?, code = ?,first_name = ?, middle_name = ?, last_name = ?, nick_name = ?,name = ?,title = ?,address = ?,phone_mobile = ?,email = ?,gender = ?,birth_date = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ?,external_uuid  = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		// check partner exist in table
		Long skyPatientId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setString(1, nurse_id);
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
		Integer typeMedic = 4; // nurse

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
			stmtInsert.setString(10, fullname);
			stmtInsert.setString(11, title);
			stmtInsert.setString(12, address);
			stmtInsert.setString(13, phone);
			stmtInsert.setString(14, email);
			stmtInsert.setInt(15, sex);
			stmtInsert.setLong(16, new Date(age).getTime());
			stmtInsert.setLong(17, updatedBy);
			stmtInsert.setLong(18, updatedDate);
			stmtInsert.setLong(19, updatedBy);
			stmtInsert.setLong(20, updatedDate);
			stmtInsert.setInt(21, version);
			stmtInsert.setString(22, nurse_id);

			stmtInsert.executeUpdate();
			rowInsert++;
			System.out.println("inserted nurse.. (" + codeName + ")");
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
			stmtUpdate.setLong(17, updatedBy);
			stmtUpdate.setLong(18, updatedDate);
			stmtUpdate.setLong(19, updatedBy);
			stmtUpdate.setLong(20, updatedDate);
			stmtUpdate.setInt(21, version);
			stmtUpdate.setString(22, nurse_id);
			stmtUpdate.setLong(23, skyPatientId);

			stmtUpdate.executeUpdate();
			rowUpdate++;
			System.out.println("updated nurse.. (" + codeName + ")");
		}

		long t2 = System.currentTimeMillis();
		log.info("updateNurse end in " + (t2 - t1) + " ms");
		return skyPatientId;
	}

}
