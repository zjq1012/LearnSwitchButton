package com.example.dorado.learnswitchbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;

import static com.example.dorado.learnswitchbutton.DensityUtils.*;

/**
 * Created by dorado on 2017/12/8.
 */

public class SwitchButton extends View implements Checkable {

    private final int DEFAULT_WIDTH = dp2px(getContext(), 64);
    private final int DEFAULT_HEIGHT = dp2px(getContext(), 36);

    /** 是否选中 **/
    private boolean isChecked;
    /** Context **/
    private Context context;

    /** 背景画笔 **/
    private Paint bgPaint;
    /** 圆形按钮画笔 **/
    private Paint circleBtnPaint;
    /** 是否开启阴影 **/
    private boolean enableShadowEffet;
    /** 总宽度 高度 **/
    private int width, height;
    /** 边框宽度px **/
    private int borderWidth = dp2px(getContext(), 1);
    /** 未选中背景颜色 **/
    private int unCheckedBgColor = ContextCompat.getColor(getContext(), R.color.unCheckedBgColor);
    /** 选中背景颜色 **/
    private int checkedBgColor;
    /** 背景颜色 **/
    private int bgColor = ContextCompat.getColor(getContext(), android.R.color.white);
    /** 背景圆角Radius **/
    private int bgCornerRadius;
    /** 阴影半径 **/
    private int shadowRadius;
    /** View绘制范围 **/
    private int left, right, top, bottom;
    /** 绘制中心 **/
    private int centerX, centerY;

    /** 是否绘制指示器 **/
    private boolean hasIndicator = true;
    /** 圆形未选中状态指示器颜色 **/
    private int indicatorUnCheckedColor = ContextCompat.getColor(getContext(),
        R.color.indicatorUnCheckedColor);
    /** 圆形未选中状态指示器x轴偏移 **/
    private int indicatorUnCheckedOffsetX = dp2px(getContext(), 10);
    /** 圆形未选中状态指示器半径 **/
    private int indicatorUnCheckedRadius = dp2px(getContext(), 4);
    /** 圆形未选中状态指示器线宽 **/
    private int indicatorUncheckedLineWidth = dp2px(getContext(), 1);

    /** 线型选中状态指示器颜色 **/
    private int indicatorCheckedColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
    /** 线型选中状态指示器宽度 **/
    private int indicatorCheckedWidth = dp2px(getContext(), 1);
    /** 线型选中状态指示器长度 **/
    private int indicatorCheckedHeight = dp2px(getContext(), 6);
    /** 线型选中状态指示器x轴偏移 **/
    private int indicatorCheckedOffsetX = dp2px(getContext(), 10);
    /** 圆形按钮x轴范围 **/
    private int circleBtnMinx, circleBtnMaxX;
    /** 圆形按钮半径 **/
    private int circleBtnRadius;
    /** 圆形按钮颜色 **/
    private int circleBtnColor = ContextCompat.getColor(getContext(), android.R.color.white);
    /** 圆形按钮边框颜色 **/
    private int circleBtnBorderColor = ContextCompat.getColor(getContext(),
        R.color.circleBtnBorderColor);
    /** 圆形按钮边框宽度 **/
    private int circleBtnBorderWidth = 1;
    /** 当前状态 **/
    private ViewState currentState;

