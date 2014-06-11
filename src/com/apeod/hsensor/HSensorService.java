package com.apeod.hsensor;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class HSensorService extends Service implements SensorEventListener {
	private static final String TAG = "HSensorService";
	private SensorManager mSensorM;
	private Sensor mTempS;
	private Sensor mPresureS;
	private Sensor mHumdityS;

	private float mTempV;
	private float mTempMax;
	private float mTempMin;

	private float mPresureV;
	private float mHumdityV;

	private boolean mScreenRegistered = false;
	private boolean mSensorRegistered = false;

	private BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_SCREEN_OFF)) {
				unregisterSensors();
			} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
				registerSensors();
			}
		}
	};

	private void registerScreen() {
		if (!mScreenRegistered) {
			IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_SCREEN_ON);
			registerReceiver(mScreenReceiver, filter);
			mScreenRegistered = true;
		}
	}

	private void unregisterScreen() {
		if (mScreenRegistered) {
			unregisterReceiver(mScreenReceiver);
			mScreenRegistered = false;
		}
	}

	private void registerSensors() {
		Log.v(TAG, "registerSensors");
		if (!mSensorRegistered) {
			if (mSensorM == null) {
				mSensorM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			}
			if (mSensorM != null) {
				mTempS = mSensorM
						.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
				mPresureS = mSensorM.getDefaultSensor(Sensor.TYPE_PRESSURE);
				mHumdityS = mSensorM
						.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
				mSensorM.registerListener(this, mTempS,
						SensorManager.SENSOR_DELAY_UI);
				mSensorM.registerListener(this, mPresureS,
						SensorManager.SENSOR_DELAY_UI);
				mSensorM.registerListener(this, mHumdityS,
						SensorManager.SENSOR_DELAY_UI);
			}
			mSensorRegistered = true;
		}
	}

	private void unregisterSensors() {
		Log.v(TAG, "unregisterSensors");
		if (mSensorRegistered) {
			if (mSensorM != null) {
				mSensorM.unregisterListener(this);
			}
			mSensorRegistered = false;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		registerScreen();
		registerSensors();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		unregisterScreen();
		unregisterSensors();
		super.onDestroy();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
			mTempV = event.values[0];
		} else if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
			mHumdityV = event.values[0];
		} else if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
			mPresureV = event.values[0];
		}
		Intent intent = new Intent("com.apeod.hsensor.data");
		HSensorDatas data = new HSensorDatas(mTempV, mPresureV, mHumdityV);
		intent.putExtra("HSensoData", data);
		// update(mContext);
		Log.v(TAG, "sensor change!:" + mTempV);
		sendBroadcast(intent);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
