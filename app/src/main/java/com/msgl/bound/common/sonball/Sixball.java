package com.msgl.bound.common.sonball;

import android.graphics.Color;
import android.util.TypedValue;

import com.msgl.bound.common.Substance;
import com.msgl.bound.common.Ball;
import com.msgl.bound.utils.Tools;

/**
 * Created by msgl on 17-12-3.
 * 装主球的东东
 */

public class Sixball  implements Ball.SonBall {

    //球的色
    private static final int[] mColor = new int[]{Color.RED, Color.GREEN, Color.YELLOW, Color.WHITE, Color.BLUE, Color.MAGENTA};
    // 子球到主球的距离
    private int mD = (int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 20);
    //子球数给
    private Substance[] mSubstances;
    //产生多少个子球,最多6个
    private int mBalls;
    // 主球到子球的方位
    private double mR;
    // 方位增量
    private double mAddR = 0;
    // 当方位小于批是数为真
    private boolean mRFlag = true;

    // 小球转速
    private double mCirVal = 0.4;

    public Sixball(int balls){
        if(balls > 6){
            balls = 6; // 最多六个子球
        }
        mBalls = balls;
        mR = 2 * Math.PI / mBalls;
        // 珪的大小,用宽高表示,取宽高和的一半为球的半径
        int wh = (int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 4);
        //构建子球数组
        mSubstances = new Substance[mBalls];
        for(int i = 0; i < mBalls; i++){
            Substance substance = new Substance(0, 0, wh, wh);
            substance.setColor(mColor[i]);
            substance.setCircle(true);
            mSubstances[i] = substance;
        }
    }

    public double getCirVal() {
        return mCirVal;
    }

    public void setCirVal(double cirVal) {
        mCirVal = cirVal;
    }

    public int getD() {
        return mD;
    }

    public void setD(int d) {
        mD = d;
    }

    @Override
    public Substance[] getSonSub() {
        return mSubstances;
    }

    @Override
    public void setSonXY(final int x, final int y) {
        for(int i = 0; i < mBalls; i++){
            mSubstances[i].setPositionX(x + (int)(Math.cos(mR * i + mAddR) * mD));
            mSubstances[i].setPositionY(y + (int)(Math.sin(mR * i + mAddR) * mD));
        }
        mAddR += mCirVal;
        //if(mRFlag){
        //    mAddR += mCirVal;
        //    if( mAddR > 1000){
        //        mRFlag = false;
        //    }
        //}else {
        //    mAddR -= mCirVal;
        //    if(mAddR < 0){
        //        mRFlag = true;
        //    }
        //}
    }

    @Override
    public boolean isCollide(Substance other) {
        for(Substance s : mSubstances){
            int dx = s.getPositionX() - other.getPositionX();
            int dy = s.getPositionY() - other.getPositionY();
            int sowh = (s.getWidth() + s.getHeight()) / 2 + (other.getWidth() + other.getHeight()) / 2;
            if(dx * dx + dy * dy <= sowh * sowh){
                return true;
            }
        }
        return false;
    }
}
