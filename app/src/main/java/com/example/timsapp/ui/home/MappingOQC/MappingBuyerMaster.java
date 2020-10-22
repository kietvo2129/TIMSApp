package com.example.timsapp.ui.home.MappingOQC;

public class MappingBuyerMaster {

    String wmtid,bb_no,mt_no,mt_cd,buyer_qr;
    int gr_qty;

    public MappingBuyerMaster(String wmtid, String bb_no, String mt_no, String mt_cd, String buyer_qr, int gr_qty) {
        this.wmtid = wmtid;
        this.bb_no = bb_no;
        this.mt_no = mt_no;
        this.mt_cd = mt_cd;
        this.buyer_qr = buyer_qr;
        this.gr_qty = gr_qty;
    }

    public String getWmtid() {
        return wmtid;
    }

    public void setWmtid(String wmtid) {
        this.wmtid = wmtid;
    }

    public String getBb_no() {
        return bb_no;
    }

    public void setBb_no(String bb_no) {
        this.bb_no = bb_no;
    }

    public String getMt_no() {
        return mt_no;
    }

    public void setMt_no(String mt_no) {
        this.mt_no = mt_no;
    }

    public String getMt_cd() {
        return mt_cd;
    }

    public void setMt_cd(String mt_cd) {
        this.mt_cd = mt_cd;
    }

    public String getBuyer_qr() {
        return buyer_qr;
    }

    public void setBuyer_qr(String buyer_qr) {
        this.buyer_qr = buyer_qr;
    }

    public int getGr_qty() {
        return gr_qty;
    }

    public void setGr_qty(int gr_qty) {
        this.gr_qty = gr_qty;
    }
}
