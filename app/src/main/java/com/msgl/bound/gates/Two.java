package com.msgl.bound.gates;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.msgl.bound.common.Substance;
import com.msgl.bound.utils.Tools;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by msgl on 17-12-6.
 * 二关
 */

public class Two extends GateDraw {
    @Override
    Set<Substance> initLayout(AssetManager am, int winWidth, int winHeight) {
        // 加载assets目录里的背景资源并改变背景
        changerBackground(am, "Two/background.png");

        // 加载assets目录里的小物件资源并设小物件背景
        String[] strings = new String[7];
        for (int i = 0; i < 7; i++) {
            strings[i] = "Two/em" + (i + 1) + ".png";
        }
        Drawable[] drawables = getDrawbales(am, strings);
        // 布局小物件并反回
        return getSets(drawables, winWidth, winHeight);
    }

    // 小物件布局
    private Set<Substance> getSets(Drawable[] drawables, int winWidth, int winHeight) {
        Set<Substance> sets = new HashSet<>();

        // 随机对象
        Random random = new Random();
        //小物件布局

        int w = (int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 50);
        int h = (int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 50);

        int pfY = (winHeight - h -24) / 16;

        // 中间一个
        Substance ab = new Substance(winWidth / 2, winHeight / 2, w, h);
        ab.setDrawable(drawables[6]);
        sets.add(ab);

        for (int i = 0; i < 8; i++) {
            Substance ac = new Substance(winWidth / 2, pfY * i + (i + 1) * 2 + pfY / 2, pfY, pfY);
            ac.setDrawable(drawables[random.nextInt(6)]);
            sets.add(ac);
        }

        int pp = (winHeight + h ) / 2;

        for (int i = 0; i < 8; i++) {
            Substance ac = new Substance((winWidth / 2) - (i + 1) * pfY , pp + pfY * i + (i + 1) * 2 + pfY / 2, pfY, pfY);
            ac.setDrawable(drawables[random.nextInt(6)]);
            sets.add(ac);
        }
        for (int i = 0; i < 8; i++) {
            Substance ac = new Substance((winWidth / 2) + (i + 1) * pfY , pp + pfY * i + (i + 1) * 2 + pfY / 2, pfY, pfY);
            ac.setDrawable(drawables[random.nextInt(6)]);
            sets.add(ac);
        }
        for (int i = 0; i < 6; i++) {
            Substance ac = new Substance((winWidth / 2) - i * pfY - w , winHeight / 2, pfY, pfY);
            ac.setDrawable(drawables[random.nextInt(6)]);
            sets.add(ac);
        }
        for (int i = 0; i < 6; i++) {
            Substance ac = new Substance((winWidth / 2) + i * pfY + w , winHeight / 2, pfY, pfY);
            ac.setDrawable(drawables[random.nextInt(6)]);
            sets.add(ac);
        }

        return sets;
    }
}
