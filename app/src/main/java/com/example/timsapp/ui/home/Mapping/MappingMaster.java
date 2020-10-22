package com.example.timsapp.ui.home.Mapping;

public class MappingMaster {
    String no,wmtid,date,mt_cd,mt_no,bbmp_sts_cd,mt_qrcode,lct_cd,bb_no,mt_barcode;
    int gr_qty,gr_qty1,cnt,sl_tru_ng;

    public MappingMaster(String no, String wmtid, String date, String mt_cd, String mt_no, String bbmp_sts_cd, String mt_qrcode, String lct_cd, String bb_no, String mt_barcode, int gr_qty, int gr_qty1,int cnt,int sl_tru_ng) {
        this.no = no;
        this.wmtid = wmtid;
        this.date = date;
        this.mt_cd = mt_cd;
        this.mt_no = mt_no;
        this.bbmp_sts_cd = bbmp_sts_cd;
        this.mt_qrcode = mt_qrcode;
        this.lct_cd = lct_cd;
        this.bb_no = bb_no;
        this.mt_barcode = mt_barcode;
        this.gr_qty = gr_qty;
        this.gr_qty1 = gr_qty1;
        this.cnt = cnt;
        this.sl_tru_ng = sl_tru_ng;
    }

    public int getSl_tru_ng() {
        return sl_tru_ng;
    }

    public void setSl_tru_ng(int sl_tru_ng) {
        this.sl_tru_ng = sl_tru_ng;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getWmtid() {
        return wmtid;
    }

    public void setWmtid(String wmtid) {
        this.wmtid = wmtid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMt_cd() {
        return mt_cd;
    }

    public void setMt_cd(String mt_cd) {
        this.mt_cd = mt_cd;
    }

    public String getMt_no() {
        return mt_no;
    }

    public void setMt_no(String mt_no) {
        this.mt_no = mt_no;
    }

    public String getBbmp_sts_cd() {
        return bbmp_sts_cd;
    }

    public void setBbmp_sts_cd(String bbmp_sts_cd) {
        this.bbmp_sts_cd = bbmp_sts_cd;
    }

    public String getMt_qrcode() {
        return mt_qrcode;
    }

    public void setMt_qrcode(String mt_qrcode) {
        this.mt_qrcode = mt_qrcode;
    }

    public String getLct_cd() {
        return lct_cd;
    }

    public void setLct_cd(String lct_cd) {
        this.lct_cd = lct_cd;
    }

    public String getBb_no() {
        return bb_no;
    }

    public void setBb_no(String bb_no) {
        this.bb_no = bb_no;
    }

    public String getMt_barcode() {
        return mt_barcode;
    }

    public void setMt_barcode(String mt_barcode) {
        this.mt_barcode = mt_barcode;
    }

    public int getGr_qty() {
        return gr_qty;
    }

    public void setGr_qty(int gr_qty) {
        this.gr_qty = gr_qty;
    }

    public int getGr_qty1() {
        return gr_qty1;
    }

    public void setGr_qty1(int gr_qty1) {
        this.gr_qty1 = gr_qty1;
    }
}
