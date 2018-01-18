package com.msgl.bound.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import com.msgl.bound.utils.Tools;

/**
 * Created by msgl on 17-11-16.
 * 所有物体的abstract 类
 */
public class Substance implements Comparable<Substance> {

    // 实现Comparable 接口为ConcurrentSkipListSet集合调用
    @Override
    public int compareTo(@NonNull Substance o) {
        if (hashCode() > o.hashCode()) {
            return 1;
        } else if (hashCode() < o.hashCode()) {
            return -1;
        } else {
            return 0;
        }
    }

    /*todo: 定义物体的状态,此物体包含的状态,根据状态产生一个带功能物件,如面板得到这物件,将得到这个功能,
      todo: 如产生多子球,或收放子球等.
     */
    public enum BallType {
        FLAG_G, FLAG_M, FLAG_S, FLAG_L, FLAG_W;
    }

    // 游戏是否开始标识
   // private boolean mStartFlag = false;
    // 物件位置
    // todo: release版本加volatile
    private int mPositionX; // 物件中心x
    private int  mPositionY; // 物件中心y
    // 物件宽高
    private int mWidth;
    private int mHeight;
    // 设置是否有可画图形
    private boolean mCircle = false; // 是否画园
    private Bitmap mBitmap;
    private Drawable mDrawable;
    //设置默认色
    private int mColor = Color.BLUE;


    public Substance() {
        mWidth = (int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 24);
        mHeight = (int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 24);
        mPositionX = mWidth / 2;
        mPositionY = mHeight / 2;
    }

    public Substance(int positionX, int positionY) {
        this();
        mPositionX = positionX;
        mPositionY = positionY;
    }

    public Substance(int positionX, int positionY, int width, int height) {
        this();
        mWidth = width;
        mHeight = height;
        mPositionX = positionX;
        mPositionY = positionY;
    }


    public int getPositionX() {
        return mPositionX;
    }

    public void setPositionX(int positionX) {
        mPositionX = positionX;
    }

    public int getPositionY() {
        return mPositionY;
    }

    public void setPositionY(int positionY) {
        mPositionY = positionY;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }


    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public void setCircle(boolean circle) {
        mCircle = circle;
    }

    /**
     * @param canvas 画布
     */
    public void drawSelf(Canvas canvas) {
        // 设置小方块画的大小及位置
        Rect rect = new Rect(mPositionX - mWidth / 2, mPositionY - mHeight / 2, mPositionX + mWidth / 2, mPositionY + mHeight / 2);

        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, null, rect, null);
        } else if (mDrawable != null) {
            mDrawable.setBounds(rect);
            mDrawable.draw(canvas);
        } else if(mCircle){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mColor);
            canvas.drawCircle(mPositionX, mPositionY, (mWidth + mHeight) / 2, paint);
        }
        else {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mColor);
            canvas.drawRect(rect, paint);
        }
    }
}
