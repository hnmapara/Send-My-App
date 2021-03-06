package com.mapara.sendmyapp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class SendAppUtility {
	
	public static final String TAG = SendAppUtility.class.getSimpleName();

    private static List<ApplicationInfo> getListofInstalledApp(Context ctx, PackageManager pm) {
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            File f = new File(packageInfo.sourceDir);
            Log.d(TAG, "Source dir : " + packageInfo.sourceDir + " & size : "+ formattedFileSize(f.length()));
            Log.d(TAG, "Installed package name : " + String.valueOf(packageInfo.loadLabel(pm)));
            //Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
       return packages;
    }

    public static List<ApkInfo> getListofInstalledAppImages(Context ctx) {
        final PackageManager pm = ctx.getPackageManager();
        List<ApplicationInfo> packages = getListofInstalledApp(ctx, pm);
        List<ApkInfo> apkImageIds = new ArrayList<ApkInfo>();
        for (ApplicationInfo ai : packages) {
            apkImageIds.add(new ApkInfo(ai.loadIcon(pm),String.valueOf(ai.loadLabel(pm)),
                                        ai.sourceDir));
        }
        return apkImageIds;
    }

    public static class ApkInfo {
        public Drawable apkImg;
        public String apkName;
        public String apkPath;

        public ApkInfo(Drawable apkImg, String apkName, String apkPath) {
            this.apkImg = apkImg;
            this.apkName = apkName;
            this.apkPath = apkPath;
        }
    }

    public static void scanSDCardFile(Context ctx, String[] filePaths) {
        MediaScannerConnection.scanFile(ctx, filePaths, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    static String[] sizeUnits = new String[] { "B", "KB", "MB"};
    /**
     *
     * @param  filesize
     * @return B/KB/MB/GB format
     */
    public static String formattedFileSize(long filesize) {
        if(filesize <= 0) return "0";
        int digGroup = (int) (Math.log10(filesize)/Math.log10(1024));

        return new DecimalFormat("#,##0.00")
                .format(filesize/Math.pow(1024, digGroup)) + " " + sizeUnits[digGroup];
    }

    private static final String FTYPE = ".mp3";
    public static FilenameFilter getFileNameFilter(final boolean withDirectory) {
        return new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                File sel = new File(dir, filename);
                return (!sel.isHidden()) && (filename.contains(FTYPE) || filename.contains(".MP3")
                        || (withDirectory? (sel.isDirectory() ? (sel.list()==null? false : true) : false) : false));
            }
        };
    }

    public static String convertTimeSecToMin(String seconds) {
        int s = -1;
        try {
            s = Integer.parseInt(seconds);
        } catch (NumberFormatException ne) {
            Log.e(TAG, "convertTimeSecToMin() " + ne);
            return null;
        }
        int min = s/60;
        int sec = s % 60;
        return String.format("%d:%02d", min, sec);
    }

    public static String convertTimeMilliToMin(int ms) {
        return String.format("%d m, %d s",
                TimeUnit.MILLISECONDS.toMinutes(ms),
                TimeUnit.MILLISECONDS.toSeconds(ms) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms))
        );
    }

    public static void transferFile(String sourcefilePath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        String filename = sourcefilePath.substring(sourcefilePath.lastIndexOf("/") + 1);
        try {
             fis = new FileInputStream(sourcefilePath);
             byte[] buf = new byte[1024];
             fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+filename);
            int bytesRead;
            while ((bytesRead = fis.read(buf)) > 0) {
                fos.write(buf, 0, bytesRead);
            }
            Log.d(TAG, filename + " file was written");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fis!=null) fis.close();
                if(fos!=null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
