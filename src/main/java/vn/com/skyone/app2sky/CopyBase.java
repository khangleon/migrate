package vn.com.skyone.app2sky;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Option.Builder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.postgresql.xa.PGXADataSource;

import com.microsoft.sqlserver.jdbc.SQLServerConnectionPoolDataSource;

public abstract class CopyBase {
    protected static Logger log = LogManager.getLogger(CopyBase.class);
    protected final static Logger logger = StatusLogger.getLogger();

    protected Integer nodeId;
    protected String urlSkyOne;
    protected String urlMetro;
    protected String urlPacs;
    protected Date startDate;
    protected Date fromDate;
    protected Date toDate;

    protected String[] args;

    protected Long companyId = Const.COMPANY_ID;
    protected Long branchId = Const.BRANCH_ID;
    protected Long createdId = Const.CREATED_ID;
    protected Long endoDepartmentId = Const.ENDO_DEPARTMENT;

    protected Integer rowInsert = 0;
    protected Integer rowUpdate = 0;
    protected Integer orderNo = 1;
    protected Integer version = 1;
    protected Integer limit = 0;
    protected Long templateId = Const.TEMPLATE_ID;
    protected Integer typeOfContent = -1;
    protected String sortType = "";
    protected boolean archive = false;
    protected String departments = "";
    protected boolean del = false;

    protected String dirPath;
    protected Long dirId;

    public Long getSysDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public CopyBase(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public CopyBase(String[] args) {
        this.args = args;
        try {
            CommandLine cl = parseComandLine(args);

            if (cl.hasOption("cmet")) {
                urlMetro = cl.getOptionValue("cmet");
            }

            if (cl.hasOption("csky")) {
                urlSkyOne = cl.getOptionValue("csky");
            }
            
            if (cl.hasOption("cpacs")) {
                urlPacs = cl.getOptionValue("cpacs");
            }

            if (cl.hasOption("startdate")) {
                String date = cl.getOptionValue("startdate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                try {
                    startDate = Lib.getStartDate(sdf.parse(date));
                } catch (java.text.ParseException e) {
                    startDate = null;
                }
            }
            if (startDate == null) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = cal.getTime();
            }

            if (cl.hasOption("fromdate")) {
                String date = cl.getOptionValue("fromdate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                try {
                    fromDate = Lib.getStartDate(sdf.parse(date));
                } catch (java.text.ParseException e) {
                    fromDate = null;
                }
            }
            if (fromDate == null) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, cal.getActualMinimum(Calendar.YEAR));
                cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
                cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
                cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
                cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
                cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
                fromDate = cal.getTime();
            }

