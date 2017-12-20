package tech.mlaboratory.subscribeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by xiaoming on 2017/12/19.
 * Don't contact before modify.
 */

public class SubscribeView extends View {

    private static final int COLOR_YELLOW = 0xffffc81f;
    private static final int COLOR_TRANSPARENT = 0x00000000;
    private static final int COLOR_DARK_GREY = 0xffd2d2d2;
    private static final int COLOR_LIGHT_GREY = 0xffcccccc;
    private static final int COLOR_LIGHT_BLACK = 0xff333333;
    private static final int COLOR_WHITE = 0xffffffff;

    private static final int GREY_MULTIPLIER = 0xcccccc;
    private static final int BLACK_MULTIPLIER = 0x333333;
    public static final int NO_MULTIPLIER = 0xffffff;

    @Retention(SOURCE)
    @IntDef({STATUS_NOT_SUBSCRIBED, STATUS_SUBSCRIBING, STATUS_SUBSCRIBED})
    @interface SubscribeStatus {}
    public static final int STATUS_NOT_SUBSCRIBED = 0; // 加号状态
    public static final int STATUS_SUBSCRIBING = 1; // 菊花状态
    public static final int STATUS_SUBSCRIBED = 2; // 对号状态
    private @SubscribeStatus int status = -1;

    @Retention(SOURCE)
    @IntDef({SCENE_ACTION_BAR, SCENE_HOME, SCENE_COLUMN})
    @interface SubscribeScene {}
    public static final int SCENE_ACTION_BAR = 0; // 用于列表的场景，有背景、无边框
    public static final int SCENE_HOME = 1; // 用于列表的场景，有背景、无边框
    public static final int SCENE_COLUMN = 2; // 用于栏目中心的场景，有背景、有边框
    private @SubscribeScene int scene = -1;

    private Bitmap bitmapAdd; // 加号
    private Bitmap bitmapCheck; // 对号
    private Bitmap bitmapLoad; // 菊花
    private Rect rectDraw; // drawBitmap时的矩形区域

    private Paint paintBackground; // 圆形背景画笔
    private Paint paintStroke; // 圆环边框画笔
    private Paint paintShape; // bitmap画笔

    private int rotateDegree; // 使用canvas.rotate()实现旋转动画，此为canvas当前的旋转角度
    private float strokeWidth; // 描边宽度

    private boolean isNightMode = false;

