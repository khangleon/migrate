package vn.com.skyone.metro2sky;

public class ImagingSub {

    public static final Integer OFF = 0;
    public static Integer ON = 1;
    public static String EMPTY = "";

    public ImagingSub() {
        super();
    }

    private Long imagingResultId;
    private Long indicationId;
    private Long indicationItemId;
    private Long itemId;
    private String itemCode;
    private String itemName;
    private Long unitId;
    private String unitName;
    private Double qty;
    private Double price;
    private Double amount;
    private Long imagingLabelId;
    private String label;
    private String note;
    private String labelForeign;
    private String notesForeign;
    private Integer tagged = OFF;
    private Integer subResult = OFF;
    private Integer abnormal = OFF;
    private Long filesystemId;

    public Long getImagingResultId() {
        return imagingResultId;
    }

    public void setImagingResultId(Long imagingResultId) {
        this.imagingResultId = imagingResultId;
    }

    public Long getIndicationId() {
        return indicationId;
    }

    public void setIndicationId(Long indicationId) {
        this.indicationId = indicationId;
    }

    public Long getIndicationItemId() {
        return indicationItemId;
    }

    public void setIndicationItemId(Long indicationItemId) {
        this.indicationItemId = indicationItemId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getImagingLabelId() {
        return imagingLabelId;
    }

    public void setImagingLabelId(Long imagingLabelId) {
        this.imagingLabelId = imagingLabelId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLabelForeign() {
        return labelForeign;
    }

    public void setLabelForeign(String labelForeign) {
        this.labelForeign = labelForeign;
    }

    public String getNotesForeign() {
        return notesForeign;
    }

    public void setNotesForeign(String notesForeign) {
        this.notesForeign = notesForeign;
    }

    public Integer getTagged() {
        return tagged;
    }

    public void setTagged(Integer tagged) {
        this.tagged = tagged;
    }

    public Integer getSubResult() {
        return subResult;
    }

    public void setSubResult(Integer subResult) {
        this.subResult = subResult;
    }

    public boolean isAbnormal() {
        return ON.equals(this.abnormal);
    }

    public void setAbnormal(Integer abnormal) {
        this.abnormal = abnormal;
    }

    public Long getFilesystemId() {
        return filesystemId;
    }

    public void setFilesystemId(Long filesystemId) {
        this.filesystemId = filesystemId;
    }

    public boolean isSubResult() {
        return ON.equals(this.subResult);
    }

    public void setSubResult(boolean subResult) {
        this.subResult = subResult ? ON : OFF;
    }

}
