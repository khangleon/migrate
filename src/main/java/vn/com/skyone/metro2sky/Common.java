package vn.com.skyone.metro2sky;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.ExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.json.JSONArray;

public class Common extends CopyBase {

    public Common(Integer nodeId) {
        super(nodeId);
    }

    public Common(String[] args) {
        super(args);
    }

    public void exportImagingResult(Long imagingResultId) throws Exception {
        Connection connSkyOne = null;
        String reportName = "es.jasper";
        String headerName = "header.png";
        boolean preview = true;
        boolean printLogo = true;
        boolean fillFrame = false;

        boolean cropImage = false;
        int cropTop = 0;
        int cropBottom = 0;
        int cropLeft = 0;
        int cropRight = 0;

        String currentDirectory = System.getProperty("user.dir");
        System.out.println("currentDirectory: " + currentDirectory);
        File reportFile = new File(currentDirectory + "/data/report/pk91phc/" + reportName);
        InputStream header = new FileInputStream(currentDirectory + "/data/report/pk91phc/" + headerName);

        Long filesystemId = Const.DIR_PATH_ID;
        String dirpath = Const.DIR_PATH;
        String resultCode = "";
        Long resultDate = 0L;
        Long patientId = 0L;
        Long itemId = 0L;
        Long indicatorId = 0L;
        Long providerId = 0L;
        String title = "";

        String diagnosis = "";
        String deviceName = "";
        String technique = "";
        String findings = "";
        String impression = "";
        String recommendation = "";
        String note = "";
        String subIndication = "";

        try {
            connSkyOne = getSkyOneConnection();
            PreparedStatement stmt = connSkyOne.prepareStatement("select * from imaging_result where id = ?");
            stmt.setLong(1, imagingResultId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                imagingResultId = rs.getLong("id");
                resultCode = rs.getString("code");
                resultDate = rs.getLong("result_date");
                patientId = rs.getLong("patient_id");
                itemId = rs.getLong("item_id");
                indicatorId = rs.getLong("indicator_id");
                providerId = rs.getLong("provider_id");
                title = rs.getString("title");

                diagnosis = rs.getString("diagnosis");
                technique = rs.getString("technique");
                findings = Lib.markupHtml(rs.getString("findings"));
                impression = rs.getString("impression");
                recommendation = rs.getString("recommendation");
                note = rs.getString("note");
            }

            JSONArray dataSource = null;
            Map<String, Object> param = new HashMap<String, Object>();

            param.put("REPORT_LOCALE", new Locale("vi", "VN"));
            param.put("REPORT_TIME_ZONE", TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

            param.put("resultCode", resultCode);
            if (resultDate != null) {
                param.put("resultDate", new Date(resultDate));
                param.put("performanceDate", new Date(resultDate));
            }
            param.put("header", header);
            param.put("printLogo", printLogo);
            param.put("preview", preview);

            // Patient
            stmt = connSkyOne.prepareStatement("select * from partner where id = ?");
            stmt.setLong(1, patientId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                param.put("patientCode", rs.getString("code"));
                param.put("patientName", rs.getString("name"));
                Long birthDate = rs.getLong("birth_date");
                if (birthDate != null) {
                    param.put("patientBirthDate", new Date(birthDate));
                }
                param.put("patientGender", rs.getInt("gender") == 1 ? "Nam" : rs.getInt("gender") == 2 ? "Nữ" : "");
                param.put("patientPhone", rs.getString("phone_mobile"));
                param.put("patientEmail", rs.getString("email"));
                param.put("patientAddress", rs.getString("address"));
            }

            // Item
            stmt = connSkyOne.prepareStatement("select image from item where id = ?");
            stmt.setLong(1, itemId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                byte[] proceItem = rs.getBytes("image");
                if (proceItem != null) {
                    param.put("procedureImage", new ByteArrayInputStream(proceItem));
                }
            }

            // Indication
            stmt = connSkyOne.prepareStatement("select sourcer_id from indication where id = ?");
            stmt.setLong(1, indicatorId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Long sourcerId = rs.getLong("sourcer_id");
                if (sourcerId != null) {
                    stmt = connSkyOne.prepareStatement("select title, name from partner where id = ?");
                    stmt.setLong(1, sourcerId);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        String t = rs.getString("title");
                        String n = Lib.capitalize(rs.getString("name"));
                        param.put("sourcerName", Lib.isEmpty(t) ? n : t + " " + n);
                    }
                }
            }

            stmt = connSkyOne.prepareStatement("select title, name from partner where id = ?");
            stmt.setLong(1, indicatorId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String t = rs.getString("title");
                String n = Lib.capitalize(rs.getString("name"));
                param.put("indicatorName", Lib.isEmpty(t) ? n : t + " " + n);
            }
            param.put("indicationDepartmentName", "");

            // Provider
            stmt = connSkyOne.prepareStatement("select title, name, signature_data from partner where id = ?");
            stmt.setLong(1, providerId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String t = rs.getString("title");
                String n = Lib.capitalize(rs.getString("name"));
                byte[] signature = rs.getBytes("signature_data");
                param.put("providerName", Lib.isEmpty(t) ? n : t + " " + n);
                if (signature != null) {
                    param.put("signature", new ByteArrayInputStream(signature));
                }
            }

            String titleForeign = "";
            String[] titles = title != null ? title.split("([\\n\\r]+|[\\r\\n]+|[\\r]+|[\\n]+)") : new String[] {};
            if (titles.length > 0) {
                title = titles[0];
            }
            if (titles.length > 1) {
                titleForeign = titles[1];
            }

            // imagingSub
            List<ImagingSub> imagingSubs = new ArrayList<ImagingSub>();
            stmt = connSkyOne.prepareStatement(
                    "select * from imaging_sub where deleted_by is null and imaging_result_id = ? order by sort ");
            stmt.setLong(1, imagingResultId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    Integer subResult = rs.getInt("sub_result");
                    String label = rs.getString("label");
                    String labelForeign = rs.getString("label_foreign");
                    Double price = rs.getDouble("price");
                    String noteSub = rs.getString("note");

                    ImagingSub imagingSub = new ImagingSub();
                    imagingSub.setSubResult(subResult);
                    imagingSub.setLabel(label);
                    imagingSub.setLabelForeign(labelForeign);
                    imagingSub.setPrice(price);
                    imagingSub.setPrice(price);
                    imagingSub.setNote(noteSub);
                    imagingSubs.add(imagingSub);
                } catch (Exception ex) {

                }
            }

            List<ImagingSub> subResults = new ArrayList<ImagingSub>();
            if (imagingSubs != null && !imagingSubs.isEmpty()) {
                ImagingSub imagingSub = imagingSubs.get(0);
                subIndication += imagingSub.getLabel();
                if (imagingSub.isSubResult()) {
                    subResults.add(imagingSub);
                }

                for (int i = 1; i < imagingSubs.size(); i++) {
                    imagingSub = imagingSubs.get(i);

                    subIndication += " + " + imagingSub.getLabel();

                    if (imagingSub.isSubResult()) {
                        subResults.add(imagingSub);
                    }
                }
            }

            // imaging document
            List<ImagingDocument> imagingImages = new ArrayList<ImagingDocument>();
            stmt = connSkyOne.prepareStatement(
                    "select * from imaging_image where deleted_by is null and disabled = 0 and imaging_result_id = ? order by created_date ");
            stmt.setLong(1, imagingResultId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    String filepath = rs.getString("filepath");
                    String filename = rs.getString("filename");
                    String filetype = rs.getString("filetype");
                    Integer keyImage = rs.getInt("key_image");
                    Integer keyIndex = rs.getInt("key_index");
                    Integer posIndex = rs.getInt("pos_index");
                    String noteImage = rs.getString("note");

                    ImagingDocument imagingDocument = new ImagingDocument();
                    imagingDocument.setDirpath(Const.DIR_PATH);
                    imagingDocument.setFilepath(filepath);
                    imagingDocument.setFilename(filename);
                    imagingDocument.setFiletype(filetype);
                    imagingDocument.setKeyImage(keyImage);
                    imagingDocument.setKeyIndex(keyIndex);
                    imagingDocument.setPosIndex(posIndex);
                    imagingDocument.setNote(noteImage);

                    imagingImages.add(imagingDocument);
                } catch (Exception ex) {

                }
            }

