package com.smartory.utils;

import com.smartory.models.CheckListModel;
import com.smartory.models.LevObsModel;

import java.util.Comparator;

public class ComparadorChecks implements Comparator<CheckListModel> {
    public int compare(CheckListModel a, CheckListModel b) {
        /*if (a.getNumber().equals("Sin Enviar") || b.getNumber().equals("Sin Enviar")){
            return -1;
        } else {
            return (-1) * a.getNumber().compareTo(b.getNumber());
        }*/
        return (-1) * a.getNumber().compareTo(b.getNumber());
    }
}