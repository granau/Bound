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
 * Created by msgl on 17-11-21.
 * todo: 第一关布局形式,用于测试
 */

public class One extends GateDraw {

    // 实现这个方法进行第一关物件布局
    @Override
    public Set<Substance> initLayout(AssetManager am, int winWidth, int winHeight) {

        // 加载assets目录里的背景资源并改变背景
        changerBackground(am, "One/background.png");

        // 加载assets目录里的小物件资源并设小物件背景
        String[] strings = new String[5];
        for (int i = 0; i < 5; i++) {
            strings[i] = "One/cat" + (i + 1) + ".png";
        }
        Drawable[] drawables = getDrawbales(am, strings);

        // 布局小物件并反回
        return getSets(drawables, winWidth, winHeight);
    }

    // 小物件布局
    private Set<Substance> getSets(Drawable[] drawables, int winWidth, int winHeight) {
        Set<Substance> sets = new HashSet<>();
        //小物件布局
        int pfY = (winHeight - 34) / 16;
        int pfX = winWidth / 2;
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            if (i == 3 || i == 7 || i == 8 || i == 12) continue;
            Substance ab = new Substance(pfX, pfY * i + (i + 1) * 2 + pfY / 2, pfY, pfY);
            ab.setDrawable(drawables[random.nextInt(5)]);
            sets.add(ab);
        }

        for (int i = 0; i < 16; i++) {
            if (i == 3 || i == 4 || i == 11 || i == 12) continue;
            Substance ab = new Substance(pfX + pfY + 2, pfY * i + (i + 1) * 2 + pfY / 2, pfY, pfY);
            ab.setDrawable(drawables[random.nextInt(5)]);
            sets.add(ab);
        }

        for (int i = 0; i < 16; i++) {
            if (i == 3 || i == 4 || i == 11 || i == 12) continue;
            Substance ab = new Substance(pfX - pfY - 2, pfY * i + (i + 1) * 2 + pfY / 2, pfY, pfY);
            ab.setDrawable(drawables[random.nextInt(5)]);
            sets.add(ab);
        }


        int px = (int) Tools.unitSwap(TypedValue.COMPLEX_UNIT_DIP, 80);

        Substance ab;
        ab = new Substance(pfX - px, pfY * 4 + pfY / 2, pfY * 2, pfY * 2);
        ab.setDrawable(drawables[random.nextInt(5)]);
        sets.add(ab);
        ab = new Substance(pfX - px, pfY * 8 + pfY / 2, pfY, pfY * 2);
        ab.setDrawable(drawables[random.nextInt(5)]);
        sets.add(ab);
        ab = new Substance(pfX - px, pfY * 12 + pfY / 2, pfY * 2, pfY * 2);
        ab.setDrawable(drawables[random.nextInt(5)]);
        sets.add(ab);

        ab = new Substance(pfX + px, pfY * 4 + pfY / 2, pfY * 2, pfY * 2);
        ab.setDrawable(drawables[random.nextInt(5)]);
        sets.add(ab);
        ab = new Substance(pfX + px, pfY * 8 + pfY / 2, pfY, pfY * 2);
        ab.setDrawable(drawables[random.nextInt(5)]);
        sets.add(ab);
        ab = new Substance(pfX + px, pfY * 12 + pfY / 2, pfY * 2, pfY * 2);
        ab.setDrawable(drawables[random.nextInt(5)]);
        sets.add(ab);

        return sets;
    }
}
