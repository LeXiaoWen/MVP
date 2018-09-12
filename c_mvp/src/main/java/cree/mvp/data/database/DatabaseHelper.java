package cree.mvp.data.database;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.Date;
import java.util.Set;

import cree.mvp.util.data.MapUtil;
import cree.mvp.util.develop.DevelopUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Title:数据库操作帮助类 PS:这个里面提供的方法是不异步的
 * Description:子类ashelper 里面所有带as的方法是异步的
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/4/1  14:37
 *
 * @author luyongjiang
 * @version 1.0
 */
public class DatabaseHelper {

    public static <T extends BaseMigration> void init(Context context, T migration) {
        dataInit(context, migration);
    }

    /**
     * 初始化数据库操作
     *
     * @param context 尽量传入applicaiotn的值
     */
    public static void init(Context context) {
        dataInit(context, null);
    }

    private static void dataInit(Context context, BaseMigration migration) {
        Realm.init(context);
        BaseMigration baseMigration = null;
        if (migration == null) {
            baseMigration = new TestMigration();
        } else {
            baseMigration = migration;
        }
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .schemaVersion(DataBaseConfig.getDbVersion())
                .migration(baseMigration)
                .name(DataBaseConfig.getDbName())
//                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        if (DevelopUtil.isDebug)
            Stetho.initialize(
                    Stetho.newInitializerBuilder(context)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(context).build())
                            .build());
    }

    /**
     * 不被外部所发现的方法
     *
     * @return
     */
    protected static Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    /**
     * 开始事务
     */
    public static void beginTransaction() {
        getRealm().beginTransaction();
    }

    /**
     * 提交事物
     */
    public static void commitTransaction() {
        getRealm().commitTransaction();
    }


    /**
     * 判断是否存在给定键值对的数据
     *
     * @param clazz   实力类class类型
     * @param mapUtil 键值对<多种数据类型的支持> 不要传入其他类型的maputil,其他类型无效
     * @param <E>     返回是否存在
     * @return
     */
    public static <E extends RealmModel> boolean isExist(Class<E> clazz, MapUtil<String, Object> mapUtil) {

        RealmQuery<E> realm = getAppendRealm(clazz, mapUtil);
        E first = realm.findFirst();
        if (first == null)
            return false;
        return true;
    }

    /**
     * 判断是否包含给定键值对的数据
     *
     * @param clazz   实力类class类型
     * @param mapUtil 键值对<String,String> 不要传入其他类型的maputil,其他类型无效
     * @param <E>     返回是否存在
     * @return
     */
    public static <E extends RealmModel> boolean isContains(Class<E> clazz, MapUtil<String, Object> mapUtil) {
        RealmQuery<E> realm = getAppendRealm(clazz, mapUtil);
        E first = realm.findFirst();
        if (first == null)
            return false;
        return true;
    }


    /**
     * 查找包含给定键值对的数据
     *
     * @param <E>     返回是否存在
     * @param clazz   实力类class类型
     * @param mapUtil 键值对<Object,Object> 不要传入其他类型的maputil,其他类型无效
     * @return
     */
    public static <E extends RealmModel> RealmResults<E> findContains(Class<E> clazz, MapUtil<String, Object> mapUtil) {
        RealmQuery<E> realm = getAppendRealm(clazz, mapUtil);
        return realm.findAll();
    }

    /**
     * 同步的插入一条数据, 不是数据特别多够用了,  因为速度很快
     *
     * @param bean
     * @param <E>
     * @return
     */
    public static <E extends RealmModel> E insert(E bean) {
        getRealm().beginTransaction();
        E e = getRealm().copyToRealmOrUpdate(bean);
        getRealm().commitTransaction();
        return e;
    }


    /**
     * 无条件查找全部数据
     *
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E extends RealmModel> RealmResults<E> findAll(Class<E> clazz) {
        return getRealm().where(clazz).findAll();
    }

    /**
     * 返回查找到的第一条数据
     *
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E extends RealmModel> E findFirst(Class<E> clazz) {
        RealmResults<E> all = getRealm().where(clazz).findAll();
        if (all != null && all.size() > 0)
            return all.first();
        else return null;
    }


    /**
     * 删除数据
     */
    public static <E extends RealmModel> void delete(Class<E> clazz) {
        getRealm().beginTransaction();
        getRealm().delete(clazz);
        getRealm().commitTransaction();
    }


    /**
     * 获取多类型判断查询Realm数据库
     *
     * @param clazz   类名
     * @param mapUtil 键值对工具类
     * @param <E>     Realm数据库对应的实体类
     * @return
     */
    private static <E extends RealmModel> RealmQuery<E> getAppendRealm(Class<E> clazz, MapUtil<String, Object> mapUtil) {
        RealmQuery<E> realm = getRealm().where(clazz);
        Set<String> keySet = mapUtil.map().keySet();
        int count = 0;
        for (String key : keySet) {
            count++;
            Object o = mapUtil.map().get(key);
            if (o instanceof String) {
                realm.contains(key, (String) o);
            } else if (o instanceof Boolean) {//boolean
                realm.equalTo(key, (Boolean) o);
            } else if (o instanceof Byte) {//byte
                realm.equalTo(key, (Byte) o);
            } else if (o instanceof byte[]) {//byte[]
                realm.equalTo(key, (byte[]) o);
            } else if (o instanceof Long) {//long
                realm.equalTo(key, (Long) o);
            } else if (o instanceof Short) {//short
                realm.equalTo(key, (Short) o);
            } else if (o instanceof Double) {//double
                realm.equalTo(key, (Double) o);
            } else if (o instanceof Integer) {//Integer
                realm.equalTo(key, (Integer) o);
            } else if (o instanceof Float) {//Float
                realm.equalTo(key, (Float) o);
            } else if (o instanceof Date) {//Data
                realm.equalTo(key, (Date) o);
            }
            if (count < keySet.size())
                realm = realm.or();

        }
        return realm;
    }
}