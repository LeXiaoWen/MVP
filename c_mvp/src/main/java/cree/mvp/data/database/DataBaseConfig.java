package cree.mvp.data.database;

/**
 * Title:数据库配置类
 * Description: 版本号和数据库名字的配置
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/4/6  10:27
 *
 * @author luyongjiang
 * @version 1.0
 */
public class DataBaseConfig {
    //数据库版本号
    private static int DB_VERSION = 1;
    //数据库名称
    private static String DB_NAME = "baseSql";

    public static void init(int DB_VERSION, String DB_NAME) {
        DataBaseConfig.DB_VERSION = DB_VERSION;
        DataBaseConfig.DB_NAME = DB_NAME;
    }

    public static int getDbVersion() {
        return DB_VERSION;
    }

    public static void setDbVersion(int dbVersion) {
        DB_VERSION = dbVersion;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static void setDbName(String dbName) {
        DB_NAME = dbName;
    }
}
