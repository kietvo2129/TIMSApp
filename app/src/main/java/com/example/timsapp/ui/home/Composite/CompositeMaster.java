package com.example.timsapp.ui.home.Composite;

public class CompositeMaster {

    String no,psid,staff_id,staff_tp,staff_tp_nm,uname,start_dt,end_dt,use_yn;

    public CompositeMaster(String no, String psid, String staff_id, String staff_tp, String staff_tp_nm, String uname, String start_dt, String end_dt, String use_yn) {
        this.no = no;
        this.psid = psid;
        this.staff_id = staff_id;
        this.staff_tp = staff_tp;
        this.staff_tp_nm = staff_tp_nm;
        this.uname = uname;
        this.start_dt = start_dt;
        this.end_dt = end_dt;
        this.use_yn = use_yn;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPsid() {
        return psid;
    }

    public void setPsid(String psid) {
        this.psid = psid;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_tp() {
        return staff_tp;
    }

    public void setStaff_tp(String staff_tp) {
        this.staff_tp = staff_tp;
    }

    public String getStaff_tp_nm() {
        return staff_tp_nm;
    }

    public void setStaff_tp_nm(String staff_tp_nm) {
        this.staff_tp_nm = staff_tp_nm;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(String start_dt) {
        this.start_dt = start_dt;
    }

    public String getEnd_dt() {
        return end_dt;
    }

    public void setEnd_dt(String end_dt) {
        this.end_dt = end_dt;
    }

    public String getUse_yn() {
        return use_yn;
    }

    public void setUse_yn(String use_yn) {
        this.use_yn = use_yn;
    }
}
