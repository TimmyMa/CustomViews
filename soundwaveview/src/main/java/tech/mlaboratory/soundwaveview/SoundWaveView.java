package tech.mlaboratory.soundwaveview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by xiaoming on 2017/11/8.
 * Contact before modify.
 */

public class SoundWaveView extends View {

    Paint mPaint;
    Path mPath;

    // How many lines [1, +∞)
    int mLineNum;
    // Line color
    int mLineColor;
    // Line width
    float mLineWidth;
    // Offset between multiple lines [0, +∞)
    float mLineOffset;
    // How many sine waves (0, +∞)
    float mWaveNum;
    // Amplitude ratio in view [0, 1]
    float mWaveHeightRatio;
    // Wave speed (waves/sec) (0, +∞)
    float mWaveFloatSpeed;

    // Variable for animation
    float mWaveShiftRatio;
    // Variable for drawing sine waves
    float mWaveWidth;

    // Animation.
    private ObjectAnimator mWaveShiftAnimator;
    private AnimatorSet mAnimatorSet;

    public SoundWaveView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SoundWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SoundWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SoundWaveView, defStyleAttr, 0);

        mLineNum = attributes.getInteger(R.styleable.SoundWaveView_swv_line_num, 1);
        mLineColor = attributes.getColor(R.styleable.SoundWaveView_swv_line_color, Color.BLACK);
        mLineWidth = attributes.getDimension(R.styleable.SoundWaveView_swv_line_width, context.getResources().getDisplayMetrics().density * 2 + 0.5f);
        mLineOffset = attributes.getDimension(R.styleable.SoundWaveView_swv_line_offset, context.getResources().getDisplayMetrics().density * 5 + 0.5f);
        mWaveNum = attributes.getFloat(R.styleable.SoundWaveView_swv_wave_num, 1f);
        mWaveHeightRatio = Math.min(1f, attributes.getFloat(R.styleable.SoundWaveView_swv_wave_height_ratio, 1f));
        mWaveFloatSpeed = attributes.getFloat(R.styleable.SoundWaveView_swv_wave_float_speed, 1f);

        attributes.recycle();

        mPaint = new Paint();
        mPaint.setColor(mLineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();

        if (!isInEditMode()) {
            initAnimation();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWaveWidth = (int) (getMeasuredWidth() / mWaveNum);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mLineNum; i++) {
            calculatePathWithHorizontalOffset(i * mLineOffset);
            canvas.drawPath(mPath, mPaint);
        }
    }

    private void calculatePathWithHorizontalOffset(float offset) {
        mPath.reset();
        float amplitude = getMeasuredHeight() * 0.50f;

        mPath.moveTo(0, amplitude - (float) (amplitude * mWaveHeightRatio * Math.sin(mWaveShiftRatio * 2 * Math.PI + offset / mWaveWidth * 2 * Math.PI)));
        for (int i = 0; i < getMeasuredWidth() + 50; i += 50) {
            mPath.lineTo(i, amplitude - (float) (amplitude * mWaveHeightRatio * Math.sin(2d * Math.PI / mWaveWidth * i + mWaveShiftRatio * 2 * Math.PI + offset / mWaveWidth * 2 * Math.PI)));
        }
    }


    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    public void setWaveShiftRatio(float waveShiftRatio) {
        this.mWaveShiftRatio = waveShiftRatio;
    }

    private void initAnimation() {
        mWaveShiftAnimator = ObjectAnimator.ofFloat(this, "waveShiftRatio", 0f, 1f);
        mWaveShiftAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mWaveShiftAnimator.setDuration((long) (1000f / mWaveFloatSpeed));
        mWaveShiftAnimator.setInterpolator(new LinearInterpolator());
        mWaveShiftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(mWaveShiftAnimator);

    }

    public void startAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    public void endAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }

    public void cancelAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        startAnimation();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelAnimation();
        super.onDetachedFromWindow();
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        this.mLineColor = lineColor;
        mPaint.setColor(this.mLineColor);
        invalidate();
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.mLineWidth = lineWidth;
        mPaint.setStrokeWidth(this.mLineWidth);
        invalidate();
    }

    public float getWaveNum() {
        return mWaveNum;
    }

    public void setWaveNum(float waveNum) {
        this.mWaveNum = waveNum;
        mWaveWidth = getMeasuredWidth() / this.mWaveNum;
        invalidate();
    }

    public float getWaveHeightRatio() {
        return mWaveHeightRatio;
    }

    public void setWaveHeightRatio(float waveHeightRatio) {
        this.mWaveHeightRatio = waveHeightRatio;
        invalidate();
    }

    public float getWaveFloatSpeed() {
        return mWaveFloatSpeed;
    }

    public void setWaveFloatSpeed(float waveFloatSpeed) {
        this.mWaveFloatSpeed = waveFloatSpeed;
        mWaveShiftAnimator.setDuration((long) (1000f / waveFloatSpeed));
        invalidate();
    }

    public float getLineOffset() {
        return mLineOffset;
    }

    public void setLineOffset(float lineOffset) {
        this.mLineOffset = lineOffset;
        invalidate();
    }

    public int getLineNum() {
        return mLineNum;
    }

    public void setLineNum(int lineNum) {
        this.mLineNum = lineNum;
        invalidate();
    }
}
