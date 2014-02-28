package com.mapara.sendmyapp;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.Menu;

public class MainActivity extends Activity {
    private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
