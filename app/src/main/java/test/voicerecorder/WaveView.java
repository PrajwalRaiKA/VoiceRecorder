////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)

/*
package test.voicerecorder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import developer.shivam.library.R.styleable;

public class WaveView extends View {
    private Handler handler;
    private Context mContext = null;
    private int width = 0;
    float x;
    float y1;
    float y2;
    private Paint firstWaveColor;
    private Paint secondWaveColor;
    int frequency = 180;
    int amplitude = 80;
    private float shift = 0.0F;
    private int quadrant;
    Path firstWavePath = new Path();
    Path secondWavePath = new Path();
    private float speed = 0.5F;

    public WaveView(Context context) {
        super(context);
        this.init(context, (AttributeSet)null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributes = this.getContext().obtainStyledAttributes(attrs, styleable.wave, 0, 0);
            int height = attributes.getInt(styleable.wave_wave_height, 8);
            if (height > 10) {
                this.amplitude = 200;
            } else {
                this.amplitude = height * 20;
            }

            float s = (float)attributes.getInt(styleable.wave_wave_speed, 1);
            if (s > 10.0F) {
                this.speed = 0.25F;
            } else {
                this.speed = s / 8.0F;
            }
        }

        this.mContext = context;
        this.firstWaveColor = new Paint();
        this.firstWaveColor.setAntiAlias(true);
        this.firstWaveColor.setStrokeWidth(2.0F);
        this.firstWaveColor.setColor(Color.parseColor("#64B5F6"));
        this.secondWaveColor = new Paint();
        this.secondWaveColor.setAntiAlias(true);
        this.secondWaveColor.setStrokeWidth(2.0F);
        this.secondWaveColor.setColor(Color.parseColor("#2196F3"));
        this.handler = new Handler();
        this.handler.postDelayed(new WaveView.WaveRunnable(), 16L);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#BBDEFB"));
        this.quadrant = this.getHeight() / 3;
        this.width = canvas.getWidth();
        this.firstWavePath.moveTo(0.0F, (float)this.getHeight());
        this.secondWavePath.moveTo(0.0F, (float)this.getHeight());
        this.firstWavePath.lineTo(0.0F, (float)this.quadrant);
        this.secondWavePath.lineTo(0.0F, (float)(this.quadrant * 1.2));

        for(int i = 0; i < this.width + 10; i += 10) {
            this.x = (float)i;
            this.y1 = (float)this.quadrant + (float)this.amplitude * (float)Math.sin((double)(i + 10) * 3.141592653589793D / (double)this.frequency + (double)this.shift);
            this.y2 = (float)(this.quadrant ) + (float)this.amplitude * (float)Math.sin((double)(i + 10) * 3.141592653589793D / (double)this.frequency + (double)this.shift);
            this.firstWavePath.lineTo(this.x, this.y1);
            this.secondWavePath.lineTo(this.x, this.y2);
        }

        this.firstWavePath.lineTo((float)this.getWidth(), (float)this.getHeight());
        this.secondWavePath.lineTo((float)this.getWidth(), (float)this.getHeight());
        canvas.drawPath(this.firstWavePath, this.firstWaveColor);
        canvas.drawPath(this.secondWavePath, this.secondWaveColor);
    }

    public void setSpeed(float speed) {
        this.speed = speed / 8.0F;
        this.invalidate();
    }

    public void setAmplitude(int amplitude) {
        this.amplitude = amplitude * 20;
        this.invalidate();
    }

    private class WaveRunnable implements Runnable {
        private WaveRunnable() {
        }

        public void run() {
            WaveView.this.firstWavePath.reset();
            WaveView.this.secondWavePath.reset();
            WaveView.this.shift = WaveView.this.shift + WaveView.this.speed;
            WaveView.this.invalidate();
            WaveView.this.handler.postDelayed(WaveView.this.new WaveRunnable(), 16L);
        }
    }
}
*/
