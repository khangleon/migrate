package vn.com.skyone.app2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Common extends CopyBase {

    public Common(Integer nodeId) {
        super(nodeId);
    }

    public Common(String[] args) {
        super(args);
    }

    public Map<Long, String> getDepartmentList() {
        Map<Long, String> items = new HashMap<Long, String>();
        items.put(13L, "Phòng khám");
        items.put(23L, "Cấp cứu");
        items.put(33L, "Khám sức kh�?e");
        items.put(43L, "Vaccine");
        items.put(53L, "Nội soi");
        items.put(63L, "Tai - Mũi - H�?ng");
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
        items.put(773L, "Giải phẫu bệnh - Tế bào bệnh h�?c");
        items.put(853L, "Quầy thuốc");
        items.put(863L, "Kho dược - Vật tư");
        items.put(883L, "Trang thiết bị - Dụng cụ y tế");
        items.put(893L, "�?iện nước");
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
                        if (departmentList[i].trim().toUpperCase().equals(code)){
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
            PreparedStatement stmt = connSkyOne
                    .prepareStatement("select id from partner where external_uuid = ? limit 1");
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
            //id = copy.copyDoctorByExternalId(connMetro, connSkyOne, externalId);
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

}
