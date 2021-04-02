package vn.com.skyone.app2sky;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Convert extends CopyBase {

    public Convert(Integer nodeId) {
        super(nodeId);
    }

    public Convert(String[] args) {
        super(args);
    }

    public void covertText() {
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

        covertText(connMetro, connSkyOne, sysDate);

        close(connMetro);
        close(connSkyOne);

        long t2 = System.currentTimeMillis();
        log.info("CopyDoctor end in " + (t2 - t1) + " ms");
    }

    public void covertText1(Connection sourceConn, Connection targetConn, Long sysDate) {
        String sql = "select * from \"TE_PhieuCLS\" ";
        PreparedStatement sourceSelect = null;
        ResultSet rs = null;

        String path = "/text-convert/data/rtf/";
        String path1 = "/text-convert/data/html/";
        try {
            sourceSelect = sourceConn.prepareStatement(sql);
            rs = sourceSelect.executeQuery();
            int i = 0;
            while (rs.next()) {
                String name = rs.getString("HoTen");
                String result = rs.getString("Ketqua");
                String html = Lib.rtfToHtml(new StringReader(result));
                i += 1;
                String fileName = path + Lib.viLatin(name) + "_" + i + ".rtf";
                String fileName1 = path1 + Lib.viLatin(name) + "_" + i + ".html";

                Lib.writeFile(new File(fileName), result.getBytes(Charset.forName("UTF-8")));
                Lib.writeFile(new File(fileName1), html.getBytes(Charset.forName("UTF-8")));

                System.out.println("Ten " + i + ": " + name);
                System.out.println(result);
                System.out.println("-----------------------------------------------------------------------");
                System.out.println(html);
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                // System.out.println(Lib.convertToRTF(html));
                System.out.println("=======================================================================");
            }
        } catch (Exception e) {
            log.error("Error copyPatient: " + startDate, e);
        } finally {
            close(sourceConn);
            close(targetConn);
        }

    }

    public void covertText(Connection sourceConn, Connection targetConn, Long sysDate) {
        String path = "/text-convert/";
        // String text = Lib.fileToString(new File(path + "mau.html"));
        // String rtf = Lib.convertToRTF(text);
        String html = "";

        //System.out.println(text);
        // System.out.println(html);
        File target = new File(path + "mau-2.rtf");
        try {
            Lib.writeFile(target, "Lê Phúc Vĩnh".getBytes(Charset.forName("UTF-8")));
            // Lib.writeFile(target, rtf.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
