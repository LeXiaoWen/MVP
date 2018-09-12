package cree.mvp.net.rx;

import cree.mvp.base.bean.BaseBean;
import rx.Subscriber;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/11  11:02
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseSubscriber<T extends BaseBean> extends Subscriber<T> {
    @Override
    public void onCompleted() {
        onCallCompleted();
    }

    @Override
    public void onError(Throwable e) {
        onCallError(e);
    }

    @Override
    public void onNext(T t) {
        onCallNext(t);
    }

    public void onCallCompleted() {

    }

    public abstract void onCallError(Throwable e);

    public abstract void onCallNext(T t);
}
