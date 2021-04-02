package vn.com.skyone.app2sky;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileSystem extends CopyBase {

	public FileSystem(Integer nodeId) {
		super(nodeId);
	}

	public FileSystem(String[] args) {
		super(args);
	}

	/**
	 * 
	 * @param path
	 * @throws Exception
	 */
	public void fileSystem() throws Exception {
		Connection connSkyOne = null;
		try {
			connSkyOne = getSkyOneConnection();
			log.info("CopyDoctor connect SKY ok");
		} catch (SQLException e) {
			close(connSkyOne);
			return;
		}

		fileSystem(connSkyOne, companyId, branchId);

		close(connSkyOne);
	}

	public void fileSystem(Connection connSkyOne, Long companyId, Long branchId) throws Exception {
		String sqlSelect = "select id, dirpath from filesystem where deleted_by is null and company_id = ? and branch_id = ?";
		String sqlInsert = "insert into filesystem(id,company_id, branch_id, dirpath, fs_group_id, category, min_space_free, status, next_id ) VALUES(?,?,?,?,?,?,?,?,?)";
		PreparedStatement skyOneSelect = null;
		PreparedStatement stmtInsert = null;
		ResultSet rsSkyOne = null;

		Long skyFileSystemId = null;
		String path = "";
		skyOneSelect = connSkyOne.prepareStatement(sqlSelect);
		skyOneSelect.setLong(1, companyId);
		skyOneSelect.setLong(2, branchId);
		rsSkyOne = skyOneSelect.executeQuery();
		if (rsSkyOne.next()) {
			skyFileSystemId = rsSkyOne.getLong("id");
			path = rsSkyOne.getString("dirpath");
		}

		if (skyFileSystemId == null) {
			skyFileSystemId = getId("Filesystem", connSkyOne);
			stmtInsert = connSkyOne.prepareStatement(sqlInsert);
			stmtInsert.setLong(1, skyFileSystemId);
			stmtInsert.setLong(2, companyId);
			stmtInsert.setLong(3, branchId);

			stmtInsert.setString(4, dirPath);
			stmtInsert.setString(5, Const.FS_GROUP_ID);
			stmtInsert.setString(6, Const.CATEGORY);
			stmtInsert.setLong(7, Const.MIN_SPACE_FREE);
			stmtInsert.setInt(8, 2);
			stmtInsert.setLong(9, 0);
			stmtInsert.executeUpdate();
			System.out.println("inserted filesystem");
		} else {
			dirPath = path;
		}
		dirId = skyFileSystemId;

	}

}
