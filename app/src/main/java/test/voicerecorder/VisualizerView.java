package test.voicerecorder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class VisualizerView extends View {
    private static final int LINE_WIDTH = 50; // width of visualizer lines
    private static final int LINE_SCALE = 75; // scales visualizer lines
    private float amplitudes; // amplitudes for line lengths
    private int width; // width of this View
    private int height; // height of this View
    private Paint linePaint; // specifies line drawing characteristics
    private int count;

    // constructor
    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs); // call superclass constructor
        linePaint = new Paint(); // create Paint for lines
        linePaint.setColor(Color.GREEN); // set color to green
        linePaint.setStrokeWidth(LINE_WIDTH); // set stroke width
    }

    // called when the dimensions of the View change
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w; // new width of this View
        height = h; // new height of this View
        count = (w / LINE_WIDTH) / 2;
    }

    // clear all amplitudes to prepare for a new visualization
    public void clear() {
        amplitudes = 0;
    }

    // add the given amplitude to the amplitudes ArrayList
    public void addAmplitude(float amplitude) {
        amplitudes = amplitude; // add newest to the amplitudes ArrayList

        // if the power lines completely fill the VisualizerView
        /*if (amplitudes.size() * LINE_WIDTH >= width) {
            amplitudes.remove(0); // remove oldest power value
        }*/
    }

    // draw the visualizer with scaled lines representing the amplitudes
    @Override
    public void onDraw(Canvas canvas) {
        int middle = height / 2; // get the middle of the View
        float curX = 0; // start curX at zero

        // for each item in the amplitudes ArrayList
        for (int i = 1; i < 12; i++) {
            float scaledHeight = amplitudes * (i * 0.5f); // scale the power
            curX += LINE_WIDTH; // increase X by LINE_WIDTH

            // draw a line representing this item in the amplitudes ArrayList
            canvas.drawLine(curX, middle + scaledHeight / 2, curX, middle
                    - scaledHeight / 2, linePaint);
        }

        for (int i =  10; i > 0; i--) {
            float scaledHeight = amplitudes * (i * 0.5f); // scale the power
            curX += LINE_WIDTH; // increase X by LINE_WIDTH

            // draw a line representing this item in the amplitudes ArrayList
            canvas.drawLine(curX, middle + scaledHeight / 2, curX, middle
                    - scaledHeight / 2, linePaint);
        }
    }

}