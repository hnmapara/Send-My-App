package com.mapara.sendmyapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;
import android.widget.TextView;

import com.mapara.sendmyapp.helper.CrashLog;
import com.mapara.sendmyapp.helper.SendAppUtility;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_EMAIL;
import static android.content.Intent.EXTRA_STREAM;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private  SendAppApplication app;
    private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        app = (SendAppApplication) getApplicationContext();
        maybeSendCrashLog(CrashLog.instance(app).previousCrashLog());
        List<Drawable> imageList =  SendAppUtility.getListofInstalledAppImages(this);
        gridView = (GridView)findViewById(R.id.apk_grid);
        gridView.setAdapter(new GridAdapter(this, imageList));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private static final String ZIPFile = "attach.gz";
    private static final String CRPromptString =
            "Mp3Editor closed unexpectedly. " +
                    "Send error report?";
    private static final String DefaultBody =
            "Please tell us what you were doing when SendMyApp crashed.";
    private void maybeSendCrashLog(byte[] log)
    {
        if (log == null) return;

        Resources res = getResources();
        String[] addrs = res.getStringArray(R.array.default_support_email);

        if (addrs == null || addrs.length == 0)
            return;

        Intent intent = new Intent(ACTION_SEND);
        intent.setType("application/gzip");
        intent.putExtra(EXTRA_SUBJECT,
                String.format("CrashReport: Mp3Editor %s",
                        app.buildVersion()));
        intent.putExtra(EXTRA_EMAIL, addrs);
        intent.putExtra(EXTRA_TEXT, DefaultBody);

        try {
            OutputStream ous = openFileOutput(ZIPFile, MODE_WORLD_READABLE);
            ous.write(log);
            ous.close();
            intent.putExtra(EXTRA_STREAM,
                    Uri.parse("file://" +
                            getFileStreamPath(ZIPFile)));
            startActivity(Intent.createChooser(intent, CRPromptString));
        } catch (IOException ioe) {
            Log.e(TAG, "Couldn't store zip attachment.", ioe);
        }
    }


}
