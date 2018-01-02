package com.danzoye.lib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Ramdhany Dwi Nugroho on Feb 2015.
 */
public class CommonHelper {
    private static final long MAX_CACHE_SIZE = 20 * 1000000;
    private static final long NORMAL_CACHE_SIZE = 5 * 1000000;

    public static final void checkStorageAndDeleteCacheIfNeeded(
            Context context, File root) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }

        long fileSize = root.length();

        if (fileSize > MAX_CACHE_SIZE) {
            Log.d("danzoye", "cache dir need to be trimed! size = " + fileSize);

            File[] files = root.listFiles();
            if (files != null && files.length > 0) {
                List<File> sortedFiles = Arrays.asList(files);
                Collections.sort(sortedFiles, new Comparator<File>() {
                    @Override
                    public int compare(File lhs, File rhs) {
                        return (int) (lhs.lastModified() - rhs.lastModified());
                    }
                });

                SimpleDateFormat format = new SimpleDateFormat(
                        "dd MM yy HH:mm:ss", Locale.US);
                for (File file : sortedFiles) {
                    Log.d("danzoye",
                            "deleting file = "
                                    + file.getAbsolutePath()
                                    + "; modified = "
                                    + format.format(new Date(file
                                    .lastModified())));
                    file.delete();

                    Log.d("danzoye", "check size!");
                    long newSize = root.length();
                    Log.d("danzoye", "" + newSize);

                    if (newSize <= NORMAL_CACHE_SIZE) {
                        Log.d("danzoye", "cache dir trimmed to " + newSize);
                        break;
                    }
                }
            }
        }
    }

    public static final int getVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("danzoye", e.toString());
            return 0;
        }
    }

    public static final String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("danzoye", e.toString());
            return null;
        }
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static final boolean isApplicationInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static class Installation {
        private static final String INSTALLATION = "installation";
        private static String sID = null;

        public synchronized static String id(Context context) {
            if (sID == null) {
                File installation = new File(context.getFilesDir(),
                        INSTALLATION);
                try {
                    if (!installation.exists())
                        writeInstallationFile(context, installation);
                    sID = readInstallationFile(installation);
                } catch (Exception e) {
                    Log.e("danzoye", "Installation failed get id", e);
                    throw new RuntimeException(e);
                }
            }
            Log.d("danzoye", "HW ID : " + sID);
            return sID;
        }

        private static String readInstallationFile(File installation)
                throws IOException {
            RandomAccessFile f = new RandomAccessFile(installation, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            f.close();
            return new String(bytes);
        }

        private static void writeInstallationFile(Context context,
                                                  File installation) throws IOException {
            // String androidID = android.provider.Settings.Secure.ANDROID_ID;
            String androidID = ((TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            FileOutputStream out = new FileOutputStream(installation);
            String id = null;
            if (androidID != null && androidID.length() > 0) {
                id = UUID.nameUUIDFromBytes(androidID.getBytes()).toString();
            } else {
                id = UUID.randomUUID().toString();
            }
            out.write(id.getBytes());
            out.close();
        }
    }

}
