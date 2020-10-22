package com.example.timsapp.ui.home.Mapping.QCcheck;

public class QcCheckerLessItem {
    private boolean check;
    private String sub;
    private String qty;
    private String icdno;
    private String stt;
    private String textSub;
    private String icno;
    private String check_id;

    public QcCheckerLessItem(boolean check, String sub, String qty, String icdno, String stt, String textSub, String icno, String check_id) {
        this.check = check;
        this.sub = sub;
        this.qty = qty;
        this.icdno = icdno;
        this.stt = stt;
        this.textSub = textSub;
        this.icno = icno;
        this.check_id = check_id;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getIcdno() {
        return icdno;
    }

    public void setIcdno(String icdno) {
        this.icdno = icdno;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getTextSub() {
        return textSub;
    }

    public void setTextSub(String textSub) {
        this.textSub = textSub;
    }

    public String getIcno() {
        return icno;
    }

    public void setIcno(String icno) {
        this.icno = icno;
    }

    public String getCheck_id() {
        return check_id;
    }

    public void setCheck_id(String check_id) {
        this.check_id = check_id;
    }
}
