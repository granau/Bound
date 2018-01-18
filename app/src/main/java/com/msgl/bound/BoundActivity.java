package com.msgl.bound;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.msgl.bound.common.Face;
import com.msgl.bound.common.sonball.Sixball;
import com.msgl.bound.gates.*;
import com.msgl.bound.utils.Tools;

public class BoundActivity extends AppCompatActivity {
    // 声音
    private SoundPool mSoundPool;
    //小球子球们对象
    private Sixball mSixball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 得到默认显示, 方便使用.
        Tools.addDisplayMetrics(this);

        //加载声音并引用到Face类
        mSoundPool = getSoundPool();
        Face.getInstance().setSoundPool(mSoundPool);

        // 设置全屏
        getWindow().hasFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 设置视图
        setContentView(R.layout.activity_bound);

        // 添加游戏关数布局类的字符串,所有所加的关数放到这个Class类型数组,
        // todo: 新写的关数加入到里面
        Class[] cls = new Class[]{
                One.class,    // 第一关小图件布局
                Two.class     // 每二关小图件布局
        };

        // 获得face类
        final Face face = Face.getInstance();
        // 设置Class[]这个关卡数组
        face.setGates(cls);
        // 设置左右面板资源,可为Bitmap或Drawable, 如果不设, 默认只画一长方形,如都设,则先用Bitmap
        face.setLeftPlanerDraw(getResources().getDrawable(R.drawable.silder_left_background));
        face.setRightPlanerDraw(getResources().getDrawable(R.drawable.silder_right_background));
        /* todo: 下面是用于测试bitmap是否显示正确, 如果要这功能需在Face类取掉这个法的注释
            face.setLeftPlanerBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.test));
            face.setRightPlanerBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.test));
         */

        // 设置按钮监听
        Integer[] resIds = {R.id.left_button, R.id.left_button1, R.id.left_button2, R.id.left_button3,
                R.id.left_button4, R.id.left_button5, R.id.right_button1, R.id.right_button2,
                R.id.right_button3, R.id.right_button4, R.id.right_button5, R.id.right_button6};
        for(final Integer resId : resIds){
            Button button = findViewById(resId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonAct(resId, face);
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //释放声音资源
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
        Face.getInstance().setStopGame();
        Face.getInstance().restrory();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //停掉游戏
        //释放声音资源
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
        Face.getInstance().setStopGame();
        Face.getInstance().restrory();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放声音资源
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
        Face.getInstance().setStopGame();
        Face.getInstance().restrory();
    }

    //加载声音资源方法
    private SoundPool getSoundPool() {
        SoundPool sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sp.load(this, R.raw.dong, 1);
        sp.load(this, R.raw.tang, 1);
        sp.load(this, R.raw.ping, 1);
        return sp;
    }

    // 按钮执行事件, 让球开始各种变化,限制最多6子球, 传7及以上的数子还是只有6子球.
    private void buttonAct(int resId, Face face){
        switch (resId){
            case R.id.left_button:   // 没子球
                face.getBall().setSonBall(null);
                break;
            case R.id.left_button1:    // 一子球
                mSixball = new Sixball(1);
                face.getBall().setSonBall(mSixball);
                break;
            case R.id.left_button2:    // 二子球
                mSixball = new Sixball(2);
                face.getBall().setSonBall(mSixball);
                break;
            case R.id.left_button3:    // 三子球
                mSixball = new Sixball(3);
                face.getBall().setSonBall(mSixball);
                break;
            case R.id.left_button4:     // 四子球
                mSixball = new Sixball(4);
                face.getBall().setSonBall(mSixball);
                break;
            case R.id.left_button5:     // 五子球
                mSixball = new Sixball(5);
                face.getBall().setSonBall(mSixball);
                break;
            case R.id.right_button1:    // 正转加速
                if (mSixball != null) {
                    mSixball.setCirVal(mSixball.getCirVal() + 0.1);
                }
                break;
            case R.id.right_button2:    // 反转加速
                if (mSixball != null) {
                    mSixball.setCirVal(mSixball.getCirVal() - 0.1);
                }
                break;
            case R.id.right_button3:    // 扩开子球
                if (mSixball != null) {
                    mSixball.setD(mSixball.getD() + 6);
                }
                break;
            case R.id.right_button4:     // 收紧子球
                if (mSixball != null) {
                    mSixball.setD(mSixball.getD() - 6);
                }
                break;
            case R.id.right_button5:     // 把球停住
                face.getBall().setStop(true);
                break;
            case R.id.right_button6:     // 让球走
                face.getBall().setStop(false);
                break;
            default:
                break;
        }
    }
}
