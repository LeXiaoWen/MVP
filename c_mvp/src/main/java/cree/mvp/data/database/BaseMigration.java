package cree.mvp.data.database;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Title:数据库升级基类
 * Description:尽量不要在操作中新开线程
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/4/6  09:01
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseMigration implements RealmMigration {

    private RealmSchema schema;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        init(realm);
        for (long i = oldVersion; i < newVersion; i++) {
            levelUp(oldVersion);
            oldVersion++;
        }
    }

    /**
     * 数据库升级的时候会调用这个方法
     *
     * @param oldVersion 每次版本号+1 ,例如 1-4 调用方式为,1,2,3
     */
    public abstract void levelUp(long oldVersion);


    private void init(DynamicRealm realm) {
        schema = realm.getSchema();
    }

    /**
     * 数据库结构结构升级控制类
     */
    class LevelUpObjController {
        private RealmObjectSchema beanSchema;

        public LevelUpObjController(String beanName) {
            beanSchema = schema.get(beanName);
        }

        /**
         * 添加可以为空的列
         *
         * @param fieldName
         * @param fieldType
         * @return
         */
        public LevelUpObjController addFieldRequire(String fieldName, Class<?> fieldType) {
            beanSchema.addField(fieldName, fieldType, FieldAttribute.REQUIRED);
            return this;
        }

        /**
         * 添加值不能为空的列
         * 配合transform使用,在回调里面对初始化的数据赋值
         *
         * @param fieldName
         * @param fieldType
         * @return
         */
        public LevelUpObjController addFieldNotRequire(String fieldName, Class<?> fieldType) {
            beanSchema.addField(fieldName, fieldType);
            return this;
        }

        /**
         * 添加主键
         *
         * @param fieldName
         * @param fieldType
         * @return
         */
        public LevelUpObjController addFieldKey(String fieldName, Class<?> fieldType) {
            beanSchema.addField(fieldName, fieldType, FieldAttribute.PRIMARY_KEY);
            return this;
        }

        /**
         * 删除某列
         *
         * @param fieldName
         * @return
         */
        public LevelUpObjController removeField(String fieldName) {
            beanSchema.removeField(fieldName);
            return this;
        }

        /**
         * 这里面的回调可以对一些移除的列的值的移植
         *
         * @param function
         * @return
         */
        public LevelUpObjController transform(RealmObjectSchema.Function function) {
            beanSchema.transform(function);
            return this;
        }

    }


}

