package com.example.timsapp.ui.home.MappingOQC;

public class MappingOQCMaster {

    String wmtid,bb_no,mt_no,mt_cd;
    int gr_qty,count_ng;

    public MappingOQCMaster(String wmtid, String bb_no, String mt_no, String mt_cd, int gr_qty, int count_ng) {
        this.wmtid = wmtid;
        this.bb_no = bb_no;
        this.mt_no = mt_no;
        this.mt_cd = mt_cd;
        this.gr_qty = gr_qty;
        this.count_ng = count_ng;
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

    public int getGr_qty() {
        return gr_qty;
    }

    public void setGr_qty(int gr_qty) {
        this.gr_qty = gr_qty;
    }

    public int getCount_ng() {
        return count_ng;
    }

    public void setCount_ng(int count_ng) {
        this.count_ng = count_ng;
    }
}
