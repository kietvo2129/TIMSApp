package com.example.timsapp.ui.home.Mapping.QCcheck;

public class QCCheckdetailMaster {
    String fqno,fq_no,work_dt,check_qty,ok_qty,defect_qty;

    public QCCheckdetailMaster(String fqno, String fq_no, String work_dt, String check_qty, String ok_qty, String defect_qty) {
        this.fqno = fqno;
        this.fq_no = fq_no;
        this.work_dt = work_dt;
        this.check_qty = check_qty;
        this.ok_qty = ok_qty;
        this.defect_qty = defect_qty;
    }

    public String getFqno() {
        return fqno;
    }

    public void setFqno(String fqno) {
        this.fqno = fqno;
    }

    public String getFq_no() {
        return fq_no;
    }

    public void setFq_no(String fq_no) {
        this.fq_no = fq_no;
    }

    public String getWork_dt() {
        return work_dt;
    }

    public void setWork_dt(String work_dt) {
        this.work_dt = work_dt;
    }

    public String getCheck_qty() {
        return check_qty;
    }

    public void setCheck_qty(String check_qty) {
        this.check_qty = check_qty;
    }

    public String getOk_qty() {
        return ok_qty;
    }

    public void setOk_qty(String ok_qty) {
        this.ok_qty = ok_qty;
    }

    public String getDefect_qty() {
        return defect_qty;
    }

    public void setDefect_qty(String defect_qty) {
        this.defect_qty = defect_qty;
    }
}
