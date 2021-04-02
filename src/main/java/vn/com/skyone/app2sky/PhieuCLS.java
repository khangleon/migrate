package vn.com.skyone.app2sky;

import java.sql.Timestamp;

public class PhieuCLS {

    public PhieuCLS() {
        super();
    }

    private String MaBN;
    private String HoTen;
    private String Namsinh;
    private String Gioitinh;
    private String Diachi;

    private String SoBHYT;
    private String NoiChidinh;
    private String Chandoan;

    private String TenDV;
    private String BsChidinh;
    private String BsDocKQ;
    private String Ketqua;
    private String Ketluan;

    private Timestamp TGThuchien;
    private Timestamp TGCapnhat;
    private String Trangthai;
    private String MaDV;
    private Number IDBaocao;

    private String MaPCD;
    private String MaPhieuCLS;
    private String ID;
    private String MaBSChidinh;
    private String MaBSThuchien;
    private String MaNoiChidinh;
    private String KeyPacs;

    public PhieuCLS(String maBN, String hoTen, String namsinh, String gioitinh, String diachi, String soBHYT,
            String noiChidinh, String chandoan, String tenDV, String bsChidinh, String bsDocKQ, String ketqua,
            String ketluan, Timestamp tGThuchien, Timestamp tGCapnhat, String trangthai, String maDV, Number iDBaocao,
            String maPCD, String maPhieuCLS, String iD, String maBSChidinh, String maBSThuchien, String maNoiChidinh,
            String keyPacs) {
        super();
        MaBN = maBN;
        HoTen = hoTen;
        Namsinh = namsinh;
        Gioitinh = gioitinh;
        Diachi = diachi;
        SoBHYT = soBHYT;
        NoiChidinh = noiChidinh;
        Chandoan = chandoan;
        TenDV = tenDV;
        BsChidinh = bsChidinh;
        BsDocKQ = bsDocKQ;
        Ketqua = ketqua;
        Ketluan = ketluan;
        TGThuchien = tGThuchien;
        TGCapnhat = tGCapnhat;
        Trangthai = trangthai;
        MaDV = maDV;
        IDBaocao = iDBaocao;
        MaPCD = maPCD;
        MaPhieuCLS = maPhieuCLS;
        ID = iD;
        MaBSChidinh = maBSChidinh;
        MaBSThuchien = maBSThuchien;
        MaNoiChidinh = maNoiChidinh;
        KeyPacs = keyPacs;
    }

    public String getMaBN() {
        return MaBN;
    }

    public void setMaBN(String maBN) {
        MaBN = maBN;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getNamsinh() {
        return Namsinh;
    }

    public void setNamsinh(String namsinh) {
        Namsinh = namsinh;
    }

    public String getGioitinh() {
        return Gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        Gioitinh = gioitinh;
    }

    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String diachi) {
        Diachi = diachi;
    }

    public String getSoBHYT() {
        return SoBHYT;
    }

    public void setSoBHYT(String soBHYT) {
        SoBHYT = soBHYT;
    }

    public String getNoiChidinh() {
        return NoiChidinh;
    }

    public void setNoiChidinh(String noiChidinh) {
        NoiChidinh = noiChidinh;
    }

    public String getChandoan() {
        return Chandoan;
    }

    public void setChandoan(String chandoan) {
        Chandoan = chandoan;
    }

    public String getTenDV() {
        return TenDV;
    }

    public void setTenDV(String tenDV) {
        TenDV = tenDV;
    }

    public String getBsChidinh() {
        return BsChidinh;
    }

    public void setBsChidinh(String bsChidinh) {
        BsChidinh = bsChidinh;
    }

    public String getBsDocKQ() {
        return BsDocKQ;
    }

    public void setBsDocKQ(String bsDocKQ) {
        BsDocKQ = bsDocKQ;
    }

    public String getKetqua() {
        return Ketqua;
    }

    public void setKetqua(String ketqua) {
        Ketqua = ketqua;
    }

    public String getKetluan() {
        return Ketluan;
    }

    public void setKetluan(String ketluan) {
        Ketluan = ketluan;
    }

    public Timestamp getTGThuchien() {
        return TGThuchien;
    }

    public void setTGThuchien(Timestamp tGThuchien) {
        TGThuchien = tGThuchien;
    }

    public Timestamp getTGCapnhat() {
        return TGCapnhat;
    }

    public void setTGCapnhat(Timestamp tGCapnhat) {
        TGCapnhat = tGCapnhat;
    }

    public String getTrangthai() {
        return Trangthai;
    }

    public void setTrangthai(String trangthai) {
        Trangthai = trangthai;
    }

    public String getMaDV() {
        return MaDV;
    }

    public void setMaDV(String maDV) {
        MaDV = maDV;
    }

    public Number getIDBaocao() {
        return IDBaocao;
    }

    public void setIDBaocao(Number iDBaocao) {
        IDBaocao = iDBaocao;
    }

    public String getMaPCD() {
        return MaPCD;
    }

    public void setMaPCD(String maPCD) {
        MaPCD = maPCD;
    }

    public String getMaPhieuCLS() {
        return MaPhieuCLS;
    }

    public void setMaPhieuCLS(String maPhieuCLS) {
        MaPhieuCLS = maPhieuCLS;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getMaBSChidinh() {
        return MaBSChidinh;
    }

    public void setMaBSChidinh(String maBSChidinh) {
        MaBSChidinh = maBSChidinh;
    }

    public String getMaBSThuchien() {
        return MaBSThuchien;
    }

    public void setMaBSThuchien(String maBSThuchien) {
        MaBSThuchien = maBSThuchien;
    }

    public String getMaNoiChidinh() {
        return MaNoiChidinh;
    }

    public void setMaNoiChidinh(String maNoiChidinh) {
        MaNoiChidinh = maNoiChidinh;
    }

    public String getKeyPacs() {
        return KeyPacs;
    }

    public void setKeyPacs(String keyPacs) {
        KeyPacs = keyPacs;
    }

}
