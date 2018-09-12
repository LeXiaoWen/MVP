package cree.mvp.net.factory;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Title:
 * Description:
 * CreateTime:2017/7/17  16:59
 *
 * @author luyongjiang
 * @version 1.0
 */
public class BodyFactory {
    public static final int FILE = 0, FIELD = 1;

    /**
     * 创建用于网络请求的 请求实体信息
     *
     * @param type {@link #FILE,#STRING}
     * @return {@link RequestBody}
     */
    public static RequestBody createBody(int type, Object obj) {
        switch (type) {
            case FILE:
                if (obj instanceof File) {
                    return createFileBody((File) obj);
                } else {
                    return createFileBody(obj.toString());
                }
            case FIELD:
                return createField(obj.toString());
        }
        return null;
    }

    private static RequestBody createFileBody(String path) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), new File(path));

    }

    private static RequestBody createFileBody(File file) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), file);

    }

    private static RequestBody createField(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

}