    public SubscribeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SubscribeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SubscribeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public SubscribeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SubscribeView, defStyleAttr, 0);
        int status = attributes.getInt(R.styleable.SubscribeView_sv_status, STATUS_NOT_SUBSCRIBED);
        int scene = attributes.getInt(R.styleable.SubscribeView_sv_scene, SCENE_COLUMN);
        attributes.recycle();

        paintBackground = new Paint();
        paintBackground.setAntiAlias(true);

        paintStroke = new Paint();
        paintStroke.setAntiAlias(true);
        paintStroke.setStyle(Paint.Style.STROKE);

        paintShape = new Paint();

        bitmapAdd = BitmapFactory.decodeResource(getResources(), R.drawable.follow);
        bitmapCheck = BitmapFactory.decodeResource(getResources(), R.drawable.followed);
        bitmapLoad = BitmapFactory.decodeResource(getResources(), R.drawable.icon_loading_2);
        rectDraw = new Rect();

        setStatus(status, false);
        setScene(scene);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 描边宽度预设为宽度的1/24
        setStrokeWidth((getMeasuredWidth() + getMeasuredHeight()) / 2f / 24f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画背景圆
        canvas.drawCircle(this.getWidth() / 2f,
                this.getHeight() / 2f,
                (this.getWidth() + this.getHeight()) / 4f - this.strokeWidth,
                paintBackground);

        if (strokeWidth > 0) {
            // 画描边
            canvas.drawCircle(this.getWidth() / 2f,
                    this.getHeight() / 2f,
                    (this.getWidth() + this.getHeight()) / 4f - this.strokeWidth / 2,
                    paintStroke);
        }

        // 绘制图形的区域为整个view长宽的1/3正中央
        rectDraw.set((int) (getWidth() / 3f), (int) (getHeight() / 3f), (int) (getWidth() / 3f * 2f), (int) (getHeight() / 3f * 2f));

        canvas.save();
        canvas.rotate(rotateDegree, canvas.getWidth() / 2f, canvas.getHeight() / 2f);
        switch (status) {
            case STATUS_NOT_SUBSCRIBED:
                canvas.drawBitmap(bitmapAdd, null, rectDraw, paintShape);
                canvas.restore();

                if (rotateDegree != 0) {
                    rotateDegree = (rotateDegree + 15) % 360; // 以15度的速度旋转至角度0
                    invalidate();
                }
                break;
            case STATUS_SUBSCRIBING:
                canvas.drawBitmap(bitmapLoad, null, rectDraw, paintShape);
                canvas.restore();

                rotateDegree = (rotateDegree + 30) % 360; // 菊花每次转动角度为30度（360度/12瓣）
                postInvalidateDelayed(80); // 每帧间隔为80ms
                break;
            case STATUS_SUBSCRIBED:
                canvas.drawBitmap(bitmapCheck, null, rectDraw, paintShape);
                canvas.restore();

                if (rotateDegree != 0) {
                    rotateDegree = (rotateDegree + 15) % 360; // 以15度的速度旋转至角度0
                    invalidate();
                }
                break;
        }
    }

    public void setStatus(@SubscribeStatus int status, boolean animate) {
        if (this.status != status) {
            this.status = status;
            switch (status) {
                case STATUS_NOT_SUBSCRIBED:
                    rotateDegree = 270; // 从-90度开始旋转至0度的动画
                    break;
                case STATUS_SUBSCRIBING:
                    rotateDegree = 0; // 菊花从0度还是旋转
                    break;
                case STATUS_SUBSCRIBED:
                    rotateDegree = 270; // 从-90度开始旋转至0度的动画
                    break;
            }
            dealWithPaintColor();
            if (!animate) // 取消除菊花以外的动画？
                rotateDegree = 0;

            invalidate();
        }
    }

    public void setScene(@SubscribeScene int scene) {
        if (this.scene != scene) {
            this.scene = scene;
            switch (scene) {
                case SCENE_ACTION_BAR:
                    // 描边宽度预设为宽度的1/24
                    setStrokeWidth((getWidth() + getHeight()) / 2f / 24f);
                    break;
                case SCENE_HOME:
                    // 消除描边
                    setStrokeWidth(0);
                    break;
                case SCENE_COLUMN:
                    // 描边宽度预设为宽度的1/24
                    setStrokeWidth((getWidth() + getHeight()) / 2f / 24f);
                    break;
            }
            dealWithPaintColor();

            // STATUS_SUBSCRIBING状态下直接等菊花的postInvalidateDelayed就行了
            if (this.status != STATUS_SUBSCRIBING)
                invalidate();
        }
    }

    public void setNightMode(boolean isNightMode) {
        if (this.isNightMode != isNightMode) {
            this.isNightMode = isNightMode;
            dealWithPaintColor();

            // STATUS_SUBSCRIBING状态下直接等菊花的postInvalidateDelayed就行了
            if (this.status != STATUS_SUBSCRIBING)
                invalidate();
        }
    }

    private void dealWithPaintColor() {
        // 背景色
        if (this.scene == SCENE_ACTION_BAR) { //
            this.paintBackground.setColor(COLOR_TRANSPARENT); // 透明背景
        } else if (this.status == STATUS_NOT_SUBSCRIBED) {
            this.paintBackground.setColor(COLOR_YELLOW); // 黄色背景
        } else {
            this.paintBackground.setColor(COLOR_DARK_GREY); // 灰色背景
        }

        // 描边色
        if (this.scene == SCENE_ACTION_BAR) {
            if (isNightMode) {
                paintStroke.setColor(COLOR_LIGHT_GREY); // 灰色描边
            } else {
                paintStroke.setColor(COLOR_LIGHT_BLACK); // 浅黑色描边
            }
        } else {
            paintStroke.setColor(COLOR_WHITE); // 描边固定白色
        }

        // 图标颜色
        if (this.scene == SCENE_ACTION_BAR && this.status != STATUS_SUBSCRIBING) { // 菊花不进行变色、其他两个图标出事颜色都为白色
            if (isNightMode) {
                paintShape.setColorFilter(new LightingColorFilter(GREY_MULTIPLIER, 0)); //白色变灰色
            } else {
                paintShape.setColorFilter(new LightingColorFilter(BLACK_MULTIPLIER, 0)); //白色变浅黑色
            }
        } else {
            paintShape.setColorFilter(new LightingColorFilter(NO_MULTIPLIER, 0)); // 原色（白色）
        }
    }

    private void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        if (strokeWidth > 0) {
            this.paintStroke.setStrokeWidth(strokeWidth);
        }
    }
}