    /** 是否按下 **/
    private boolean isTouchDown;
    /** 上次按下时间 **/
    private long lastTouchDownTime;


    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);

    }


    public SwitchButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        // @formatter:off
        // Attrs
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        enableShadowEffet = attributes.getBoolean(R.styleable.SwitchButton_enableShadowEffect, false);
        borderWidth = attributes.getDimensionPixelOffset(R.styleable.SwitchButton_borderWidth, borderWidth);
        unCheckedBgColor = attributes.getColor(R.styleable.SwitchButton_unCheckedBgColor, unCheckedBgColor);
        checkedBgColor = attributes.getColor(R.styleable.SwitchButton_checkedBgColor, checkedBgColor);
        bgColor = attributes.getColor(R.styleable.SwitchButton_bgColor, bgColor);
        hasIndicator = attributes.getBoolean(R.styleable.SwitchButton_hasIndicator, false);
        indicatorUnCheckedColor = attributes.getColor(R.styleable.SwitchButton_indicatorUnCheckedColor, indicatorUnCheckedColor);
        indicatorUnCheckedOffsetX = attributes.getDimensionPixelOffset(R.styleable.SwitchButton_indicatorUnCheckedOffsetX, indicatorUnCheckedOffsetX);
        indicatorUnCheckedRadius = attributes.getDimensionPixelOffset(R.styleable.SwitchButton_indicatorUnCheckedRadius, indicatorUnCheckedRadius);
        indicatorUncheckedLineWidth = attributes.getDimensionPixelOffset(R.styleable.SwitchButton_indicatorUnCheckedLineWidth, indicatorUncheckedLineWidth);
        indicatorCheckedColor = attributes.getColor(R.styleable.SwitchButton_indicatorCheckedColor,indicatorCheckedColor);
        indicatorCheckedWidth = attributes.getDimensionPixelOffset(R.styleable.SwitchButton_indicatorCheckedWidth,indicatorCheckedWidth);
        indicatorCheckedHeight = attributes.getDimensionPixelOffset(R.styleable.SwitchButton_indicatorCheckedHeight,indicatorCheckedHeight);
        indicatorCheckedOffsetX = attributes.getDimensionPixelOffset(R.styleable.SwitchButton_indicatorCheckedOffsetX,indicatorCheckedOffsetX);
        circleBtnColor = attributes.getColor(R.styleable.SwitchButton_circleBtnColor,circleBtnColor);
        circleBtnBorderColor = attributes.getColor(R.styleable.SwitchButton_circleBtnBorderColor,circleBtnBorderColor);
        circleBtnBorderWidth = attributes.getDimensionPixelOffset(circleBtnBorderWidth,circleBtnBorderWidth);
        attributes.recycle();
        // Other
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);

        circleBtnPaint = new Paint();
        circleBtnPaint.setAntiAlias(true);
        circleBtnPaint.setStyle(Paint.Style.FILL);
        circleBtnPaint.setColor(circleBtnColor);

        currentState = new ViewState();
    }


    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_WIDTH, MeasureSpec.EXACTLY);
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_HEIGHT,MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int padding = Math.max(shadowRadius, borderWidth);
        height = h - 2 * padding;
        width = w - 2 * padding;

        left = padding;
        right = w - padding;
        top = padding;
        bottom = h - padding;

        bgCornerRadius = height / 2;

        circleBtnRadius = bgCornerRadius - borderWidth;

        centerX = (left + right) / 2;
        centerY = (top + bottom) / 2;

        circleBtnMinx = left + bgCornerRadius;
        circleBtnMaxX = right - bgCornerRadius;
        if (isChecked()) {
            setCheckedViewState(currentState);
        } else {
            setUnCheckedViewState(currentState);
        }
    }


    private void setUnCheckedViewState(ViewState viewState) {
        viewState.radius = 0;
        viewState.checkStateColor = unCheckedBgColor;
        viewState.indicatorCheckedColor = Color.TRANSPARENT;
        viewState.buttonCenterX = circleBtnMaxX;
    }


    private void setCheckedViewState(ViewState viewState) {
        viewState.radius = bgCornerRadius;
        viewState.checkStateColor = checkedBgColor;
        viewState.indicatorCheckedColor = indicatorCheckedColor;
        viewState.buttonCenterX = circleBtnMinx;
    }


    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStrokeWidth(borderWidth);
        canvas.drawRoundRect(new RectF(left, top, right, bottom), bgCornerRadius, bgCornerRadius, bgPaint);
        // 绘制未选中的边框
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(unCheckedBgColor);
        canvas.drawRoundRect(new RectF(left, top, right, bottom), bgCornerRadius, bgCornerRadius, bgPaint);
        // 绘制未选中指示器
        if (hasIndicator) {
            bgPaint.setColor(indicatorUnCheckedColor);
            bgPaint.setStrokeWidth(indicatorUncheckedLineWidth);
            canvas.drawCircle(right - indicatorUnCheckedOffsetX, centerY, indicatorUnCheckedRadius, bgPaint);
        }

        // 绘制开启背景色
        // [0-backgroundRadius*0.5f]
        float des = currentState.radius * 0.5f;
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(currentState.checkStateColor);
        bgPaint.setStrokeWidth(borderWidth + des * 2f);
        canvas.drawRoundRect(new RectF(left + des, top + des, right - des, bottom - des), bgCornerRadius, bgCornerRadius, bgPaint);
        // 绘制选中指示器
        if (hasIndicator) {
            bgPaint.setColor(indicatorCheckedColor);
            bgPaint.setStrokeWidth(indicatorCheckedWidth);
            bgPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawLine(left + indicatorCheckedOffsetX,centerY-indicatorCheckedHeight/2,left+indicatorCheckedOffsetX,centerY + indicatorCheckedHeight/2,bgPaint);
        }
        // 绘制圆形按钮
        canvas.drawCircle(currentState.buttonCenterX,centerY,circleBtnRadius,circleBtnPaint);
        circleBtnPaint.setStrokeWidth(circleBtnBorderWidth);
        circleBtnPaint.setColor(circleBtnBorderColor);
        circleBtnPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(currentState.buttonCenterX,centerY,circleBtnRadius,circleBtnPaint);
    }


    // @formatter:on
    @Override public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouchDown = true;
                lastTouchDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float fraction = x / getWidth();
                fraction = Math.max(0f, Math.min(1f, fraction));
                currentState.buttonCenterX = circleBtnMinx + (circleBtnMaxX - circleBtnMaxX) * fraction;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }


    /**
     * Checkable实现
     */
    @Override public void setChecked(boolean checked) {
        if (!isChecked == checked) {
            isChecked = checked;
            refreshDrawableState();
        }
    }


    @Override public boolean isChecked() {
        return isChecked;
    }


    @Override public void toggle() {
        setChecked(!isChecked);
    }


    class ViewState {
        /** 按钮x位置[buttonMinX-buttonMaxX] **/
        float buttonCenterX;
        /** 状态背景颜色 **/
        int checkStateColor;
        /** 选中线的颜色 **/
        int indicatorCheckedColor;
        /** 状态背景的半径 **/
        float radius;


        private void copy(ViewState source) {
            this.buttonCenterX = source.buttonCenterX;
            this.checkStateColor = source.checkStateColor;
            this.indicatorCheckedColor = source.indicatorCheckedColor;
            this.radius = source.radius;
        }
    }

}
