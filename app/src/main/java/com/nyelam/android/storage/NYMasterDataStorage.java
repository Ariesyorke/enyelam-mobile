package com.nyelam.android.storage;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.danzoye.lib.util.DToUIThreadRunnable;
import com.nyelam.android.NYApplication;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.CategoryList;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.CountryCodeList;
import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.data.dao.NYCategory;
import com.nyelam.android.data.dao.NYCountryCode;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYInvalidReturnValueException;
import com.nyelam.android.http.NYMasterCategoriesRequest;
import com.nyelam.android.http.NYMasterCountryCodeRequest;
import com.nyelam.android.http.result.NYPaginationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/10/2018.
 */

public class NYMasterDataStorage {

    private Activity context;

    public NYMasterDataStorage(Activity context) {
        this.context = context;
    }

    public Activity getContext() {
        return context;
    }

    public void loadCountries(@NonNull LoadDataListener<CountryCode> listener, boolean clearOnSuccess) {
        loadCountriesInternal(listener, false, clearOnSuccess);
    }

    public void loadCountriesUseCacheIfAvailable(@NonNull LoadDataListener<CountryCode> listener, boolean clearOnSuccess) {
        loadCountriesInternal(listener, true, clearOnSuccess);
    }

    private List<CountryCode> loadCountriesFromCacheInternal(DaoSession session) {
        List<NYCountryCode> rawInterests = session.getNYCountryCodeDao().queryBuilder().list();
        return NYHelper.generateList(rawInterests, CountryCode.class);
    }

    private void loadCountriesInternal(@Nullable LoadDataListener<CountryCode> listener, boolean useCache, boolean clearOnSuccess) {
        DToUIThreadRunnable<Object, Void, Object> runnable = new DToUIThreadRunnable<Object, Void, Object>(
                getContext(),
                useCache,
                listener,
                clearOnSuccess
        ) {
            LoadDataListener<CountryCode> listener;
            android.location.Location location;
            @Override
            protected void onOutputInForeground(Object o) {
                super.onOutputInForeground(o);
                if (listener == null) return;

                if (o instanceof Exception) {
                    listener.onLoadFailed((Exception) o);
                } else if (o instanceof List) {
                    listener.onDataLoaded((List<CountryCode>) o);
                } else {
                    listener.onLoadFailed(new NYInvalidReturnValueException("unknown class type!"));
                }
            }

            @Override
            protected Object runInBackground(Object... objects) {
                boolean useCache = (Boolean) objects[0];
                listener = (LoadDataListener<CountryCode>) objects[1];
                boolean clearOnSuccess = (Boolean) objects[2];
                List<CountryCode> countries = new ArrayList<>();
                DaoSession daoSession = ((NYApplication) getContext().getApplication()).getDaoSession();

                if (useCache) {
                    countries = loadCountriesFromCacheInternal(daoSession);
                    if (countries != null && !countries.isEmpty()) {
                        return countries;
                    }
                }

                String nextPage = "1";

                List<NYCountryCode> gRaws = null;
                while (!TextUtils.isEmpty(nextPage)) {
                    NYLog.d("start fetch master countries for " + " page " + nextPage);
                    try {
                        NYMasterCountryCodeRequest req = new NYMasterCountryCodeRequest(getContext(), nextPage);
                        NYPaginationResult<CountryCodeList> result = req.loadDataFromNetwork();
                        if (result != null && result.item != null && result.item.getList() != null && !result.item.getList().isEmpty()) {
                            NYLog.d("+++ countries size = " + result.item.getList().size());

                            for (CountryCode c : result.item.getList()) {
                                if (gRaws == null) gRaws = new ArrayList<>();

                                NYCountryCode country = new NYCountryCode();
                                c.copyTo(country, daoSession);
                                gRaws.add(country);
                            }
                        } else {
                            NYLog.d("+++ countries size = 0");
                        }

                        NYLog.d("+++ countries size : result = "+result.toString());

                        if (result == null || result.next < 1 || result.item == null || result.item.getList() == null || result.item.getList().isEmpty()) {
                            nextPage = null;
                        } else {
                            nextPage = String.valueOf(result.next);
                        }

                        NYLog.d("+++ countries size : next page = "+nextPage);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return e;
                    }
                }

                if(gRaws != null) {
                    daoSession.getNYCountryCodeDao().insertOrReplaceInTx(gRaws);
                }
                countries = NYHelper.generateList(gRaws, CountryCode.class);
                return countries;
            }
        };
        new Thread(runnable).start();
    }

