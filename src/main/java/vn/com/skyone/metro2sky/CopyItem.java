package vn.com.skyone.metro2sky;

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
    public void copyTest() {
        long t1 = System.currentTimeMillis();
        /*
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

        
        log.info("copyItem start: " + (new Date()));
        log.info("copyItem from: " + startDate);
        System.out.println("copyItem start: " + (new Date()));

        // copy item
        copyTest(connMetro, connSkyOne, sysDate);

        close(connMetro);
        close(connSkyOne);
*/
        log.error("loi ok: "+ new Date());
        long t2 = System.currentTimeMillis();
        log.info("copyItem end in " + (t2 - t1) + " ms");
    }

    public void copyTest(Connection connMetro, Connection connSkyOne, Long startDate) {
        long t1 = System.currentTimeMillis();
        log.info("copyItem start copy: " + startDate);
        // and code not in ('NS20','NS20_1', 'NS17', 'NS23')
        String sqlMetro = "select id, name, remove_accent(name) as name_remove from partner where name like '%PHƯƠNG HÀ' AND external_uuid is not null and type_provider > 0";

        PreparedStatement metroSelect = null;
        ResultSet rsMetro = null;

        rowInsert = 0;
        rowUpdate = 0;
        try {
            metroSelect = connSkyOne.prepareStatement(sqlMetro);
            rsMetro = metroSelect.executeQuery();
            while (rsMetro.next()) {
                Long patientId = null;
                try {
                    Long id = rsMetro.getLong("id");
                    String name = rsMetro.getString("name");
                    String nameRemove = rsMetro.getString("name_remove");
                    System.out.println("Id: " + id + "; Name: " + name + "; Name remove: " + nameRemove
                            + "; Check Accent: " + Lib.checkAccent(nameRemove));
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
        log.info("copyTest end in " + (t2 - t1) + " ms");
        System.out.println("copyTest end in " + (t2 - t1) + " ms");
        System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
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

    /**
     * 
     * @param path
     */
    public void copyItem(String procedure) {
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

    /**
     * 
     * @param path
     */
    public void copyMaterialItem() {
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
        copyMaterialItem(connMetro, connSkyOne, sysDate);

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
        // and code not in ('NS20','NS20_1', 'NS17', 'NS23')
        String sqlMetro = "select * from \"EndoscopicProcedure\" where deleted = 0 order by code, cost ";

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

        // String sqlSelect = "select distinct i.id from item i join item_price
        // ip on i.id = ip.item_id where i.name like (? || '%') and ip.price = ?
        // ";
        String sqlSelect = "select distinct i.id from item i where deleted_by is null and i.name = ? ";
        String sqlInsert = "insert into item(id, company_id, external_uuid, type_service, code, name, origin_name, cpt_name, base_item, image, image_type, group_id, category_id,created_by,created_date,updated_by,updated_date,version, type, av_indication,screen, search_text, reg_code, cpt_code, barcode, unit_id, modality, sub_included, external_code ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update item set id = ?, company_id = ?, external_uuid = ?, type_service = ?, code = ?, name = ?, origin_name = ?, cpt_name = ?, base_item = ?, image = ?,image_type = ?, group_id = ?, category_id = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ?, type = ?, av_indication = ?,screen = ?, search_text = ?, reg_code = ?, cpt_code = ?, barcode = ?, unit_id = ?, modality = ?, sub_included = ?, external_code = ? where id = ?";

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

        String newCode = getCode(connSkyOne, name, "NS20_1".equals(code) ? "KB" : priority ? "NS" : "TT");

        // check partner exist in table
        Long skyItemId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, name);
        // stmtSelect.setDouble(2, cost);
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

            stmtInsert.setString(5, newCode);
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
            stmtInsert.setString(29, code);

            stmtInsert.executeUpdate();
            rowInsert++;
            System.out.println("--insert Item..");
        } else {
            stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
            stmtUpdate.setLong(1, skyItemId);
            stmtUpdate.setLong(2, companyId);
            stmtUpdate.setString(3, endoscopicprocedureId);
            stmtUpdate.setInt(4, typeService);

            stmtUpdate.setString(5, newCode);
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
            stmtUpdate.setString(29, code);

            stmtUpdate.setLong(30, skyItemId);

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

    public void copyMaterialItem(Connection connMetro, Connection connSkyOne, Long startDate) {
        long t1 = System.currentTimeMillis();
        log.info("copyItem start copy: " + startDate);
        String sqlMetro = "select * from \"Materials\" where deleted = 0 order by name, materialtype ";

        PreparedStatement metroSelect = null;
        ResultSet rsMetro = null;

        rowInsert = 0;
        rowUpdate = 0;
        int i = 0;
        try {
            metroSelect = connMetro.prepareStatement(sqlMetro);
            rsMetro = metroSelect.executeQuery();
            while (rsMetro.next()) {
                Long patientId = null;
                try {
                    String endoscopicprocedureId = rsMetro.getString("materials_id");
                    String code = rsMetro.getString("code");
                    String name = rsMetro.getString("name");
                    Integer materialtype = rsMetro.getInt("materialtype");
                    String unit = rsMetro.getString("unit");

                    String reportname = "";
                    String reportnameen = "";
                    String note = rsMetro.getString("note");
                    Boolean priority = true;
                    byte[] image = null;

                    Double cost = 0D;
                    Double wageofdoctorperform = 0D;
                    Double wageofdoctorreferral = 0D;
                    Double wageofanesthetist = 0D;
                    Double wageofnursing = 0D;

                    Timestamp dateCreated = rsMetro.getTimestamp("date_created");
                    Timestamp modifiedDate = rsMetro.getTimestamp("date_modified");

                    System.out.println((materialtype == 0 ? "Thuoc-> " : "Vat tu-> ") + (++i) + " Code: " + code
                            + "; name: " + name);

                    updateMaterialItem(connSkyOne, endoscopicprocedureId, code, name, reportname, reportnameen, note,
                            priority, image, cost, wageofdoctorperform, wageofdoctorreferral, wageofanesthetist,
                            wageofnursing, dateCreated, modifiedDate, unit, materialtype);
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

    public void updateMaterialItem(Connection connSkyOne, String endoscopicprocedureId, String code, String name,
            String reportname, String reportnameen, String note, Boolean priority, byte[] image, Double cost,
            Double wageofdoctorperform, Double wageofdoctorreferral, Double wageofanesthetist, Double wageofnursing,
            Date dateCreated, Date modifiedDate, String unit, Integer materialtype) throws SQLException {
        long t1 = System.currentTimeMillis();
        log.info("updateItem start copy: " + startDate);

        String sqlSelect = "select id from item where name = ? ";
        String sqlInsert = "insert into item(id, company_id,  external_uuid, type_service, code, name, origin_name, cpt_name, base_item, image, image_type, group_id, category_id,created_by,created_date,updated_by,updated_date,version, type, type_goods,screen, search_text, reg_code, cpt_code, barcode, unit_id, modality, sub_included, av_sale, av_purchase,av_indication, av_male, av_female, av_inpatient, av_outpatient, av_children,av_pregnancy, av_lactation,goods_lot, goods_expiry,goods_model,goods_serial, atc_code) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update item set id = ?, company_id = ?,  external_uuid = ?, type_service = ?, code = ?, name = ?, origin_name = ?, cpt_name = ?, base_item = ?, image = ?,image_type = ?, group_id = ?, category_id = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ?, type = ?, type_goods = ?,screen = ?, search_text = ?, reg_code = ?, cpt_code = ?, barcode = ?, unit_id = ?, modality = ?, sub_included = ?, av_sale = ?, av_purchase = ?,av_indication = ?, av_male = ?, av_female = ?, av_inpatient = ?, av_outpatient = ?, av_children = ?,av_pregnancy = ?, av_lactation = ?,goods_lot = ?, goods_expiry = ?,goods_model = ?,goods_serial = ?, atc_code = ? where id = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        Long createdDate = getMilliSecond(dateCreated);
        Long updatedDate = getSysDate();
        Integer version = 1;

        Integer typeService = 0;
        Integer itemType = 2;
        String screen = "";
        Integer type_goods = 31; // handle type
        if (materialtype != 0) {
            type_goods = 36;
        }

        Long unitId = 0L;
        if (unitId == 0) {
            unitId = updateUnit(connSkyOne, unit);
        }

        if (true) {
            // code = "";
            code = getCode(connSkyOne, name, materialtype);
        }

        String searchText = Lib.viLatin(code + " " + name).trim().toUpperCase();
        String atcCode = "";
        String regCode = "";
        String cptCode = "";
        String barcode = "";

        Long endoGroup = 102L;
        Long endoCategory = 0L;
        String modality = "";

        if (!priority) {
            screen = "";
            modality = "";
        }

        code = code != null ? code : "";

        Integer av_sale = 1;
        Integer av_purchase = 1;
        Integer av_indication = 1;
        Integer av_male = 1;
        Integer av_female = 1;
        Integer av_inpatient = 1;
        Integer av_outpatient = 1;
        Integer av_children = 0;
        Integer av_pregnancy = 0;
        Integer av_lactation = 0;

        Integer goods_lot = 1;
        Integer goods_expiry = 1;
        Integer goods_model = 0;
        Integer goods_serial = 0;

        // check partner exist in table
        Long skyItemId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, name);
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
            stmtInsert.setInt(20, type_goods);
            stmtInsert.setString(21, screen);

            stmtInsert.setString(22, searchText);
            stmtInsert.setString(23, regCode);
            stmtInsert.setString(24, cptCode);
            stmtInsert.setString(25, barcode);
            stmtInsert.setLong(26, unitId);
            stmtInsert.setString(27, modality);
            stmtInsert.setInt(28, priority ? 1 : 0);

            stmtInsert.setInt(29, av_sale);
            stmtInsert.setInt(30, av_purchase);
            stmtInsert.setInt(31, av_indication);
            stmtInsert.setInt(32, av_male);
            stmtInsert.setInt(33, av_female);
            stmtInsert.setInt(34, av_inpatient);
            stmtInsert.setInt(35, av_outpatient);
            stmtInsert.setInt(36, av_children);
            stmtInsert.setInt(37, av_pregnancy);
            stmtInsert.setInt(38, av_lactation);
            stmtInsert.setInt(39, goods_lot);
            stmtInsert.setInt(40, goods_expiry);
            stmtInsert.setInt(41, goods_model);
            stmtInsert.setInt(42, goods_serial);
            stmtInsert.setString(43, atcCode);

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
            stmtUpdate.setInt(20, type_goods);
            stmtUpdate.setString(21, screen);

            stmtUpdate.setString(22, searchText);
            stmtUpdate.setString(23, regCode);
            stmtUpdate.setString(24, cptCode);
            stmtUpdate.setString(25, barcode);
            stmtUpdate.setLong(26, unitId);
            stmtUpdate.setString(27, modality);
            stmtUpdate.setInt(28, priority ? 1 : 0);

            stmtUpdate.setInt(29, av_sale);
            stmtUpdate.setInt(30, av_purchase);
            stmtUpdate.setInt(31, av_indication);
            stmtUpdate.setInt(32, av_male);
            stmtUpdate.setInt(33, av_female);
            stmtUpdate.setInt(34, av_inpatient);
            stmtUpdate.setInt(35, av_outpatient);
            stmtUpdate.setInt(36, av_children);
            stmtUpdate.setInt(37, av_pregnancy);
            stmtUpdate.setInt(38, av_lactation);
            stmtUpdate.setInt(39, goods_lot);
            stmtUpdate.setInt(40, goods_expiry);
            stmtUpdate.setInt(41, goods_model);
            stmtUpdate.setInt(42, goods_serial);
            stmtUpdate.setString(43, atcCode);

            stmtUpdate.setLong(44, skyItemId);

            stmtUpdate.executeUpdate();
            rowUpdate++;
            System.out.println("--update Item..");
        }

        long t2 = System.currentTimeMillis();
        log.info("updateItem end in " + (t2 - t1) + " ms");

        // update item price
        updateItemPrice(connSkyOne, skyItemId, cost, wageofdoctorperform, wageofdoctorreferral, wageofanesthetist,
                wageofnursing, dateCreated, modifiedDate, unitId);
    }

    public void updateItemPrice(Connection connSkyOne, Long itemId, Double cost, Double wageofdoctorperform,
            Double wageofdoctorreferral, Double wageofanesthetist, Double wageofnursing, Date dateCreated,
            Date modifiedDate) throws SQLException {
        updateItemPrice(connSkyOne, itemId, cost, wageofdoctorperform, wageofdoctorreferral, wageofanesthetist,
                wageofnursing, dateCreated, modifiedDate, Const.UNIT_ID);
    }

    public void updateItemPrice(Connection connSkyOne, Long itemId, Double cost, Double wageofdoctorperform,
            Double wageofdoctorreferral, Double wageofanesthetist, Double wageofnursing, Date dateCreated,
            Date modifiedDate, Long unitId) throws SQLException {
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
        // Long unitId = Const.UNIT_ID;
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
        String sqlSkyone = "select id from item where deleted_by is null and sub_included = 0 and upper(code) like 'TT0%' order by code asc ";

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

    public Long updateUnit(Connection connSkyOne, String name) throws SQLException {
        String sqlSelect = "select id from item_unit where deleted_by is null and upper(code) = ?";
        String sqlInsert = "insert into item_unit(id, company_id, code, name) VALUES(?,?,?,?)";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        ResultSet rsSelect = null;

        String code = Lib.removeAccent(name).trim().toUpperCase();
        if (Lib.isEmpty(code)) {
            code = "UNIT";
            name = "";
        }

        // check partner exist in table
        Long skyUnitId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, code);
        rsSelect = stmtSelect.executeQuery();
        if (rsSelect.next()) {
            skyUnitId = rsSelect.getLong("id");
        }

        if (skyUnitId == null) {
            skyUnitId = getId("ItemUnit", connSkyOne);
            stmtInsert = connSkyOne.prepareStatement(sqlInsert);
            stmtInsert.setLong(1, skyUnitId);
            stmtInsert.setLong(2, 0);
            stmtInsert.setString(3, code);
            stmtInsert.setString(4, Lib.capitalize(name));
            stmtInsert.executeQuery();
        }

        return skyUnitId;
    }

    public String getCode(Connection connSkyOne, String name, Integer materialtype, String type) throws SQLException {
        String sqlSelect = "select code from item where deleted_by is null and upper(name) = ?";
        PreparedStatement stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, name.toUpperCase());
        ResultSet rsSelect = stmtSelect.executeQuery();
        String code = "";
        if (rsSelect.next()) {
            code = rsSelect.getString("code");
        }

        if (Lib.isEmpty(code)) {
            code = Lib.increaseCode(maxCode(connSkyOne, name, materialtype));
        }

        // code = "";
        return code;
    }

    public String getCode(Connection connSkyOne, String name, Integer materialtype) throws SQLException {
        String sqlSelect = "select code from item where deleted_by is null and upper(name) = ?";
        PreparedStatement stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, name.toUpperCase());
        ResultSet rsSelect = stmtSelect.executeQuery();
        String code = "";
        if (rsSelect.next()) {
            code = rsSelect.getString("code");
        }

        if (Lib.isEmpty(code)) {
            code = Lib.increaseCode(maxCode(connSkyOne, name, materialtype));
        }

        // code = "";
        return code;
    }

    public String maxCode(Connection connSkyOne, String name, Integer materialtype) throws SQLException {
        String subName = "";
        if (name != null && name.length() > 1) {
            subName = (materialtype == 0 ? "T" : "V") + Lib.removeAccent(name.substring(0, 2)).toUpperCase();
        }
        String sqlSelect = "select code from item where deleted_by is null and upper(code) like (? || '%') order by code desc limit 1";
        PreparedStatement stmtSelect = null;
        ResultSet rsSelect = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, subName);
        rsSelect = stmtSelect.executeQuery();
        String code = "";
        if (rsSelect.next()) {
            code = rsSelect.getString("code");
        }
        if (Lib.isEmpty(code)) {
            code = subName + "000";
        }
        return code;
    }

    public String getCode(Connection connSkyOne, String name, String type) throws SQLException {
        String sqlSelect = "select code from item where deleted_by is null and upper(name) = ?";
        PreparedStatement stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, name.toUpperCase());
        ResultSet rsSelect = stmtSelect.executeQuery();
        String code = "";
        if (rsSelect.next()) {
            code = rsSelect.getString("code");
        }

        if (Lib.isEmpty(code)) {
            code = maxIncreaseCode(connSkyOne, name, type);
        }

        // code = "";
        return code;
    }

    public String maxIncreaseCode(Connection connSkyOne, String name, String type) throws SQLException {
        String sqlSelect = "select code from item where deleted_by is null and upper(code) like (? || '%') order by code desc limit 1";
        PreparedStatement stmtSelect = null;
        ResultSet rsSelect = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, type);
        rsSelect = stmtSelect.executeQuery();
        String code = "";
        if (rsSelect.next()) {
            code = rsSelect.getString("code");
        }
        if (Lib.isEmpty(code)) {
            code = type + "0000";
        }
        String suffix = code.substring(2, code.length());
        if (Lib.isNumeric(suffix)) {
            return code.substring(0, 2) + Lib.num2Str(Integer.valueOf(suffix) + 1, "0000");
        }
        return "";
    }

}
