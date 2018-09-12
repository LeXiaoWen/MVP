package cree.mvp.ui.fragment.video.bean;

import java.io.File;

import cree.mvp.util.data.ObjectCacheUtils;
import cree.mvp.util.data.SPUtils;

/**
 * Title:
 * Description:
 * CreateTime:2017/7/11  15:35
 *
 * @author luyongjiang
 * @version 1.0
 */
public class VideoSelectConfig {
    //视频选择对象系列化缓存名称
    private static final String VIDEO_CACHE_FILENAME = "video_select";
    //视频选择是否遍历数据sp存储文件名
    private static final String VIDEO_SP_FILENAME = "video_select_sp";
    //视频选择是否遍历数据sp存储判断key值
    private static final String VIDEO_SP_ISNOREFRESH_KEY = "video_is_refresh_key";
    //视频图片本地缓存文件夹路径
    private static final String VIDEO_IMG_CACHEPATH = ObjectCacheUtils.getCacheFolder() + File.separator + "img_cache";

    public static String getVideoImgCachepath() {
        return VIDEO_IMG_CACHEPATH;
    }

    public static String getVideoCacheFilename() {
        return VIDEO_CACHE_FILENAME;
    }

    public static String getVideoSpFilename() {
        return VIDEO_SP_FILENAME;
    }

    public static String getVideoSpIsnorefreshKey() {
        return VIDEO_SP_ISNOREFRESH_KEY;
    }

    /**
     * 设置是否遍历内存视频文件,并且删除缓存文件
     *
     * @param isNoRefresh
     */
    public static void setNoRefresh(boolean isNoRefresh) {
        new Thread(() -> {
            SPUtils spUtils = new SPUtils(VIDEO_SP_FILENAME);
            spUtils.putBoolean(VIDEO_SP_ISNOREFRESH_KEY, isNoRefresh);
            if (!isNoRefresh) {
                File file = new File(VIDEO_IMG_CACHEPATH);
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File deleteFile : files) {
                        deleteFile.delete();
                    }
                }
            }
        }).start();
    }


}
