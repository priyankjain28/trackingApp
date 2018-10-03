package com.hora.priyank.trackingapp.util;

import com.hora.priyank.trackingapp.data.model.Event;

import java.util.Comparator;

public class DateComparator implements Comparator<Event> {
    public int compare(Event item1, Event item2) {
        Long l = Long.valueOf(item2.getCreateDate())-Long.valueOf(item1.getCreateDate());
        return l.intValue();
    }
}