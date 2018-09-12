package cree.mvp.util.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Title:
 * Description:
 * CreateTime:2017/8/11  10:42
 *
 * @author luyongjiang
 * @version 1.0
 */
public class RotateAnim extends AnimPolymorphic {

    public RotateAnim(View view) {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "rotationY", 0, 45);
        scaleY.setDuration(500);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "rotationX", 0, 45);
        scaleX.setDuration(500);
        mStartAnimSet = new AnimatorSet();
        mStartAnimSet.setDuration(500);
        mStartAnimSet.play(scaleX).with(scaleY);
        //恢复的
        ObjectAnimator reScaleY = ObjectAnimator.ofFloat(view, "rotationY", 45, 0);
        reScaleY.setDuration(500);
        ObjectAnimator reScaleX = ObjectAnimator.ofFloat(view, "rotationX", 45, 0);
        reScaleX.setDuration(500);
        mRecoverAnimSet = new AnimatorSet();
        mRecoverAnimSet.setDuration(500);
        mRecoverAnimSet.play(reScaleY).with(reScaleX);
    }
}
