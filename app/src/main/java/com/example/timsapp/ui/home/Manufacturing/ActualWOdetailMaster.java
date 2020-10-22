package com.example.timsapp.ui.home.Manufacturing;

public class ActualWOdetailMaster {

    String no,Id, MaterialCode;

    public ActualWOdetailMaster(String no, String id, String materialCode) {
        this.no = no;
        Id = id;
        MaterialCode = materialCode;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMaterialCode() {
        return MaterialCode;
    }

    public void setMaterialCode(String materialCode) {
        MaterialCode = materialCode;
    }
}
