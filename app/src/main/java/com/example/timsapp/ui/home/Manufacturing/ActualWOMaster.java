package com.example.timsapp.ui.home.Manufacturing;

import android.view.View;

public class ActualWOMaster {

    String Id,Type,Name,Date,Name_View,QCCode,
            QCName,RollCode,RollName;

    int Defective,ActualQty;

    private View.OnClickListener requestBtnClickListener;

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

    public ActualWOMaster(String id, String type, String name, String date, String name_View, String QCCode, String QCName, String rollCode, String rollName, int defective, int actualQty) {
        Id = id;
        Type = type;
        Name = name;
        Date = date;
        Name_View = name_View;
        this.QCCode = QCCode;
        this.QCName = QCName;
        RollCode = rollCode;
        RollName = rollName;
        Defective = defective;
        ActualQty = actualQty;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName_View() {
        return Name_View;
    }

    public void setName_View(String name_View) {
        Name_View = name_View;
    }

    public String getQCCode() {
        return QCCode;
    }

    public void setQCCode(String QCCode) {
        this.QCCode = QCCode;
    }

    public String getQCName() {
        return QCName;
    }

    public void setQCName(String QCName) {
        this.QCName = QCName;
    }

    public String getRollCode() {
        return RollCode;
    }

    public void setRollCode(String rollCode) {
        RollCode = rollCode;
    }

    public String getRollName() {
        return RollName;
    }

    public void setRollName(String rollName) {
        RollName = rollName;
    }

    public int getDefective() {
        return Defective;
    }

    public void setDefective(int defective) {
        Defective = defective;
    }

    public int getActualQty() {
        return ActualQty;
    }

    public void setActualQty(int actualQty) {
        ActualQty = actualQty;
    }
}
