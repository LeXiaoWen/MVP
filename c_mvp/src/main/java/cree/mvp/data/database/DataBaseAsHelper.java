package cree.mvp.data.database;

import io.realm.RealmModel;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Title:异步的数据库操作类, 但是只有带as的方法才是异步的
 * Description:父类的方法也可以通过这个类调用,但是尽量用父类去调用
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/4/5  10:32
 *
 * @author luyongjiang
 * @version 1.0
 */
public class DataBaseAsHelper extends DatabaseHelper {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public static DataBaseAsHelper init() {
        return new DataBaseAsHelper();
    }

    /**
     * 异步的插入数据
     *
     * @param eSubscriber 插入数据的回调
     * @param listBean    数据源
     * @param <E>
     * @return
     */
    public <E extends RealmModel> DataBaseAsHelper asInsert(Subscriber<E> eSubscriber, E... listBean) {
        Subscription subscribe = Observable.from(listBean).doOnNext(DatabaseHelper::insert).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(eSubscriber);
        compositeSubscription.add(subscribe);
        return this;
    }

    /**
     * 在视图销毁的时候调用这个方法
     */
    public void unSub() {
        compositeSubscription.unsubscribe();
        getRealm().close();
    }

}
