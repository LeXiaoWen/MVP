package cree.mvp.util.anim;

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
public class AlphaAnim extends AnimPolymorphic {

    public AlphaAnim(View view) {
        mStartObjectAnim = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.3f);
        mStartObjectAnim.setDuration(200);
        mRecoverObjectAnim = ObjectAnimator.ofFloat(view, "alpha", 0.3f, 1.0f);
        mStartObjectAnim.setDuration(200);
    }
}
