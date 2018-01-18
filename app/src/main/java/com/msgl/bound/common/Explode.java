package com.msgl.bound.common;

import android.graphics.Color;
import android.util.TypedValue;

import com.msgl.bound.utils.Tools;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by msgl on 17-11-29.
 * 爆炸物
 */

class Explode {

    //产生多少圈
    private static final int mCircle = 4;
    //产生多少个爆炸点
    private int mParticle = 48;
    //点间隔距离
    private double mRange = 2 * Math.PI / mParticle;
    //圈数之间隔距离
    private double mD = Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 3);
    //爆炸体数组集合
    private Substance[] mExplodes;
    //可执行线程服务
    private ExecutorService mExecutorService;
    //爆炸体色彩集
    private int[] mColor = new int[]{Color.RED, Color.GREEN, Color.YELLOW, Color.WHITE, Color.BLUE, Color.MAGENTA};


    Explode(int posX, int posY) {
        mExplodes = new Substance[mParticle];
        mExecutorService = Executors.newSingleThreadExecutor();
        create(posX, posY);
    }


    private void create(int posX, int posY) {
        int wh = (int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 3);

        for (int i = 0; i < mParticle; i++) {
            Substance explode = new Substance(posX, posY, wh, wh);
            int color = new Random().nextInt(mColor.length);
            explode.setColor(mColor[color]);
            mExplodes[i] = explode;
        }
    }

    private void changeXY() {
        for (int i = 0; i < mCircle; i++) {
            for (int j = 0; j < mParticle; j++) {
                int dx = new Random().nextInt(30);
                int dy = new Random().nextInt(20);
                mExplodes[j].setPositionX(mExplodes[j].getPositionX() + (int) (Math.cos(mRange * j) * (i * mD + dx - 15)));
                mExplodes[j].setPositionY(mExplodes[j].getPositionY() + (int) (Math.sin(mRange * j) * (i * mD + dy - 10)));
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Face.getInstance().getExplodes().remove(this);
        mExplodes[0] = null;
        mExecutorService.shutdownNow();
        mExecutorService = null;
    }

    void exported() {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                changeXY();
            }
        });
    }

    Substance[] getExplodes() {
        return mExplodes;
    }
}
