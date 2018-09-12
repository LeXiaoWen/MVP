package cree.mvp.net.factory;


import java.io.IOException;

import cree.mvp.net.Api;
import cree.mvp.net.RxSchedulers;
import cree.mvp.net.bean.LoginBean;
import cree.mvp.net.bean.RedirectBean;
import cree.mvp.net.rx.BaseSubscriber;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/16  19:14
 *
 * @author luyongjiang
 * @version 1.0
 */
public class ApiFactory {
    public static Observable<LoginBean> login(String username, String password) {
        return Api.getInstance().service.login(username, password).compose(RxSchedulers.io_main());
    }

    public static Observable<LoginBean> register(RequestBody nameBody, RequestBody passwordBody, RequestBody fileBody) {
        return Api.getInstance().service.register(nameBody, passwordBody, fileBody).compose(RxSchedulers.io_main());
    }




    public static void getRedirectUrl(String url, BaseSubscriber<RedirectBean> subscriber) {
        Observable.create((Observable.OnSubscribe<RedirectBean>) sub -> Api.getInstance().getOkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sub.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                RedirectBean redirectBean = new RedirectBean();
                redirectBean.setUrl(response.request().url().toString());
                sub.onNext(redirectBean);
                sub.onCompleted();
            }
        })).compose(RxSchedulers.io_main()).subscribe(subscriber);
    }


}
