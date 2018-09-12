package cree.mvp.util.permissions.rx;

import rx.functions.Action1;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/11  10:59
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class PerActionError implements Action1<Throwable> {
    @Override
    public void call(Throwable throwable) {
        onCallEror(throwable);
    }

    public abstract void onCallEror(Throwable throwable);
}
