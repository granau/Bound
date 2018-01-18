package com.msgl.bound.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.LinearLayout;

import com.msgl.bound.BoundActivity;
import com.msgl.bound.R;
import com.msgl.bound.gates.GateDraw;
import com.msgl.bound.utils.Tools;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by msgl on 17-11-19.
 * 控制器类
 */

public class Face {
    // 单例本类
    private static final Face sFace = new Face();

    // 声音
    SoundPool mSoundPool;

    // 游戏界面宽高
    private int mWinWidth;
    private int mWinHeight;

    // 关数Class[]数组
    private Class[] mGates;

    // 小特件集合
    private final ConcurrentSkipListSet<Substance> mSubstances = new ConcurrentSkipListSet<>();
    // 爆炸物件集合
    private final CopyOnWriteArrayList<Explode> mExplodes = new CopyOnWriteArrayList<>();

    //建一个小球对象
    private Ball mBall = new Ball();

    // 左右控制板
    private final Planer mLeftPlaner = new Planer(Planer.LEFT_PLANER);
    private final Planer mRightPlaner = new Planer(Planer.RIGHT_PLANER);

    // 左右球计数
    private int mBallLeft = 3;
    private int mBallRight = 3;
    private boolean mLRFlag = true;

    // 左右面板是否动过标识
    private boolean mLRball = true;
    // 是否开局移动球的线程
    private boolean mPlanerBall = false;

    // 关数计数
    private int mCounter = 0;

    // 游戏是否开始
    private boolean mStartGame = false;

    // 弹框消息文字
    private String mMessage = "点开始游戏!!!";


    private Face() {
    }

    // 获得此类的实例
    public static Face getInstance() {
        return sFace;
    }


    // 声音引用
    public void setSoundPool(SoundPool soundPool) {
        mSoundPool = soundPool;
    }

    // 恢得初值
    public void restrory() {
        mBall.stopBallMove();
        mBallLeft = 3;
        mBallRight = 3;
        mLRFlag = true;
        mLRball = true;
        mPlanerBall = false;
        mCounter = 0;
        mStartGame = false;


    }


    //设游戏区宽度, 高度
    public void setWinWidth(int winWidth) {
        mWinWidth = winWidth;
    }

    public void setWinHeight(int winHeight) {
        mWinHeight = winHeight;
    }

    // 获得游戏区宽高度.
    public int getWinWidth() {
        return mWinWidth;
    }

    public int getWinHeight() {
        return mWinHeight;
    }

    //获得Substance 这个类的list
    ConcurrentSkipListSet<Substance> getSubstances() {
        return mSubstances;
    }

    // 设置关数Class数组
    public void setGates(Class[] gates) {
        mGates = gates;
    }

    // 设置左, 右面板可画的
    public void setLeftPlanerDraw(Drawable draw) {
        mLeftPlaner.setDrawable(draw);
    }

    public void setRightPlanerDraw(Drawable draw) {
        mRightPlaner.setDrawable(draw);
    }

    /* todo: 是否需要设置左, 右面板Bitmap
        public void setLeftPlanerBitmap(Bitmap bitmap) {
            mLeftPlaner.setBitmap(bitmap);
        }

        public void setRightPlanerBitmap(Bitmap bitmap) {
            mRightPlaner.setBitmap(bitmap);
        }
    */

    Planer getLeftPlaner() {
        return mLeftPlaner;
    }

    Planer getRightPlaner() {
        return mRightPlaner;
    }

    // 设置游戏是否开始或结束
    public void setStopGame() {
        mStartGame = false;
    }

    CopyOnWriteArrayList<Explode> getExplodes() {
        return mExplodes;
    }

    // 得到小球
    public Ball getBall() {
        return mBall;
    }

