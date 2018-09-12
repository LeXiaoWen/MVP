package cree.mvp.util.data;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

import cree.mvp.ui.fragment.video.bean.VideoSelectConfig;

import static cree.mvp.util.data.ThreadPoolUtils.Type.FixedThread;

/**
 * Title:视频相关的Utils
 * Description:
 * CreateTime:2017/7/11  11:23
 *
 * @author luyongjiang
 * @version 1.0
 */
public class VideoUtils {
    private static VideoUtils INSTACEN = null;

    public static VideoUtils getInstance() {
        if (INSTACEN == null) {
            synchronized (VideoUtils.class) {
                if (INSTACEN == null) {
                    INSTACEN = new VideoUtils();
                }
            }
        }
        return INSTACEN;
    }


    VideoUtils() {
        int memory = 7;
        mWeakReferenceLruCache = new LruCache<String, WeakReference<Bitmap>>(memory) {
            @Override
            protected int sizeOf(String key, WeakReference<Bitmap> value) {
                if (value.get() == null) {
                    mWeakReferenceLruCache.remove(key);
                    return 0;
                }
                return value.get().getByteCount() / 1024 / 1024;
            }
        };
        mPool = new ThreadPoolUtils(FixedThread, 5);
    }

    private LruCache<String, WeakReference<Bitmap>> mWeakReferenceLruCache;
    private ThreadPoolUtils mPool;

    /**
     * 图片文件夹缓存方法,判断是否创建并且返回file引用
     *
     * @return
     */
    private File init() {
        File file = new File(VideoSelectConfig.getVideoImgCachepath());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取本地视频的第一帧
     *
     * @param filePath
     * @return
     */
    public Bitmap getLocalVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        if (mWeakReferenceLruCache == null) {
            return null;
        }
        File img = new File(init(), getCachePath(filePath));

        bitmap = getCacheFile(img, bitmap, filePath);
        if (bitmap != null) {
            return bitmap;
        }
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(filePath);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        if (mWeakReferenceLruCache == null) {
            return null;
        }
        if (bitmap == null) {
            return null;
        }
        bitmap = ImageUtils.compressBitmapToFile(bitmap);
        ImageUtils.localSaveBitmap(img, bitmap);
        mWeakReferenceLruCache.put(filePath, new WeakReference<>(bitmap));
        return bitmap;
    }

    /**
     * 获取缓存的File对象如果解析到Bitmap就将Bitmap赋值给传递进来的引用
     *
     * @param bitmap
     * @param filePath
     * @return
     */
    private Bitmap getCacheFile(File img, Bitmap bitmap, String filePath) {
        if (mWeakReferenceLruCache.get(filePath) != null) {
            bitmap = mWeakReferenceLruCache.get(filePath).get();
        }
        if (bitmap != null) {
            return bitmap;
        }
        if (img.isFile()) {
            bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
            mWeakReferenceLruCache.put(filePath, new WeakReference<>(bitmap));
        }

        return bitmap;
    }

    /**
     * 缓存名称规则
     *
     * @param filePath
     * @return
     */
    @NonNull
    private String getCachePath(String filePath) {
        File f = new File(filePath);
        String s = f.getAbsolutePath().replaceAll(File.separator, "_");
        s = s.substring(0, s.lastIndexOf("."));
        return s;
    }


    public void setLocalVideoBitmapForImageView(Activity activity, String filePath, ImageView imageView) {
        imageView.setTag(filePath);
        Bitmap bitmap = null;
        if (mWeakReferenceLruCache == null) {
            return;
        }
        File img = new File(init(), getCachePath(filePath));
        bitmap = getCacheFile(img, bitmap, filePath);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        mPool.execute(new ImgRunnable(filePath, imageView, activity));
    }


    private class ImgRunnable implements Runnable {
        private String filePath;
        private ImageView mImageView;
        private Activity mActivity;

        public ImgRunnable(String filePath, ImageView imageView, Activity activity) {
            this.filePath = filePath;
            mImageView = imageView;
            mActivity = activity;
        }

        @Override
        public void run() {
            Bitmap localVideoThumbnail = getLocalVideoThumbnail(filePath);
            if (localVideoThumbnail == null) {
                return;
            }
            if (filePath.equals(mImageView.getTag().toString())) {
                mActivity.runOnUiThread(() -> mImageView.setImageBitmap(localVideoThumbnail));
            }
        }
    }


    public void onDestroy() {
        mPool.shutDownNow();
        mWeakReferenceLruCache = null;
        mPool = null;
        VideoUtils.INSTACEN = null;
    }

}
