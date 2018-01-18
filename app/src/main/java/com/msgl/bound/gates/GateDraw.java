package com.msgl.bound.gates;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.msgl.bound.common.Face;
import com.msgl.bound.common.Substance;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by msgl on 17-11-22.
 * 游戏布局,各关数必须实现此类抽象方法进行布局
 */
abstract public class GateDraw {
    // Drawable 的资源, 游戏区窗口背景
     private Drawable mWinBackground;

    /**
     * 子类实现,可更换背景, 及小物件Substance布局实现, 可根据情况new Substance数个, 并进行定义这些对象各自的高宽
     * 以及它们放在游戏区的位置.其实现的类可参见同包下的其它类.
     * @param am  AssetManager 管理器
     * @param winWidth  游戏区总的宽度,小物件超出将不可见.
     * @param winHeight 游戏区总的高度,小物件超出将不可见.
     * @return Set<Substance> Substance对象集合.
     */
    abstract  Set<Substance> initLayout(AssetManager am, int winWidth, int winHeight);

    /**
     * 此抽像类主要用于游戏每关AbsSubstance们布局集合.通过添加不同位置的substance形成各种图形的小图件
     * 摆放形式,这里新建AbsSubstance,也可以设置准备好各种样式的小图件及大小.
     *
     * @param blocks    用于放置小图件的集合.
     */
    public void layoutBlocksAtGate(AssetManager am, ConcurrentSkipListSet<Substance> blocks){
        Face face = Face.getInstance();
        // 设置default背景
        mWinBackground = new ColorDrawable(Color.BLUE);
        mWinBackground.setBounds(0, 0, face.getWinWidth(), face.getWinHeight());
        // 执行初始化布局
        Set<Substance> sets = initLayout(am, face.getWinWidth(), face.getWinHeight());
        blocks.clear();
        blocks.addAll(sets);
    }

    // 得到背景.
    public Drawable getWinGroundback() {
        return mWinBackground;
    }

    // 加载背景资源并改变背景
    void changerBackground(AssetManager am, String backStr){
        // 加载背景资源
        Drawable background = null;
        InputStream is = null;
        try {
            is = am.open(backStr, AssetManager.ACCESS_BUFFER);
            background = Drawable.createFromStream(is, "src");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 更换背景
        if(background != null){
            mWinBackground = background;
            Face face = Face.getInstance();
            mWinBackground.setBounds(0, 0, face.getWinWidth(), face.getWinHeight());
        }
    }

    // 加载其它可画资源
    Drawable[] getDrawbales(AssetManager am, String... strings){
        // 加载小物件所需资源
        Drawable[] drawables = new Drawable[strings.length];
        InputStream is = null;
        for (int i = 0; i < strings.length; i++) {
            try {
                is = am.open(strings[i], AssetManager.ACCESS_BUFFER);
                drawables[i] = Drawable.createFromStream(is, null);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return drawables;
    }


}