    // 开始运行游戏类,初始化相关
    public void runGame(final LinearLayout parentView, SurfaceHolder holder) {

        if (mCounter == 0) {
            // 左右面版运行监听
            actLeftController(parentView.findViewById(R.id.controller_left_view));
            actRightController(parentView.findViewById(R.id.controller_right_view));

            // 初始化放小球
            putLeftBall((LinearLayout) parentView.findViewById(R.id.put_left_layout));
            putRightBall((LinearLayout) parentView.findViewById(R.id.put_right_layout));

            // 小环样式初式化
            mBall.setDrawable(parentView.getResources().getDrawable(R.drawable.ball_background));
        }


        // 得到关数布局
        GateDraw gateDraw = getGateDraw(mCounter);
        AssetManager assetManager = parentView.getContext().getAssets();
        gateDraw.layoutBlocksAtGate(assetManager, mSubstances);
        // 获得背景Drawable
        Drawable drawable = gateDraw.getWinGroundback();

        // 运行画
        runDrawGame(parentView, holder, drawable);
    }

    // 实例化关数的类,并进行布局物件加入集合.
    private GateDraw getGateDraw(int n) {
        GateDraw gateDraw = null;
        Class cls = mGates[n];
        try {
            gateDraw = (GateDraw) cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (gateDraw == null) {
            throw new NullPointerException("GateDraw---实例化对象不能为空.");
        }

        return gateDraw;
    }


    // 游戏界面画
    private void runDrawGame(final LinearLayout parentView, final SurfaceHolder holder, final Drawable drawable) {

        if (mSubstances.isEmpty() || !mStartGame) {    /*开始游戏之前需要的界面画*/

            // 如果游戏在运行中进入此条件,证明这关已过.停线程.开始第二关.
            if (mStartGame) {
                mBall.stopBallMove();
                mStartGame = false;
                if (mCounter >= mGates.length) {
                    mMessage = "恭喜!游戏全关通过,点开始再来一次.";
                    final LinearLayout ll = parentView.findViewById(R.id.put_left_layout);
                    final LinearLayout lr = parentView.findViewById(R.id.put_right_layout);
                    parentView.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            ll.removeViews(1, mBallLeft);
                            lr.removeViews(1, mBallRight);
                        }
                    });
                    restrory();
                }
                runGame(parentView, holder);
                return;
            }

            mStartGame = false;
            mCounter++;

            // 归位左右面版
            mLeftPlaner.setStopFlag();
            mRightPlaner.setStopFlag();

            // 得到画布
            Canvas canvas = holder.lockCanvas();
            // 画背景
            drawable.draw(canvas);
            // 画左右控制版
            mLeftPlaner.drawSelf(canvas);
            mRightPlaner.drawSelf(canvas);

            //小球还原归位画小球
            ballPosition(parentView);
            mBall.drawSelf(canvas);

            holder.unlockCanvasAndPost(canvas);

