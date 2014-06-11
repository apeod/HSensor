package com.apeod.hsensor;

import com.apeod.hsensor.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class HSensorWidget extends AppWidgetProvider {
	private static final String TAG = "HSensorWidget";
	private Context mContext;
	private float mTempV;
	static private float mTempMax = 0f;
	static private float mTempMin = 0f;

	private float mPresureV;
	private float mHumdityV;

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		Log.v(TAG, "onAppWidgetOptionsChanged!!!!!");
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		// Log.v(TAG, "action!!!!!!!!!!!!!" + intent.getAction());
		String action = intent.getAction();
		if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
			startService(context);
		} else if (action.equalsIgnoreCase("com.apeod.hsensor.data")) {
			HSensorDatas data = (HSensorDatas) intent
					.getSerializableExtra("HSensoData");
			if (data != null) {
				mTempV = data.getTemp();

				if (mTempV > mTempMax)
					mTempMax = mTempV;

				if (mTempMin == 0)
					mTempMin = mTempV;

				if (mTempV < mTempMin)
					mTempMin = mTempV;

				mPresureV = data.getPresure();
				mHumdityV = data.getHumdity();

				update(context);
			}
		}
	}

	private void startService(Context context) {
		Intent service = new Intent(context.getApplicationContext(),
				HSensorService.class);
		context.startService(service);
	}

	@Override
	public void onEnabled(Context context) {
		Log.v(TAG, "onEnabled!!!!!");
		startService(context);
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		Log.v(TAG, "onDisabled!!!!!");
		super.onDisabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.v(TAG, "onUpdate!!!!!");
		mContext = context;
	}

	private void update(Context context) {
		if (context == null)
			return;
		ComponentName thisWidget = new ComponentName(context,
				HSensorWidget.class);
		int[] allWidgetIds = AppWidgetManager.getInstance(context)
				.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.hsensor_widget_layout);
			String value = String.format("%.1f℃", mTempV);
			remoteViews.setTextViewText(R.id.textView1, value);

			String valueMax = String.format("Max:%.1f℃", mTempMax);
			remoteViews.setTextViewText(R.id.textView2, valueMax);
			String valueMin = String.format("Min:%.1f℃", mTempMin);
			remoteViews.setTextViewText(R.id.textView3, valueMin);

			String presure = String.format("P:%.1fhPa", mPresureV);
			remoteViews.setTextViewText(R.id.textView4, presure);

			String humidity = String.format("H:%.1f%%", mHumdityV);
			remoteViews.setTextViewText(R.id.textView5, humidity);

			AppWidgetManager.getInstance(context).updateAppWidget(widgetId,
					remoteViews);
		}
	}

}
