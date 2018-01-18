package com.msgl.bound.common;


import android.graphics.Canvas;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.msgl.bound.utils.Tools;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by msgl on 17-11-24.
 * 碰球
 */

public class Ball extends Substance {

    //默认速度距离
    private float mVel = Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 4);
    // 默认方向
    private double mDirection = 0.6;
    // 线程内部代码是否循环执行
    private boolean mThreadLoop = false;
    //并行服务
    private ExecutorService mExecutorService;
    //移动量
    private double mMoveX;
    private double mMoveY;

    //停走断定
    private boolean mStop = false;
    private boolean mStopFlag = false;
    private double mLMoveX;
    private double mLMoveY;

    // 子球对象
    private SonBall  mSonBall;

    // 构造球
    Ball() {
        super();
        mMoveX = mVel * Math.cos(mDirection);
        mMoveY = mVel * Math.sin(mDirection);
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    public void setStop(boolean stop) {
        mStop = stop;
    }

    // 移动球
    private void move(LinearLayout parentView) {
        while (mThreadLoop) {
            if(mStop && !mStopFlag){
                mLMoveX = mMoveX;
                mLMoveY = mMoveY;
                mMoveX = 0;
                mMoveY = 0;
                mStopFlag = true;
            }
            if(!mStop && mStopFlag){
                mMoveX = mLMoveX;
                mMoveY = mLMoveY;
                mStopFlag = false;
                Log.i("fkd", "move: " +"颉");
            }
            // 坐标增加,移动.
            setPositionX((int)(getPositionX() + mMoveX));
            setPositionY((int) (getPositionY() + mMoveY));

            //附加的画
            if(mSonBall != null){
                mSonBall.setSonXY(getPositionX(), getPositionY());
            }

            // 判断左面板是否相碰
            if(isCollide(Face.getInstance().getLeftPlaner())){
                Face.getInstance().mSoundPool.play(2, 1.0f, 1.0f, 1, 0, 1);
            }
            // 判断右机板是否相碰
            if(isCollide(Face.getInstance().getRightPlaner())){
                Face.getInstance().mSoundPool.play(2, 1.0f, 1.0f, 1, 0, 1);
            }

            // 判断小物件是否相碰
            ConcurrentSkipListSet<Substance> list = Face.getInstance().getSubstances();
            for (Substance s : list) {
                boolean bl = isCollide(s);
                boolean sbl = false;
                if(mSonBall != null){
                     sbl = mSonBall.isCollide(s);
                }
                if (bl || sbl) {
                    Face.getInstance().mSoundPool.play(1, 1.0f, 1.0f, 1, 0, 1);
                    list.remove(s);
                    Explode explode = new Explode(s.getPositionX(), s.getPositionY());
                    Face.getInstance().getExplodes().add(explode);
                    explode.exported();
                }
            }

            // 判断球是否在游戏区范围内
            isGameField(parentView);

            // 休眼50ms, 实际图形刷新40ms.
            try {
                Thread.sleep(36);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void startBallMove(final LinearLayout parentView) {
        mThreadLoop = true;
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                move(parentView);
            }
        });
    }

    void stopBallMove(){
        mThreadLoop = false;
    }


    // 判断小球游戏区内
    private void isGameField(LinearLayout parentView) {
        if (getPositionY() - getHeight() / 2 <= 0) {
            setPositionY(getHeight() / 2);
            mDirection = 2 * Math.PI - mDirection;
            mMoveX = mVel * Math.cos(mDirection);
            mMoveY = mVel * Math.sin(mDirection);
            Face.getInstance().mSoundPool.play(3, 1.0f, 1.0f, 1, 0, 1);
        }

        if (getPositionY() + getHeight() / 2 >= Face.getInstance().getWinHeight()) {
            setPositionY(Face.getInstance().getWinHeight() - getHeight() / 2);
            mDirection = 2 * Math.PI - mDirection;
            mMoveX = mVel * Math.cos(mDirection);
            mMoveY = mVel * Math.sin(mDirection);
            Face.getInstance().mSoundPool.play(3, 1.0f, 1.0f, 1, 0, 1);
        }

        if (getPositionX() + getWidth() <= 0 || getPositionX() - getWidth() >= Face.getInstance().getWinWidth()) {
            Face.getInstance().ballPosition(parentView);
            mThreadLoop = false;
        }
    }

    //判断物体是否相掸
    private boolean isCollide(Substance substance) {
        // Boall self lr tb;
        int sLX = getPositionX() - getWidth() / 2;
        int sRX = getPositionX() + getWidth() / 2;
        int sTY = getPositionY() - getHeight() / 2;
        int sBY = getPositionY() + getHeight() / 2;
        // 小物体四角坐标
        int LX = substance.getPositionX() - substance.getWidth() / 2;
        int RX = substance.getPositionX() + substance.getWidth() / 2;
        int TY = substance.getPositionY() - substance.getHeight() / 2;
        int BY = substance.getPositionY() + substance.getHeight() / 2;

        //    判断小球是否在物体内
        if ((sRX > LX && sLX < RX) && (sBY > TY && sTY < BY)) {
            //  物体中心到四角方位:
            double lta = azAngle(LX - substance.getPositionX(), TY - substance.getPositionY());
            double rta = azAngle(RX - substance.getPositionX(), TY - substance.getPositionY());
            double lba = azAngle(LX - substance.getPositionX(), BY - substance.getPositionY());
            double rba = azAngle(RX - substance.getPositionX(), BY - substance.getPositionY());
            // 物体中心到小球方位角
            double ltbsa = azAngle(getPositionX() - substance.getPositionX(), getPositionY() - substance.getPositionY());

            // 判断小球在哪个方向改变方向,归位到碰撞点
            if(ltbsa >= rba && ltbsa <= lba){
                mDirection = 2 * Math.PI - mDirection;
                setPositionY(BY + getHeight() / 2);
            }else if(ltbsa >= lba && ltbsa <= lta ){
                mDirection = Math.PI - mDirection;
                setPositionX(LX - getWidth() / 2);
            }else  if(ltbsa >= lta && ltbsa <= rta){
                mDirection = 2 * Math.PI - mDirection;
                setPositionY(TY - getHeight() / 2);
            }else{
                mDirection = Math.PI - mDirection;
                setPositionX(RX + getWidth() / 2);
            }

            mMoveX = mVel * Math.cos(mDirection);
            mMoveY = mVel * Math.sin(mDirection);
            return true;
        }
        return false;
    }

    // 求方位角
    private double azAngle(int lx, int ly) {
        if ((lx < 0 && ly > 0) || (lx < 0 && ly < 0)) {
            return Math.atan(ly / lx) + Math.PI;
        } else if (lx > 0 && ly < 0) {
            return Math.atan(ly / lx) + 2 * Math.PI;
        } else {
            return Math.atan(ly / lx);
        }
    }

    public void setSonBall(SonBall sonBall) {
        mSonBall = sonBall;
    }

    @Override
    public void drawSelf(Canvas canvas) {
        super.drawSelf(canvas);
        if(mSonBall != null){
            Substance[] substances = mSonBall.getSonSub();
            for(Substance s : substances){
                s.drawSelf(canvas);
            }
        }
    }

    public interface SonBall{
        Substance[] getSonSub();
        void setSonXY(int x, int y);
        boolean isCollide(Substance other);
    }
}
