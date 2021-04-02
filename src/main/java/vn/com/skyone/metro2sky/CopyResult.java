package vn.com.skyone.metro2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.cli.CommandLine;

public class CopyResult extends Common {

    public CopyResult(Integer nodeId) {
        super(nodeId);
    }

    public CopyResult(String[] args) {
        super(args);
    }

    /**
     * 
     * @param path
     */
    public void copyResult() {
        Connection connMetro = null;
        Connection connSkyOne = null;
        Long sysDate = getSysDate();
        try {
            connMetro = getMetroConnection();
            log.info("copyResult connect Metro ok");
            connSkyOne = getSkyOneConnection();
            log.info("copyResult connect SKY ok");
        } catch (SQLException e) {
            log.error("copyResult Connect database error", e);
            close(connMetro);
            close(connSkyOne);
            return;
        }

        long t1 = System.currentTimeMillis();
        log.info("copyResult start: " + (new Date()));
        log.info("copyResult from: " + startDate);
        System.out.println("copyResult start: " + (new Date()));

        copyResult(connMetro, connSkyOne, sysDate);

        close(connMetro);
        close(connSkyOne);

        long t2 = System.currentTimeMillis();
        log.info("copyResult end in " + (t2 - t1) + " ms");
    }

    public void copyResult(Connection connMetro, Connection connSkyOne, Long startDate) {
        long t1 = System.currentTimeMillis();
        log.info("copyPatient start copy: " + startDate);
        String sqlMetro = "select * from \"EndoscopicMedicalRecord\" where deleted = 0 ";
        if (!Lib.isEmpty(Const.ENDOSCOPIC_SORT)) {
            sqlMetro += Const.ENDOSCOPIC_SORT;
        }
        if (limit > 0) {
            sqlMetro += " limit " + limit;
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
                    String endoscopicmedicalrecordId = rsMetro.getString("endoscopicmedicalrecord_id");

                    String code = rsMetro.getString("code");
                    String primaryprocedureId = rsMetro.getString("primaryprocedure_id");
                    Timestamp dateExam = rsMetro.getTimestamp("date_exam");
                    String patientsId = rsMetro.getString("patients_id");

                    String doctorreferralId = rsMetro.getString("doctorreferral_id");
                    String doctorperformId = rsMetro.getString("doctorperform_id");
                    String doctoranesthetistId = rsMetro.getString("doctoranesthetist_id");
                    String nurse1Id = rsMetro.getString("nurse1_id");
                    String nurse2Id = rsMetro.getString("nurse2_id");

                    String preliminarydiagnosis = rsMetro.getString("preliminarydiagnosis");
                    String results = rsMetro.getString("results");
                    String conclusions = rsMetro.getString("conclusions");
                    String proposal = rsMetro.getString("proposal");
                    String note = rsMetro.getString("note");

                    updateImagingResult(connSkyOne, endoscopicmedicalrecordId, code, primaryprocedureId, dateExam,
                            patientsId, doctorreferralId, doctorperformId, doctoranesthetistId, nurse1Id, nurse2Id,
                            preliminarydiagnosis, results, conclusions, proposal, note);
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
        System.out.println("copyResult end in " + (t2 - t1) + " ms");
        System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
    }

    public void copyResultRange(Connection connMetro, Connection connSkyOne, Long startDate, Date fromDate,
            Date toDate) {
        long t1 = System.currentTimeMillis();
        log.info("copyPatient start copy: " + startDate);
        String sqlMetro = "select * from \"EndoscopicMedicalRecord\" where deleted = 0";

        sqlMetro += " and date_exam between '" + fromDate + "' and '" + toDate + "'";
        sqlMetro += " order by date_exam " + sortType;

        PreparedStatement metroSelect = null;
        ResultSet rsMetro = null;

        rowInsert = 0;
        rowUpdate = 0;

        System.out.println("********** Start copy Result (" + Lib.dt2Str(fromDate, "dd/MM/yyyy HH:mm:ss") + " - "
                + Lib.dt2Str(toDate, "dd/MM/yyyy HH:mm:ss") + ") **********");

        try {
            metroSelect = connMetro.prepareStatement(sqlMetro);
            rsMetro = metroSelect.executeQuery();
            int cnt = 0;
            while (rsMetro.next()) {
                Long patientId = null;
                try {
                    long t3 = System.currentTimeMillis();
                    String endoscopicmedicalrecordId = rsMetro.getString("endoscopicmedicalrecord_id");

                    String code = rsMetro.getString("code");
                    String primaryprocedureId = rsMetro.getString("primaryprocedure_id");
                    Timestamp dateExam = rsMetro.getTimestamp("date_exam");
                    String patientsId = rsMetro.getString("patients_id");

                    String doctorreferralId = rsMetro.getString("doctorreferral_id");
                    String doctorperformId = rsMetro.getString("doctorperform_id");
                    String doctoranesthetistId = rsMetro.getString("doctoranesthetist_id");
                    String nurse1Id = rsMetro.getString("nurse1_id");
                    String nurse2Id = rsMetro.getString("nurse2_id");

                    String preliminarydiagnosis = rsMetro.getString("preliminarydiagnosis");
                    String results = rsMetro.getString("results");
                    String conclusions = rsMetro.getString("conclusions");
                    String proposal = rsMetro.getString("proposal");
                    String note = rsMetro.getString("note");
                    Timestamp dateCreated = rsMetro.getTimestamp("date_created");

                    System.out.println("---------- Start Result (" + code + ") ----------");
                    Long imagingResultId = updateImagingResult(connSkyOne, endoscopicmedicalrecordId, code,
                            primaryprocedureId, dateExam, patientsId, doctorreferralId, doctorperformId,
                            doctoranesthetistId, nurse1Id, nurse2Id, preliminarydiagnosis, results, conclusions,
                            proposal, note);

                    // Update indication
                    System.out.println("########### Start copy Indication ###########");
                    CopyIndication copyIndication = new CopyIndication(args);
                    // copyIndication.copyIndicationByImagingResult(connSkyOne,
                    // imagingResultId);

                    // Update imaging sub
                    System.out.println("########### Start copy item ###########");
                    CopyImagingSub copyImagingSub = new CopyImagingSub(args);
                    copyImagingSub.copyImagingSubByImagingResult(connMetro, connSkyOne, imagingResultId);

                    // Update imaging image
                    if (archive) {
                        System.out.println("########### Start copy imaging image ###########");
                        CopyImagingImage copyImagingImage = new CopyImagingImage(args);
                        copyImagingImage.copyImagingImageByImagingResult(connMetro, connSkyOne, imagingResultId);
                    }

                    if (cnt == 1000) {
                        cnt = 0;
                        //connSkyOne.commit();
                    }
                    cnt++;

                    long t4 = System.currentTimeMillis();
                    System.out.println("---------- End Result total(" + Lib.elapsedTime(t3, t4) + ") ----------");
                    System.out.println("Create Results: " + Lib.dt2Str(dateCreated, "dd/MM/yyyy HH:mm:ss"));
                } catch (Exception e) {
                    log.error("Error update patientId: " + patientId, e);
                }
            }

            //connSkyOne.commit();

        } catch (Exception e) {
            log.error("Error copyPatient: " + startDate, e);
        } finally {
            close(rsMetro);
            close(metroSelect);
        }

        long t2 = System.currentTimeMillis();
        // log.info("copyPatient end in " + (t2 - t1) + " ms");
        System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
        System.out.println("********** End copy Result Sum Total: " + Lib.elapsedTime(t1, t2));
    }

    public Long updateImagingResult(Connection connSkyOne, String endoscopicmedicalrecordId, String code,
            String primaryprocedureId, Date dateExam, String patientsId, String doctorreferralId,
            String doctorperformId, String doctoranesthetistId, String nurse1Id, String nurse2Id,
            String preliminarydiagnosis, String results, String conclusions, String proposal, String note)
            throws SQLException {
        long t1 = System.currentTimeMillis();
        log.info("updateImagingResult start copy: " + startDate);

        String sqlSelect = "select id from imaging_result where external_uuid = ? ";
        String sqlInsert = "insert into imaging_result(id, company_id, branch_id, status, screen, created_by, result_date, patient_id, external_uuid, code, order_no, diagnosis, findings, impression, recommendation, note, indicator_id, provider_id, anesthesiologist_id, nurse_id, technician_id, department_id, item_id, item_code, item_name, technique, search_text, created_date, updated_by, updated_date, title) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update imaging_result set id = ?, company_id = ?, branch_id = ?, status = ?, screen = ?, created_by = ?, result_date = ?, patient_id = ?, external_uuid = ?, code = ?, order_no = ?, diagnosis = ?, findings = ?, impression = ?, recommendation = ?, note = ?,  indicator_id = ?, provider_id = ?, anesthesiologist_id = ?, nurse_id = ?, technician_id  = ?, department_id = ?, item_id = ?, item_code = ?, item_name = ?, technique = ?, search_text = ?, created_date = ?, updated_by = ?, updated_date = ?, title = ? where id = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        Integer status = 9;
        String screen = "img211";

        Long performanceDate = getMilliSecond(dateExam);
        Long updatedDate = getSysDate();

        Long patientId = findPartnerByExternalId(patientsId);

        Long indicatorId = findPartnerByExternalId(doctorreferralId);
        Long performerId = findPartnerByExternalId(doctorperformId);
        Long anesthesiologistId = findPartnerByExternalId(doctoranesthetistId);
        Long technicianId = findPartnerByExternalId(nurse1Id);
        Long technician2Id = findPartnerByExternalId(nurse2Id);

        Long itemId = 0L;
        String itemCode = "";
        String itemName = "";
        String reportName = "";
        Item item = findItemByExternalUUID(primaryprocedureId);
        if (item != null) {
            itemId = item.getId();
            itemCode = item.getCode();
            itemName = item.getName();
            reportName = item.getReportName();
        }

        String searchText = Lib.viLatin((code + " " + itemName)).trim().toUpperCase();
        String title = reportName;

        // Long endoDepartmentId = Const.ENDO_DEPARTMENT;

        // Integer orderNo = getOrderNo(performanceDate);
        // proposal = proposal != null ? proposal : "";

        // check partner exist in table
        Long skyImagingResultId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, endoscopicmedicalrecordId);
        rsSelect = stmtSelect.executeQuery();
        if (rsSelect.next()) {
            skyImagingResultId = rsSelect.getLong("id");
        }

        if (skyImagingResultId == null) {
            skyImagingResultId = getId("ImagingResult", connSkyOne);
            log.info("Insert (UUID: " + endoscopicmedicalrecordId + ", CODE: " + code + ", ID: " + skyImagingResultId
                    + ")");

            stmtInsert = connSkyOne.prepareStatement(sqlInsert);
            stmtInsert.setLong(1, skyImagingResultId);
            stmtInsert.setLong(2, companyId);
            stmtInsert.setLong(3, branchId);
            stmtInsert.setInt(4, status);
            stmtInsert.setString(5, screen);
            stmtInsert.setLong(6, createdId);
            stmtInsert.setLong(7, performanceDate);
            stmtInsert.setLong(8, patientId);

            stmtInsert.setString(9, endoscopicmedicalrecordId);
            stmtInsert.setString(10, code);
            stmtInsert.setInt(11, orderNo);

            stmtInsert.setString(12, preliminarydiagnosis);
            stmtInsert.setString(13, results);
            stmtInsert.setString(14, conclusions);
            stmtInsert.setString(15, coalesce(proposal, ""));
            stmtInsert.setString(16, note);

            stmtInsert.setLong(17, indicatorId);
            stmtInsert.setLong(18, performerId);
            stmtInsert.setLong(19, anesthesiologistId);
            stmtInsert.setLong(20, technicianId);
            stmtInsert.setLong(21, technician2Id);
            stmtInsert.setLong(22, endoDepartmentId);

            stmtInsert.setLong(23, itemId);
            stmtInsert.setString(24, itemCode);
            stmtInsert.setString(25, itemName);
            stmtInsert.setString(26, reportName);
            stmtInsert.setString(27, searchText);

            stmtInsert.setLong(28, performanceDate);
            stmtInsert.setLong(29, createdId);
            stmtInsert.setLong(30, updatedDate);
            stmtInsert.setString(31, title);

            stmtInsert.executeUpdate();
            rowInsert++;
            System.out.println("--insert ImagingResult..(UUID: " + endoscopicmedicalrecordId + ", CODE: " + code
                    + ", ID: " + skyImagingResultId + ")");
        } else {
            log.info("Update (UUID: " + endoscopicmedicalrecordId + ", CODE: " + code + ", ID: " + skyImagingResultId
                    + ")");
            stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
            stmtUpdate.setLong(1, skyImagingResultId);
            stmtUpdate.setLong(2, companyId);
            stmtUpdate.setLong(3, branchId);
            stmtUpdate.setInt(4, status);
            stmtUpdate.setString(5, screen);
            stmtUpdate.setLong(6, createdId);
            stmtUpdate.setLong(7, performanceDate);
            stmtUpdate.setLong(8, patientId);

            stmtUpdate.setString(9, endoscopicmedicalrecordId);
            stmtUpdate.setString(10, code);
            stmtUpdate.setInt(11, orderNo);

            stmtUpdate.setString(12, preliminarydiagnosis);
            stmtUpdate.setString(13, results);
            stmtUpdate.setString(14, conclusions);
            stmtUpdate.setString(15, coalesce(proposal, ""));
            stmtUpdate.setString(16, note);

            stmtUpdate.setLong(17, indicatorId);
            stmtUpdate.setLong(18, performerId);
            stmtUpdate.setLong(19, anesthesiologistId);
            stmtUpdate.setLong(20, technicianId);
            stmtUpdate.setLong(21, technician2Id);
            stmtUpdate.setLong(22, endoDepartmentId);

            stmtUpdate.setLong(23, itemId);
            stmtUpdate.setString(24, itemCode);
            stmtUpdate.setString(25, itemName);
            stmtUpdate.setString(26, reportName);
            stmtUpdate.setString(27, searchText);

            stmtUpdate.setLong(28, performanceDate);
            stmtUpdate.setLong(29, createdId);
            stmtUpdate.setLong(30, updatedDate);
            stmtUpdate.setString(31, title);
            stmtUpdate.setLong(32, skyImagingResultId);

            stmtUpdate.executeUpdate();
            rowUpdate++;
            System.out.println("--update ImagingResult..(UUID: " + endoscopicmedicalrecordId + ", CODE: " + code
                    + ", ID: " + skyImagingResultId + ")");
        }

        long t2 = System.currentTimeMillis();
        log.info("updateImaginResult end in " + (t2 - t1) + " ms");

        return skyImagingResultId;
    }

    public void copyResultRange() {
        Connection connMetro = null;
        Connection connSkyOne = null;
        Long sysDate = getSysDate();
        try {
            connMetro = getMetroConnection();
            log.info("copyResult connect Metro ok");
            connSkyOne = getSkyOneConnection();
            log.info("copyResult connect SKY ok");
        } catch (SQLException e) {
            log.error("copyResult Connect database error", e);
            close(connMetro);
            close(connSkyOne);
            return;
        }

        long t1 = System.currentTimeMillis();
        log.info("copyResult start: " + (new Date()));
        log.info("copyResult from: " + startDate);
        System.out.println("copyResult start: " + (new Date()));

        // Start copy result range
        copyResultRange(connMetro, connSkyOne, sysDate, fromDate, toDate);

        close(connMetro);
        close(connSkyOne);

        long t2 = System.currentTimeMillis();
        log.info("copyResult end in " + (t2 - t1) + " ms");
    }

}
