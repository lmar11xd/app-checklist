package com.smartory.utils;

import com.smartory.models.ItemCheckListModel;

import java.util.Comparator;

public class ComparadorItemsChecklist implements Comparator<ItemCheckListModel> {
    public int compare(ItemCheckListModel a, ItemCheckListModel b) {
        return a.getName().compareTo(b.getName());
    }
}