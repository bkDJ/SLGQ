package com.dkebaili.slgq.display;

import java.util.Date;

import com.dkebaili.slgq.SlgqUtils;
import com.dkebaili.slgq.SlgqUtils.TimeType;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;

public class AnalogSlgqDrawable extends Drawable {
	boolean autoUpdates;
	float s,l,g,q;
	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	Path borderPath = new Path();
	PointF center = new PointF();
	float radius;
	
	public AnalogSlgqDrawable(boolean autoUpdates) {
		this.autoUpdates = autoUpdates;
		paint.setStrokeCap(Paint.Cap.ROUND);
		updateTimeValues();
	}
	@Override public void setAlpha(int alpha) {
	}
	@Override public void setColorFilter(ColorFilter cf) {
	}
	@Override public int getOpacity() {
		return android.graphics.PixelFormat.TRANSLUCENT;
	}
	
	private void updateTimeValues() {
		Date d = new Date();
		s = (float)SlgqUtils.currentValue(TimeType.S, d) / SlgqUtils.getMaxValue(TimeType.S);
		l = (float)SlgqUtils.currentValue(TimeType.L, d) / SlgqUtils.getMaxValue(TimeType.L);
		g = (float)SlgqUtils.currentValue(TimeType.G, d) / SlgqUtils.getMaxValue(TimeType.G);
		q = (float)SlgqUtils.currentValue(TimeType.Q, d) / SlgqUtils.getMaxValue(TimeType.Q);
		
		if (autoUpdates) {
			Handler h = new Handler();
			h.postDelayed(new Runnable() { @Override public void run() {
				updateTimeValues();
				invalidateSelf();
			}}, SlgqUtils.millisTillNextUpdate(TimeType.Q, d));
		}
	}
	@Override protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		radius = Math.min(bounds.width(), bounds.height()) / 2.0f;
		center.set(bounds.width()/2.0f, bounds.height()/2.0f);
		borderPath.reset();
		borderPath.addCircle(center.x, center.y, radius, Direction.CW);
		borderPath.addCircle(center.x, center.y, radius*0.95f, Direction.CCW);
	}
	@Override public void draw(Canvas canvas) {
		// draw border
		paint.setStyle(Paint.Style.FILL);
		
		paint.setColor(Color.BLACK);
		canvas.drawPath(borderPath, paint);
		
		// draw components
		paint.setStyle(Paint.Style.STROKE);
		drawHand(canvas, TimeType.S, s, radius * 0.4f, radius * 0.06f);
		drawHand(canvas, TimeType.L, l, radius * 0.6f, radius * 0.05f);
		drawHand(canvas, TimeType.G, g, radius * 0.8f, radius * 0.04f);
		drawHand(canvas, TimeType.Q, q, radius * 0.9f, radius * 0.03f);
	}
	private void drawHand(Canvas canvas, TimeType type, float revPercent, float length, float width) {
		float angle = -revPercent * 2.0f * (float)Math.PI;
		paint.setColor(SlgqUtils.colorOf(type));
		paint.setStrokeWidth(width);
		canvas.drawLine(
				center.x,
				center.y,
				center.x + (float)Math.cos(angle) * length,
				center.y + (float)Math.sin(angle) * length,
				paint
		);
	}
}
