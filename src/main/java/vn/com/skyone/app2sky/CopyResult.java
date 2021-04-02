package vn.com.skyone.app2sky;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Pattern;

public class CopyResult extends Common {

    public CopyResult(Integer nodeId) {
        super(nodeId);
    }

    public CopyResult(String[] args) {
        super(args);
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

    public void copyResultRange(Connection connMetro, Connection connSkyOne, Long startDate, Date fromDate,
            Date toDate) {
        long t1 = System.currentTimeMillis();
        log.info("copyResultRange start copy: " + startDate);
        String sqlMetro = "select * from \"TE_PhieuCLS\" where \"MaPhieuCLS\" is not null ";

        sqlMetro += " and \"TGThuchien\" between '" + fromDate + "' and '" + toDate + "'";
        sqlMetro += " order by \"TGThuchien\" " + sortType;

        sqlMetro += " limit 100";

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
                    String MaPhieuCLS = rsMetro.getString("MaPhieuCLS");
                    String TenDV = rsMetro.getString("TenDV");
                    String Ketqua = rsMetro.getString("Ketqua");
                    String Ketluan = rsMetro.getString("Ketluan");
                    String DeNghi = "";
                    String GhiChu = "";
                    Timestamp TGThuchien = rsMetro.getTimestamp("TGThuchien");
                    Timestamp TGCapnhat = rsMetro.getTimestamp("TGCapnhat");

                    String MaBN = rsMetro.getString("MaBN");
                    String HoTen = rsMetro.getString("HoTen");
                    String Namsinh = rsMetro.getString("Namsinh");
                    String Gioitinh = rsMetro.getString("Gioitinh");
                    String Diachi = rsMetro.getString("Diachi");
                    String SoBHYT = rsMetro.getString("SoBHYT");
                    String NoiChidinh = rsMetro.getString("NoiChidinh");
                    String Chandoan = rsMetro.getString("Chandoan");
                    String BsChidinh = rsMetro.getString("BsChidinh");
                    String BsDocKQ = rsMetro.getString("BsDocKQ");
                    String Trangthai = rsMetro.getString("Trangthai");
                    String MaDV = rsMetro.getString("MaDV");
                    Number IDBaocao = rsMetro.getBigDecimal("IDBaocao");
                    String MaPCD = rsMetro.getString("MaPCD");
                    String ID = rsMetro.getString("ID");
                    String MaBSChidinh = rsMetro.getString("MaBSChidinh");
                    String MaBSThuchien = rsMetro.getString("MaBSThuchien");
                    String MaNoiChidinh = rsMetro.getString("MaNoiChidinh");
                    String KeyPacs = rsMetro.getString("KeyPacs");

                    long t3 = System.currentTimeMillis();
                    updateImagingResult(connSkyOne, MaPhieuCLS, TenDV, Ketqua, Ketluan, DeNghi, GhiChu, TGThuchien,
                            TGCapnhat, MaBN, HoTen, Namsinh, Gioitinh, Diachi, SoBHYT, NoiChidinh, Chandoan, BsChidinh,
                            BsDocKQ, Trangthai, MaDV, IDBaocao, MaPCD, ID, MaBSChidinh, MaBSThuchien, MaNoiChidinh,
                            KeyPacs);

                    System.out
                            .println("------------------------------------------------------------ End (" + Lib.elapsedTime(t3, System.currentTimeMillis()) + ") ------------------------------------------------------------");
                } catch (Exception e) {
                    log.error("Error update copyResultRange: " + patientId, e);
                }
            }

        } catch (Exception e) {
            log.error("Error copyResultRange: " + startDate, e);
        } finally {
            close(rsMetro);
            close(metroSelect);
        }

        long t2 = System.currentTimeMillis();
        log.info("copyResultRange end in " + (t2 - t1) + " ms");
        System.out.println("copyResultRange end in " + (t2 - t1) + " ms");
        System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
    }

    // proccessing 1
    public Long updateImagingResult(Connection connSkyOne, String code, String technique, String findings,
            String impression, String recommendation, String notes, Date createdDate, Date updateDate,
            String patientCode, String patientName, String birth, String sex, String address, String insuranceNumber,
            String departmentName, String diagnosis, String indicationName, String providerName, String statuss,
            String itemCodes, Number IdReport, String imagingResultCode, String ids, String indicationDoctorCode,
            String providerDoctorCode, String departmentCode, String keyPacs) throws SQLException, IOException {

        long t1 = System.currentTimeMillis();
        log.info("updateImagingResult start copy: " + startDate);

        String sqlSelect = "select id from imaging_result where code = ? ";
        String sqlInsert = "insert into imaging_result(id, company_id, branch_id, status, screen, created_by, result_date, patient_id, external_uuid, code, order_no, diagnosis, findings, impression, recommendation, note, indicator_id, provider_id, anesthesiologist_id, nurse_id, technician_id, department_id, item_id, item_code, item_name, technique, search_text, created_date, updated_by, updated_date, accession_no, indication_id, indication_item_id, indication_date, appt_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update imaging_result set id = ?, company_id = ?, branch_id = ?, status = ?, screen = ?, created_by = ?, result_date = ?, patient_id = ?, external_uuid = ?, code = ?, order_no = ?, diagnosis = ?, findings = ?, impression = ?, recommendation = ?, note = ?,  indicator_id = ?, provider_id = ?, anesthesiologist_id = ?, nurse_id = ?, technician_id  = ?, department_id = ?, item_id = ?, item_code = ?, item_name = ?, technique = ?, search_text = ?, created_date = ?, updated_by = ?, updated_date = ?, accession_no = ?, indication_id = ?, indication_item_id = ?, indication_date = ?, appt_date = ? where id = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        String templateCode = "";
        Integer status = 9;
        String screen = "img111";
        if (!Lib.isEmpty(itemCodes)) {
            String[] sc = itemCodes.split("-");
            if (sc.length > 1) {
                if ("XQ".equals(sc[0])) {
                    screen = "img111";
                } else if ("CT".equals(sc[0])) {
                    screen = "img131";
                } else if ("MRI".equals(sc[0])) {
                    screen = "img141";
                }
            }
        }

        Long performanceDate = getMilliSecond(createdDate);
        Long updatedDate = getSysDate();

        Long patientId = updatePatient(connSkyOne, patientCode, patientName, birth, sex, address, insuranceNumber,
                createdDate); // findPartnerByExternalId(patientsId);

        System.out.println("patientId: " + patientId);

        Long indicatorId = 0L; // findPartnerByExternalId(doctorreferralId);
        if (!Lib.isEmpty(indicationDoctorCode)) {
            indicatorId = updateDoctor(connSkyOne, indicationDoctorCode, indicationName);
        }

        Long performerId = 0L; // findPartnerByExternalId(doctorperformId);
                               // //1209992018L
        if (!Lib.isEmpty(providerDoctorCode)) {
            performerId = updateDoctor(connSkyOne, providerDoctorCode, providerName);
        }

        Long departmentId = 0L;
        if (!Lib.isEmpty(departmentCode)) {
            departmentId = updateDepartment(connSkyOne, departmentCode, departmentName, createdDate);
        }

        Long anesthesiologistId = 0L; // findPartnerByExternalId(doctoranesthetistId);
        Long technicianId = 0L; // findPartnerByExternalId(nurse1Id);
        Long technician2Id = 0L; // findPartnerByExternalId(nurse2Id);

        String accessionNo = keyPacs;
        Long indicationId = 0L;
        Long indicationItemId = 0L;
        Long indicationDate = performanceDate;
        Long apptDate = indicationDate;

        Long itemId = 0L;
        String itemCode = itemCodes;
        String itemName = Lib.toPlainText(new StringReader(technique)) ;
        if (!Lib.isEmpty(itemCode)) {
            itemId = updateItem(connSkyOne, itemCode, itemName, createdDate);
        }

        // Item item = findItemByExternalUUID(primaryprocedureId);
        // if (item != null) {
        // itemId = item.getId();
        // itemCode = item.getCode();
        // itemName = item.getName();
        // reportName = item.getReportName();
        // }

        String searchText = Lib.viLatin((code + " " + itemName)).trim().toUpperCase();
        String endoscopicmedicalrecordId = "";
        String preliminarydiagnosis = diagnosis;

        String reportName = itemName.toUpperCase();
        String results = Lib.getBody(Lib.rtfToHtmlPlus(new StringReader(findings)));

        String conclusions = impression; // ket luan
        String proposal = recommendation; // de nghi
        String note = notes; // ghi chu

        if (!Lib.isEmpty(imagingResultCode)) {
            indicationId = updateIndication(connSkyOne, code, patientId, indicatorId, performanceDate,
                    preliminarydiagnosis, departmentId);
            if (indicationId != null && indicationId != 0L) {
                indicationItemId = updateIndicationItem(connSkyOne, indicationId, itemId);
            }
        }

        // check partner exist in table
        Long skyImagingResultId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, code);
        rsSelect = stmtSelect.executeQuery();
        if (rsSelect.next()) {
            skyImagingResultId = rsSelect.getLong("id");
        }

        if (skyImagingResultId == null) {
            skyImagingResultId = getId("ImagingResult", connSkyOne);
            log.info("Insert (CODE: " + code + ", ID: " + skyImagingResultId + ")");

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
            stmtInsert.setString(15, proposal);
            stmtInsert.setString(16, note);

            stmtInsert.setLong(17, indicatorId);
            stmtInsert.setLong(18, performerId);
            stmtInsert.setLong(19, anesthesiologistId);
            stmtInsert.setLong(20, technicianId);
            stmtInsert.setLong(21, technician2Id);
            stmtInsert.setLong(22, 0L); //departmentId

            stmtInsert.setLong(23, itemId);
            stmtInsert.setString(24, itemCode);
            stmtInsert.setString(25, itemName);
            stmtInsert.setString(26, reportName);
            stmtInsert.setString(27, searchText);

            stmtInsert.setLong(28, performanceDate);
            stmtInsert.setLong(29, createdId);
            stmtInsert.setLong(30, updatedDate);
            stmtInsert.setString(31, accessionNo);

            stmtInsert.setLong(32, indicationId);
            stmtInsert.setLong(33, indicationItemId);
            stmtInsert.setLong(34, indicationDate);
            stmtInsert.setLong(35, apptDate);

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
            stmtUpdate.setString(15, proposal);
            stmtUpdate.setString(16, note);

            stmtUpdate.setLong(17, indicatorId);
            stmtUpdate.setLong(18, performerId);
            stmtUpdate.setLong(19, anesthesiologistId);
            stmtUpdate.setLong(20, technicianId);
            stmtUpdate.setLong(21, technician2Id);
            stmtUpdate.setLong(22, 0L); //departmentId

            stmtUpdate.setLong(23, itemId);
            stmtUpdate.setString(24, itemCode);
            stmtUpdate.setString(25, itemName);
            stmtUpdate.setString(26, reportName);
            stmtUpdate.setString(27, searchText);

            stmtUpdate.setLong(28, performanceDate);
            stmtUpdate.setLong(29, createdId);
            stmtUpdate.setLong(30, updatedDate);
            stmtUpdate.setString(31, accessionNo);
            stmtUpdate.setLong(32, indicationId);
            stmtUpdate.setLong(33, indicationItemId);
            stmtUpdate.setLong(34, indicationDate);
            stmtUpdate.setLong(35, apptDate);

            stmtUpdate.setLong(36, skyImagingResultId);

            stmtUpdate.executeUpdate();
            rowUpdate++;
            System.out.println("--update ImagingResult..(UUID: " + endoscopicmedicalrecordId + ", CODE: " + code
                    + ", ID: " + skyImagingResultId + ")");
        }

        long t2 = System.currentTimeMillis();
        log.info("updateImaginResult end in " + (t2 - t1) + " ms");

        return skyImagingResultId;
    }

    // proccessing 2
    public Long updatePatient(Connection targetConn, String code, String name, String birth, String sex, String address,
            String insuranceNumber, Date crDate) throws SQLException {
        Long id = null;
        String sqlSelect = "select * from partner where deleted_by is null and code = ?";
        String sqlInsert = "insert into partner(id, code, name, first_name, middle_name, last_name, nick_name, search_text, birth_date, gender, address, sky_code, created_date, type_patient) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update partner set id = ?, code = ?, name = ?, first_name = ?, middle_name = ?, last_name = ?, nick_name = ?, search_text = ?, birth_date = ?, gender = ?, address = ?, sky_code = ?, created_date = ?, type_patient = ? where id = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        stmtSelect = targetConn.prepareStatement(sqlSelect);
        stmtSelect.setString(1, code);
        rsSelect = stmtSelect.executeQuery();
        if (rsSelect.next()) {
            id = rsSelect.getLong("id");
        }

        String fullname = name;
        String phone = "";
        String email = "";

        address = address != null ? address : "";
        phone = phone != null ? phone : "";
        email = email != null ? email : "";

        fullname = fullname.toUpperCase();
        String firstName = Lib.getName(fullname, Const.TYPE_NAME.FIRST_NAME);
        String middleName = Lib.getName(fullname, Const.TYPE_NAME.MIDDLE_NAME);
        String lastName = Lib.getName(fullname, Const.TYPE_NAME.LAST_NAME);
        String codeName = code;
        String nickName = Lib.viLatin(Lib.getName(fullname, Const.TYPE_NAME.ACCOUNT)).toLowerCase();
        Long birthDay = Lib.getMilliSecond(Lib.str2Dt(birth));
        
        String searchText = Lib.searchText(codeName, "", "", Lib.dt2Str(birthDay, "yyyy"), name, phone, "", email, "");
        
        Integer gender = Lib.getSex(sex);
        Long createdDate = Lib.getMilliSecond(crDate);
        Integer typePatient = 1; // Patient

        if (id == null) {
            id = getId("Partner", targetConn);
            log.info("Insert (CODE: " + code + ", ID: " + targetConn + ")");

            stmtInsert = targetConn.prepareStatement(sqlInsert);
            stmtInsert.setLong(1, id);
            stmtInsert.setString(2, code);
            stmtInsert.setString(3, fullname);
            stmtInsert.setString(4, firstName);
            stmtInsert.setString(5, middleName);
            stmtInsert.setString(6, lastName);
            stmtInsert.setString(7, nickName);
            stmtInsert.setString(8, searchText);
            stmtInsert.setLong(9, birthDay);
            stmtInsert.setInt(10, gender);
            stmtInsert.setString(11, address);
            stmtInsert.setString(12, insuranceNumber);
            stmtInsert.setLong(13, createdDate);
            stmtInsert.setInt(14, typePatient);
            stmtInsert.executeUpdate();
            rowInsert++;
            System.out.println("--insert patient (" + code + ")");
        } else {
            stmtUpdate = targetConn.prepareStatement(sqlUpdate);
            stmtUpdate.setLong(1, id);
            stmtUpdate.setString(2, code);
            stmtUpdate.setString(3, fullname);
            stmtUpdate.setString(4, firstName);
            stmtUpdate.setString(5, middleName);
            stmtUpdate.setString(6, lastName);
            stmtUpdate.setString(7, nickName);
            stmtUpdate.setString(8, searchText);
            stmtUpdate.setLong(9, birthDay);
            stmtUpdate.setInt(10, gender);
            stmtUpdate.setString(11, address);
            stmtUpdate.setString(12, insuranceNumber);
            stmtUpdate.setLong(13, createdDate);
            stmtUpdate.setInt(14, typePatient);
            stmtUpdate.setLong(15, id);
            stmtUpdate.executeUpdate();
            rowUpdate++;
            System.out.println("--update patient (" + code + ")");
        }
        // UPDATE PATIENT
        System.out.println("==Patient==");
        updatePatient(targetConn, id, code, createdId, createdDate);
        System.out.println("==Patient(ID: " + id + ")==");

        return id;
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

    public Long updateImagingResult(Connection connSkyOne, String endoscopicmedicalrecordId, String code,
            String primaryprocedureId, Date dateExam, String patientsId, String doctorreferralId,
            String doctorperformId, String doctoranesthetistId, String nurse1Id, String nurse2Id,
            String preliminarydiagnosis, String results, String conclusions, String proposal, String note)
            throws SQLException {
        long t1 = System.currentTimeMillis();
        log.info("updateImagingResult start copy: " + startDate);

        String sqlSelect = "select id from imaging_result where external_uuid = ? ";
        String sqlInsert = "insert into imaging_result(id, company_id, branch_id, status, screen, created_by, result_date, patient_id, external_uuid, code, order_no, diagnosis, findings, impression, recommendation, notes, indicator_id, provider_id, anesthesiologist_id, nurse_id, technician_id, department_id, item_id, item_code, item_name, technique, search_text, created_date, updated_by, updated_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update imaging_result set id = ?, company_id = ?, branch_id = ?, status = ?, screen = ?, created_by = ?, result_date = ?, patient_id = ?, external_uuid = ?, code = ?, order_no = ?, diagnosis = ?, findings = ?, impression = ?, recommendation = ?, notes = ?,  indicator_id = ?, provider_id = ?, anesthesiologist_id = ?, nurse_id = ?, technician_id  = ?, department_id = ?, item_id = ?, item_code = ?, item_name = ?, technique = ?, search_text = ?, created_date = ?, updated_by = ?, updated_date = ? where id = ?";

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

        Long itemId = null;
        String itemCode = null;
        String itemName = null;
        String reportName = null;
        Item item = findItemByExternalUUID(primaryprocedureId);
        if (item != null) {
            itemId = item.getId();
            itemCode = item.getCode();
            itemName = item.getName();
            reportName = item.getReportName();
        }

        String searchText = Lib.viLatin((code + " " + itemName)).trim().toUpperCase();

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

            stmtUpdate.setLong(31, skyImagingResultId);

            stmtUpdate.executeUpdate();
            rowUpdate++;
            System.out.println("--update ImagingResult..(UUID: " + endoscopicmedicalrecordId + ", CODE: " + code
                    + ", ID: " + skyImagingResultId + ")");
        }

        long t2 = System.currentTimeMillis();
        log.info("updateImaginResult end in " + (t2 - t1) + " ms");

        return skyImagingResultId;
    }

    public Long updateDoctor(Connection connSkyOne, String code, String fullname) throws SQLException {
        long t1 = System.currentTimeMillis();
        log.info("updateDoctor start copy: " + startDate);

        String sqlSelect = "select id from partner where deleted_by is null and code = ? ";
        String sqlInsert = "insert into partner(id, company_id, type_medic,search_text,code,first_name,middle_name,last_name,nick_name, name,title,address,phone_mobile,email,gender,birth_date,signature_data,signature_type,created_by,created_date,updated_by,updated_date,version,external_uuid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update partner set id = ?, company_id = ?, type_medic = ?, search_text = ?, code = ?,first_name = ?, middle_name = ?, last_name = ?, nick_name = ?,name = ?,title = ?,address = ?,phone_mobile = ?,email = ?,gender = ?,birth_date = ?,signature_data = ?,signature_type = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ?,external_uuid  = ? where id = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        // check partner exist in table
        Long skyDoctorId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, code);
        rsSelect = stmtSelect.executeQuery();
        if (rsSelect.next()) {
            skyDoctorId = rsSelect.getLong("id");
        }

        String title = "";
        String address = "";
        String phone = "";
        String email = "";
        Integer sex = 3;
        Integer age = 0;
        byte[] digitalsignature = null;
        String doctor_id = "";

        address = address != null ? address : "";
        phone = phone != null ? phone : "";
        email = email != null ? email : "";

        // System.out.println("fullname: " + fullname);
        fullname = fullname.toUpperCase().replace(".NT", ".NT.");
        String[] str = fullname.split(Pattern.quote("."));
        if (str.length > 1) {
            for (int i = 0; i < str.length - 1; i++) {
                title += (str[i] + ".").trim();
            }
            title = title.trim();
            fullname = str[str.length - 1].trim();
        }

        // System.out.println("title: " + title);
        // System.out.println("fullname1: " + fullname);

        String firsName = Lib.getName(fullname, Const.TYPE_NAME.FIRST_NAME);
        String middleName = Lib.getName(fullname, Const.TYPE_NAME.MIDDLE_NAME);
        String lastName = Lib.getName(fullname, Const.TYPE_NAME.LAST_NAME);
        String codeName = code; // Lib.viLatin(Lib.getName(fullname,
                                // Const.TYPE_NAME.CODE));
        String nickName = Lib.viLatin(Lib.getName(fullname, Const.TYPE_NAME.ACCOUNT)).toLowerCase();
        String searchText = Lib.viLatin(codeName + " " + fullname + " " + address).toUpperCase().replace("NULL", "")
                .trim();

        Long updatedBy = 0L;
        Long updatedDate = new Date().getTime();
        Integer version = 1;
        Integer typeMedic = 1; // doctor

        if (skyDoctorId == null) {
            skyDoctorId = getId("Partner", connSkyOne);
            stmtInsert = connSkyOne.prepareStatement(sqlInsert);
            stmtInsert.setLong(1, skyDoctorId);
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
            System.out.println("--inserted doctor.. (" + codeName + ")");
        } else {
            stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
            stmtUpdate.setLong(1, skyDoctorId);
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
            stmtUpdate.setLong(25, skyDoctorId);

            stmtUpdate.executeUpdate();
            rowUpdate++;
            System.out.println("--updated doctor.. (" + codeName + ")");
        }

        long t2 = System.currentTimeMillis();
        log.info("updateDoctor end in " + (t2 - t1) + " ms");
        return skyDoctorId;
    }

    // handle department
    public Long updateDepartment(Connection connSkyOne, String code, String fullname, Date createDate)
            throws SQLException {
        long t1 = System.currentTimeMillis();
        log.info("updateDoctor start copy: " + startDate);

        String sqlSelect = "select id from department where deleted_by is null and code = ? ";
        String sqlInsert = "insert into department(id, branch_id, partner_id, code, name, name_en, nick_name, created_date, search_text) VALUES(?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update department set id = ?, branch_id = ?, partner_id = ?, code = ?, name = ?, name_en = ?, nick_name = ?, created_date = ?, search_text = ? where id = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        // check partner exist in table
        Long skyDepartmentId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, code);
        rsSelect = stmtSelect.executeQuery();
        if (rsSelect.next()) {
            skyDepartmentId = rsSelect.getLong("id");
        }

        Long partnerId = 0L;
        String name = fullname;
        String searchText = Lib.viLatin(code + " " + name);
        String nameEn = "";
        String nickName = name;

        Long createdDate = Lib.getMilliSecond(createDate);

        if (skyDepartmentId == null) {
            skyDepartmentId = getId("Department", connSkyOne);
            stmtInsert = connSkyOne.prepareStatement(sqlInsert);
            stmtInsert.setLong(1, skyDepartmentId);
            stmtInsert.setLong(2, branchId);
            stmtInsert.setLong(3, partnerId);
            stmtInsert.setString(4, code);
            stmtInsert.setString(5, name);
            stmtInsert.setString(6, nameEn);
            stmtInsert.setString(7, nickName);
            stmtInsert.setLong(8, createdDate);
            stmtInsert.setString(9, searchText);
            stmtInsert.executeUpdate();
            rowInsert++;
            System.out.println("--inserted department.. (" + code + ")");
        } else {
            stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
            stmtUpdate.setLong(1, skyDepartmentId);
            stmtUpdate.setLong(2, branchId);
            stmtUpdate.setLong(3, partnerId);
            stmtUpdate.setString(4, code);
            stmtUpdate.setString(5, name);
            stmtUpdate.setString(6, nameEn);
            stmtUpdate.setString(7, nickName);
            stmtUpdate.setLong(8, createdDate);
            stmtUpdate.setString(9, searchText);
            stmtUpdate.setLong(10, skyDepartmentId);
            stmtUpdate.executeUpdate();

            rowUpdate++;
            System.out.println("--updated department.. (" + code + ")");
        }

        long t2 = System.currentTimeMillis();
        log.info("updateDepartment end in " + (t2 - t1) + " ms");
        return skyDepartmentId;
    }

    // handle item
    public Long updateItem(Connection connSkyOne, String code, String name, Date dateCreated) throws SQLException {
        long t1 = System.currentTimeMillis();
        log.info("updateItem start copy: " + startDate);

        String sqlSelect = "select id from item where code = ? ";
        String sqlInsert = "insert into item(id, company_id, external_uuid, type_service, code, name, origin_name, cpt_name, base_item, image, image_type, group_id, category_id,created_by,created_date,updated_by,updated_date,version, type, av_indication,screen, search_text, reg_code, cpt_code, barcode, unit_id, modality, sub_included ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update item set id = ?, company_id = ?, external_uuid = ?, type_service = ?, code = ?, name = ?, origin_name = ?, cpt_name = ?, base_item = ?, image = ?,image_type = ?, group_id = ?, category_id = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ?, type = ?, av_indication = ?,screen = ?, search_text = ?, reg_code = ?, cpt_code = ?, barcode = ?, unit_id = ?, modality = ?, sub_included = ? where id = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        String reportname = "";
        String reportnameen = "";
        String note = "";
        Boolean priority = true;
        byte[] image = null;
        Double cost = 0D;
        Double wageofdoctorperform = 0D;
        Double wageofdoctorreferral = 0D;
        Double wageofanesthetist = 0D;
        Double wageofnursing = 0D;
        Date modifiedDate = dateCreated;
        String endoscopicprocedureId = "";

        Long createdDate = getMilliSecond(dateCreated);
        Long updatedDate = getSysDate();
        Integer version = 1;

        Integer typeService = 3;
        Integer itemType = 1;
        Integer avIndication = 1;
        Long unitId = 199992018L;

        String searchText = Lib.viLatin(code + " " + name).trim().toUpperCase();
        String regCode = "";
        String cptCode = "";
        String barcode = "";

        Long endoGroup = 301L;
        Long endoCategory = 0L;
        String modality = "";

        String screen = "img111";
        if (!Lib.isEmpty(code)) {
            String[] sc = code.split("-");
            if (sc.length > 1) {
                if ("XQ".equals(sc[0])) {
                    screen = "img111";
                    endoCategory = 100301L;
                } else if ("CT".equals(sc[0])) {
                    screen = "img131";
                    endoCategory = 102301L;
                } else if ("MRI".equals(sc[0])) {
                    screen = "img141";
                    endoCategory = 103301L;
                }
            }
        }

        if (!priority) {
            screen = "";
            modality = "";
        }

        // check partner exist in table
        Long skyItemId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, code);
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
        return skyItemId;
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

    // handle indication
    public Long updateIndication(Connection connSkyOne, String code, Long patientId, Long indicatorId,
            Long performanceDate, String diagnosis, Long departmentId) throws SQLException {
        long t1 = System.currentTimeMillis();
        log.info("updateIndication start copy: " + startDate);

        String sqlSelect = "select id from indication where code = ? ";
        String sqlInsert = "insert into indication(id, company_id, branch_id, department_id, search_text, code, patient_id, indicator_id, indication_date, appt_date, diagnosis, created_by, created_date, updated_by, updated_date, version) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update indication set id = ?, company_id = ?, branch_id = ?, department_id = ?, search_text = ?, code = ?, patient_id = ?, indicator_id = ?, indication_date = ?, appt_date = ?, diagnosis = ?, created_by = ?, created_date = ?, updated_by = ?, updated_date = ?, version = ? where id = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        String searchText = "";

        Long indicationDepartmentId = departmentId;

        Long skyIndicationId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setString(1, code);
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

        // Long indicationItemId = updateIndicationItem(connSkyOne,
        // indicationId, itemId);

        long t2 = System.currentTimeMillis();
        log.info("updateIndication end in " + (t2 - t1) + " ms");
        return skyIndicationId;
    }

    public Long updateIndicationItem(Connection connSkyOne, Long indicationId, Long itemId) throws SQLException {
        long t1 = System.currentTimeMillis();
        log.info("updateIndicationItem start copy: " + startDate);

        String sqlSelect = "select id from indication_item where indication_id = ? ";
        String sqlInsert = "insert into indication_item(id, indication_id, item_id, item_code, item_name, screen, unit_id, qty, note, category_id, created_by, created_date, updated_by, updated_date, version) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update indication_item set id = ?, indication_id = ?, item_id = ?, item_code = ?, item_name = ?, screen = ?, unit_id = ?, qty = ?, note = ?, category_id = ?, created_by = ?, created_date = ?, updated_by = ?, updated_date = ?, version = ? where id = ?";

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

}
