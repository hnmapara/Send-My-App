package com.mapara.sendmyapp.helper;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

public class SendAppUtility {
	
	public static final String TAG = "Mp3Utility";


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
}