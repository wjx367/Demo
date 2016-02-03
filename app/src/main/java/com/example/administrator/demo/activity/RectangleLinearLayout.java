package com.example.administrator.demo.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.administrator.demo.R;

/**
 * Created by Administrator on 2016/1/11.
 */
public class RectangleLinearLayout extends LinearLayout  {
    private  int cloumns;
    public RectangleLinearLayout(Context context) {
        super(context);
    }

    public RectangleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context,AttributeSet attrs){
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs, R.styleable.RectangleLinearLayout,0,0);

        cloumns=typedArray.getInteger(R.styleable.RectangleLinearLayout_cloumns,1);
    }
    public RectangleLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0,widthMeasureSpec),getDefaultSize(heightMeasureSpec,0));
        int childWidthSize = getMeasuredWidth();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = widthMeasureSpec/cloumns;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
