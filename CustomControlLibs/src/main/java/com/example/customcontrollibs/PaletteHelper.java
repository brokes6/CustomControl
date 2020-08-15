package com.example.customcontrollibs;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;

/**
 * 主色调工具类
 */
public class PaletteHelper {
    private static final String TAG = "PaletteHelper";

    /**
     * 设置图片主色调
     *
     * @param bitmap
     * @return
     */
    public static void setPaletteColor(Bitmap bitmap, final View view) {
        if (bitmap == null) {
            return;
        }
        Palette.from(bitmap).maximumColorCount(10).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
//                List<Palette.Swatch> list = palette.getSwatches();
//                int colorSize = 0;
//                Palette.Swatch maxSwatch = null;
//                for (int i = 0; i < list.size(); i++) {
//                    Palette.Swatch swatch = list.get(i);
//                    if (swatch != null) {
//                        int population = swatch.getPopulation();
//                        if (colorSize < population) {
//                            colorSize = population;
//                            maxSwatch = swatch;
//                        }
//                    }
//                }
                Palette.Swatch s = palette.getDominantSwatch();//独特的一种
                Palette.Swatch s1 = palette.getVibrantSwatch();       //获取到充满活力的这种色调
                Palette.Swatch s2 = palette.getDarkVibrantSwatch();    //获取充满活力的黑
                Palette.Swatch s3 = palette.getLightVibrantSwatch();   //获取充满活力的亮
                Palette.Swatch s4 = palette.getMutedSwatch();           //获取柔和的色调
                Palette.Swatch s5 = palette.getDarkMutedSwatch();      //获取柔和的黑
                Palette.Swatch s6 = palette.getLightMutedSwatch();    //获取柔和的亮
                if (s1 != null) {
                    Log.e(TAG, "当前遮挡的颜色为:"+ s1.getRgb());
                    view.setBackgroundColor(s1.getRgb());
                }
            }
        });

    }

}