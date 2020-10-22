package com.example.timsapp.ui.home.Mapping.QCcheck;

public class QCCheckDetailChildMaster {
    String fqhno,check_subject,check_value,check_qty;

    public QCCheckDetailChildMaster(String fqhno, String check_subject, String check_value, String check_qty) {
        this.fqhno = fqhno;
        this.check_subject = check_subject;
        this.check_value = check_value;
        this.check_qty = check_qty;
    }

    public String getFqhno() {
        return fqhno;
    }

    public void setFqhno(String fqhno) {
        this.fqhno = fqhno;
    }

    public String getCheck_subject() {
        return check_subject;
    }

    public void setCheck_subject(String check_subject) {
        this.check_subject = check_subject;
    }

    public String getCheck_value() {
        return check_value;
    }

    public void setCheck_value(String check_value) {
        this.check_value = check_value;
    }

    public String getCheck_qty() {
        return check_qty;
    }

    public void setCheck_qty(String check_qty) {
        this.check_qty = check_qty;
    }
}
