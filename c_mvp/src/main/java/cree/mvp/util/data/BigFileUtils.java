package cree.mvp.util.data;

import android.Manifest;
import android.app.Activity;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import cree.mvp.net.RxSchedulers;
import cree.mvp.util.permissions.PermissionsUtils;
import cree.mvp.util.permissions.rx.PerSubscriber;
import rx.Observable;

/**
 * Title:
 * Description:
 * CreateTime:2017/7/17  11:23
 *
 * @author luyongjiang
 * @version 1.0
 */
public class BigFileUtils {
    /**
     * 切割后单个文件的大小
     */
    private static double singleFileSize = 1024 * 1024 * 2;
    /**
     * 文件缓存路径
     */
    public static String mCacheGroupPath = Environment.getExternalStorageDirectory() + File.separator + "CutCacheFile";

    public interface OnBigFileListener {
        void onSplitUiStart();

        void onSplitAllReuslt(CutFileBean[] cut);

        void onSplitUiEnd();

        void onSplitError(Throwable throwable);
    }

    public static void init(Activity activity, String filePath, OnBigFileListener onBigFileListener) {
        activity.runOnUiThread(() -> {
            if (onBigFileListener != null) {
                onBigFileListener.onSplitUiStart();
            }
        });
        PermissionsUtils.getInstance(activity).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscrbe(new PerSubscriber() {
                    @Override
                    public void onCallError(Throwable throwable) {
                        if (onBigFileListener != null) {
                            onBigFileListener.onSplitError(throwable);
                        }
                    }

                    @Override
                    public void onCallNext(Boolean aBoolean) {
                        Observable.create((Observable.OnSubscribe<ArrayList<CutFileBean>>) subscriber -> {
                            String origin = filePath;
                            String cutPath = mCacheGroupPath;
                            File originFile = new File(origin);
                            long originLength = originFile.length();
                            ArrayList<CutFileBean> mCutFileBeen1 = null;
                            mCutFileBeen1 = countSize(origin, cutPath, originLength);
                            Observable<CutFileBean>[] observables = splitFile(mCutFileBeen1);
                            Observable.zip(observables, args -> {
                                CutFileBean[] cut = new CutFileBean[args.length];
                                for (int i = 0; i < cut.length; i++) {
                                    cut[i] = (CutFileBean) args[i];
                                }

                                if (onBigFileListener != null) {
                                    onBigFileListener.onSplitAllReuslt(cut);
                                }
                                return cut[0];
                            }).compose(RxSchedulers.io_main()).subscribe(cutFileBean -> {

                            });
                            subscriber.onNext(mCutFileBeen1);
                            subscriber.onCompleted();
                        }).compose(RxSchedulers.io_main()).subscribe(cutFileBeen -> {
                            // TODO: 2017/7/17 拆分完成后需要处理的UI事件
                            if (onBigFileListener != null) {
                                onBigFileListener.onSplitUiEnd();
                            }
                        });

                    }
                });
    }


    /**
     * 合成的代码
     * //    private void hecheng() {
     * //        mDialog = new CrDialog.Builder(this).setTitle("合成").isProgress().setMessage("进行中...").isNoAutoCancel().create();
     * //        mDialog.show();
     * //        new Thread(new Runnable() {
     * //            @Override
     * //            public void run() {
     * //                File file = new File(mCacheGroupPath);
     * //                File testFile = new File(Environment.getExternalStorageDirectory() + File.separator + "test.mp4");
     * //                File[] files = file.listFiles();
     * //                for (int i = 0; i < files.length; i++) {
     * //                    BufferedInputStream bufferedInputStream = null;
     * //                    RandomAccessFile randomAccessFile = null;
     * //                    try {
     * //                        File childFile = files[i];
     * //                        bufferedInputStream = new BufferedInputStream(new FileInputStream(childFile));
     * //                        randomAccessFile = new RandomAccessFile(testFile, "rw");
     * //                        randomAccessFile.seek((long) (i * singleFileSize));
     * //                        byte[] bys = new byte[1024];
     * //                        int len = 0;
     * //                        while ((len = bufferedInputStream.read(bys)) != -1) {
     * //                            randomAccessFile.write(bys, 0, len);
     * //                        }
     * //                    } catch (FileNotFoundException e) {
     * //                        e.printStackTrace();
     * //                    } catch (IOException e) {
     * //                        e.printStackTrace();
     * //                    } finally {
     * //                        if (bufferedInputStream != null) {
     * //                            try {
     * //                                bufferedInputStream.close();
     * //                            } catch (IOException e) {
     * //                                e.printStackTrace();
     * //                            }
     * //                            bufferedInputStream = null;
     * //                        }
     * //                        if (randomAccessFile != null) {
     * //                            try {
     * //                                randomAccessFile.close();
     * //                            } catch (IOException e) {
     * //                                e.printStackTrace();
     * //                            }
     * //                            randomAccessFile = null;
     * //                        }
     * //                    }
     * //                }
     * //
     * //                String orginMd5 = FileUtils.getFileMD5ToString(new File(Environment.getExternalStorageDirectory() + childeFile));
     * //                String testMd5 = FileUtils.getFileMD5ToString(testFile);
     * //            }
     * //        }).start();
     * //    }
     *
     * @param cutFileBean
     */