            if (cl.hasOption("todate")) {
                String date = cl.getOptionValue("todate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                try {
                    toDate = Lib.getEndDate(sdf.parse(date));
                } catch (java.text.ParseException e) {
                    startDate = null;
                }
            }
            if (toDate == null) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, 9999);
                cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
                cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
                cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
                toDate = cal.getTime();
            }

            if (cl.hasOption("nid")) {
                nodeId = Integer.parseInt(cl.getOptionValue("nid"));
            } else {
                nodeId = Const.NODE_ID;
            }

            if (cl.hasOption("cid")) {
                companyId = Long.parseLong(cl.getOptionValue("cid"));
            } else {
                companyId = Const.COMPANY_ID;
            }

            if (cl.hasOption("bid")) {
                branchId = Long.parseLong(cl.getOptionValue("bid"));
            } else {
                branchId = Const.BRANCH_ID;
            }

            if (cl.hasOption("crud")) {
                createdId = Long.parseLong(cl.getOptionValue("crud"));
            } else {
                createdId = Const.CREATED_ID;
            }

            if (cl.hasOption("edi")) {
                endoDepartmentId = Long.parseLong(cl.getOptionValue("edi"));
            }

            if (cl.hasOption("cdirpath")) {
                dirPath = String.valueOf(cl.getOptionValue("cdirpath"));
            } else {
                dirPath = Const.DIR_PATH;
            }

            if (cl.hasOption("cdirid")) {
                dirId = Long.parseLong(cl.getOptionValue("cdirid"));
            } else {
                dirId = Const.DIR_PATH_ID;
            }

            if (cl.hasOption("limit")) {
                limit = Integer.parseInt(cl.getOptionValue("limit"));
            } else {
                limit = 0;
            }

            if (cl.hasOption("tid")) {
                templateId = Long.parseLong(cl.getOptionValue("tid"));
            } else {
                templateId = Const.TEMPLATE_ID;
            }

            if (cl.hasOption("toc")) {
                typeOfContent = Integer.parseInt(cl.getOptionValue("toc"));
            } else {
                typeOfContent = -1;
            }

            if (cl.hasOption("sorttype")) {
                sortType = cl.getOptionValue("sorttype");
            } else {
                sortType = "";
            }

            if (cl.hasOption("archive")) {
                archive = true;
            }

            if (cl.hasOption("del")) {
                del = true;
            } else {
                del = false;
            }

            if (cl.hasOption("departments")) {
                departments = cl.getOptionValue("departments");
            } else {
                departments = "";
            }

            if (log.isDebugEnabled()) {
                // log.debug("urlMetro = " + urlMetro);
                // log.debug("urlSkyOne = " + urlSkyOne);
                log.debug("startDate = " + startDate);
            }
        } catch (ParseException e) {
            log.error("Init error", e);
        }
    }

    private static CommandLine parseComandLine(String[] args) throws ParseException {
        Options opts = new Options();
        addConnectOption(opts);

        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(opts, args);
        return cl;
    }

    public static void addConnectOption(Options opts) {
        Builder builder = Option.builder("cmet");
        builder.hasArg();
        builder.argName("url");
        builder.desc(
                "Url connect database metro: jdbc:postgresql://host:port/databaseName?user=user&password=password");
        builder.longOpt("connectmet");
        opts.addOption(builder.build());

        builder = Option.builder("csky");
        builder.hasArg();
        builder.argName("url");
        builder.desc(
                "Url connect database skyapp: jdbc:postgresql://host:port/databaseName?user=user&password=password");
        builder.longOpt("connectsky");
        opts.addOption(builder.build());
        
        builder = Option.builder("cpacs");
        builder.hasArg();
        builder.argName("url");
        builder.desc(
                "Url connect database lab: jdbc:sqlserver://host:port;instanceName=instanceName;databaseName=databaseName;user=user;password=password");
        builder.longOpt("connectpacs");
        opts.addOption(builder.build());

        builder = Option.builder("startdate");
        builder.hasArg();
        builder.argName("date");
        builder.desc("Date yyyyMMdd");
        builder.longOpt("startdate");
        opts.addOption(builder.build());

        builder = Option.builder("fromdate");
        builder.hasArg();
        builder.argName("date");
        builder.desc("Date yyyyMMdd");
        builder.longOpt("fromdate");
        opts.addOption(builder.build());

        builder = Option.builder("todate");
        builder.hasArg();
        builder.argName("date");
        builder.desc("Date yyyyMMdd");
        builder.longOpt("todate");
        opts.addOption(builder.build());

        builder = Option.builder("interval");
        builder.hasArg();
        builder.argName("ms");
        builder.desc("Interval run copy in ms");
        builder.longOpt("interval");
        opts.addOption(builder.build());

        builder = Option.builder("nid");
        builder.hasArg();
        builder.argName("");
        builder.desc("Id of node");
        builder.longOpt("nodeid");
        opts.addOption(builder.build());

        builder = Option.builder("cid");
        builder.hasArg();
        builder.argName("");
        builder.desc("Id of company");
        builder.longOpt("companyid");
        opts.addOption(builder.build());

        builder = Option.builder("bid");
        builder.hasArg();
        builder.argName("");
        builder.desc("Id of branch");
        builder.longOpt("branchid");
        opts.addOption(builder.build());

        builder = Option.builder("crud");
        builder.hasArg();
        builder.argName("");
        builder.desc("Id of creator");
        builder.longOpt("crudid");
        opts.addOption(builder.build());

        builder = Option.builder("edi");
        builder.hasArg();
        builder.argName("");
        builder.desc("Endo department id");
        builder.longOpt("endodepartmentid");
        opts.addOption(builder.build());

        builder = Option.builder("cdirpath");
        builder.hasArg();
        builder.argName("");
        builder.desc("Id of creator");
        builder.longOpt("cdirpath");
        opts.addOption(builder.build());

        builder = Option.builder("cdirid");
        builder.hasArg();
        builder.argName("");
        builder.desc("Id of creator");
        builder.longOpt("cdirid");
        opts.addOption(builder.build());

        builder = Option.builder("info");
        builder.hasArg(false);
        builder.argName("");
        builder.desc("Only show infomation");
        builder.longOpt("infomation");
        opts.addOption(builder.build());

        builder = Option.builder("account");
        builder.hasArg();
        builder.argName("");
        builder.desc("Create account");
        builder.longOpt("account");
        opts.addOption(builder.build());

        builder = Option.builder("name");
        builder.hasArg();
        builder.argName("");
        builder.desc("Create name");
        builder.longOpt("name");
        opts.addOption(builder.build());

        builder = Option.builder("pw");
        builder.hasArg();
        builder.argName("");
        builder.desc("Create password");
        builder.longOpt("password");
        opts.addOption(builder.build());

        builder = Option.builder("departments");
        builder.hasArg();
        builder.argName("");
        builder.desc("Create department list");
        builder.longOpt("departments");
        opts.addOption(builder.build());

        builder = Option.builder("limit");
        builder.hasArg();
        builder.argName("");
        builder.desc("LIMIT");
        builder.longOpt("limit");
        opts.addOption(builder.build());

        builder = Option.builder("sub");
        builder.hasArg(false);
        builder.argName("");
        builder.desc("Make sub item");
        builder.longOpt("subitem");
        opts.addOption(builder.build());

        builder = Option.builder("tid");
        builder.hasArg();
        builder.argName("");
        builder.desc("Template ID");
        builder.longOpt("templateid");
        opts.addOption(builder.build());

        builder = Option.builder("toc");
        builder.hasArg();
        builder.argName("");
        builder.desc("Type Of Content");
        builder.longOpt("typeofcontent");
        opts.addOption(builder.build());

        builder = Option.builder("st");
        builder.hasArg();
        builder.argName("");
        builder.desc("Sort type asc/desc");
        builder.longOpt("sorttype");
        opts.addOption(builder.build());

        builder = Option.builder("archive");
        builder.hasArg(false);
        builder.argName("");
        builder.desc("Store file");
        builder.longOpt("archive");
        opts.addOption(builder.build());
        
        builder = Option.builder("del");
        builder.hasArg(false);
        builder.argName("");
        builder.desc("Delete");
        builder.longOpt("del");
        opts.addOption(builder.build());
    }

    private static PGXADataSource skyOneDS = null;

    public Connection getSkyOneConnection() throws SQLException {
        if (skyOneDS == null) {
            skyOneDS = new PGXADataSource();
            skyOneDS.setDatabaseName("SkyOne");

            if (urlSkyOne == null) {
                StringBuffer sb = new StringBuffer("jdbc:postgresql://");
                sb.append(Const.SKY_HOST);
                sb.append(":");
                sb.append(Const.SKY_PORT);
                sb.append("/");
                sb.append(Const.SKY_DB);
                sb.append("?user=");
                sb.append(Const.SKY_USER);
                sb.append("&password=");
                sb.append(Const.SKY_PASS);
                sb.append("&useUnicode=true&characterEncoding=UTF-8");
                urlSkyOne = sb.toString();
            }
            skyOneDS.setUrl(urlSkyOne);
            skyOneDS.setConnectTimeout(50);
        }
        return skyOneDS.getConnection();
    }

    private static PGXADataSource metroDS = null;

    public Connection getMetroConnection() throws SQLException {
        if (metroDS == null) {
            metroDS = new PGXADataSource();
            metroDS.setDatabaseName("Metromed");

            if (urlMetro == null) {
                StringBuffer sb = new StringBuffer("jdbc:postgresql://");
                sb.append(Const.METRO_HOST);
                sb.append(":");
                sb.append(Const.METRO_PORT);
                sb.append("/");
                sb.append(Const.METRO_DB);
                sb.append("?user=");
                sb.append(Const.METRO_USER);
                sb.append("&password=");
                sb.append(Const.METRO_PASS);
                sb.append("&useUnicode=true&characterEncoding=UTF-8");
                urlMetro = sb.toString();
            }
            metroDS.setUrl(urlMetro);
            metroDS.setConnectTimeout(50);
        }
        return metroDS.getConnection();
    }
    
    SQLServerConnectionPoolDataSource pacsDS = null;

    public Connection getPacsConnection() throws SQLException {
        if (pacsDS == null) {
            pacsDS = new SQLServerConnectionPoolDataSource();

            if (urlPacs == null) {
                urlPacs = "jdbc:sqlserver://192.168.0.9:1433;instanceName=SQL2008;databaseName=mdsoft;user=thongke;password=thongke;useUnicode=true;characterEncoding=UTF-8";
            }

            pacsDS.setURL(urlPacs);
        }
        return pacsDS.getConnection();
    }

    public void close(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }

    public void close(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                if (!stmt.isClosed()) {
                    stmt.close();
                }
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }

    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                if (!rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }

    public void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    public Long getGlobalId(String name, Connection conn) {
        String sqlSelect = "select t.value from gen_id t where t.name = ?";
        String sqlInsert = "insert into gen_id(name, value) values(?, ?)";
        String sqlUpdate = "update gen_id set value = ? where name = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;
        ResultSet rsInsert = null;
        InputStream blobData = null;
        Long value = null;
        try {
            stmtSelect = conn.prepareStatement(sqlSelect);
            stmtSelect.setObject(1, name);
            rsSelect = stmtSelect.executeQuery();
            if (rsSelect.next()) {
                value = rsSelect.getLong(1);
            }

            if (value == null) {
                value = 1L;
                stmtInsert = conn.prepareStatement(sqlInsert);
                stmtInsert.setString(1, name);
                stmtInsert.setLong(2, value);

                stmtInsert.executeUpdate();
            } else {
                value++;
                stmtUpdate = conn.prepareStatement(sqlUpdate);
                stmtUpdate.setLong(1, value);
                stmtUpdate.setString(2, name);

                stmtUpdate.executeUpdate();
            }
        } catch (Exception e) {
            log.error("Error getId: " + name, e);
        } finally {
            close(stmtSelect);
            close(stmtInsert);
            close(stmtUpdate);
            close(rsSelect);
            close(rsInsert);
            close(blobData);
        }

        return value;
    }

    public Long getId(String name, Connection conn) {
        String sqlSelect = "select t.value from gen_id t where t.name = ?";
        String sqlInsert = "insert into gen_id(name, value) values(?, ?)";
        String sqlUpdate = "update gen_id set value = ? where name = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;
        ResultSet rsInsert = null;
        InputStream blobData = null;
        Long value = null;
        try {
            Integer year = Calendar.getInstance().get(Calendar.YEAR);
            name = year + name + nodeId;
            stmtSelect = conn.prepareStatement(sqlSelect);
            stmtSelect.setObject(1, name);
            rsSelect = stmtSelect.executeQuery();
            if (rsSelect.next()) {
                value = rsSelect.getLong(1);
            }

            if (value == null) {
                value = 1L;
                stmtInsert = conn.prepareStatement(sqlInsert);
                stmtInsert.setString(1, name);
                stmtInsert.setLong(2, value);

                stmtInsert.executeUpdate();
            } else {
                value++;
                stmtUpdate = conn.prepareStatement(sqlUpdate);
                stmtUpdate.setLong(1, value);
                stmtUpdate.setString(2, name);

                stmtUpdate.executeUpdate();
            }

            value = value * 100000000 + nodeId * 10000 + year;
        } catch (Exception e) {
            log.error("Error getId: " + name, e);
        } finally {
            close(stmtSelect);
            close(stmtInsert);
            close(stmtUpdate);
            close(rsSelect);
            close(rsInsert);
            close(blobData);
        }

        return value;
    }

    public Long getSeq(String name, Connection conn) {
        String sqlSelect = "select t.value from gen_seq t where t.name = ?";
        String sqlInsert = "insert into gen_seq(name, value) values(?, ?)";
        String sqlUpdate = "update gen_seq set value = ? where name = ?";

        PreparedStatement stmtSelect = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;
        ResultSet rsSelect = null;
        ResultSet rsInsert = null;
        InputStream blobData = null;
        Long value = null;
        try {
            stmtSelect = conn.prepareStatement(sqlSelect);
            stmtSelect.setObject(1, name);
            rsSelect = stmtSelect.executeQuery();
            if (rsSelect.next()) {
                value = rsSelect.getLong(1);
            }

            if (value == null) {
                value = 1L;
                stmtInsert = conn.prepareStatement(sqlInsert);
                stmtInsert.setString(1, name);
                stmtInsert.setLong(2, value);

                stmtInsert.executeUpdate();
            } else {
                value++;
                stmtUpdate = conn.prepareStatement(sqlUpdate);
                stmtUpdate.setLong(1, value);
                stmtUpdate.setString(2, name);

                stmtUpdate.executeUpdate();
            }
        } catch (Exception e) {
            log.error("Error getSeq: " + name, e);
        } finally {
            close(stmtSelect);
            close(stmtInsert);
            close(stmtUpdate);
            close(rsSelect);
            close(rsInsert);
            close(blobData);
        }

        return value;
    }

    public long getMilliSecond(Date dt) {
        if (dt == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.getTimeInMillis();
    }

}
