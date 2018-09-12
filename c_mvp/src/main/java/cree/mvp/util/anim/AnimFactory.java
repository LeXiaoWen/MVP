package cree.mvp.util.anim;

import android.view.View;

/**
 * Title:动画工厂类
 * Description:
 * CreateTime:2017/8/11  10:58
 *
 * @author luyongjiang
 * @version 1.0
 */
public class AnimFactory {
    /**
     * 透明动画控制类
     *
     * @param view
     * @return
     */
    public static AnimController createAlphaAnim(View view) {
        return new AlphaAnim(view);
    }

    public static AnimController createScaleAnim(View view) {
        return new ScaleAnim(view);
    }

    public static AnimController createRotateAnim(View view) {
        return new RotateAnim(view);
    }

    public static AnimController createColorAnim(View view) {
        return new ColorAnim(view);
    }
}
