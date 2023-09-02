package com.smartory.utils;

import com.smartory.models.LevObsModel;

import java.util.Comparator;

public class ComparadorObs implements Comparator<LevObsModel> {
    public int compare(LevObsModel a, LevObsModel b) {
        return (-1)*a.getCheck_list().compareTo(b.getCheck_list());
    }
}