    public void loadCategories(@NonNull LoadDataListener<Category> listener, boolean clearOnSuccess) {
        loadCategoriesInternal(listener, false, clearOnSuccess);
    }

    public void loadCategoriesUseCacheIfAvailable(@NonNull LoadDataListener<Category> listener, boolean clearOnSuccess) {
        loadCategoriesInternal(listener, true, clearOnSuccess);
    }

    private List<Category> loadCategoriesFromCacheInternal(DaoSession session) {
        List<NYCategory> rawInterests = session.getNYCategoryDao().queryBuilder().list();
        return NYHelper.generateList(rawInterests, Category.class);
    }

    private void loadCategoriesInternal(@Nullable LoadDataListener<Category> listener, boolean useCache, boolean clearOnSuccess) {
        DToUIThreadRunnable<Object, Void, Object> runnable = new DToUIThreadRunnable<Object, Void, Object>(
                getContext(),
                useCache,
                listener,
                clearOnSuccess
        ) {
            LoadDataListener<Category> listener;
            android.location.Location location;
            @Override
            protected void onOutputInForeground(Object o) {
                super.onOutputInForeground(o);
                if (listener == null) return;

                if (o instanceof Exception) {
                    listener.onLoadFailed((Exception) o);
                } else if (o instanceof List) {
                    listener.onDataLoaded((List<Category>) o);
                } else {
                    listener.onLoadFailed(new NYInvalidReturnValueException("unknown class type!"));
                }
            }

            @Override
            protected Object runInBackground(Object... objects) {
                boolean useCache = (Boolean) objects[0];
                listener = (LoadDataListener<Category>) objects[1];
                boolean clearOnSuccess = (Boolean) objects[2];
                List<Category> categories = new ArrayList<>();
                DaoSession daoSession = ((NYApplication) getContext().getApplication()).getDaoSession();

                if (useCache) {
                    categories = loadCategoriesFromCacheInternal(daoSession);
                    if (categories != null && !categories.isEmpty()) {
                        return categories;
                    }
                }

                String nextPage = "1";

                List<NYCategory> gRaws = null;
                while (!TextUtils.isEmpty(nextPage)) {
                    NYLog.d("start fetch master categories for " + " page " + nextPage);
                    try {
                        NYMasterCategoriesRequest req = new NYMasterCategoriesRequest(getContext(), nextPage);
                        NYPaginationResult<CategoryList> result = req.loadDataFromNetwork();
                        if (result != null && result.item != null && result.item.getList() != null && !result.item.getList().isEmpty()) {
                            NYLog.d("+++ categories size = " + result.item.getList().size());

                            for (Category c : result.item.getList()) {
                                if (gRaws == null) gRaws = new ArrayList<>();

                                NYCategory category = new NYCategory();
                                c.copyTo(category, daoSession);
                                gRaws.add(category);
                            }
                        } else {
                            NYLog.d("+++ categories size = 0");
                        }

                        NYLog.d("+++ categories size : result = "+result.toString());

                        if (result == null || result.next < 1 || result.item == null || result.item.getList() == null || result.item.getList().isEmpty()) {
                            nextPage = null;
                        } else {
                            nextPage = String.valueOf(result.next);
                        }

                        NYLog.d("+++ categories size : next page = "+nextPage);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return e;
                    }
                }

                if(gRaws != null) {
                    daoSession.getNYCategoryDao().insertOrReplaceInTx(gRaws);
                }
                categories = NYHelper.generateList(gRaws, Category.class);
                return categories;
            }
        };
        new Thread(runnable).start();
    }

    public interface LoadDataListener<MODEL> {
        void onLoadFailed(Exception e);
        void onDataLoaded(List<MODEL> items);
    }

}
