package vn.com.skyone.metro2sky;

public class Const {

    // Configuration system
    public static enum TYPE_NAME {
        FIRST_NAME, MIDDLE_NAME, LAST_NAME, ACCOUNT, CODE
    }

    // Configuration Metromed
    public static final String METRO_HOST = "localhost";
    public static final String METRO_PORT = "5432";
    public static final String METRO_DB = "Metromed";
    public static final String METRO_USER = "sa";
    public static final String METRO_PASS = "123456";

    // Configuration Skyone
    public static final String SKY_HOST = "localhost";
    public static final String SKY_PORT = "5432";
    public static final String SKY_DB = "skyone";
    public static final String SKY_USER = "skyone";
    public static final String SKY_PASS = "sky@#skyone";

    // Config common
    public static final Integer NODE_ID = 1021;
    public static final Long COMPANY_ID = 21L;
    public static final Long BRANCH_ID = 100021L;
    public static final Long CREATED_ID = 0L;
    public static final Long INDICATION_DEPARTMENT = 50100021L;
    public static final Long ENDO_DEPARTMENT = 50100021L;

    // Config limit and sort
    public static final String PATIENT_SORT = " order by date_created asc ";
    public static final Integer PATIENT_LIMIT = 0;

    public static final String ENDOSCOPIC_SORT = " order by date_exam desc ";
    public static final Integer ENDOSCOPIC_LIMIT = 10;

    public static final String IMAGING_RESULT_SORT = " order by result_date desc ";
    public static final Integer IMAGING_RESULT_LIMIT = 10;

    // Config group
    public static final Long IMAGE_GROUP = 301L;
    public static final Long ENDO_GROUP = 311L;
    public static final String MODALITY = "ES";

    // Config category
    public static final Long ENDO_CATEGORY = 100311L;

    // Config screen
    public static final String ENDO_SCREEN = "img211";

    // Config Unit
    public static final Long UNIT_ID = 1L;

    public static final String DIR_PATH = "/data/skyone/pk91phc/imageresults";
    public static final Long DIR_PATH_ID = 110212018L;
    public static final String FS_GROUP_ID = "ONLINE_STORAGE";
    public static final String CATEGORY = "IMAGING_RESULTS";
    public static final Long MIN_SPACE_FREE = 100000000L;

    // Config procedure
    public static final String CLO_TEST_CODE = "NS21";
    public static final String BIOPSY_CODE = "NS17";
    public static final String HP_CODE = "NS18";

    // Config report
    public static final String METRO_REPORT_TEMPLATE = " AND LOWER(title) like '%' and title != '' ";
    public static final Long TEMPLATE_ID = 110212019L;

    // 0 = Noi soi
    // 1 = Dãn TMTQ
    // 2 = Xet nghiem
    // 4 = Sieu am
    // 5 = BN tăng lipid máu

    public static final String FILE_TYPE_PDF = "pdf";
    public static final String EXTENSION_PDF = ".pdf";
    public static final String CONTENT_TYPE_PDF = "application/pdf";
    public static final String DOT = ".";
    public static final String FILE_TYPE_DOCX = "docx";
    public static final String FILE_TYPE_XLSX = "xlsx";

    public static final String DIR_SOURCE_FILE_DATA_1 = "/data/pk91phc/hoso/ProfileData/NoiSoi";
    public static final String DIR_SOURCE_FILE_DATA_2 = "/data/pk91phc/hoso/Temporary/NoiSoi";


}
