package com.example.timsapp.ui.home.Composite;

public class ItemStaffMaster {
    String no,position_cd,userid,RowNum,uname,nick_name,mc_no;

    public ItemStaffMaster(String no,String position_cd, String userid, String rowNum, String uname, String nick_name, String mc_no) {
        this.no = no;
        this.position_cd = position_cd;
        this.userid = userid;
        RowNum = rowNum;
        this.uname = uname;
        this.nick_name = nick_name;
        this.mc_no = mc_no;
    }

    public String getPosition_cd() {
        return position_cd;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setPosition_cd(String position_cd) {
        this.position_cd = position_cd;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRowNum() {
        return RowNum;
    }

    public void setRowNum(String rowNum) {
        RowNum = rowNum;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getMc_no() {
        return mc_no;
    }

    public void setMc_no(String mc_no) {
        this.mc_no = mc_no;
    }
}
