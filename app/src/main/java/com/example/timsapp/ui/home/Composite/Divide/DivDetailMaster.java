package com.example.timsapp.ui.home.Composite.Divide;

public class DivDetailMaster {

    String no,mt_cd,mt_no,bb_no,wmtid;
    int sl_tru_ng;

    public DivDetailMaster(String no,String mt_cd, String mt_no, String bb_no, String wmtid,int sl_tru_ng) {
        this.no = no;
        this.mt_cd = mt_cd;
        this.mt_no = mt_no;
        this.bb_no = bb_no;
        this.wmtid = wmtid;

        this.sl_tru_ng = sl_tru_ng;
    }

    public String getWmtid() {
        return wmtid;
    }

    public void setWmtid(String wmtid) {
        this.wmtid = wmtid;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getBb_no() {
        return bb_no;
    }

    public void setBb_no(String bb_no) {
        this.bb_no = bb_no;
    }

    public int getSl_tru_ng() {
        return sl_tru_ng;
    }

    public void setSl_tru_ng(int sl_tru_ng) {
        this.sl_tru_ng = sl_tru_ng;
    }
}
