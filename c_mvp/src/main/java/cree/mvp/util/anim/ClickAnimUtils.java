package cree.mvp.util.anim;

import android.view.View;
import android.view.ViewGroup;

import cree.mvp.util.anim.widget.CustomClickViewLayout;


/**
 * Title:点击动画工具
 * Description:
 * CreateTime:2017/8/11  10:19
 *
 * @author luyongjiang
 * @version 1.0
 */
public class ClickAnimUtils {
    //透明动画标记
    public static final int ALPHA_ANIM = 0;
    //缩放动画标记
    public static final int SCALE_ANIM = 1;
    //平移动画标记
    public static final int TRANSLATION_ANIM = 2;
    //颜色渐变动画标记
    public static final int COLOR_ANIM = 3;
    //颜色渐变动画标记
    public static final int ROTATE_ANIM = 4;

    public static void depolyClickAnim(View view, int animTag) {
        CustomClickViewLayout viewLayout = init(view);
        switch (animTag) {
            case ALPHA_ANIM:
                alphaAnim(viewLayout);
                break;
            case SCALE_ANIM:
                scaleAnim(viewLayout);
                break;
            case TRANSLATION_ANIM:
                translationAnim(viewLayout);
                break;
            case COLOR_ANIM:
                colorAnim(viewLayout);
                break;
            case ROTATE_ANIM:
                rotateAnim(viewLayout);
                break;
        }
    }

    private static CustomClickViewLayout init(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        int index = viewGroup.indexOfChild(view);
        viewGroup.removeView(view);
        if (viewGroup instanceof CustomClickViewLayout) {
            ViewGroup cViewGroup = (ViewGroup) viewGroup.getParent();
            cViewGroup.removeView(viewGroup);
            viewGroup = cViewGroup;
        }
        CustomClickViewLayout customView = new CustomClickViewLayout(view.getContext());
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(layoutParams);
        marginLayoutParams.setMargins(0, 0, 0, 0);
        view.setLayoutParams(marginLayoutParams);
        customView.setLayoutParams(layoutParams);
        customView.addView(view);
        viewGroup.addView(customView, index);
        return customView;
    }

    private static void alphaAnim(CustomClickViewLayout customView) {
        customView.setTag(CustomClickViewLayout.ALPHA_ANIM);
    }

    private static void scaleAnim(CustomClickViewLayout customView) {
        customView.setTag(CustomClickViewLayout.SCALE_ANIM);
    }

    private static void translationAnim(CustomClickViewLayout customView) {
        customView.setTag(CustomClickViewLayout.TRANSLATION_ANIM);
    }

    private static void colorAnim(CustomClickViewLayout customView) {
        customView.setTag(CustomClickViewLayout.COLOR_ANIM);
    }

    private static void rotateAnim(CustomClickViewLayout customView) {
        customView.setTag(CustomClickViewLayout.ROTATE_ANIM);
    }

}
