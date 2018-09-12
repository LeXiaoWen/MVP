package cree.mvp.util.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

/**
 * Title:
 * Description:
 * CreateTime:2017/8/11  10:42
 *
 * @author luyongjiang
 * @version 1.0
 */
public class AnimPolymorphic implements AnimController {

    ObjectAnimator mStartObjectAnim;
    ObjectAnimator mRecoverObjectAnim;
    AnimatorSet mStartAnimSet;
    AnimatorSet mRecoverAnimSet;

    @Override
    public void start() {
        if (mRecoverObjectAnim != null) {
            mRecoverObjectAnim.cancel();
        }
        if (mRecoverAnimSet != null) {
            mRecoverAnimSet.cancel();
        }
        if (mStartObjectAnim != null) {
            mStartObjectAnim.start();
        }
        if (mStartAnimSet != null) {
            mStartAnimSet.start();
        }
    }

    @Override
    public void recover() {
        if (mStartObjectAnim != null) {
            mStartObjectAnim.cancel();
        }

        if (mStartAnimSet != null) {
            mStartAnimSet.cancel();
        }
        if (mRecoverObjectAnim != null) {
            mRecoverObjectAnim.start();
        }
        if (mRecoverAnimSet != null) {
            mRecoverAnimSet.start();
        }
    }


}
