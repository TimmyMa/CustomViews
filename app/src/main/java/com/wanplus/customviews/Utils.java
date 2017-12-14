package com.wanplus.customviews;

import android.content.Context;

/**
 * Created by xiaoming on 2017/11/7.
 * Contact before modify.
 */

public class Utils {

    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