    private static void readAndWriteFile(CutFileBean cutFileBean) {
        File originFile = new File(cutFileBean.getOrigin());
        File cutFile = new File(cutFileBean.getCutPath());
        RandomAccessFile accessFile = null;
        BufferedOutputStream buffOut = null;
        try {
            accessFile = new RandomAccessFile(originFile, "r");
            buffOut = new BufferedOutputStream(new FileOutputStream(cutFile));
            accessFile.seek(cutFileBean.getStart());
            long len = 0;
            long content = cutFileBean.getLength();
            long readCount = 0;
            byte[] bys = new byte[1024];
            StringBuffer buffer = new StringBuffer();
            while ((len = accessFile.read(bys)) != -1) {
                if ((readCount + len) > content) {
                    len = content - readCount;
                }
                readCount += len;
                buffOut.write(bys, 0, (int) len);
                if (readCount >= content) {
                    break;
                }
            }
            buffOut.flush();
            System.out.println(buffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (accessFile != null) {
                try {
                    accessFile.close();
                    accessFile = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (buffOut != null) {
                try {
                    buffOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buffOut = null;
            }
        }
    }


    private static Observable<CutFileBean>[] splitFile(ArrayList<CutFileBean> cutFileBeen) {
        File cacheFile = new File(mCacheGroupPath);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        Observable<CutFileBean>[] observables = new Observable[cutFileBeen.size()];
        for (int i = 0; i < observables.length; i++) {
            int index = i;
            observables[i] = Observable.create(subscriber -> {
                CutFileBean bean = cutFileBeen.get(index);
                readAndWriteFile(bean);
                subscriber.onNext(bean);
                subscriber.onCompleted();
            });
        }
        return observables;

    }


    private static ArrayList<CutFileBean> countSize(String origin, String cutPath, long length) {
        ArrayList<CutFileBean> arrays = new ArrayList<>();
        int size = (int) ((length + 0.0f) / singleFileSize);
        for (int i = 1; i <= size; i++) {
            CutFileBean cutFileBean = CutFileBean.getBean();
            long start = (long) ((i - 1) * singleFileSize);
            long end = length;
            if (i != size) {
                end = (long) (i * singleFileSize);
            }
            String splitPath = cutPath + File.separator + origin.replaceAll(File.separator, "_") + "_" + i;
            cutFileBean
                    .setStart(start)
                    .setEnd(end)
                    .setLength(end - start)
                    .setIndex(i)
                    .setOrigin(origin)
                    .setCutPath(splitPath);
            arrays.add(cutFileBean);
        }
        return arrays;
    }


    public static class CutFileBean {
        private long start;
        private long end;
        private long length;
        private int index;
        private String origin;
        private String cutPath;

        public String getOrigin() {
            return origin;
        }

        public CutFileBean setOrigin(String origin) {
            this.origin = origin;
            return this;
        }

        public String getCutPath() {
            return cutPath;
        }

        public CutFileBean setCutPath(String cutPath) {
            this.cutPath = cutPath;
            return this;
        }

        public long getStart() {
            return start;
        }

        public CutFileBean setStart(long start) {
            this.start = start;
            return this;
        }

        public long getEnd() {
            return end;
        }

        public CutFileBean setEnd(long end) {
            this.end = end;
            return this;
        }

        public static CutFileBean getBean() {
            return new CutFileBean();
        }

        public long getLength() {
            return length;
        }

        public CutFileBean setLength(long length) {
            this.length = length;
            return this;
        }

        public int getIndex() {
            return index;
        }

        public CutFileBean setIndex(int index) {
            this.index = index;
            return this;
        }

        @Override
        public String toString() {
            return "CutFileBean{" +
                    "start=" + start +
                    ", end=" + end +
                    ", length=" + length +
                    ", index=" + index +
                    '}';
        }
    }
}
