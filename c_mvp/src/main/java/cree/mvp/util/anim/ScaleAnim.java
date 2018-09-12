package cree.mvp.util.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Title:缩放动画
 * Description:
 * CreateTime:2017/8/11  10:42
 *
 * @author luyongjiang
 * @version 1.0
 */
public class ScaleAnim extends AnimPolymorphic {

    public ScaleAnim(View view) {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.7f);
        scaleY.setDuration(500);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.7f);
        scaleX.setDuration(500);
        mStartAnimSet = new AnimatorSet();
        mStartAnimSet.setDuration(500);
        mStartAnimSet.play(scaleX).with(scaleY);
        //恢复的
        ObjectAnimator reScaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.7f, 1.0f);
        reScaleY.setDuration(500);
        ObjectAnimator reScaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.7f, 1.0f);
        reScaleX.setDuration(500);
        mRecoverAnimSet = new AnimatorSet();
        mRecoverAnimSet.setDuration(500);
        mRecoverAnimSet.play(reScaleY).with(reScaleX);


    }
}
