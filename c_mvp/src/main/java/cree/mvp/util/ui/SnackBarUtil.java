package cree.mvp.util.ui;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/20  11:00
 *
 * @author luyongjiang
 * @version 1.0
 */
public class SnackBarUtil {
    public static final int duration = 500;

    public static void showBar(View mView, String message) {
        Snackbar.make(mView, message, duration).show();
    }
}
