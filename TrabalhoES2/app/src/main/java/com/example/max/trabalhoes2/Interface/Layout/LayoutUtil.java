package com.example.max.trabalhoes2.Interface.Layout;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class LayoutUtil {

    /**
     * Retorna uma instancia de DisplayMetrics dada uma Activity.
     * @param activity Activity que se quer obter informação.
     * @return DisplayMetrics contante informações de tamanho da activity.
     */
    public static DisplayMetrics getDisplay(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
}
