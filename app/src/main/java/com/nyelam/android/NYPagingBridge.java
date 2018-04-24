package com.nyelam.android;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by dantech on 10/8/16.
 */
public interface NYPagingBridge<T> {

    void addItemIntoTop(T item);
    void addItemIntoBottom(T item);
    void addListIntoTop(List<T> t);
    void addListIntoBottom(List<T> t);
    List<T> removeSameDatas(List<T> items);
    boolean isDataSame(T item);

}
