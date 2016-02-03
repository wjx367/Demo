package com.example.administrator.demo.entry;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.demo.R;

/**
 * Created by Administrator on 2015/12/4.
 */
public class DrawCircle extends View {
    private Paint mPaint;
    private int CircleColor;
    private float CircleRadius;
    private int CircleY;
    private  int CircleX;
    // 年利率
    private double mpercent;
    public DrawCircle (Context context){
        super(context,null);
    }
    public DrawCircle(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint=new Paint();
        mPaint.setColor(CircleColor);
        mPaint.setStyle(Paint.Style.FILL);
        CircleX =  getWidth()/2;
        CircleY =  getHeight()/2;
        canvas.drawCircle(CircleX, CircleY, CircleRadius, mPaint);

    }

    private void init(Context context,AttributeSet attrs){
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.DrawCircle);
        CircleColor=a.getColor(R.styleable.DrawCircle_CircleColor,Color.BLUE);
        CircleRadius=a.getDimension(R.styleable.DrawCircle_CircleRadius, 100);
        CircleX = a.getInt(R.styleable.DrawCircle_CircleX, 50);
        CircleY = a.getInt( R.styleable.DrawCircle_CircleY,50);
//        int n=a.getIndexCount();
//        for(int i=0;i<n;i++){
//            int attr = a.getIndex(i);
//            switch (attr){
//                case R.styleable.DrawCircle_CircleColor:
//                    CircleColor=a.getColor(attr, Color.BLUE);
//                    break;
//                case R.styleable.DrawCircle_CircleRadius:
//                    CircleRadius=a.getDimension(attr,100);
//                    break;
//                case  R.styleable.DrawCircle_CircleX:
//                    CircleX=a.getInt(attr,50);
//                    break;
//                case  R.styleable.DrawCircle_CircleY:
//                    CircleY=a.getInt(attr,50);
//                    break;
//            }
//
//        }
        a.recycle();

    }

}
