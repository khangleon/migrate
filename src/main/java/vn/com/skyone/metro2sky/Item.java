package vn.com.skyone.metro2sky;

public class Item {

    public Item() {
        super();
    }

    public Item(Long id, String code, String name) {
        this.id = id != null ? id : 0L;
        this.code = code != null ? code : "";
        this.name = name != null ? name : "";
    }

    public Item(Long id, String code, String name, String reportName, String reportEnName) {
        this.id = id != null ? id : 0L;
        this.code = code != null ? code : "";
        this.name = name != null ? name : "";
        this.reportName = reportName != null ? reportName : "";
        this.reportEnName = reportEnName != null ? reportEnName : "";
    }

    private Long id;
    private String code;
    private String name;
    private String reportName;
    private String reportEnName;
    private Long unitId;
    private Double price = 0D;
    private String screen;
    private Long categoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportEnName() {
        return reportEnName;
    }

    public void setReportEnName(String reportEnName) {
        this.reportEnName = reportEnName;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

}
