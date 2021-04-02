package vn.com.skyone.metro2sky;

import java.io.InputStream;

public class ImagingDocument {

    public static final Integer ON = 1;
    public static final Integer OFF = 0;
    public static final Integer LEFT = -1;
    public static final Integer RIGHT = 1;

    public ImagingDocument() {
        super();
    }

    private Long imagingResultId;

    private Long filesystemId;

    private String dirpath;

    private String filepath;

    private String filename;

    private String filetype;

    private String filemime;

    private String fileMd5;

    private Integer fileSize;

    private Integer keyImage = OFF;

    private Integer keyIndex = 1;

    private Integer posIndex;

    private Integer side = 0;

    private Integer video = OFF;

    private String note;

    private Long pacsId;

    private String studyId;

    private String studyDesc;

    private String seriesId;

    private String seriesDesc;

    private String instanceId;

    private Integer wl = 0;

    private Integer ww = 0;

    private String remarkImage;

    private Integer published = ON;

    private Integer sort = 0;

    private Integer disabled = OFF;

    private byte[] filedata;

    private InputStream fileStream;

    public Long getImagingResultId() {
        return imagingResultId;
    }

    public Long getFilesystemId() {
        return filesystemId;
    }

    public String getDirpath() {
        return dirpath;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getFilename() {
        return filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public String getFilemime() {
        return filemime;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public Integer getKeyImage() {
        return keyImage;
    }

    public Integer getKeyIndex() {
        return keyIndex;
    }

    public void setKeyImage(Integer keyImage) {
        this.keyImage = keyImage;
    }

    public void setKeyIndex(Integer keyIndex) {
        this.keyIndex = keyIndex;
    }

    public Integer getPosIndex() {
        return posIndex;
    }

    public Integer getSide() {
        return side;
    }

    public Integer getVideo() {
        return video;
    }

    public String getNote() {
        return note;
    }

    public Long getPacsId() {
        return pacsId;
    }

    public String getStudyId() {
        return studyId;
    }

    public String getStudyDesc() {
        return studyDesc;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public String getSeriesDesc() {
        return seriesDesc;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public Integer getWl() {
        return wl;
    }

    public Integer getWw() {
        return ww;
    }

    public String getRemarkImage() {
        return remarkImage;
    }

    public Integer getPublished() {
        return published;
    }

    public Integer getSort() {
        return sort;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public byte[] getFiledata() {
        return filedata;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setImagingResultId(Long imagingResultId) {
        this.imagingResultId = imagingResultId;
    }

    public void setFilesystemId(Long filesystemId) {
        this.filesystemId = filesystemId;
    }

    public void setDirpath(String dirpath) {
        this.dirpath = dirpath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public void setFilemime(String filemime) {
        this.filemime = filemime;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isKeyImage() {
        return ON.equals(this.keyImage);
    }

    public void setKeyImage(boolean keyImage) {
        this.keyImage = keyImage ? ON : OFF;
    }

    public void setPosIndex(Integer posIndex) {
        this.posIndex = posIndex;
    }

    public void setSide(Integer side) {
        this.side = side;
    }

    public void setVideo(Integer video) {
        this.video = video;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPacsId(Long pacsId) {
        this.pacsId = pacsId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public void setStudyDesc(String studyDesc) {
        this.studyDesc = studyDesc;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public void setSeriesDesc(String seriesDesc) {
        this.seriesDesc = seriesDesc;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setWl(Integer wl) {
        this.wl = wl;
    }

    public void setWw(Integer ww) {
        this.ww = ww;
    }

    public void setRemarkImage(String remarkImage) {
        this.remarkImage = remarkImage;
    }

    public void setPublished(Integer published) {
        this.published = published;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public void setFiledata(byte[] filedata) {
        this.filedata = filedata;
    }

    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    public String getFileNameFull() {
        String fileNameFull = dirpath;
        fileNameFull += "/" + this.filepath + "/" + this.filename;
        return fileNameFull;
    }

}
