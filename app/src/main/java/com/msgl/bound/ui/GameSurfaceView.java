package com.msgl.bound.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import com.msgl.bound.common.Face;

/**
 * Created by msgl on 17-11-17.
 * 游戏视图区,此类直接用资源文件布局
 */

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public GameSurfaceView(Context context) {
        super(context);
        // 注册holder回调
        getHolder().addCallback(this);
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 注册holder回调
        getHolder().addCallback(this);
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 注册holder回调
        getHolder().addCallback(this);
    }

    // 界面第一次构建时得到高宽,以及开始运行游戏.
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Face face = Face.getInstance();
        face.setWinWidth(getWidth());  // 此视图宽度传给Face类
        face.setWinHeight(getHeight()); //此视图高度传给Face类
        LinearLayout parent = (LinearLayout) getParent();
        face.runGame(parent, holder);   // 调用face对象方法开始运行游戏
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //暂时不需要
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //暂时不需要
    }
}
