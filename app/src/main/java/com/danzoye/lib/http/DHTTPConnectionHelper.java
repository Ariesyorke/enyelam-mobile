package com.danzoye.lib.http;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Ramdhany Dwi Nugroho on Feb 2015.
 */
public class DHTTPConnectionHelper {
    public static final String HTTP_POST = "POST";
    public static final String HTTP_GET = "GET";
    private static final boolean DEBUG = true;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    public static String DELIMITER = "--";
    public static String BOUNDARY = "----------V2ymHFg03ehbqgZCaKO6jy";

    public static byte[] connect(DBaseRequest request, String requestType, String cacheControl)
            throws Exception {
        if (requestType == null) {
            requestType = defineRequestType(request);
        }

        if (DEBUG) {
            Log.d("danzoye", "http url = " + request.getUrl());
            Log.d("danzoye", "http type = " + requestType);
        }

        if (requestType.equals(HTTP_GET)) {
            StringBuffer buf = new StringBuffer(request.getUrl());
            List<Object[]> qs = request.getQueries();
            if (qs != null && !qs.isEmpty()) {
                buf.append("?");

                int n = qs.size();
                int i = 0;
                for (Object[] o : qs) {
                    if (o[0] instanceof String && o[1] instanceof String) {
                        buf.append((String) o[0]);
                        buf.append("=");
                        buf.append((String) o[1]);
                        if (i < n - 1) {
                            buf.append("&");
                        }
                    } else {
                        if (DEBUG) {
                            Log.e("danzoye",
                                    "http get only support string paramaters and values! = ("
                                            + o[0] + ", " + o[1] + ")");
                        }
                    }
                    i++;
                }
            }

            URL url = new URL(buf.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if(!TextUtils.isEmpty(cacheControl)) {
                conn.setRequestProperty("Cache-Control", cacheControl);
            } else {
                conn.setRequestProperty("Cache-Control", "no-cache, no-transform");
            }

            if (request.getHeaders() != null) {
                List<Header> headers = request.getHeaders();
                for (Header header : headers) {
                    conn.setRequestProperty(header.getName(), header.getValue());
                }
            }

            conn.setRequestMethod(requestType);

            Map<String, List<String>> reqProp = conn.getRequestProperties();
            if (DEBUG) {
                for (String key : reqProp.keySet()) {
                    Log.d("danzoye", "Headers[" + key + "] = ");
                    List<String> values = reqProp.get(key);
                    for (String value : values) {
                        Log.d("danzoye", "---" + value);
                    }
                }
            }

            int response = conn.getResponseCode();
            byte[] result = null;

            if (response >= 200 && response < 300) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                long count = 0;
                int n = 0;
                while (-1 != (n = is.read(buffer))) {
                    bos.write(buffer, 0, n);
                    count += n;
                }
                result = bos.toByteArray();

                is.close();
                bos.close();
            } else {
                int ch;
                InputStream errorStream = conn.getErrorStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((ch = errorStream.read()) != -1) {
                    baos.write((char) ch);
                }
                byte[] errorData = baos.toByteArray();
                errorStream.close();
                baos.close();
                conn.disconnect();
                throw new IOException(new StringBuffer(
                        conn.getResponseMessage() + " = "
                                + new String(errorData)).toString());
            }

            conn.disconnect();

            return result;
        } else {
            URL url = new URL(request.getUrl());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (request instanceof DJSONRequest) {
                conn.setRequestProperty("Content-Type", "application/json");
            } else if (isMultipartRequest(request)) {
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + BOUNDARY);
            }
            conn.setRequestProperty("Cache-Control", "no-cache, no-transform");

            if (request.getHeaders() != null) {
                List<Header> headers = request.getHeaders();
                for (Header header : headers) {
                    conn.setRequestProperty(header.getName(), header.getValue());
                }
            }

            conn.setRequestMethod(requestType);

            Map<String, List<String>> reqProp = conn.getRequestProperties();
            if (DEBUG) {
                for (String key : reqProp.keySet()) {
                    Log.d("danzoye", "Headers[" + key + "] = ");
                    List<String> values = reqProp.get(key);
                    for (String value : values) {
                        Log.d("danzoye", "---" + value);
                    }
                }
            }

            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            if (request instanceof DJSONRequest) {
                if (DEBUG) Log.d("danzoye", "json post");
                writeJSONPost(os, (DJSONRequest) request);
            } else if (isMultipartRequest(request)) {
                if (DEBUG) Log.d("danzoye", "multi part post");
                writeMultipartPost(os, request.getQueries());
            } else {
                if (DEBUG) Log.d("danzoye", "common post");
                writePost(os, request.getQueries());
            }

            int response = conn.getResponseCode();
            byte[] result = null;

            if (response >= 200 && response < 300) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                long count = 0;
                int n = 0;
                while (-1 != (n = is.read(buffer))) {
                    bos.write(buffer, 0, n);
                    count += n;
                }
                result = bos.toByteArray();

                is.close();
                bos.close();
            } else {
                int ch;
                InputStream errorStream = conn.getErrorStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((ch = errorStream.read()) != -1) {
                    baos.write((char) ch);
                }
                byte[] errorData = baos.toByteArray();
                errorStream.close();
                baos.close();
                os.close();
                conn.disconnect();
                throw new IOException(new StringBuffer(
                        conn.getResponseMessage() + " = "
                                + new String(errorData)).toString());
            }

