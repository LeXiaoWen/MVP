package cree.mvp.util.permissions.rx;

import rx.functions.Action1;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/11  10:57
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class PerAction1<T extends Boolean> implements Action1<T> {
    @Override
    public void call(T t) {
        onCallBack(t);
    }


    public abstract void onCallBack(T t);
}
