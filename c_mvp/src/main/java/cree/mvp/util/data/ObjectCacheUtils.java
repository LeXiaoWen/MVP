package cree.mvp.util.data;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Title:对象系列化工具类
 * Description:
 * CreateTime:2017/7/11  15:45
 *
 * @author luyongjiang
 * @version 1.0
 */
public class ObjectCacheUtils {
    private static final String CACHE_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "CreeCache";

    public static String getCacheFolder() {
        return CACHE_FOLDER;
    }

    private static void init() {
        File file = new File(CACHE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static String getPath(String name) {
        return CACHE_FOLDER + File.separator + name;
    }

    /**
     * 保存序列化对象
     *
     * @param serializable
     * @param fileName
     */
    public static void savenObject(Serializable serializable, String fileName) {
        init();
        //序列化对象
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(getPath(fileName)));
            out.writeObject(serializable);    //写入customer对象
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除序列化对象
     *
     * @param fileName
     * @param <T>
     * @return
     */
    public static <T> T readObject(String fileName) {
        init();
        //反序列化对象
        ObjectInputStream in = null;
        T obj3 = null;    //读取customer对象
        try {
            in = new ObjectInputStream(new FileInputStream(getPath(fileName)));
            obj3 = (T) in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj3;
    }
}
