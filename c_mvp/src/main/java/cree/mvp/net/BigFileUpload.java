package cree.mvp.net;

import android.app.Activity;

import java.io.File;

import javax.annotation.Nonnull;

import cree.mvp.base.bean.BaseBean;
import cree.mvp.ui.dialog.CrDialog;
import cree.mvp.util.data.BigFileUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Title:大文件上传主文件
 * Description:
 * CreateTime:2017/7/17  11:38
 *
 * @author luyongjiang
 * @version 1.0
 */
public class BigFileUpload {


    private interface BigSplitFileListener {

        void onBodyAllresult(RequestBody[] bodys, BigFileUtils.CutFileBean[] cut);
    }

    public interface BigSplitObservable<T extends BaseBean> {
        Observable<T>[] getObservables(RequestBody[] requestBody, BigFileUtils.CutFileBean[] cut);

        void onSingleUISuccess(T ts);

        void onError(Throwable e);

        void onUpUIStart();

        void onUpUIEnd();
    }


    public static <T extends BaseBean> void upFile(Activity activity, String filePath, boolean isShowDialog, @Nonnull BigSplitObservable<T> bigSplitObservable) {
        getUpLoadBody(activity, filePath, isShowDialog, (bodys, cut) -> {
            Observable[] observables = bigSplitObservable.getObservables(bodys, cut);
            if (observables == null) {
                try {
                    throw new NullPointerException("BigSplitObservable.getObservables()方法返回值为空");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            activity.runOnUiThread(() -> bigSplitObservable.onUpUIStart());
            Observable.merge(observables, 1).compose(RxSchedulers.io_main()).subscribe(new Subscriber() {
                @Override
                public void onCompleted() {
                    File file = new File(BigFileUtils.mCacheGroupPath);
                    File[] files = file.listFiles();
                    for (File childFile : files) {
                        childFile.delete();
                    }
                    bigSplitObservable.onUpUIEnd();
                }

                @Override
                public void onError(Throwable e) {
                    bigSplitObservable.onError(e);
                }

                @Override
                public void onNext(Object o) {
                    bigSplitObservable.onSingleUISuccess((T) o);
                }
            });
        });
    }

    private static void getUpLoadBody(Activity activity, String filePath, boolean isShowDialog, @Nonnull BigSplitFileListener bigSplitFileListener) {
        CrDialog crDialog = new CrDialog.Builder(activity)
                .setTitle("大文件上传")
                .setMessage("拆分中...")
                .isNoAutoCancel()
                .isProgress().create();
        BigFileUtils.init(activity, filePath, new BigFileUtils.OnBigFileListener() {
            @Override
            public void onSplitUiStart() {
                if (isShowDialog) {
                    crDialog.show();
                }
            }

            @Override
            public void onSplitAllReuslt(BigFileUtils.CutFileBean[] cut) {
                RequestBody[] bodys = new RequestBody[cut.length];
                for (int i = 0; i < cut.length; i++) {
                    bodys[i] = RequestBody.create(MediaType.parse("multipart/form-data"), new File(cut[i].getCutPath()));
                }
                bigSplitFileListener.onBodyAllresult(bodys, cut);
            }

            @Override
            public void onSplitUiEnd() {
                dismiss(crDialog, isShowDialog);
            }

            @Override
            public void onSplitError(Throwable throwable) {
                dismiss(crDialog, isShowDialog);
            }
        });
    }


    private static void dismiss(CrDialog crDialog, boolean isShow) {
        if (isShow && crDialog.isShowing()) {
            crDialog.dismiss();
        }
    }
}
