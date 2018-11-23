package com.danzoye.lib.http;

import com.nyelam.android.dev.NYLog;
import com.octo.android.robospice.request.SpiceRequest;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ramdhany Dwi Nugroho on Feb 2015.
 */
public abstract class DBaseRequest<RESULT> extends SpiceRequest<RESULT> {
    private String url;
    private List<Object[]> queries;
    private List<Header> headers;

    protected DBaseRequest(final Class clazz, String url) {
        super(clazz);
        this.url = url;
    }

    public void addHeader(Header header) {
        if (headers == null) {
            headers = Collections.synchronizedList(new ArrayList<Header>());
        }
        headers.add(header);
    }

    public void addHeaders(List<Header> headers) {
        if (this.headers == null) {
            this.headers = Collections.synchronizedList(new ArrayList<Header>());
        }
        this.headers.addAll(headers);
    }

    public void addMultiPartFile(String param, File file) {
        if (this.queries == null) {
            this.queries = Collections.synchronizedList(new ArrayList<Object[]>());
        }
        this.queries.add(new Object[]{param, file});
    }

    public void addQueries(List<String[]> queries) {
        if (this.queries == null) {
            this.queries = Collections.synchronizedList(new ArrayList<Object[]>());
        }
        this.queries.addAll(queries);
    }

    public void addQuery(String param, String value) {
        if (this.queries == null) {
            this.queries = Collections.synchronizedList(new ArrayList<Object[]>());
        }
        this.queries.add(new String[]{param, value});

        if (param != null && value != null)NYLog.e(param+" : "+value);
    }

    public String getHTTPType() {
        return null;
    }

    public String getCacheControl() {
        return null;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public List<Object[]> getQueries() {
        return queries;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public RESULT loadDataFromNetwork() throws Exception {
        byte[] data = DHTTPConnectionHelper.connect(this, getHTTPType(), getCacheControl());
        return onProcessData(data);
    }

    protected abstract RESULT onProcessData(byte[] data) throws Exception;
}
