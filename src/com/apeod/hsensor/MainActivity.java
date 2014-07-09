package com.apeod.hsensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	private static final String TAG = "HSensor";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// mSensorM = (SensorManager) getSystemService(SENSOR_SERVICE);
		// mTempS = mSensorM.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		// mSensorM.registerListener(this, mTempS,
		// SensorManager.SENSOR_DELAY_NORMAL);
		Log.v(TAG, "action!!!!!!!!!!!!!");
	}

}
