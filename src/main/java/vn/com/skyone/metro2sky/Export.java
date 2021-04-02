package vn.com.skyone.metro2sky;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Export extends Common {

    public Export(Integer nodeId) {
        super(nodeId);
    }

    public Export(String[] args) {
        super(args);
    }

    /**
     * 
     * @param path
     */
    public void exportPDF() {
        Connection connSkyOne = null;
        Long sysDate = getSysDate();
        try {
            connSkyOne = getSkyOneConnection();
            log.info("exportPDF connect SKY ok");
        } catch (SQLException e) {
            log.error("exportPDF Connect database error", e);
            close(connSkyOne);
            return;
        }

        long t1 = System.currentTimeMillis();
        log.info("exportPDF start: " + (new Date()));
        log.info("exportPDF from: " + startDate);
        System.out.println("exportPDF start: " + (new Date()));

        exportPDF(connSkyOne, sysDate, Lib.dateToLong(fromDate), Lib.dateToLong(toDate));

        // close(connMetro);
        close(connSkyOne);

        long t2 = System.currentTimeMillis();
        log.info("exportPDF end in " + (t2 - t1) + " ms");
    }

    public void updatePDF() {
        Connection connSkyOne = null;
        Long sysDate = getSysDate();
        try {
            connSkyOne = getSkyOneConnection();
            log.info("exportPDF connect SKY ok");
        } catch (SQLException e) {
            log.error("exportPDF Connect database error", e);
            close(connSkyOne);
            return;
        }

        long t1 = System.currentTimeMillis();
        log.info("exportPDF start: " + (new Date()));
        log.info("exportPDF from: " + startDate);
        System.out.println("exportPDF start: " + (new Date()));

        updatePDF(connSkyOne, sysDate, Lib.dateToLong(fromDate), Lib.dateToLong(toDate));

        // close(connMetro);
        close(connSkyOne);

        long t2 = System.currentTimeMillis();
        log.info("exportPDF end in " + (t2 - t1) + " ms");
    }

    public void updatePDF(Connection connSkyOne, Long startDate, Long fromDate, Long toDate) {
        long t1 = System.currentTimeMillis();
        log.info("exportPDF start copy: " + startDate);
        String sqlSkyOne = "select id, search_text from imaging_result where deleted_by is null and external_uuid is not null and branch_id = 100021 ";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("********** Start update pdf (" + Lib.dt2Str(fromDate, "dd/MM/yyyy HH:mm:ss") + " - "
                + Lib.dt2Str(toDate, "dd/MM/yyyy HH:mm:ss") + ") **********");

        // handle update pdf
        String currentDir = System.getProperty("user.dir");
        String pathName = currentDir + "/data/base/partner.txt";
        String partnerIds = "";
        BufferedReader reader;
        int i = 0;
        try {
            reader = new BufferedReader(new FileReader(pathName));
            String line;
            while ((line = reader.readLine()) != null) {
                partnerIds += "," + line;
                i = i + 1;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        partnerIds = partnerIds.length() > 1 ? partnerIds.substring(1) : "";

        // sqlSkyOne += " and result_date between '" + fromDate + "' and '" +
        // toDate + "'";
        sqlSkyOne += " and patient_id in (" + partnerIds + ") ";
        sqlSkyOne += " order by patient_id, result_date " + sortType;

        //System.out.println("Danh sach: " + partnerIds);
        System.out.println("Tong khach hang: " + i);
        int j = 0;
        try {
            stmt = connSkyOne.prepareStatement(sqlSkyOne);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Long imagingResultId = rs.getLong("id");
                String searchText = rs.getString("search_text");
                exportImagingResult(imagingResultId);
                j = j + 1;
                System.out.println("Dang cap nhat: " + j + "; " + searchText);
            }
        } catch (Exception e) {
            log.error("Error exportPDF: " + startDate, e);
        } finally {
            close(rs);
            close(stmt);
        }

        long t2 = System.currentTimeMillis();
        log.info("exportPDF end in " + (t2 - t1) + " ms");
        System.out.println("********** End export pdf Sum Total: " + Lib.elapsedTime(t1, t2));
    }

    public void exportPDF(Connection connSkyOne, Long startDate, Long fromDate, Long toDate) {
        long t1 = System.currentTimeMillis();
        log.info("exportPDF start copy: " + startDate);
        String sqlSkyOne = "select id from imaging_result where deleted_by is null and external_uuid is not null and branch_id = 100021 ";

        sqlSkyOne += " and result_date between '" + fromDate + "' and '" + toDate + "'";
        sqlSkyOne += " order by result_date " + sortType;

        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("********** Start export pdf (" + Lib.dt2Str(fromDate, "dd/MM/yyyy HH:mm:ss") + " - "
                + Lib.dt2Str(toDate, "dd/MM/yyyy HH:mm:ss") + ") **********");

        try {
            stmt = connSkyOne.prepareStatement(sqlSkyOne);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Long imagingResultId = rs.getLong("id");
                exportImagingResult(imagingResultId);
            }

        } catch (Exception e) {
            log.error("Error exportPDF: " + startDate, e);
        } finally {
            close(rs);
            close(stmt);
        }

        long t2 = System.currentTimeMillis();
        log.info("exportPDF end in " + (t2 - t1) + " ms");
        System.out.println("********** End export pdf Sum Total: " + Lib.elapsedTime(t1, t2));
    }

    public void copyFiles() {
        Connection connSkyOne = null;
        Long sysDate = getSysDate();
        try {
            connSkyOne = getSkyOneConnection();
            log.info("copyFiles connect SKY ok");
        } catch (SQLException e) {
            log.error("copyFiles Connect database error", e);
            close(connSkyOne);
            return;
        }

        long t1 = System.currentTimeMillis();
        log.info("copyFiles start: " + (new Date()));
        log.info("copyFiles from: " + startDate);
        System.out.println("copyFiles start: " + (new Date()));

        copyFiles(connSkyOne, sysDate, Lib.dateToLong(fromDate), Lib.dateToLong(toDate));

        // close(connMetro);
        close(connSkyOne);

        long t2 = System.currentTimeMillis();
        log.info("copyFiles end in " + (t2 - t1) + " ms");
    }

    public void copyFiles(Connection connSkyOne, Long startDate, Long fromDate, Long toDate) {
        String dirPath = Const.DIR_PATH;

        long t1 = System.currentTimeMillis();
        log.info("copyFiles start copy: " + startDate);
        String sqlSkyOne = "select id, code, result_date from imaging_result where deleted_by is null and external_uuid is not null and branch_id = 100021 ";

        // sqlSkyOne += " and code = '20200713-98817' ";
        sqlSkyOne += " and result_date between '" + fromDate + "' and '" + toDate + "'";
        sqlSkyOne += " order by result_date " + sortType;

        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("********** Start copy files (" + Lib.dt2Str(fromDate, "dd/MM/yyyy HH:mm:ss") + " - "
                + Lib.dt2Str(toDate, "dd/MM/yyyy HH:mm:ss") + ") **********");

        try {
            stmt = connSkyOne.prepareStatement(sqlSkyOne);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Long imagingResultId = rs.getLong("id");
                String code = rs.getString("code");
                Long resultDate = rs.getLong("result_date");

                String sourceDir = "";
                boolean source = false;
                File path = new File(Const.DIR_SOURCE_FILE_DATA_1, code);
                if (path.exists()) {
                    source = true;
                    sourceDir = path.getAbsolutePath();
                } else {
                    path = new File(Const.DIR_SOURCE_FILE_DATA_2, code);
                    if (path.exists()) {
                        source = true;
                        sourceDir = path.getAbsolutePath();
                    } else {
                        source = false;
                        sourceDir = "";
                    }
                }

                log.info("Source directory: " + sourceDir);
                if (source && !Lib.isEmpty(sourceDir)) {
                    for (String e : listFiles(sourceDir)) {
                        File sourceFile = new File(sourceDir + "/" + e);
                        byte[] data = Lib.readFile(sourceFile);
                        if (data != null) {
                            Long dateCreated = 0L;
                            try {
                                dateCreated = getCreationTime(sourceFile).toMillis();
                            } catch (Exception ex) {
                                dateCreated = 0L;
                            }
                            updateImagingImage(connSkyOne, imagingResultId, code, data, resultDate, dateCreated, e, 0,
                                    null);
                        }
                    }
                }

            }

        } catch (Exception e) {
            log.error("Error copyFiles: " + startDate, e);
        } finally {
            close(rs);
            close(stmt);
        }

        long t2 = System.currentTimeMillis();
        log.info("copyFiles end in " + (t2 - t1) + " ms");
        System.out.println("********** End export pdf Sum Total: " + Lib.elapsedTime(t1, t2));
    }

    // 20200713-98817

    public Set<String> listFiles(String dir) {
        return Stream.of(new File(dir).listFiles()).filter(file -> !file.isDirectory()).map(File::getName)
                .collect(Collectors.toSet());
    }

    public static FileTime getCreationTime(File file) throws IOException {
        Path p = Paths.get(file.getAbsolutePath());
        BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
        FileTime fileTime = view.creationTime();
        return fileTime;
    }

    public void updateImagingImage(Connection connSkyOne, Long id, String code, byte[] data, Long dateCreated,
            Long dateModified, String mediainfoId, Integer keyImage, Integer keyIndex) throws Exception {
        long t1 = System.currentTimeMillis();
        log.info("updateImagingImage start copy: " + startDate);
        String sqlSelect = "select id from imaging_image where deleted_by is null and external_uuid = ? ";
        String sqlInsert = "insert into imaging_image(id, imaging_result_id, filesystem_id, filepath, filename, filetype, file_md5, file_size, created_by, created_date, updated_by, updated_date, version, external_uuid, key_image, key_index, published, filemime) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlUpdate = "update imaging_image set id = ?, imaging_result_id = ?, filesystem_id = ?, filepath = ?, filename = ?, filetype = ?, file_md5 = ?, file_size = ?, created_by = ?, created_date = ?, updated_by = ?, updated_date = ?, version = ?, external_uuid = ?,  key_image = ?, key_index = ?, published = ?, filemime = ? where id = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        // Long dirId = Const.DIR_PATH_ID;
        // String dirPath = Const.DIR_PATH;
        String filePath = "";
        String fileName = "";
        String fileType = "jpeg";
        String fileMd5 = Lib.getMD5Checksum(data);
        Long fileSize = (long) data.length;
        Long createdDate = dateCreated; // getMilliSecond(dateCreated);
        Long updatedDate = dateModified; // getMilliSecond(dateModified);
        Integer published = 1;
        String filemime = "image/jpeg";

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

            fileName = code + "_" + skyImagingImageId + "." + fileType;
            log.info("Insert (Copy files name: " + fileName + ")");

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
            stmtInsert.setLong(10, updatedDate);
            stmtInsert.setLong(11, createdId);
            stmtInsert.setLong(12, updatedDate);
            stmtInsert.setInt(13, version);
            stmtInsert.setString(14, mediainfoId);
            stmtInsert.setInt(15, keyImage);
            if (keyIndex == null) {
                stmtInsert.setNull(16, java.sql.Types.INTEGER);
            } else {
                stmtInsert.setInt(16, keyIndex);
            }
            stmtInsert.setInt(17, published);
            stmtInsert.setString(18, filemime);

            stmtInsert.executeUpdate();
            rowInsert++;
            System.out.println("Insert (Copy files name: " + fileName + ")");
        } else {

            fileName = code + "_" + skyImagingImageId + "." + fileType;
            log.info("Update (Copy files name: " + fileName + ")");

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
            stmtUpdate.setLong(10, updatedDate);
            stmtUpdate.setLong(11, createdId);
            stmtUpdate.setLong(12, updatedDate);
            stmtUpdate.setInt(13, version);
            stmtUpdate.setString(14, mediainfoId);
            stmtUpdate.setInt(15, keyImage);
            if (keyIndex == null) {
                stmtUpdate.setNull(16, java.sql.Types.INTEGER);
            } else {
                stmtUpdate.setInt(16, keyIndex);
            }
            stmtUpdate.setInt(17, published);
            stmtUpdate.setString(18, filemime);

            stmtUpdate.setLong(19, skyImagingImageId);
            stmtUpdate.executeUpdate();
            rowUpdate++;
            System.out.println("Update (Copy files name: " + fileName + ")");
        }

        File file = new File(path, fileName);
        Files.write(file.toPath(), data);

        long t2 = System.currentTimeMillis();
        log.info("updateImagingImage end in " + (t2 - t1) + " ms");
    }

}
