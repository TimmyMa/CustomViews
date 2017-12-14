package com.wanplus.scrollingimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import static java.lang.Math.abs;

/**
 * Created by bfour on 2016/6/7.
 * Contact before modify
 */
public class ScrollingImageView extends View {

    public static int HORIZONTAL = 0;
    public static int VERTICAL = 1;

    private Bitmap bitmap;
    private float speed;
    private int orientation;


    private Matrix matrix = new Matrix();
    private Rect clipBounds = new Rect();
    private float offset = 0;
    private float scaleRatio;
    private boolean isStarted;

    public ScrollingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollingImageView, 0, 0);
        int initialState = 0;
        try {
            initialState = ta.getInt(R.styleable.ScrollingImageView_siv_initialState, 0);
            speed = ta.getDimension(R.styleable.ScrollingImageView_siv_speed, 10);
            orientation = ta.getInt(R.styleable.ScrollingImageView_siv_orientation, 0);
            bitmap = BitmapFactory.decodeResource(context.getResources(), ta.getResourceId(R.styleable.ScrollingImageView_siv_src, 0));
        } finally {
            ta.recycle();
        }

        if (initialState == 0) {
            start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (orientation == HORIZONTAL) {
            scaleRatio = (float) getMeasuredHeight() / bitmap.getHeight();
        } else if (orientation == VERTICAL){
            scaleRatio = (float) getMeasuredWidth() / bitmap.getWidth();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (canvas == null || bitmap == null) {
            return;
        }

        canvas.getClipBounds(clipBounds);

        if (orientation == HORIZONTAL) {
            while (offset <= -bitmap.getWidth() * scaleRatio) {
                offset += bitmap.getWidth() * scaleRatio;
            }
        } else if (orientation == VERTICAL) {
            while (offset <= -bitmap.getHeight() * scaleRatio) {
                offset += bitmap.getHeight() * scaleRatio;
            }
        }

        float off = offset;
        if (orientation == HORIZONTAL) {
            while (off < clipBounds.width()) {
                int width = (int) (bitmap.getWidth() * scaleRatio);
                matrix.setTranslate(getBitmapLeft(width, off), 0);
                matrix.preScale(scaleRatio, scaleRatio);
                canvas.drawBitmap(bitmap, matrix, null);
                off += width;
            }
        } else if (orientation == VERTICAL) {
            while (off < clipBounds.height()) {
                int height = (int) (bitmap.getHeight() * scaleRatio);
                matrix.setTranslate(0, getBitmapTop(height, off));
                matrix.preScale(scaleRatio, scaleRatio);
                canvas.drawBitmap(bitmap, matrix, null);
                off += height;
            }
        }

        if (!isInEditMode() && isStarted && speed != 0) {
            offset -= abs(speed);
            postInvalidateOnAnimation();
        }
    }

    private float getBitmapLeft(float layerWidth, float left) {
        if (speed < 0) {
            return clipBounds.width() - layerWidth - left;
        } else {
            return left;
        }
    }

    private float getBitmapTop(float layerHeight, float top) {
        if (speed < 0) {
            return clipBounds.height() - layerHeight - top;
        } else {
            return top;
        }
    }

    public void start() {
        if (!isStarted) {
            isStarted = true;
            postInvalidateOnAnimation();
        }
    }

    public void stop() {
        if (isStarted) {
            isStarted = false;
            postInvalidateOnAnimation();
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        if (isStarted) {
            postInvalidateOnAnimation();
        }
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        postInvalidateOnAnimation();
    }
}