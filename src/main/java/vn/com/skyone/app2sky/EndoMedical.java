package vn.com.skyone.app2sky;

import java.util.Date;

public class EndoMedical {

    public EndoMedical() {
        super();
    }

    private String endoscopicmedicalrecordId;
    private Integer cloTestResult = 0;
    private Date cloTestRemindTime;
    private String biosyResult;
    private Date biopsyRemindTime;
    private String hpResult;
    private Date hpRemindTime;

    public String getEndoscopicmedicalrecordId() {
        return endoscopicmedicalrecordId;
    }

    public void setEndoscopicmedicalrecordId(String endoscopicmedicalrecordId) {
        this.endoscopicmedicalrecordId = endoscopicmedicalrecordId;
    }

    public Integer getCloTestResult() {
        return cloTestResult;
    }

    public void setCloTestResult(Integer cloTestResult) {
        this.cloTestResult = cloTestResult;
    }

    public Date getCloTestRemindTime() {
        return cloTestRemindTime;
    }

    public void setCloTestRemindTime(Date cloTestRemindTime) {
        this.cloTestRemindTime = cloTestRemindTime;
    }

    public String getBiosyResult() {
        return biosyResult;
    }

    public void setBiosyResult(String biosyResult) {
        this.biosyResult = biosyResult;
    }

    public Date getBiopsyRemindTime() {
        return biopsyRemindTime;
    }

    public void setBiopsyRemindTime(Date biopsyRemindTime) {
        this.biopsyRemindTime = biopsyRemindTime;
    }

    public String getHpResult() {
        return hpResult;
    }

    public void setHpResult(String hpResult) {
        this.hpResult = hpResult;
    }

    public Date getHpRemindTime() {
        return hpRemindTime;
    }

    public void setHpRemindTime(Date hpRemindTime) {
        this.hpRemindTime = hpRemindTime;
    }

}
