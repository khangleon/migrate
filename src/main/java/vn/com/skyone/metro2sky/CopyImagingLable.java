package vn.com.skyone.metro2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class CopyImagingLable extends Common {

    public CopyImagingLable(Integer nodeId) {
        super(nodeId);
    }

    public CopyImagingLable(String[] args) {
        super(args);
    }

    /**
     * 
     * @param path
     */
    public void copyImagingLabel() {
        Connection connSkyOne = null;
        Long sysDate = getSysDate();
        try {
            connSkyOne = getSkyOneConnection();
            log.info("copyImagingLabel connect SKY ok");
        } catch (SQLException e) {
            log.error("copyImagingLabel Connect database error", e);
            close(connSkyOne);
            return;
        }

        long t1 = System.currentTimeMillis();
        log.info("copyImagingLabel start: " + (new Date()));
        log.info("copyImagingLabel from: " + startDate);
        System.out.println("copyImagingLabel start: " + (new Date()));

        copyImagingLabel(connSkyOne, sysDate);

        close(connSkyOne);

        long t2 = System.currentTimeMillis();
        log.info("copyImagingLabel end in " + (t2 - t1) + " ms");
    }

    public void copyImagingLabel(Connection connSkyOne, Long startDate) {
        long t1 = System.currentTimeMillis();
        log.info("copyImagingLabel start copy: " + startDate);
        String sqlSkyone = "select * from item where deleted_by is null and base_item = 0";

        PreparedStatement metroSelect = null;
        ResultSet rsSkyone = null;

        rowInsert = 0;
        rowUpdate = 0;
        try {
            metroSelect = connSkyOne.prepareStatement(sqlSkyone);
            rsSkyone = metroSelect.executeQuery();
            while (rsSkyone.next()) {
                Long patientId = null;
                try {
                    Long id = rsSkyone.getLong("id");
                    String code = rsSkyone.getString("code");
                    String name = rsSkyone.getString("name");
                    String nameEn = rsSkyone.getString("cpt_name");
                    String category = rsSkyone.getString("screen");
                    Long createdDate = rsSkyone.getLong("created_date");

                    updateImagingLabel(connSkyOne, id, code, name, nameEn, category, createdDate);
                } catch (Exception e) {
                    log.error("Error update patientId: " + patientId, e);
                }
            }

        } catch (Exception e) {
            log.error("Error copyImagingLabel: " + startDate, e);
        } finally {
            close(metroSelect);
        }

        long t2 = System.currentTimeMillis();
        log.info("copyImagingLabel end in " + (t2 - t1) + " ms");
        System.out.println("copyImagingLabel end in " + (t2 - t1) + " ms");
        System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
    }

    public void updateImagingLabel(Connection connSkyOne, Long id, String code, String name, String nameEn, String category, Long createdDate) throws SQLException {
        long t1 = System.currentTimeMillis();
        log.info("updateImagingLabel start copy: " + startDate);

        String sqlSelect = "select id from imaging_label where item_id = ? ";
        String sqlInsert = "insert into imaging_label(id, company_id, item_id, category, label, value, label_foreign,created_by,created_date,updated_by,updated_date,version) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update imaging_label set id = ?, company_id = ?, item_id = ?, category = ?, label = ?, value = ?, label_foreign = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ? where id = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        Long updatedDate = getSysDate();
        String value = "";

        // check partner exist in table
        Long skyImagingLabelId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setLong(1, id);
        rsSelect = stmtSelect.executeQuery();
        if (rsSelect.next()) {
            skyImagingLabelId = rsSelect.getLong("id");
        }

        if (skyImagingLabelId == null) {
            skyImagingLabelId = getId("ImagingResult", connSkyOne);
            log.info("Insert (CODE: " + code + ", ID: " + skyImagingLabelId + ")");

            stmtInsert = connSkyOne.prepareStatement(sqlInsert);
            stmtInsert.setLong(1, skyImagingLabelId);
            stmtInsert.setLong(2, companyId);
            stmtInsert.setLong(3, id);
            stmtInsert.setString(4, category);
            stmtInsert.setString(5, name);
            stmtInsert.setString(6, value);
            stmtInsert.setString(7, nameEn);

            stmtInsert.setLong(8, createdId);
            stmtInsert.setLong(9, createdDate);
            stmtInsert.setLong(10, createdId);
            stmtInsert.setLong(11, updatedDate);
            stmtInsert.setInt(12, version);

            stmtInsert.executeUpdate();
            rowInsert++;
            System.out.println("--insert ImagingLabel..");
        } else {
            log.info("Insert (CODE: " + code + ", ID: " + skyImagingLabelId + ")");
            stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
            stmtUpdate.setLong(1, skyImagingLabelId);
            stmtUpdate.setLong(2, companyId);
            stmtUpdate.setLong(3, id);
            stmtUpdate.setString(4, category);
            stmtUpdate.setString(5, name);
            stmtUpdate.setString(6, value);
            stmtUpdate.setString(7, nameEn);

            stmtUpdate.setLong(8, createdId);
            stmtUpdate.setLong(9, createdDate);
            stmtUpdate.setLong(10, createdId);
            stmtUpdate.setLong(11, updatedDate);
            stmtUpdate.setInt(12, version);

            stmtUpdate.setLong(13, skyImagingLabelId);

            stmtUpdate.executeUpdate();
            rowUpdate++;
            System.out.println("--update ImagingLabel..");
        }

        long t2 = System.currentTimeMillis();
        log.info("updateImaginResult end in " + (t2 - t1) + " ms");

    }

}
