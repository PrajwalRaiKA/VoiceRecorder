package test.voicerecorder;/*
 * Copyright (C) 2017 Gautam Chibde
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import test.voicerecorder.BaseVisualizer;

/**
 * Custom view that creates a Circle and Bar visualizer effect for
 * the android {@link android.media.MediaPlayer}
 *
 * Created by gautam chibde on 20/11/17.
 */

public class CircleBarVisualizer extends BaseVisualizer {
    private float[] points;
    private Paint circlePaint;
    private int radius;
    protected String timeValue = "";

    public CircleBarVisualizer(Context context) {
        super(context);
    }

    public CircleBarVisualizer(Context context,
                               @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleBarVisualizer(Context context,
                               @Nullable AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBytes(byte[] bytes){
        this.bytes = bytes;
    }

    public void setTimeValue(String time){
        timeValue = time;
    }

    @Override
    protected void init() {
        paint.setStyle(Paint.Style.STROKE);
        circlePaint = new Paint();
        radius = -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (radius == -1) {
            radius = getHeight() < getWidth() ? getHeight() : getWidth();
            radius = (int) (radius * 0.65 / 2);
            double circumference = 2 * Math.PI * radius;
            paint.setStrokeWidth((float) (circumference / 120));
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeWidth(4);
        }
        circlePaint.setColor(color);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, circlePaint);
        if (bytes != null) {
            if (points == null || points.length < bytes.length * 4) {
                points = new float[bytes.length * 4];
            }
            double angle = 0;

            for (int i = 0; i < 120; i++, angle += 3) {
                int x = (int) Math.ceil(i * 8.5);
                int t = ((byte) (Math.abs(bytes[x] - 128)) - 32) * (canvas.getHeight() / 4) / 128;

                points[i * 4] = (float) (getWidth() / 2
                        + radius
                        * Math.cos(Math.toRadians(angle)));

                points[i * 4 + 1] = (float) (getHeight() / 2
                        + radius
                        * Math.sin(Math.toRadians(angle)));

                points[i * 4 + 2] = (float) (getWidth() / 2
                        + (radius + t)
                        * Math.cos(Math.toRadians(angle)));

                points[i * 4 + 3] = (float) (getHeight() / 2
                        + (radius + t)
                        * Math.sin(Math.toRadians(angle)));
            }

            canvas.drawLines(points, paint);
        }
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setColor(Color.RED);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, circlePaint);

        Paint textPaint = new Paint();

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(64);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(timeValue, getWidth() / 2, (getHeight() / 2)+32, textPaint);
        super.onDraw(canvas);
    }
}