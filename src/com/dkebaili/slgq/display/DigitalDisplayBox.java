package com.dkebaili.slgq.display;

import java.util.Calendar;
import java.util.Date;

import com.dkebaili.slgq.R;
import com.dkebaili.slgq.SlgqUtils;
import com.dkebaili.slgq.SlgqUtils.TimeType;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DigitalDisplayBox extends RelativeLayout {
	boolean paused = false;
	boolean autoUpdates;
	
	TimeType type;
	String name;
	
	TextView tvName;
	TextView tvNumber;
	
	public DigitalDisplayBox() {
		super(null);
		initDigitalDisplayBox(null, null);
	}
	public DigitalDisplayBox(Context context) {
		super(context);
		initDigitalDisplayBox(context, null);
	}
	public DigitalDisplayBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDigitalDisplayBox(context, attrs);
	}
	public DigitalDisplayBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initDigitalDisplayBox(context, attrs);
	}
	private void initDigitalDisplayBox(Context context, AttributeSet attrs) {
		if (context == null || attrs == null) {
			return;
		}
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DigitalDisplayBox, 0, 0);
		name = a.getString(R.styleable.DigitalDisplayBox_slgqtype);
		autoUpdates = a.getBoolean(R.styleable.DigitalDisplayBox_autoUpdate, false);
		a.recycle();
		type = TimeType.valueOf(name);
		int typeColor = SlgqUtils.colorOf(type);

		setGravity(Gravity.CENTER_HORIZONTAL);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.slgq_digital_box, this, true);

		tvName = (TextView)findViewById(R.id.digitalbox_name);
		tvName.setText(name);
		tvName.setTextColor(typeColor);
		
		tvNumber = (TextView)findViewById(R.id.digitalbox_number);
		tvNumber.setTextColor(typeColor);
		updater.run();
		
		findViewById(R.id.digitalbox_numberframe).getBackground().setColorFilter(new PorterDuffColorFilter(
				typeColor,
				PorterDuff.Mode.SRC_ATOP
		));
	}
	
	public void onPause() {
		paused = true;
	}
	public void onResume() {
		paused = false;
		if (autoUpdates) {
			updateNumber(new Date());
		}
	}
	
	Runnable updater = new Runnable() { @Override public void run() {
		updateNumber(new Date());
	}};
	public void updateNumber(Date d) {
		tvNumber.setText(String.valueOf(SlgqUtils.currentValue(type, d)));
		tvNumber.invalidate();
		
		if (autoUpdates) {
			Handler h = new Handler();
			long millisTillNextUpdate = SlgqUtils.millisTillNextUpdate(type, d);
			h.postDelayed(updater, millisTillNextUpdate);
		}
	}
}