            List<ImagingDocument> images = new ArrayList<ImagingDocument>();
            ImagingDocument imagingImage = null;
            String fileNameImage = null;
            File fileImage = null;
            InputStream fileStream = null;
            if (imagingImages != null) {
                double t = cropTop;
                double l = cropLeft;
                double r = cropRight;
                double b = cropBottom;
                for (ImagingDocument e : imagingImages) {
                    if (e.isKeyImage()) {
                        fileNameImage = e.getFileNameFull();
                        fileImage = new File(fileNameImage);
                        if (fileImage.exists()) {
                            fileStream = new FileInputStream(fileImage);
                            imagingImage = new ImagingDocument();
                            if (cropImage) {
                                imagingImage.setFileStream(Lib.cropImage(fileStream, e.getFiletype(), t, l, r, b));
                            } else {
                                imagingImage.setFileStream(fileStream);
                            }
                            imagingImage.setNote(e.getNote());
                            imagingImage.setKeyImage(e.isKeyImage());
                            imagingImage.setKeyIndex(e.getKeyIndex());
                            imagingImage.setPosIndex(e.getPosIndex());
                            images.add(imagingImage);
                        }
                    }
                }
            }

            Collections.sort(images, new Comparator<ImagingDocument>() {
                public int compare(final ImagingDocument o1, final ImagingDocument o2) {
                    if (o1.getKeyIndex() == null) {
                        return 1;
                    } else if (o2.getKeyIndex() == null) {
                        return -1;
                    }
                    return o1.getKeyIndex().compareTo(o2.getKeyIndex());
                }
            });

