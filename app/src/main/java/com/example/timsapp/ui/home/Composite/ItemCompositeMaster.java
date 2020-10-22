package com.example.timsapp.ui.home.Composite;

public class ItemCompositeMaster {
    String no,type,su_dung,RowNum,mdno,md_no,md_nm,purpose,barcode,re_mark,use_yn,del_yn;

    public ItemCompositeMaster(String no,String type,String su_dung, String rowNum, String mdno, String md_no, String md_nm, String purpose, String barcode, String re_mark, String use_yn, String del_yn) {
        this.no = no;
        this.type = type;
        this.su_dung = su_dung;
        RowNum = rowNum;
        this.mdno = mdno;
        this.md_no = md_no;
        this.md_nm = md_nm;
        this.purpose = purpose;
        this.barcode = barcode;
        this.re_mark = re_mark;
        this.use_yn = use_yn;
        this.del_yn = del_yn;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSu_dung() {
        return su_dung;
    }

    public void setSu_dung(String su_dung) {
        this.su_dung = su_dung;
    }

    public String getRowNum() {
        return RowNum;
    }

    public void setRowNum(String rowNum) {
        RowNum = rowNum;
    }

    public String getMdno() {
        return mdno;
    }

    public void setMdno(String mdno) {
        this.mdno = mdno;
    }

    public String getMd_no() {
        return md_no;
    }

    public void setMd_no(String md_no) {
        this.md_no = md_no;
    }

    public String getMd_nm() {
        return md_nm;
    }

    public void setMd_nm(String md_nm) {
        this.md_nm = md_nm;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getRe_mark() {
        return re_mark;
    }

    public void setRe_mark(String re_mark) {
        this.re_mark = re_mark;
    }

    public String getUse_yn() {
        return use_yn;
    }

    public void setUse_yn(String use_yn) {
        this.use_yn = use_yn;
    }

    public String getDel_yn() {
        return del_yn;
    }

    public void setDel_yn(String del_yn) {
        this.del_yn = del_yn;
    }
}
