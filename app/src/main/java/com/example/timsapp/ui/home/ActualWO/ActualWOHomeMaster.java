package com.example.timsapp.ui.home.ActualWO;

public class ActualWOHomeMaster {
    String id_actualpr,at_no,type,product,remark,style_nm,reg_dt;
    int target;

    public ActualWOHomeMaster(String id_actualpr, String at_no, String type, String product, String remark, String style_nm, int target, String reg_dt) {
        this.id_actualpr = id_actualpr;
        this.at_no = at_no;
        this.type = type;
        this.product = product;
        this.remark = remark;
        this.style_nm = style_nm;
        this.target = target;
        this.reg_dt = reg_dt;
    }

    public String getReg_dt() {
        return reg_dt;
    }

    public void setReg_dt(String reg_dt) {
        this.reg_dt = reg_dt;
    }

    public String getId_actualpr() {
        return id_actualpr;
    }

    public void setId_actualpr(String id_actualpr) {
        this.id_actualpr = id_actualpr;
    }

    public String getAt_no() {
        return at_no;
    }

    public void setAt_no(String at_no) {
        this.at_no = at_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStyle_nm() {
        return style_nm;
    }

    public void setStyle_nm(String style_nm) {
        this.style_nm = style_nm;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
