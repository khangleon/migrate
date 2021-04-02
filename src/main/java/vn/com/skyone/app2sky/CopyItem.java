package vn.com.skyone.app2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class CopyItem extends Common {

	public CopyItem(Integer nodeId) {
		super(nodeId);
	}

	public CopyItem(String[] args) {
		super(args);
	}

	/**
	 * 
	 * @param path
	 */
	public void copyItem() {
		Connection connMetro = null;
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connMetro = getMetroConnection();
			log.info("copyItem connect Metro ok");
			connSkyOne = getSkyOneConnection();
			log.info("copyItem connect SKY ok");
		} catch (SQLException e) {
			log.error("copyItem Connect database error", e);
			close(connMetro);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("copyItem start: " + (new Date()));
		log.info("copyItem from: " + startDate);
		System.out.println("copyItem start: " + (new Date()));

		// copy item
		copyItem(connMetro, connSkyOne, sysDate);

		close(connMetro);
		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("copyItem end in " + (t2 - t1) + " ms");
	}

	public void copyItemSub() {
		Connection connSkyOne = null;
		Long sysDate = getSysDate();
		try {
			connSkyOne = getSkyOneConnection();
			log.info("copyItemSub connect SKY ok");
		} catch (SQLException e) {
			log.error("copyItemSub Connect database error", e);
			close(connSkyOne);
			return;
		}

		long t1 = System.currentTimeMillis();
		log.info("copyItemSub start: " + (new Date()));
		log.info("copyItemSub from: " + startDate);
		System.out.println("copyItemSub start: " + (new Date()));

		// copy sub_item
		copyItemSub(connSkyOne, sysDate);

		close(connSkyOne);

		long t2 = System.currentTimeMillis();
		log.info("copyItemSub end in " + (t2 - t1) + " ms");
	}

	public void copyItem(Connection connMetro, Connection connSkyOne, Long startDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyItem start copy: " + startDate);
		String sqlMetro = "select * from \"EndoscopicProcedure\" where deleted = 0 order by date_created asc ";

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
					String endoscopicprocedureId = rsMetro.getString("endoscopicprocedure_id");
					String code = rsMetro.getString("code");
					String name = rsMetro.getString("name");

					String reportname = rsMetro.getString("reportname");
					String reportnameen = rsMetro.getString("reportnameen");
					String note = rsMetro.getString("note");
					Boolean priority = rsMetro.getBoolean("isprimary");
					byte[] image = rsMetro.getBytes("logo");

					Double cost = rsMetro.getDouble("cost");
					Double wageofdoctorperform = rsMetro.getDouble("wageofdoctorperform");
					Double wageofdoctorreferral = rsMetro.getDouble("wageofdoctorreferral");
					Double wageofanesthetist = rsMetro.getDouble("wageofanesthetist");
					Double wageofnursing = rsMetro.getDouble("wageofnursing");

					Timestamp dateCreated = rsMetro.getTimestamp("date_created");
					Timestamp modifiedDate = rsMetro.getTimestamp("date_modified");

					updateItem(connSkyOne, endoscopicprocedureId, code, name, reportname, reportnameen, note, priority,
							image, cost, wageofdoctorperform, wageofdoctorreferral, wageofanesthetist, wageofnursing,
							dateCreated, modifiedDate);
				} catch (Exception e) {
					log.error("Error update patientId: " + patientId, e);
				}
			}

		} catch (Exception e) {
			log.error("Error copyItem: " + startDate, e);
		} finally {
			close(rsMetro);
			close(metroSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyItem end in " + (t2 - t1) + " ms");
		System.out.println("copyItem end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void updateItem(Connection connSkyOne, String endoscopicprocedureId, String code, String name,
			String reportname, String reportnameen, String note, Boolean priority, byte[] image, Double cost,
			Double wageofdoctorperform, Double wageofdoctorreferral, Double wageofanesthetist, Double wageofnursing,
			Date dateCreated, Date modifiedDate) throws SQLException {
		long t1 = System.currentTimeMillis();
		log.info("updateItem start copy: " + startDate);

		String sqlSelect = "select id from item where external_uuid = ? ";
		String sqlInsert = "insert into item(id, company_id,  external_uuid, type_service, code, name, origin_name, cpt_name, base_item, image, image_type, group_id, category_id,created_by,created_date,updated_by,updated_date,version, item_type, av_indication,screen, search_text, reg_code, cpt_code, barcode, unit_id, modality, sub_included ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update item set id = ?, company_id = ?,  external_uuid = ?, type_service = ?, code = ?, name = ?, origin_name = ?, cpt_name = ?, base_item = ?, image = ?,image_type = ?, group_id = ?, category_id = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ?, item_type = ?, av_indication = ?,screen = ?, search_text = ?, reg_code = ?, cpt_code = ?, barcode = ?, unit_id = ?, modality = ?, sub_included = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		Long createdDate = getMilliSecond(dateCreated);
		Long updatedDate = getSysDate();
		Integer version = 1;

		Integer typeService = 3;
		Integer itemType = 1;
		String screen = Const.ENDO_SCREEN;
		Integer avIndication = 1;
		Long unitId = Const.UNIT_ID;

		String searchText = Lib.viLatin(code + " " + name).trim().toUpperCase();
		String regCode = "";
		String cptCode = "";
		String barcode = "";

		Long endoGroup = Const.ENDO_GROUP;
		Long endoCategory = Const.ENDO_CATEGORY;
		String modality = Const.MODALITY;

		if (!priority) {
			screen = "";
			modality = "";
		}

		// check partner exist in table
		Long skyItemId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setString(1, endoscopicprocedureId);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyItemId = rsSelect.getLong("id");
		}

		if (skyItemId == null) {
			skyItemId = getId("Item", connSkyOne);
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);

			stmtInsert.setLong(1, skyItemId);
			stmtInsert.setLong(2, companyId);
			stmtInsert.setString(3, endoscopicprocedureId);
			stmtInsert.setInt(4, typeService);

			stmtInsert.setString(5, code);
			stmtInsert.setString(6, name);
			stmtInsert.setString(7, reportname);
			stmtInsert.setString(8, reportnameen);
			stmtInsert.setInt(9, priority ? 1 : 0);
			stmtInsert.setBytes(10, image);
			stmtInsert.setString(11, image != null ? "png" : "");
			stmtInsert.setLong(12, endoGroup);
			stmtInsert.setLong(13, endoCategory);

			stmtInsert.setLong(14, createdId);
			stmtInsert.setLong(15, createdDate);
			stmtInsert.setLong(16, createdId);
			stmtInsert.setLong(17, updatedDate);
			stmtInsert.setInt(18, version);

			stmtInsert.setInt(19, itemType);
			stmtInsert.setInt(20, avIndication);
			stmtInsert.setString(21, screen);

			stmtInsert.setString(22, searchText);
			stmtInsert.setString(23, regCode);
			stmtInsert.setString(24, cptCode);
			stmtInsert.setString(25, barcode);
			stmtInsert.setLong(26, unitId);
			stmtInsert.setString(27, modality);
			stmtInsert.setInt(28, priority ? 1 : 0);

			stmtInsert.executeUpdate();
			rowInsert++;
			System.out.println("--insert Item..");
		} else {
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyItemId);
			stmtUpdate.setLong(2, companyId);
			stmtUpdate.setString(3, endoscopicprocedureId);
			stmtUpdate.setInt(4, typeService);

			stmtUpdate.setString(5, code);
			stmtUpdate.setString(6, name);
			stmtUpdate.setString(7, reportname);
			stmtUpdate.setString(8, reportnameen);
			stmtUpdate.setInt(9, priority ? 1 : 0);
			stmtUpdate.setBytes(10, image);
			stmtUpdate.setString(11, image != null ? "png" : "");
			stmtUpdate.setLong(12, endoGroup);
			stmtUpdate.setLong(13, endoCategory);

			stmtUpdate.setLong(14, createdId);
			stmtUpdate.setLong(15, createdDate);
			stmtUpdate.setLong(16, createdId);
			stmtUpdate.setLong(17, updatedDate);
			stmtUpdate.setInt(18, version);

			stmtUpdate.setInt(19, itemType);
			stmtUpdate.setInt(20, avIndication);
			stmtUpdate.setString(21, screen);

			stmtUpdate.setString(22, searchText);
			stmtUpdate.setString(23, regCode);
			stmtUpdate.setString(24, cptCode);
			stmtUpdate.setString(25, barcode);
			stmtUpdate.setLong(26, unitId);
			stmtUpdate.setString(27, modality);
			stmtUpdate.setInt(28, priority ? 1 : 0);

			stmtUpdate.setLong(29, skyItemId);

			stmtUpdate.executeUpdate();
			rowUpdate++;
			System.out.println("--update Item..");
		}

		long t2 = System.currentTimeMillis();
		log.info("updateItem end in " + (t2 - t1) + " ms");

		// update item price
		updateItemPrice(connSkyOne, skyItemId, cost, wageofdoctorperform, wageofdoctorreferral, wageofanesthetist,
				wageofnursing, dateCreated, modifiedDate);
	}

	public void updateItemPrice(Connection connSkyOne, Long itemId, Double cost, Double wageofdoctorperform,
			Double wageofdoctorreferral, Double wageofanesthetist, Double wageofnursing, Date dateCreated,
			Date modifiedDate) throws SQLException {
		log.info("updateItem start copy: " + startDate);
		System.out.println("==Begin ItemPrice==");

		String sqlSelect = "select id from item_price where item_id = ? ";
		String sqlInsert = "insert into item_price(id, item_id, unit_id, effect_date, price, perform_amount, indication_amount, anesthesiologist_amount, technician_amount, created_by,created_date,updated_by,updated_date,version,av_indication, grand_price ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlUpdate = "update item_price set id = ?, item_id = ?, unit_id = ?, effect_date = ?, price = ?, perform_amount = ?, indication_amount = ?, anesthesiologist_amount = ?, technician_amount = ?, created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ?, av_indication = ?, grand_price = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		Long createdDate = getMilliSecond(dateCreated);
		Long updatedDate = getSysDate();
		Long effectDate = getMilliSecond(modifiedDate);
		Long unitId = Const.UNIT_ID;
		Integer avIndication = 1;

		// check partner exist in table
		Long skyItemPriceId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setLong(1, itemId);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyItemPriceId = rsSelect.getLong("id");
		}

		if (skyItemPriceId == null) {
			skyItemPriceId = getId("ItemPrice", connSkyOne);
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyItemPriceId);
			stmtInsert.setLong(2, itemId);
			stmtInsert.setLong(3, unitId);
			stmtInsert.setLong(4, effectDate);

			stmtInsert.setDouble(5, cost);
			stmtInsert.setDouble(6, wageofdoctorperform);
			stmtInsert.setDouble(7, wageofdoctorreferral);
			stmtInsert.setDouble(8, wageofanesthetist);
			stmtInsert.setDouble(9, wageofnursing);

			stmtInsert.setLong(10, createdId);
			stmtInsert.setLong(11, createdDate);
			stmtInsert.setLong(12, createdId);
			stmtInsert.setLong(13, updatedDate);
			stmtInsert.setInt(14, version);
			stmtInsert.setInt(15, avIndication);
			stmtInsert.setDouble(16, cost);

			stmtInsert.executeUpdate();
			System.out.println("--insert ItemPrice..");
		} else {
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyItemPriceId);
			stmtUpdate.setLong(2, itemId);
			stmtUpdate.setLong(3, unitId);
			stmtUpdate.setLong(4, effectDate);

			stmtUpdate.setDouble(5, cost);
			stmtUpdate.setDouble(6, wageofdoctorperform);
			stmtUpdate.setDouble(7, wageofdoctorreferral);
			stmtUpdate.setDouble(8, wageofanesthetist);
			stmtUpdate.setDouble(9, wageofnursing);

			stmtUpdate.setLong(10, createdId);
			stmtUpdate.setLong(11, createdDate);
			stmtUpdate.setLong(12, createdId);
			stmtUpdate.setLong(13, updatedDate);
			stmtUpdate.setInt(14, version);
			stmtUpdate.setInt(15, avIndication);
			stmtUpdate.setDouble(16, cost);

			stmtUpdate.setLong(17, skyItemPriceId);

			stmtUpdate.executeUpdate();
			System.out.println("--update ItemPrice..");
		}

		System.out.println("==End ItemPrice==");
	}

	public void copyItemSub(Connection connSkyOne, Long sysDate) {
		long t1 = System.currentTimeMillis();
		log.info("copyItemSub start copy: " + startDate);
		String sqlSkyone = "select id from item where deleted_by is null and sub_included = 1 order by code asc ";

		PreparedStatement skyoneSelect = null;
		ResultSet rsSkyone = null;

		rowInsert = 0;
		rowUpdate = 0;
		try {
			skyoneSelect = connSkyOne.prepareStatement(sqlSkyone);
			rsSkyone = skyoneSelect.executeQuery();
			while (rsSkyone.next()) {
				Long patientId = null;
				try {
					Long id = rsSkyone.getLong("id");
					updateItemSub(connSkyOne, id);
				} catch (Exception e) {
					log.error("Error update patientId: " + patientId, e);
				}
			}

		} catch (Exception e) {
			log.error("Error copyItemSub: " + startDate, e);
		} finally {
			close(rsSkyone);
			close(skyoneSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyItemSub end in " + (t2 - t1) + " ms");
		System.out.println("copyItemSub end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void updateItemSub(Connection connSkyOne, Long itemId) {
		long t1 = System.currentTimeMillis();
		log.info("copyItemSub start copy: " + startDate);
		System.out.println("==Begin ItemSub==");
		String sqlSkyone = "select id from item where deleted_by is null and sub_included = 0 order by code asc ";

		PreparedStatement skyoneSelect = null;
		ResultSet rsSkyone = null;

		rowInsert = 0;
		rowUpdate = 0;
		try {
			skyoneSelect = connSkyOne.prepareStatement(sqlSkyone);
			rsSkyone = skyoneSelect.executeQuery();
			while (rsSkyone.next()) {
				Long patientId = null;
				try {
					Long id = rsSkyone.getLong("id");
					updateItemSub(connSkyOne, itemId, id);
				} catch (Exception e) {
					log.error("Error update patientId: " + patientId, e);
				}
			}

		} catch (Exception e) {
			log.error("Error copyItemSub: " + startDate, e);
		} finally {
			close(rsSkyone);
			close(skyoneSelect);
		}

		long t2 = System.currentTimeMillis();
		log.info("copyItemSub end in " + (t2 - t1) + " ms");
		System.out.println("copyItemSub end in " + (t2 - t1) + " ms");
		System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
	}

	public void updateItemSub(Connection connSkyOne, Long itemId, Long itemSubId) throws SQLException {
		log.info("updateItemSub start copy: " + startDate);
		System.out.println("==Begin ItemSub==");

		String sqlSelect = "select id from item_sub where deleted_by is null and main_id = ? and item_id = ?";
		String sqlInsert = "insert into item_sub(id, main_id, item_id ) VALUES(?,?,?)";
		String sqlUpdate = "update item_sub set id = ?, main_id = ?, item_id = ? where id = ?";

		PreparedStatement stmtSelect = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		ResultSet rsSelect = null;

		// check partner exist in table
		Long skyItemSubId = null;
		stmtSelect = connSkyOne.prepareStatement(sqlSelect);
		stmtSelect.setLong(1, itemId);
		stmtSelect.setLong(2, itemSubId);
		rsSelect = stmtSelect.executeQuery();
		if (rsSelect.next()) {
			skyItemSubId = rsSelect.getLong("id");
		}

		if (skyItemSubId == null) {
			skyItemSubId = getId("ItemSub", connSkyOne);
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyItemSubId);
			stmtInsert.setLong(2, itemId);
			stmtInsert.setLong(3, itemSubId);
			stmtInsert.executeUpdate();
			System.out.println("--insert ItemSub..");
		} else {
			stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
			stmtUpdate.setLong(1, skyItemSubId);
			stmtUpdate.setLong(2, itemId);
			stmtUpdate.setLong(3, itemSubId);
			stmtUpdate.setLong(4, skyItemSubId);
			stmtUpdate.executeUpdate();

			stmtUpdate.executeUpdate();
			System.out.println("--update ItemSub..");
		}
	}

}