            //弹出框
            parentView.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    goDialog(parentView, holder, drawable).show();
                }
            });

        } else if (mBallLeft == -1 && mBallRight == -1) {    /*小球用完了的要作的事*/
            mMessage = "你没有通过,点开始继续!";
            mStartGame = false;
            mCounter = 0;
            mSubstances.clear();
            mBallLeft = 3;
            mBallRight = 3;
            // 归位左右面版
            mLeftPlaner.setStopFlag();
            mRightPlaner.setStopFlag();
            runGame(parentView, holder);
        } else {                                /*游戏运行中代码*/
            // 得到画布
            Canvas canvas = holder.lockCanvas();
            // 画背景
            drawable.draw(canvas);
            // 画左右控制版
            mLeftPlaner.drawSelf(canvas);
            mRightPlaner.drawSelf(canvas);

            // 画小球
            mBall.drawSelf(canvas);
            // 画小物件
            for (Substance s : mSubstances) {
                s.drawSelf(canvas);
            }
            // 爆炸物件检测画
            for (Explode ex : mExplodes) {
                Substance[] sb = ex.getExplodes();
                if (sb[0] != null) {
                    for (Substance s : sb) {
                        s.drawSelf(canvas);
                    }
                }
            }
            holder.unlockCanvasAndPost(canvas);
        }

    }

    // 小球位置初始化.
    void ballPosition(LinearLayout parentView) {
        mPlanerBall = false;
        mBall.setSonBall(null);

        if ((mLRFlag || mBallRight == 0) && mBallLeft != 0) {
            mBall.setPositionX(mLeftPlaner.getPositionX() + mLeftPlaner.getWidth() / 2 + mBall.getWidth() / 2);
            mBall.setPositionY(mLeftPlaner.getPositionY());

            mLRFlag = false;
            mLRball = true;
            mBallLeft--;

            final LinearLayout ltLeft = parentView.findViewById(R.id.put_left_layout);
            ltLeft.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    ltLeft.removeViewAt(1);
                }
            });
        } else if (mBallRight != 0) {
            mBall.setPositionX(mRightPlaner.getPositionX() - mRightPlaner.getWidth() / 2 - mBall.getWidth() / 2);
            mBall.setPositionY(mRightPlaner.getPositionY());

            mLRFlag = true;
            mLRball = false;
            mBallRight--;

            final LinearLayout ltRight = parentView.findViewById(R.id.put_right_layout);
            ltRight.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    ltRight.removeViewAt(1);
                }
            });
        } else {
            mBallLeft = -1;
            mBallRight = -1;
        }
    }

    // 游戏关数弹框,确定后开始循环画游戏
    private AlertDialog goDialog(final LinearLayout parentView, final SurfaceHolder holder, final Drawable drawable) {
        AlertDialog dialog = new AlertDialog.Builder(parentView.getContext())
                .setTitle("第" + mCounter + "关")
                .setMessage(mMessage)
                .setCancelable(false)
                .setPositiveButton("开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        mStartGame = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 弹框消失运行游戏循环画.
                                while (mStartGame) {
                                    runDrawGame(parentView, holder, drawable);
                                }
                            }
                        }, "game_thread").start();
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restrory();

                        Activity activity = ((BoundActivity) (parentView.getContext()));
                        activity.finish();
                    }
                }).create();
        mMessage = "点开始游戏!!!";
        return dialog;
    }


    /*-----------------------------------------面版控制滑动---------------------------------------*/

    private void slid(MotionEvent event, Substance planer) {
        int hs = event.getHistorySize();
        if (planer.getPositionY() - planer.getHeight() / 2 > 0 && planer.getPositionY() + planer.getHeight() / 2 < mWinHeight) {
            for (int i = 0; i < hs; i++) {
                planer.setPositionY((int) ((event.getHistoricalY(i) - event.getHistoricalY(0)) * 3 + planer.getPositionY()));
            }
        } else if (planer.getPositionY() - planer.getHeight() / 2 <= 0) {
            planer.setPositionY(2 + planer.getHeight() / 2);
        } else if (planer.getPositionY() + planer.getHeight() / 2 >= mWinHeight) {
            planer.setPositionY(mWinHeight - planer.getHeight() / 2 - 2);
        }
    }

    //左控制版
    private void actLeftController(final View leftControllerView) {
        leftControllerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mPlanerBall && mLRball) {
                    mBall.startBallMove((LinearLayout) leftControllerView.getParent());
                    mPlanerBall = true;
                }
                v.performClick();
                slid(event, mLeftPlaner);
                return true;
            }
        });
    }

    //右控制版
    private void actRightController(final View rightControllerView) {
        rightControllerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mPlanerBall && !mLRball) {
                    mBall.startBallMove((LinearLayout) rightControllerView.getParent());
                    mPlanerBall = true;
                }
                v.performClick();
                slid(event, mRightPlaner);
                return true;
            }
        });
    }


    /*------------------------------放球区-------------------------*/
    // 左放球
    private void putLeftBall(final LinearLayout leftView) {
        int wh = (int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wh, wh);
        for (int i = 0; i < mBallLeft; i++) {
            final View view = new View(leftView.getContext());
            view.setBackground(view.getResources().getDrawable(R.drawable.ball_background));
            view.setLayoutParams(params);
            leftView.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    leftView.addView(view);
                }
            });

        }
    }

    //右放球
    private void putRightBall(final LinearLayout rightView) {
        int wh = (int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wh, wh);
        for (int i = 0; i < mBallRight; i++) {
            final View view = new View(rightView.getContext());
            view.setBackground(view.getResources().getDrawable(R.drawable.ball_background));
            view.setLayoutParams(params);
            rightView.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    rightView.addView(view);
                }
            });

        }
    }
}
