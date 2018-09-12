package cree.mvp.ui.selectimg;

import android.content.Context;
import android.support.annotation.Nullable;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.event.BaseResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.ui.RxGalleryListener;
import cn.finalteam.rxgalleryfinal.ui.base.IMultiImageCheckedListener;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/20  15:26
 *
 * @author luyongjiang
 * @version 1.0
 */
public class SelectImg {
    public static ImageLoaderType imageLoaderType;

    public static void init() {
        imageLoaderType = ImageLoaderType.FRESCO;
    }

    public static void openSingleGallery(Context context, AsResultSubscriber<ImageRadioResultEvent> sub) {
        RxGalleryFinal
                .with(context)
                .image()
                .radio()
                .crop()
                .imageLoader(imageLoaderType)
                .subscribe(sub).openGallery();
    }

    public static <T extends BaseResultEvent> void openMultipleGallery(Context context, int maxSize, AsResultSubscriber<ImageMultipleResultEvent> sub, @Nullable IMultiImageCheckedListener checkedListener) {
        //自定义多选
        RxGalleryFinal
                .with(context)
                .image()
                .multiple()
                .maxSize(maxSize)
                .imageLoader(imageLoaderType)
                .subscribe(sub)
                .openGallery();

        if (checkedListener == null) {
            RxGalleryListener.getInstance().setMultiImageCheckedListener(new IMultiImageCheckedListener() {
                @Override
                public void selectedImg(Object t, boolean isChecked) {
                    //这个主要点击或者按到就会触发，所以不建议在这里进行 Toast
                        /*  Toast.makeText(getBaseContext(),"->"+isChecked,Toast.LENGTH_SHORT).show();*/
                }

                @Override
                public void selectedImgMax(Object t, boolean isChecked, int maxSize) {
                    //                Toast.makeText(co, "你最多只能选择" + maxSize + "张图片", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            RxGalleryListener.getInstance().setMultiImageCheckedListener(checkedListener);
        }
    }


}
