package com.danzoye.lib.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ramdhany Dwi Nugroho on May 2015.
 */
public abstract class DCommonBaseAdapter extends DBaseAdapter {
    protected static final int VIEW_TYPE_PLACE = 1;
    protected static final int VIEW_TYPE_LOADING = 0;
    protected static final int VIEW_TYPE_ERROR = 2;
    protected static final int[] VIEW_TYPES = new int[]{VIEW_TYPE_LOADING, VIEW_TYPE_PLACE, VIEW_TYPE_ERROR};
    private boolean isLoading;
    private boolean isError;
    private CharSequence errorMessage;

    public DCommonBaseAdapter(Activity context) {
        super(context);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoading && position == getCount() - 1) {
            return VIEW_TYPE_LOADING;
        } else if (isError && position == getCount() - 1) {
            return VIEW_TYPE_ERROR;
        }
        return VIEW_TYPE_PLACE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_LOADING) {
            return getLoadingView(convertView);
        } else if (viewType == VIEW_TYPE_ERROR) {
            return getErrorView(convertView, errorMessage);
        } else {
            return getItemView(position, convertView);
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPES.length;
    }

    public boolean isError() {
        return isError;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setError(boolean isError, CharSequence errorMessage) {
        this.isError = isError;
        this.errorMessage = errorMessage;
    }

    protected abstract View getErrorView(View convertView, CharSequence errorMessage);

    protected abstract View getItemView(int position, View convertView);

    protected abstract View getLoadingView(View convertView);
}
