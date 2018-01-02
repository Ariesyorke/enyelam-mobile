package com.danzoye.lib.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class RemoteHelper {

    public static byte[] httpGet(String remoteUrl, Setting setting) {
        if (remoteUrl == null) {
            return null;
        }
        HttpURLConnection conn = null;

        try {
            URL url = new URL(remoteUrl);
            // Log.d("danzoye", "-- start fetch");
            conn = (HttpURLConnection) url.openConnection();

            if (setting != null) {
                conn.setReadTimeout(setting.readTimeout);
                conn.setConnectTimeout(setting.connectTimeout);
                if (!TextUtils.isEmpty(setting.userAgent))
                    conn.addRequestProperty("User-Agent", setting.userAgent);
                if (!TextUtils.isEmpty(setting.basicAuthorizationUsername) &&
                        !TextUtils.isEmpty(setting.basicAuthorizationPassword)) {
                    String httpAuth = new StringBuffer("Basic ")
                            .append(Base64.encodeToString(
                                    new StringBuffer(setting.basicAuthorizationUsername)
                                            .append(":")
                                            .append(setting.basicAuthorizationPassword)
                                            .toString()
                                            .getBytes(),
                                    Base64.DEFAULT)).toString();
                    conn.addRequestProperty("Authorization", httpAuth);
                }
                if (setting.headers != null && !setting.headers.isEmpty()) {
                    for (Header header : setting.headers) {
                        conn.setRequestProperty(header.getName(), header.getValue());
                    }
                }
            } else {
                conn.setReadTimeout(60000);
                conn.setConnectTimeout(60000);
            }

            // Log.d("danzoye", "wait for response ...");
            int response = conn.getResponseCode();

            // Log.d("danzoye",
            //        "got response : " + response + " "
            //                + conn.getResponseMessage());

            if (response >= HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                if (conn.getContentEncoding() != null
                        && conn.getContentEncoding().equalsIgnoreCase("gzip")) {
                    is = new GZIPInputStream(is);
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                int ch;

                while ((ch = is.read()) != -1) {
                    baos.write((char) ch);
                }
                is.close();
                return baos.toByteArray();
            }
        } catch (IOException e) {
            // Log.e("danzoye", "failed fetch remote page image : " + e.toString());
            e.printStackTrace();
        }

        return null;
    }

    public static File httpGetAndSave(Context context, String url, Setting setting, File saveTo) {
        byte[] data = httpGet(url, setting);
        if (data != null) {
            try {
                FileOutputStream fos = new FileOutputStream(saveTo);
                fos.write(data);
                return saveTo;
            } catch (IOException e) {
                e.printStackTrace();
                // Log.e("danzoye", e.toString());
            }
        }
        return null;
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
        public int connectTimeout = 60000;
        public int readTimeout = 60000;

        public Setting() {
        }

        public Setting(Setting setting) {
            basicAuthorizationPassword = setting.basicAuthorizationPassword;
            basicAuthorizationUsername = setting.basicAuthorizationUsername;
            userAgent = setting.userAgent;
            headers = setting.headers;
            connectTimeout = setting.connectTimeout;
            readTimeout = setting.readTimeout;
        }

        public Setting(String jsonSetting) {
            JSONObject obj = null;
            try {
                obj = new JSONObject(jsonSetting);
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.e("danzoye", e.toString());
            }

            if (obj == null) {
                return;
            }

            if (!obj.isNull(KEY_BASIC_AUTH_UNAME)) {
                try {
                    basicAuthorizationUsername = obj.getString(KEY_BASIC_AUTH_UNAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Log.e("danzoye", e.toString());
                }
            }

            if (!obj.isNull(KEY_BASIC_AUTH_PASSWORD)) {
                try {
                    basicAuthorizationPassword = obj.getString(KEY_BASIC_AUTH_PASSWORD);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Log.e("danzoye", e.toString());
                }
            }

            if (!obj.isNull(KEY_USERAGENT)) {
                try {
                    userAgent = obj.getString(KEY_USERAGENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Log.e("danzoye", e.toString());
                }
            }

            if (!obj.isNull(KEY_HEADERS)) {
                try {
                    JSONArray array = obj.getJSONArray(KEY_HEADERS);
                    headers = new ArrayList<Header>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Header header = new BasicHeader(o.getString(KEY_HEADER_KEY), o.getString(KEY_HEADER_VALUE));
                        headers.add(header);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Log.e("danzoye", e.toString());
                }
            }

            if (!obj.isNull(KEY_CONN_TIMEOUT)) {
                try {
                    connectTimeout = obj.getInt(KEY_CONN_TIMEOUT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Log.e("danzoye", e.toString());
                }
            }

            if (!obj.isNull(KEY_READ_TIMEOUT)) {
                try {
                    readTimeout = obj.getInt(KEY_READ_TIMEOUT);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Log.e("danzoye", e.toString());
                }
            }
        }

        @Override
        public String toString() {
            JSONObject obj = new JSONObject();

            try {
                if (!TextUtils.isEmpty(basicAuthorizationPassword)) {
                    obj.put(KEY_BASIC_AUTH_PASSWORD, basicAuthorizationPassword);
                } else {
                    obj.put(KEY_BASIC_AUTH_PASSWORD, JSONObject.NULL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.e("danzoye", e.toString());
            }

            try {
                if (!TextUtils.isEmpty(basicAuthorizationUsername)) {
                    obj.put(KEY_BASIC_AUTH_UNAME, basicAuthorizationUsername);
                } else {
                    obj.put(KEY_BASIC_AUTH_UNAME, JSONObject.NULL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.e("danzoye", e.toString());
            }

            try {
                if (!TextUtils.isEmpty(userAgent)) {
                    obj.put(KEY_USERAGENT, userAgent);
                } else {
                    obj.put(KEY_USERAGENT, JSONObject.NULL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.e("danzoye", e.toString());
            }

            try {
                if (headers != null && !headers.isEmpty()) {
                    JSONArray array = new JSONArray();
                    for (Header header : headers) {
                        JSONObject o = new JSONObject();
                        o.put(KEY_HEADER_KEY, header.getName());
                        o.put(KEY_HEADER_VALUE, header.getValue());
                        array.put(o);
                    }
                    obj.put(KEY_HEADERS, array);
                } else {
                    obj.put(KEY_HEADERS, JSONObject.NULL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.e("danzoye", e.toString());
            }

            try {
                if (connectTimeout > 0) {
                    obj.put(KEY_CONN_TIMEOUT, connectTimeout);
                } else {
                    obj.put(KEY_CONN_TIMEOUT, 60000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.e("danzoye", e.toString());
            }

            try {
                if (readTimeout > 0) {
                    obj.put(KEY_READ_TIMEOUT, readTimeout);
                } else {
                    obj.put(KEY_READ_TIMEOUT, 60000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.e("danzoye", e.toString());
            }

            try {
                return obj.toString(3);
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.e("danzoye", e.toString());
                return new JSONObject().toString();
            }
        }
    }
}
