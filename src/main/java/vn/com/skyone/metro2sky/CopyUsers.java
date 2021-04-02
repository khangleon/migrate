package vn.com.skyone.metro2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class CopyUsers extends Common {

    public CopyUsers(Integer nodeId) {
        super(nodeId);
    }

    public CopyUsers(String[] args) {
        super(args);
    }

    /**
     * 
     * @param path
     */
    public void copyUsers() {
        Connection connSkyOne = null;
        Long sysDate = getSysDate();
        try {
            connSkyOne = getSkyOneConnection();
            log.info("copyUsers connect SKY ok");
        } catch (SQLException e) {
            log.error("copyUsers Connect database error", e);
            close(connSkyOne);
            return;
        }

        long t1 = System.currentTimeMillis();
        log.info("copyUsers start: " + (new Date()));
        log.info("copyUsers from: " + startDate);
        System.out.println("copyUsers start: " + (new Date()));

        copyUsers(connSkyOne, sysDate);

        close(connSkyOne);

        long t2 = System.currentTimeMillis();
        log.info("copyUsers end in " + (t2 - t1) + " ms");
    }

    public void copyUsers(Connection connSkyOne, Long startDate) {
        long t1 = System.currentTimeMillis();
        log.info("copyPatient start copy: " + startDate);
        String sqlSkyOne = "select id, user_id, name, nick_name from partner where deleted_by is null and type_medic = 1; ";
        PreparedStatement skyOneSelect = null;
        ResultSet rsSkyOne = null;

        rowInsert = 0;
        rowUpdate = 0;
        try {
            skyOneSelect = connSkyOne.prepareStatement(sqlSkyOne);
            rsSkyOne = skyOneSelect.executeQuery();
            while (rsSkyOne.next()) {
                Long patientId = null;
                try {
                    Long id = rsSkyOne.getLong("id");
                    Long userId = rsSkyOne.getLong("user_id");
                    String name = rsSkyOne.getString("name");
                    String nickName = rsSkyOne.getString("nick_name");
                    updateUsers(connSkyOne, id, userId, name, nickName);
                } catch (Exception e) {
                    log.error("Error update patientId: " + patientId, e);
                }
            }

        } catch (Exception e) {
            log.error("Error copyPatient: " + startDate, e);
        } finally {
            close(rsSkyOne);
            close(skyOneSelect);
        }

        long t2 = System.currentTimeMillis();
        log.info("copyPatient end in " + (t2 - t1) + " ms");
        System.out.println("copyUsers end in " + (t2 - t1) + " ms");
        System.out.println("Insert: " + rowInsert + " Update: " + rowUpdate);
    }

    public void updateUsers(Connection connSkyOne, Long id, Long userId, String name, String nickName)
            throws SQLException {
        long t1 = System.currentTimeMillis();
        log.info("updateUsers start copy: " + startDate);

        String sqlSelect = "select id from users where id = ? ";
        String sqlInsert = "insert into users(id,name,account,password,created_by,created_date,updated_by,updated_date,version) VALUES(?,?,?,?,?,?,?,?,?);";
        String sqlUpdate = "update users set id = ?,name = ?,account = ?,password = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ? where id = ? ;";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;

        // check partner exist in table
        Long skyUserId = null;
        stmtSelect = connSkyOne.prepareStatement(sqlSelect);
        stmtSelect.setLong(1, userId);
        rsSelect = stmtSelect.executeQuery();
        if (rsSelect.next()) {
            skyUserId = rsSelect.getLong("id");
        }

        Long updatedBy = 0L;
        Long updatedDate = new Date().getTime();
        Integer version = 1;
        String password = "974120113cfe59c243ef575dee6432f6e075c086";

        if (skyUserId == null) {
            skyUserId = getId("Users", connSkyOne);
            stmtInsert = connSkyOne.prepareStatement(sqlInsert);
            stmtInsert.setLong(1, skyUserId);
            stmtInsert.setString(2, name);
            stmtInsert.setString(3, nickName);
            stmtInsert.setString(4, password);

            stmtInsert.setLong(5, updatedBy);
            stmtInsert.setLong(6, updatedDate);
            stmtInsert.setLong(7, updatedBy);
            stmtInsert.setLong(8, updatedDate);
            stmtInsert.setInt(9, version);

            stmtInsert.executeUpdate();
            rowInsert++;
            System.out.println("inserted.. ");
        } else {
            stmtUpdate = connSkyOne.prepareStatement(sqlUpdate);
            stmtUpdate.setLong(1, skyUserId);
            stmtUpdate.setString(2, name);
            stmtUpdate.setString(3, nickName);
            stmtUpdate.setString(4, password);
            stmtUpdate.setLong(5, updatedBy);
            stmtUpdate.setLong(6, updatedDate);
            stmtUpdate.setLong(7, updatedBy);
            stmtUpdate.setLong(8, updatedDate);
            stmtUpdate.setInt(9, version);
            stmtUpdate.setLong(10, skyUserId);

            stmtUpdate.executeUpdate();
            rowUpdate++;
            System.out.println("updated.. ");
        }

        if (id != null) {
            String sqlUpdatePartner = "update partner set user_id = ? where id = ?";
            PreparedStatement stmtUpdatePartner = connSkyOne.prepareStatement(sqlUpdatePartner);
            stmtUpdatePartner.setLong(1, skyUserId);
            stmtUpdatePartner.setLong(2, id);
            stmtUpdatePartner.executeUpdate();
            System.out.println(" update Partner(id = " + id + ") by Users(id = " + skyUserId + ") ");
        }

        long t2 = System.currentTimeMillis();
        log.info("updateUsers end in " + (t2 - t1) + " ms");

    }

    @SuppressWarnings("resource")
    public Long updateUser(Connection connSkyOne, String name, String account, String passwords) throws SQLException {
        System.out.println("==Begin User==");
        String sqlSelect = "select id from users where account = ? ";
        String sqlInsert = "insert into users(id,name,account,password,created_by,created_date,updated_by,updated_date,version) VALUES(?,?,?,?,?,?,?,?,?);";
        String sqlUpdate = "update users set id = ?,name = ?,account = ?,password = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ? where id = ? ;";

        String sqlSelectPartner = "select id from partner where user_id = ?";
        String sqlInserPartner = "insert into partner(id, company_id, user_id, type_user, name, nick_name,created_by,created_date,updated_by,updated_date,version, search_text ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
        String sqlUpdatePartner = "update partner set company_id = ?, user_id = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ?, search_text = ? where id = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Long updatedBy = 0L;
        Long updatedDate = getSysDate();
        Integer version = 1;
        String password = "974120113cfe59c243ef575dee6432f6e075c086";
        if (passwords != null) {
            password = Lib.getPassword(passwords);
        }

        Long skyUserId = null;
        stmt = connSkyOne.prepareStatement(sqlSelect);
        stmt.setString(1, account);
        rs = stmt.executeQuery();
        if (rs.next()) {
            skyUserId = rs.getLong("id");
        }

        // update user
        if (skyUserId == null) {
            skyUserId = getId("Users", connSkyOne);
            stmt = connSkyOne.prepareStatement(sqlInsert);
            stmt.setLong(1, skyUserId);
            stmt.setString(2, name);
            stmt.setString(3, account);
            stmt.setString(4, password);

            stmt.setLong(5, updatedBy);
            stmt.setLong(6, updatedDate);
            stmt.setLong(7, updatedBy);
            stmt.setLong(8, updatedDate);
            stmt.setInt(9, version);

            stmt.executeUpdate();
            rowInsert++;
            System.out.println("-inserted user.. ");
        } else {
            stmt = connSkyOne.prepareStatement(sqlUpdate);
            stmt.setLong(1, skyUserId);
            stmt.setString(2, name);
            stmt.setString(3, account);
            stmt.setString(4, password);
            stmt.setLong(5, updatedBy);
            stmt.setLong(6, updatedDate);
            stmt.setLong(7, updatedBy);
            stmt.setLong(8, updatedDate);
            stmt.setInt(9, version);
            stmt.setLong(10, skyUserId);
            stmt.executeUpdate();
            rowUpdate++;
            System.out.println("-updated user.. ");
        }

        // update partner
        // find partner
        if (skyUserId != null) {
            Long partnerId = null;
            stmt = connSkyOne.prepareStatement(sqlSelectPartner);
            stmt.setLong(1, skyUserId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                partnerId = rs.getLong("id");
            }

            if (partnerId == null) {
                partnerId = getId("Partner", connSkyOne);
                stmt = connSkyOne.prepareStatement(sqlInserPartner);
                stmt.setLong(1, partnerId);
                stmt.setLong(2, companyId);
                stmt.setLong(3, skyUserId);
                stmt.setInt(4, 1);
                stmt.setString(5, name);
                stmt.setString(6, account);
                stmt.setLong(7, updatedBy);
                stmt.setLong(8, updatedDate);
                stmt.setLong(9, updatedBy);
                stmt.setLong(10, updatedDate);
                stmt.setInt(11, version);
                stmt.setString(12, Lib.viLatin(name).toUpperCase());

                stmt.executeUpdate();
                rowInsert++;
                System.out.println("--inserted partner.. ");
            } else {
                stmt = connSkyOne.prepareStatement(sqlUpdatePartner);
                stmt.setLong(1, companyId);
                stmt.setLong(2, skyUserId);
                stmt.setLong(3, updatedBy);
                stmt.setLong(4, updatedDate);
                stmt.setLong(5, updatedBy);
                stmt.setLong(6, updatedDate);
                stmt.setInt(7, version);
                stmt.setString(8, Lib.viLatin(name).toUpperCase());
                stmt.setLong(9, partnerId);

                stmt.executeUpdate();
                System.out.println("--update partner(" + partnerId + ") by Users(" + skyUserId + ")..");
            }
        }
        System.out.println("==End User(" + skyUserId + ")==");
        return skyUserId;
    }

    @SuppressWarnings("resource")
    public Long updateUser(Connection connSkyOne, String name, String account) throws SQLException {
        return updateUser(connSkyOne, name, account, null);
    }

    /**
     * Update user brand
     * 
     * @throws SQLException
     */
    @SuppressWarnings("resource")
    public void updateUserBranch(Connection connSkyOne, Long branchId, Long userId) throws SQLException {
        String sqlSelectUserBranch = "select id from user_branch where user_id = ?";
        String sqlInsertUserBranch = "insert into user_branch(id, branch_id, user_id,created_by,created_date,updated_by,updated_date,version) values(?,?,?,?,?,?,?,?)";
        String sqlUpdateUserBranch = "update user_branch set id = ?, branch_id = ?, user_id = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ? where id = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Long updatedBy = 0L;
        Long updatedDate = getSysDate();
        Integer version = 1;

        // find user branch
        Long userBranchId = null;
        stmt = connSkyOne.prepareStatement(sqlSelectUserBranch);
        stmt.setLong(1, userId);
        rs = stmt.executeQuery();
        if (rs.next()) {
            userBranchId = rs.getLong("id");
        }

        if (userBranchId == null) {
            userBranchId = getId("UserBranch", connSkyOne);
            stmt = connSkyOne.prepareStatement(sqlInsertUserBranch);
            stmt.setLong(1, userBranchId);
            stmt.setLong(2, branchId);
            stmt.setLong(3, userId);
            stmt.setLong(4, updatedBy);
            stmt.setLong(5, updatedDate);
            stmt.setLong(6, updatedBy);
            stmt.setLong(7, updatedDate);
            stmt.setInt(8, version);
            stmt.executeUpdate();
            System.out.println("-insert UserBranch -> (" + userId + "," + branchId + ")");
        } else {
            stmt = connSkyOne.prepareStatement(sqlUpdateUserBranch);
            stmt.setLong(1, userBranchId);
            stmt.setLong(2, branchId);
            stmt.setLong(3, userId);
            stmt.setLong(4, updatedBy);
            stmt.setLong(5, updatedDate);
            stmt.setLong(6, updatedBy);
            stmt.setLong(7, updatedDate);
            stmt.setInt(8, version);
            stmt.setLong(9, userBranchId);
            stmt.executeUpdate();
            System.out.println("-update UserBranch -> (" + userId + "," + branchId + ")");
        }
    }

    public void updateUserDepartment(Connection connSkyOne, Long branchId, Long userId) throws SQLException {
        Map<Long, String> departments = getDepartmentsList();
        for (Map.Entry<Long, String> entry : departments.entrySet()) {
            updateUserDepartment(connSkyOne, branchId, userId, entry.getKey());
        }
    }

    /**
     * Update user deparment
     * 
     * @throws SQLException
     */
    @SuppressWarnings("resource")
    public void updateUserDepartment(Connection connSkyOne, Long branchId, Long userId, Long departmentId)
            throws SQLException {
        String sqlSelectUserDepartment = "select id from user_department where user_id = ? and department_id = ?";
        String sqlInsertUserDepartment = "insert into user_department(id, branch_id, user_id,department_id, created_by,created_date,updated_by,updated_date,version) values(?,?,?,?,?,?,?,?,?)";
        String sqlUpdateUserDepartment = "update user_department set id = ?, branch_id = ?, user_id = ?,department_id = ?,created_by = ?,created_date = ?,updated_by = ?,updated_date = ?,version = ? where id = ?";

        String sqlDeleteUserDepartment = "delete from user_department where id = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Long updatedBy = 0L;
        Long updatedDate = getSysDate();
        Integer version = 1;

        // find user department
        Long userDepartmentId = null;
        stmt = connSkyOne.prepareStatement(sqlSelectUserDepartment);
        stmt.setLong(1, userId);
        stmt.setLong(2, departmentId);
        rs = stmt.executeQuery();
        if (rs.next()) {
            userDepartmentId = rs.getLong("id");
        }

        if (userDepartmentId == null) {
            userDepartmentId = getId("UserDepartment", connSkyOne);
            stmt = connSkyOne.prepareStatement(sqlInsertUserDepartment);
            stmt.setLong(1, userDepartmentId);
            stmt.setLong(2, branchId);
            stmt.setLong(3, userId);
            stmt.setLong(4, departmentId);
            stmt.setLong(5, updatedBy);
            stmt.setLong(6, updatedDate);
            stmt.setLong(7, updatedBy);
            stmt.setLong(8, updatedDate);
            stmt.setInt(9, version);
            stmt.executeUpdate();
            System.out.println("-insert UserDepartment -> (" + userId + "," + departmentId + ")");
        } else {

            stmt = connSkyOne.prepareStatement(sqlUpdateUserDepartment);
            stmt.setLong(1, userDepartmentId);
            stmt.setLong(2, branchId);
            stmt.setLong(3, userId);
            stmt.setLong(4, departmentId);
            stmt.setLong(5, updatedBy);
            stmt.setLong(6, updatedDate);
            stmt.setLong(7, updatedBy);
            stmt.setLong(8, updatedDate);
            stmt.setInt(9, version);
            stmt.setLong(10, userDepartmentId);
            stmt.executeUpdate();
            System.out.println("-update UserDepartment -> (" + userId + "," + departmentId + ")");
        }
    }

    /**
     * Delete user department
     * 
     * @throws SQLException
     */
    public void deleteUserDepartment(Connection connSkyOne, Long branchId, Long userId) throws SQLException {
        String sqlSelectUserDepartment = "select us.id, us.department_id, d.code from user_department us join department d on us.department_id = d.id where us.deleted_by is null and us.branch_id = ? and us.user_id = ? ";

        if (!Lib.isEmpty(departments)) {
            String[] ds = departments.split(" ");
            String list = "";
            for (String s : ds) {
                list += ",'" + s.toUpperCase() + "'";
            }
            list = list.length() > 1 ? list.substring(1) : list;
            sqlSelectUserDepartment += " and upper(d.code) not in (" + list + ")";
        }
        //System.out.println("sql: " + sqlSelectUserDepartment);
        String sqlDeleteUserDepartment = "delete from user_department where id = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        // find user department
        Long userDepartmentId = null;
        Long departmentId = null;
        String departmentCode = "";
        stmt = connSkyOne.prepareStatement(sqlSelectUserDepartment);
        stmt.setLong(1, branchId);
        stmt.setLong(2, userId);
        rs = stmt.executeQuery();
        while (rs.next()) {
            userDepartmentId = rs.getLong("id");
            departmentId = rs.getLong("department_id");
            departmentCode = rs.getString("code");
            if (userDepartmentId != null) {
                stmt = connSkyOne.prepareStatement(sqlDeleteUserDepartment);
                stmt.setLong(1, userDepartmentId);
                stmt.executeUpdate();
                System.out.println(
                        "--delete UserDepartment -> (" + userId + "," + departmentId + "[" + departmentCode + "])");
            }
        }
    }

    public void newUser() {
        Connection connSkyOne = null;
        try {
            connSkyOne = getSkyOneConnection();
            updateUser(connSkyOne, "Administrator", "admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(connSkyOne);
    }

    public void newUser(String name, String account, String password) {
        Connection connSkyOne = null;
        Long userId = null;

        // create user
        try {
            connSkyOne = getSkyOneConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            userId = updateUser(connSkyOne, name, account, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // create user branch
        try {
            updateUserBranch(connSkyOne, branchId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // create user department
        try {
            updateUserDepartment(connSkyOne, branchId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (del) {
            // delete user department
            try {
                deleteUserDepartment(connSkyOne, branchId, userId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Create Account: " + account + ", name: " + name);
        close(connSkyOne);
    }

    public void newUser(String name, String account) {
        newUser(name, account, null);
    }

}
