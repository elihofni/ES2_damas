package com.example.max.trabalhoes2.Interface.Layout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.max.trabalhoes2.R;


public class LayoutUtil {

    public int peoes[] = {R.drawable.pen_peao, R.drawable.pp_peao, R.drawable.democratas_peao, R.drawable.psb_peao, R.drawable.pt_peao_2, R.drawable.sindicatos_peao, R.drawable.congresso_peao, R.drawable.stf_peao};
    public int damas[] = {R.drawable.pen_dama, R.drawable.pp_dama_2, R.drawable.democratas_dama, R.drawable.psb_dama, R.drawable.pt_dama_2, R.drawable.sindicatos_peao, R.drawable.congresso_peao, R.drawable.stf_peao};
    public int bandeiras[] = {R.drawable.bandeira_pen, R.drawable.bandeira_pp, R.drawable.bandeira_democratas, R.drawable.bandeira_psb, R.drawable.bandeira_pt, R.drawable.bandeira_sindicatos, R.drawable.bandeira_congresso, R.drawable.bandeira_stf};
    public int imgFim[] = {R.drawable.vitoria, R.drawable.vitoria};

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

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float pxToDp(int px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
