package com.example.timsapp.ui.home.Mapping;

public class MappingDetailMaster {

    String no,wmmid,mt_lot,mt_cd,use_yn,reg_dt,mt_no,bb_no,Remain;
    int gr_qty,Used;

    public MappingDetailMaster(String no, String wmmid, String mt_lot, String mt_cd, String use_yn, String reg_dt, String mt_no, String bb_no, String remain, int gr_qty, int used) {
        this.no = no;
        this.wmmid = wmmid;
        this.mt_lot = mt_lot;
        this.mt_cd = mt_cd;
        this.use_yn = use_yn;
        this.reg_dt = reg_dt;
        this.mt_no = mt_no;
        this.bb_no = bb_no;
        Remain = remain;
        this.gr_qty = gr_qty;
        Used = used;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getWmmid() {
        return wmmid;
    }

    public void setWmmid(String wmmid) {
        this.wmmid = wmmid;
    }

    public String getMt_lot() {
        return mt_lot;
    }

    public void setMt_lot(String mt_lot) {
        this.mt_lot = mt_lot;
    }

    public String getMt_cd() {
        return mt_cd;
    }

    public void setMt_cd(String mt_cd) {
        this.mt_cd = mt_cd;
    }

    public String getUse_yn() {
        return use_yn;
    }

    public void setUse_yn(String use_yn) {
        this.use_yn = use_yn;
    }

    public String getReg_dt() {
        return reg_dt;
    }

    public void setReg_dt(String reg_dt) {
        this.reg_dt = reg_dt;
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

    public String getRemain() {
        return Remain;
    }

    public void setRemain(String remain) {
        Remain = remain;
    }

    public int getGr_qty() {
        return gr_qty;
    }

    public void setGr_qty(int gr_qty) {
        this.gr_qty = gr_qty;
    }

    public int getUsed() {
        return Used;
    }

    public void setUsed(int used) {
        Used = used;
    }
}