            // dto.setTotalKeyImage(images.size());

            List<ImagingDocument> images1 = new ArrayList<ImagingDocument>();
            List<ImagingDocument> images2 = new ArrayList<ImagingDocument>();
            List<ImagingDocument> images3 = new ArrayList<ImagingDocument>();
            List<ImagingDocument> images4 = new ArrayList<ImagingDocument>();

            Integer imageCol = Lib.toInteger(param.get("imageCol"), 1);
            imageCol = imageCol == 2 || imageCol == 3 || imageCol == 4 ? imageCol : 3;
            if (imageCol == 3) {
                for (int i = 0; i < images.size(); i++) {
                    if (i % imageCol == 0) {
                        images1.add(images.get(i));
                    } else if (i % imageCol == 1) {
                        images2.add(images.get(i));
                    } else {
                        images3.add(images.get(i));
                    }
                }
                param.put("images1", images1);
                param.put("images2", images2);
                param.put("images3", images3);
            } else if (imageCol == 4) {
                for (int i = 0; i < images.size(); i++) {
                    if (i % imageCol == 0) {
                        images1.add(images.get(i));
                    } else if (i % imageCol == 1) {
                        images2.add(images.get(i));
                    } else if (i % imageCol == 2) {
                        images3.add(images.get(i));
                    } else {
                        images4.add(images.get(i));
                    }
                }
                param.put("images1", images1);
                param.put("images2", images2);
                param.put("images3", images3);
                param.put("images4", images4);
            } else {
                for (int i = 0; i < images.size(); i++) {
                    if (i % imageCol == 0) {
                        images1.add(images.get(i));
                    } else {
                        images2.add(images.get(i));
                    }
                }
                param.put("images1", images1);
                param.put("images2", images2);
            }

            param.put("diagnosis", Lib.nIb(diagnosis));
            param.put("deviceName", deviceName);
            param.put("title", title);
            param.put("titleForeign", titleForeign);
            param.put("technique", technique);
            param.put("findings", findings);
            param.put("impression", impression);
            param.put("recommendation", recommendation);
            param.put("note", note);
            param.put("subIndication", Lib.nIb(subIndication));
            param.put("subResults", subResults);
            param.put("fillFrame", fillFrame);

            //
            byte[] dataExport = pdf(param, dataSource, reportFile);
            if (dataExport == null) {
                return;
            }

