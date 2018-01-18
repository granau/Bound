package com.msgl.bound.common;

import android.graphics.Canvas;
import android.util.Log;
import android.util.TypedValue;

import com.msgl.bound.utils.Tools;

/**
 * Created by msgl on 17-11-21.
 * 左面板
 */

public class Planer extends Substance {
    //定义左右面板常量
    static final int LEFT_PLANER = 1;
    static final int RIGHT_PLANER = 2;
    // 面板标识
    private final int mLRPlaner;

    // 游戏停止标识
    private boolean mStopFlag = true;

    Planer(int lrPlaner) {
        super();
        mLRPlaner = lrPlaner;
        setWidth((int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 16));
        setHeight((int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 56));
        setPositionX(getWidth() / 2);
        setPositionY(getHeight() / 2);
    }

    @Override
    public void drawSelf(Canvas canvas) {
        if (mStopFlag) {
            setPositionY(Face.getInstance().getWinHeight() / 2);
            if (mLRPlaner == RIGHT_PLANER) {
                setPositionX(Face.getInstance().getWinWidth() - getWidth() / 2);
            } else if (mLRPlaner == LEFT_PLANER) {
                Log.i("左面版", "drawSelf: " + "不需要设置什么的");
            }
            mStopFlag = false;
        }
        super.drawSelf(canvas);
    }

    // 游戏停止标识
    void setStopFlag() {
        mStopFlag = true;
    }

}