            os.close();
            conn.disconnect();
            return result;
        }
    }

    /**
     * WARNING : This is blocking method. Do not invoke this method in UI Thread.
     *
     * @param sUrl    the url
     * @param headers the headers or null
     * @param files   the file posts or null
     * @param strings the string posts or null
     * @return
     * @throws IOException           possible cause : request timed out, unable connect to server, server error, php error
     * @throws MalformedURLException possible cause : invalid url
     */
    public static byte[] httpPostFile(
            String sUrl,
            List<Header> headers,
            List<Pair<String, File>> files,
            List<Pair<String, String>> strings)
            throws IOException, MalformedURLException {

        URL url = new URL(sUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);
        conn.setRequestProperty("Cache-Control", "no-cache, no-transform");

        if (headers != null && !headers.isEmpty()) {
            for (Header header : headers) {
                conn.setRequestProperty(header.getName(), header.getValue());
            }
        }

        conn.setRequestMethod(HTTP_POST);

        Map<String, List<String>> reqProp = conn.getRequestProperties();
        if (DEBUG) {
            for (String key : reqProp.keySet()) {
                Log.d("danzoye", "Headers[" + key + "] = ");
                List<String> values = reqProp.get(key);
                for (String value : values) {
                    Log.d("danzoye", "---" + value);
                }
            }
        }

        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();

        if (DEBUG) Log.d("danzoye", "write multi part post");

        List<Object[]> queries = new ArrayList<Object[]>();
        if (files != null && !files.isEmpty()) {
            for (Pair<String, File> f : files) {
                queries.add(new Object[]{f.first, f.second});
            }
        }
        if (strings != null && !strings.isEmpty()) {
            for (Pair<String, String> f : strings) {
                queries.add(new Object[]{f.first, f.second});
            }
        }

        writeMultipartPost(os, queries);

        int response = conn.getResponseCode();
        byte[] result = null;

        if (response >= 200 && response < 300) {
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            long count = 0;
            int n = 0;
            while (-1 != (n = is.read(buffer))) {
                bos.write(buffer, 0, n);
                count += n;
            }
            result = bos.toByteArray();

            is.close();
            bos.close();
        } else {
            int ch;
            InputStream errorStream = conn.getErrorStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = errorStream.read()) != -1) {
                baos.write((char) ch);
            }
            byte[] errorData = baos.toByteArray();
            errorStream.close();
            baos.close();
            os.close();
            conn.disconnect();
            throw new IOException(new StringBuffer(
                    conn.getResponseMessage() + " = "
                            + new String(errorData)).toString());
        }

        os.close();
        conn.disconnect();
        return result;
    }

    public static byte[] httpGet(String url, List<Pair<String, String>> params, DHTTPConnectionHelper.Setting setting) throws IOException {
        StringBuffer buf = new StringBuffer(url);
        if(params != null && !params.isEmpty()) {
            buf.append("?");
            int mUrl = params.size();

            for(int conn = 0; conn < mUrl; ++conn) {
                Pair p = (Pair)params.get(conn);
                buf.append((String)p.first);
                buf.append("=");
                buf.append((String)p.second);
                if(conn < mUrl - 1) {
                    buf.append("&");
                }
            }
        }

        URL var7 = new URL(buf.toString());
        HttpURLConnection var8 = (HttpURLConnection)var7.openConnection();
        settingConnection(var8, setting);
        var8.setRequestMethod("GET");
        debugHeaders(var8);
        return handleResponseConnection(var8);
    }

    private static void readInputStreamToOutputStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        boolean n = false;

        int n1;
        while(-1 != (n1 = input.read(buffer))) {
            output.write(buffer, 0, n1);
        }

    }

    private static byte[] handleResponseConnection(HttpURLConnection conn) throws IOException {
        int response = conn.getResponseCode();
        Object result = null;
        InputStream is;
        ByteArrayOutputStream bos;
        if(response >= 200 && response < 300) {
            is = conn.getInputStream();
            bos = new ByteArrayOutputStream();
            readInputStreamToOutputStream(is, bos);
            byte[] result1 = bos.toByteArray();
            is.close();
            bos.close();
            conn.disconnect();
            return result1;
        } else {
            is = conn.getErrorStream();
            bos = new ByteArrayOutputStream();
            readInputStreamToOutputStream(is, bos);
            byte[] errorData = bos.toByteArray();
            is.close();
            bos.close();
            conn.disconnect();
            throw new IOException(conn.getResponseMessage() + " = " + new String(errorData));
        }
    }

    private static void debugHeaders(URLConnection conn) {
//        Map reqProp = conn.getRequestProperties();
//        Iterator var2 = reqProp.keySet().iterator();
//
//        while(var2.hasNext()) {
//            String key = (String)var2.next();
//            OXLog.d("Headers[" + key + "] = ");
//            List values = (List)reqProp.get(key);
//            Iterator var5 = values.iterator();
//
//            while(var5.hasNext()) {
//                String value = (String)var5.next();
//                OXLog.d("---" + value);
//            }
//        }
    }

    private static String defineRequestType(DBaseRequest request) {
        if (request instanceof DJSONRequest) {
            return HTTP_POST;
        }
        if (request.getQueries() != null && !request.getQueries().isEmpty()) {
            return HTTP_POST;
        }

        return HTTP_GET;
    }

    private static String getMime(String ext) {
        if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
            return "image/" + ext;
        }

        if (DEBUG) Log.w("danzoye", "can't define mime type = " + ext);
        // TODO tambah type lain

        return "*/*";
    }

    private static void settingConnection(URLConnection conn, DHTTPConnectionHelper.Setting setting) {
        conn.setRequestProperty("Cache-Control", "no-cache, no-transform");
        if(setting != null && setting.headers != null && !setting.headers.isEmpty()) {
            Iterator var2 = setting.headers.iterator();

            while(var2.hasNext()) {
                Header header = (Header)var2.next();
                conn.setRequestProperty(header.getName(), header.getValue());
            }

            conn.setReadTimeout(setting.readTimeout);
            conn.setConnectTimeout(setting.connectTimeout);
        } else {
            conn.setReadTimeout('\uea60');
            conn.setConnectTimeout('\uea60');
        }

    }

    private static boolean isMultipartRequest(DBaseRequest request) {
        List<Object[]> qs = request.getQueries();
        for (Object[] q : qs) {
            if (q[1] instanceof File) {
                return true;
            }
        }

        return false;
    }

    private static byte[] loadData(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    private static void writeJSONPost(OutputStream os, DJSONRequest req)
            throws IOException, JSONException {
        JSONObject obj = req.getJSON();
        if (obj == null) {
            List<Object[]> queries = req.getQueries();
            if (queries != null && !queries.isEmpty()) {
                for (Object[] o : queries) {
                    if (o[0] instanceof String && o[1] instanceof String) {
                        if (DEBUG) {
                            Log.d("danzoye",
                                    "" + o[0].toString() + " = " + o[1].toString());
                        }
                        if (obj == null) {
                            obj = new JSONObject();
                        }
                        obj.put((String) o[0], (String) o[1]);
                    } else {
                        if (DEBUG) {
                            Log.e("danzoye",
                                    "http post json only support string paramaters and values! = ("
                                            + o[0] + ", " + o[1] + ")");
                        }
                    }
                }
            }
        }

        if (obj != null) {
            if (DEBUG) Log.d("danzoye", "json post = " + obj.toString(3));
            os.write(obj.toString().getBytes());
        }

        os.flush();
    }

    private static void writeMultipartPost(OutputStream os,
                                           List<Object[]> queries) throws IOException {
        if (queries != null && !queries.isEmpty()) {
            for (Object[] post : queries) {
                if (post[0] instanceof String
                        && (post[1] instanceof String || post[1] instanceof File)) {
                } else {
                    String detailMessage = "http multipart post only support string paramaters = "
                            + post[0]
                            + " and string or multipart values = "
                            + post[1];
                    if (DEBUG) Log.e("danzoye", detailMessage);
                    throw new IOException(detailMessage);
                }
            }

            for (Object[] post : queries) {
                os.write(DELIMITER.getBytes());
                os.write(BOUNDARY.getBytes());
                os.write("\r\n".getBytes());
                os.write("Content-Disposition: form-data; name=\"".getBytes());
                os.write(((String) post[0]).getBytes());

                if (post[1] instanceof String) {
                    if (DEBUG) {
                        Log.d("danzoye",
                                "" + post[0].toString() + " = "
                                        + post[1].toString());
                    }

                    os.write("\"\r\n\r\n".getBytes());
                    os.write(((String) post[1]).getBytes());
                } else if (post[1] instanceof File) {
                    if (DEBUG) {
                        Log.d("danzoye", "" + post[0].toString() + " = "
                                + ((File) post[1]).getAbsolutePath());
                    }

                    File file = (File) post[1];

                    String path = file.getAbsolutePath();
                    String ext = null;
                    int index = path.lastIndexOf(".");
                    if (index > -1) {
                        ext = path.substring(index + 1);
                    } else {
                        ext = "jpg";
                        index = path.length();
                    }

                    String filename = path.replace("/", "_");
                    String mime = getMime(ext);
                    byte[] data = loadData(file);

                    os.write("\"; filename=\"".getBytes());
                    os.write(filename.getBytes());
                    os.write("\"\r\n".getBytes());
                    os.write("Content-Type: ".getBytes());
                    os.write(mime.getBytes());
                    os.write("\r\n\r\n".getBytes());
                    os.write(data);
                }

                os.write("\r\n".getBytes());
            }

            os.write(DELIMITER.getBytes());
            os.write(BOUNDARY.getBytes());
            os.write("\r\n".getBytes());
            os.flush();
        }
    }

    private static void writePost(OutputStream os, List<Object[]> queries)
            throws IOException {
        if (queries != null && !queries.isEmpty()) {
            int n = queries.size();
            int i = 0;
            for (Object[] o : queries) {
                if (o[0] instanceof String && o[1] instanceof String) {
                    if (DEBUG) {
                        Log.d("danzoye",
                                "" + o[0].toString() + " = " + o[1].toString());
                    }

                    os.write(URLEncoder.encode((String) o[0], "UTF-8")
                            .getBytes());
                    os.write("=".getBytes());
                    os.write(URLEncoder.encode((String) o[1], "UTF-8")
                            .getBytes());
                    if (i < n - 1) {
                        os.write("&".getBytes());
                    }
                } else {
                    if (DEBUG) {
                        Log.e("danzoye",
                                "http post only support string paramaters and values! = ("
                                        + o[0] + ", " + o[1] + ")");
                    }
                }
                i++;
            }
        }
    }

    public static class Setting {
        private static final String KEY_BASIC_AUTH_UNAME = "basic auth uname";
        private static final String KEY_BASIC_AUTH_PASSWORD = "basic auth password";
        private static final String KEY_USERAGENT = "user agent";
        private static final String KEY_HEADERS = "headers";
        private static final String KEY_HEADER_KEY = "key";
        private static final String KEY_HEADER_VALUE = "value";
        private static final String KEY_CONN_TIMEOUT = "conn timeout";
        private static final String KEY_READ_TIMEOUT = "read timeout";
        public String basicAuthorizationUsername = null;
        public String basicAuthorizationPassword = null;
        public String userAgent = null;
        public List<Header> headers = null;
        public int connectTimeout = '\uea60';
        public int readTimeout = '\uea60';

        public Setting() {
        }

        public Setting(DHTTPConnectionHelper.Setting setting) {
            this.basicAuthorizationPassword = setting.basicAuthorizationPassword;
            this.basicAuthorizationUsername = setting.basicAuthorizationUsername;
            this.userAgent = setting.userAgent;
            this.headers = setting.headers;
            this.connectTimeout = setting.connectTimeout;
            this.readTimeout = setting.readTimeout;
        }

        public Setting(String jsonSetting) {
            JSONObject obj = null;

            try {
                obj = new JSONObject(jsonSetting);
            } catch (JSONException var12) {
                var12.printStackTrace();
            }

            if(obj != null) {
                if(!obj.isNull("basic auth uname")) {
                    try {
                        this.basicAuthorizationUsername = obj.getString("basic auth uname");
                    } catch (JSONException var11) {
                        var11.printStackTrace();
                    }
                }

                if(!obj.isNull("basic auth password")) {
                    try {
                        this.basicAuthorizationPassword = obj.getString("basic auth password");
                    } catch (JSONException var10) {
                        var10.printStackTrace();
                    }
                }

                if(!obj.isNull("user agent")) {
                    try {
                        this.userAgent = obj.getString("user agent");
                    } catch (JSONException var9) {
                        var9.printStackTrace();
                    }
                }

                if(!obj.isNull("headers")) {
                    try {
                        JSONArray e = obj.getJSONArray("headers");
                        this.headers = new ArrayList();

                        for(int i = 0; i < e.length(); ++i) {
                            JSONObject o = e.getJSONObject(i);
                            BasicHeader header = new BasicHeader(o.getString("key"), o.getString("value"));
                            this.headers.add(header);
                        }
                    } catch (JSONException var13) {
                        var13.printStackTrace();
                    }
                }

                if(!obj.isNull("conn timeout")) {
                    try {
                        this.connectTimeout = obj.getInt("conn timeout");
                    } catch (JSONException var8) {
                        var8.printStackTrace();
                    }
                }

                if(!obj.isNull("read timeout")) {
                    try {
                        this.readTimeout = obj.getInt("read timeout");
                    } catch (JSONException var7) {
                        var7.printStackTrace();
                    }
                }

            }
        }

        public String toString() {
            JSONObject obj = new JSONObject();

            try {
                if(!TextUtils.isEmpty(this.basicAuthorizationPassword)) {
                    obj.put("basic auth password", this.basicAuthorizationPassword);
                } else {
                    obj.put("basic auth password", JSONObject.NULL);
                }
            } catch (JSONException var11) {
                var11.printStackTrace();
            }

            try {
                if(!TextUtils.isEmpty(this.basicAuthorizationUsername)) {
                    obj.put("basic auth uname", this.basicAuthorizationUsername);
                } else {
                    obj.put("basic auth uname", JSONObject.NULL);
                }
            } catch (JSONException var10) {
                var10.printStackTrace();
            }

            try {
                if(!TextUtils.isEmpty(this.userAgent)) {
                    obj.put("user agent", this.userAgent);
                } else {
                    obj.put("user agent", JSONObject.NULL);
                }
            } catch (JSONException var9) {
                var9.printStackTrace();
            }

            try {
                if(this.headers != null && !this.headers.isEmpty()) {
                    JSONArray e = new JSONArray();
                    Iterator var3 = this.headers.iterator();

                    while(var3.hasNext()) {
                        Header header = (Header)var3.next();
                        JSONObject o = new JSONObject();
                        o.put("key", header.getName());
                        o.put("value", header.getValue());
                        e.put(o);
                    }

                    obj.put("headers", e);
                } else {
                    obj.put("headers", JSONObject.NULL);
                }
            } catch (JSONException var12) {
                var12.printStackTrace();
            }

            try {
                if(this.connectTimeout > 0) {
                    obj.put("conn timeout", this.connectTimeout);
                } else {
                    obj.put("conn timeout", '\uea60');
                }
            } catch (JSONException var8) {
                var8.printStackTrace();
            }

            try {
                if(this.readTimeout > 0) {
                    obj.put("read timeout", this.readTimeout);
                } else {
                    obj.put("read timeout", '\uea60');
                }
            } catch (JSONException var7) {
                var7.printStackTrace();
            }

            try {
                return obj.toString(3);
            } catch (JSONException var6) {
                var6.printStackTrace();
                return (new JSONObject()).toString();
            }
        }
    }
}