            String fileName = resultCode + Const.EXTENSION_PDF;
            String filepath = resultCode;
            filepath = new SimpleDateFormat("yyyy/MM/dd").format(new Date(resultDate)) + "/" + filepath;
            File path = new File(dirpath, filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            // Output file pdf
            File file = new File(path, fileName);
            Files.write(file.toPath(), dataExport);
            log.info("file path: " + file.toPath());

            String sqlUpdate = "update imaging_result set filesystem_id = ?, filepath = ?, filename = ?, filetype = ?, filemime = ?, file_md5 = ?, file_size = ? where id = ?";
            PreparedStatement stmtUpdate = null;
            if (imagingResultId != null) {
                stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
                stmtUpdate.setLong(1, filesystemId);
                stmtUpdate.setString(2, filepath);
                stmtUpdate.setString(3, fileName);
                stmtUpdate.setString(4, Const.FILE_TYPE_PDF);
                stmtUpdate.setString(5, Const.CONTENT_TYPE_PDF);
                stmtUpdate.setString(6, Lib.getMD5Checksum(dataExport));
                stmtUpdate.setInt(7, dataExport.length);
                stmtUpdate.setLong(8, imagingResultId);
                stmtUpdate.executeUpdate();
                log.info("Update Imaging Result PDF (CODE: " + resultCode + ", ID: " + imagingResultId + ")");
            }

            close(stmt);
            close(rs);

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error create PDF" + e.toString());
        }
        close(connSkyOne);
        log.info("Create PDF: " + resultCode + "; " + Lib.dt2Str(resultDate, "dd/MM/yyyy HH:mm"));
    }

    public byte[] pdf(Map<String, Object> param, List<?> data, File report) throws Exception {
        return export(new JRPdfExporter(), param, data, report);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public byte[] export(Exporter exporter, Map<String, Object> param, List<?> data, File reportFile) throws Exception {
        if (!reportFile.exists()) {
            return null;
        }
        if (param == null) {
            param = new HashMap<String, Object>();
        }

        String reportPath = reportFile.getPath();

        param.put("SUBREPORT_DIR", reportPath);
        param.put("REPORT_PATH", reportPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRDataSource dataSource = null;
        if (data == null || data.isEmpty()) {
            dataSource = new JREmptyDataSource();
        } else {
            dataSource = new JRBeanCollectionDataSource(data, false);
        }

        JasperReport report = (JasperReport) JRLoader.loadObject(reportFile);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, dataSource);

        // Export data
        ExporterInput input = new SimpleExporterInput(jasperPrint);
        ExporterOutput output = new SimpleOutputStreamExporterOutput(baos);
        exporter.setExporterInput(input);
        exporter.setExporterOutput(output);
        exporter.exportReport();

        return baos.toByteArray();
    }

    public Map<Long, String> getDepartmentList() {
        Map<Long, String> items = new HashMap<Long, String>();
        items.put(13L, "Phòng khám");
        items.put(23L, "Cấp cứu");
        items.put(33L, "Khám sức khỏe");
        items.put(43L, "Vaccine");
        items.put(53L, "Nội soi");
        items.put(63L, "Tai - Mũi - Họng");
        items.put(73L, "Phụ khoa");
        items.put(83L, "Nam khoa");
        items.put(93L, "Nhãn khoa");
        items.put(103L, "Răng - Hàm - Mặt");
        items.put(113L, "Sản khoa");
        items.put(123L, "Nhi khoa");
        items.put(143L, "Hiếm muộn - Vô sinh");
        items.put(153L, "Tim mạch");
        items.put(713L, "Chẩn đoán hình ảnh");
        items.put(723L, "Thăm dò chức năng");
        items.put(743L, "Xét nghiệm");
        items.put(773L, "Giải phẫu bệnh - Tế bào bệnh học");
        items.put(853L, "Quầy thuốc");
        items.put(863L, "Kho dược - Vật tư");
        items.put(883L, "Trang thiết bị - Dụng cụ y tế");
        items.put(893L, "Điện nước");
        items.put(903L, "Phòng IT");
        items.put(913L, "Marketing");
        items.put(923L, "Bán hàng");
        items.put(933L, "Mua hàng");
        items.put(943L, "Tiếp tân");
        items.put(953L, "Thu ngân");
        items.put(963L, "Bảo hiểm");
        items.put(973L, "Kế toán");
        items.put(983L, "Hành chính - Nhân Sự");

        return items;
    }

    public Map<Long, String> getDepartmentsList() {
        Map<Long, String> items = new HashMap<Long, String>();
        Connection connSkyOne = null;

        try {
            connSkyOne = getSkyOneConnection();
            String sql = "select id, code, name from department where deleted_by is null";
            sql += " and branch_id = " + branchId;
            PreparedStatement stmt = connSkyOne.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            Long id = 0L;
            String code = "";
            String name = "";
            while (rs.next()) {
                id = rs.getLong("id");
                name = rs.getString("name");
                code = rs.getString("code").trim().toUpperCase();
                if (Lib.isEmpty(departments)) {
                    items.put(id, name);
                } else {
                    String[] departmentList = departments.split(" ");
                    for (int i = 0; i < departmentList.length; i++) {
                        if (departmentList[i].trim().toUpperCase().equals(code)) {
                            items.put(id, name);
                        }
                    }
                }

            }
            close(stmt);
            close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(connSkyOne);
        return items;
    }

    public Long findPartnerByExternalId(String externalId) {
        Long id = 0L;
        Connection connSkyOne = null;
        try {
            connSkyOne = getSkyOneConnection();
            PreparedStatement stmt = null;
            ResultSet rs = null;

            // check provider id from dlc
            stmt = connSkyOne.prepareStatement("select dlc_id from medic_map where metro = ? limit 1");
            stmt.setString(1, externalId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getLong("dlc_id");
                log.info("dlc_id from medic_map: " + id);
            }

            if (id == null || id == 0L) {
                stmt = connSkyOne.prepareStatement("select id from partner where external_uuid = ? limit 1");
                stmt.setString(1, externalId);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    id = rs.getLong("id");
                    log.info("dlc_id from partner: " + id);
                }
            }
            close(stmt);
            close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(connSkyOne);
        return id;
    }

    public Long doctorToPartnerByExternalId(String externalId) {
        Long id = 0L;
        Connection connSkyOne = null;
        Connection connMetro = null;
        try {
            connMetro = getMetroConnection();
            connSkyOne = getSkyOneConnection();
            PreparedStatement stmt = connSkyOne.prepareStatement("select id from partner where external_uuid = ?");
            stmt.setString(1, externalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getLong("id");
            }
            close(stmt);
            close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (id.equals(0)) {
            CopyDoctor copy = new CopyDoctor(args);
            id = copy.copyDoctorByExternalId(connMetro, connSkyOne, externalId);
        }

        close(connMetro);
        close(connSkyOne);
        return id;
    }

    public Long nurseToPartnerByExternalId(String externalId) {
        Long id = 0L;
        Connection connSkyOne = null;
        Connection connMetro = null;
        try {
            connMetro = getMetroConnection();
            connSkyOne = getSkyOneConnection();
            PreparedStatement stmt = connSkyOne.prepareStatement("select id from partner where external_uuid = ?");
            stmt.setString(1, externalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getLong("id");
            }
            close(stmt);
            close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (id.equals(0)) {
            CopyNurse copy = new CopyNurse(args);
            id = copy.copyNurseByExternalId(connMetro, connSkyOne, externalId);
        }

        close(connMetro);
        close(connSkyOne);
        return id;
    }

    public Long patientToPartnerByExternalId(String externalId) {
        Long id = 0L;
        Connection connSkyOne = null;
        Connection connMetro = null;
        try {
            connMetro = getMetroConnection();
            connSkyOne = getSkyOneConnection();
            PreparedStatement stmt = connSkyOne.prepareStatement("select id from partner where external_uuid = ?");
            stmt.setString(1, externalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getLong("id");
            }
            close(stmt);
            close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (id.equals(0)) {
            CopyPatients copy = new CopyPatients(args);
            id = copy.copyPatientsByExternalId(connMetro, connSkyOne, externalId);
        }

        close(connMetro);
        close(connSkyOne);
        return id;
    }

    public Integer getOrderNo(Long performanceDate) {
        Integer no = null;
        Connection connSkyOne = null;
        Long startDate = Lib.getStartDate(performanceDate);
        Long endDate = Lib.getEndDate(performanceDate);
        try {
            connSkyOne = getSkyOneConnection();
            PreparedStatement stmt = connSkyOne.prepareStatement(
                    "select id from imaging_result where  result_date between ? and ? order by result_date");
            stmt.setLong(1, startDate);
            stmt.setLong(2, endDate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                orderNo += 1;
            } else {
                orderNo = 1;
            }
            close(stmt);
            close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        close(connSkyOne);
        return orderNo;
    }

    public Item findItemByExternalUUID(String externalUUID) {
        Item item = null;
        Connection connSkyOne = null;
        try {
            connSkyOne = getSkyOneConnection();
            PreparedStatement stmt = connSkyOne.prepareStatement("select * from item where external_uuid = ? limit 1");
            stmt.setString(1, externalUUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                item = new Item(rs.getLong("id"), rs.getString("code"), rs.getString("name"),
                        rs.getString("origin_name"), rs.getString("cpt_name"));
            }
            close(stmt);
            close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (item == null) {
            System.out.println("externalUUID NULL: " + externalUUID);
            try {
                connSkyOne = getSkyOneConnection();
                PreparedStatement stmt = connSkyOne.prepareStatement(
                        "select * from \"EndoscopicProcedure\" where endoscopicprocedure_id = ? limit 1");
                stmt.setString(1, externalUUID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    item = new Item(0L, rs.getString("code"), rs.getString("reportname"), rs.getString("reportname"),
                            rs.getString("reportnameen"));
                }
                close(stmt);
                close(rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        close(connSkyOne);
        return item;
    }

    public Item findItemById(Long id) {
        Item item = null;
        Connection connSkyOne = null;
        try {
            connSkyOne = getSkyOneConnection();
            PreparedStatement stmt = connSkyOne.prepareStatement("select * from item where id = ?");
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                item = new Item();
                item.setId(rs.getLong("id"));
                item.setCode(rs.getString("code"));
                item.setUnitId(rs.getLong("unit_id"));
                item.setCategoryId(rs.getLong("category_id"));
                item.setScreen(rs.getString("screen"));
            }
            close(stmt);
            close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(connSkyOne);
        return item;
    }

    public String coalesce(String... objects) {
        for (String o : objects) {
            if (o != null) {
                return o;
            }
        }
        return "";
    }

    public Long coalesce(Long... objects) {
        for (Long o : objects) {
            if (o != null) {
                return o;
            }
        }
        return 0L;
    }

    public Long findImagingSub(String code) {
        Long id = 0L;
        Connection connSkyOne = null;
        try {
            connSkyOne = getSkyOneConnection();
            String sql = "select l.id id from item i join imaging_label l on i.id = l.item_id where i.code = ?";
            PreparedStatement stmt = connSkyOne.prepareStatement(sql);
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getLong("id");
            }
            close(stmt);
            close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(connSkyOne);
        return id != null ? id : 0L;
    }

    public EndoMedical findEndoMedicalById(String endoscopicmedicalrecordId) {
        EndoMedical item = null;
        Connection connMetro = null;
        try {
            connMetro = getMetroConnection();
            String sql = "select * from \"EndoscopicMedicalRecord\" where endoscopicmedicalrecord_id = ?::uuid";
            PreparedStatement stmt = connMetro.prepareStatement(sql);
            stmt.setString(1, endoscopicmedicalrecordId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                item = new EndoMedical();
                item.setCloTestResult(rs.getInt("clo_testresult"));
                item.setCloTestRemindTime(rs.getTimestamp("clo_testremindtime"));
                item.setBiosyResult(rs.getString("biopsyresult"));
                item.setBiopsyRemindTime(rs.getTimestamp("biopsyremindtime"));
                item.setHpResult(rs.getString("hpresult"));
                item.setHpRemindTime(rs.getTimestamp("hpremindtime"));
            }
            close(stmt);
            close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(connMetro);
        return item;

    }

    public String getCloTestItems(Integer cloTest) {
        String result = "";
        if (cloTest == 0) {
            result = "Chưa có kết quả";
        } else if (cloTest == 1) {
            result = "Âm tính";
        } else if (cloTest == 2) {
            result = "Dương tính";
        }
        return result;
    }

    public ResultSet findTable(String name, Long id) {
        Connection connSkyOne = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connSkyOne = getSkyOneConnection();
            stmt = connSkyOne.prepareStatement("select * from " + name + " where id = ?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(connSkyOne);
        close(stmt);
        return rs;
    }

}
