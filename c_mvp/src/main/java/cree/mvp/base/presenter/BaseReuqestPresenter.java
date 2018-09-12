package cree.mvp.base.presenter;

import cree.mvp.base.activity.BaseRequestActivity;
import cree.mvp.base.bean.BaseBean;
import cree.mvp.base.model.BaseModel;
import cree.mvp.net.rx.BaseSubscriber;
import cree.mvp.util.develop.LogUtils;
import rx.Subscription;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/17  09:56
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseReuqestPresenter<T extends BaseRequestActivity, E extends BaseModel> extends BasePresenter<T, E> {


    public <D extends BaseBean> void successData(D data) {
        mActivity.dismissProgress();
        if (data.getCode() != 0) {
            failed(new Throwable(data.getMessage()));
            return;
        }
        LogUtils.d(data);
        success(data);
    }

    public void failed(Throwable message) {
        mActivity.dismissProgress();
        //---------------------------请求出错---------------------------------
        message.printStackTrace();
        LogUtils.d(message);
        failed(message.getMessage());
    }


    public abstract <D extends BaseBean> void success(D data);

    public abstract void failed(String message);

    public void loadingExecute(Subscription subscriber) {
        mActivity.showProgress();
        super.execute(subscriber);
    }

    public <O extends BaseBean> BaseSubscriber<O> onSubscriber() {
        return new BaseSubscriber<O>() {

            @Override
            public void onCallError(Throwable e) {
                failed(e);
            }

            @Override
            public void onCallNext(O o) {
                successData(o);
            }
        };
    }
}
