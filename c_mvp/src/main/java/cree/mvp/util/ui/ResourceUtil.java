package cree.mvp.util.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;

/**
 * Title:资源获取相关工具类
 * Description:用于获取颜色,字符串,尺寸等资源
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/14  21:01
 *
 * @author luyongjiang
 * @version 1.0
 */
public class ResourceUtil {
    /**
     * 通过传入的colorID获取颜色值
     *
     * @param context 上下文
     * @param colorId 资源id
     * @return
     */
    public static int getColor(Context context, @ColorRes int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(colorId);
        } else {
            return context.getResources().getColor(colorId);
        }
    }
}
