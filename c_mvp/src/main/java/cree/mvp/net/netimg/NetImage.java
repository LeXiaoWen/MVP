package cree.mvp.net.netimg;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.HashSet;
import java.util.Set;

import cree.mvp.net.Api;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/17  11:06
 *
 * @author luyongjiang
 * @version 1.0
 */
public class NetImage {
    public static void init(Context context) {
        Set<RequestListener> listeners = new HashSet<>();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(context, Api.getInstance().getOkHttpClient())
                .setDownsampleEnabled(true)
                .setRequestListeners(listeners)
                .build();

        Fresco.initialize(context, config);
    }

    /**
     * 以压缩的形式加载预览图
     *
     * @param uri        文件uri地址
     * @param draweeView netimageview控件
     * @param width      需要压缩的宽度
     * @param height     需要压缩的高度
     */
    public static void load(Uri uri, NetImageView draweeView, int width, int height) {
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(width, height))
                        //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                        // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();

        draweeView.setController(controller);
    }

    /**
     * 以压缩的形式加载预览图
     *
     * @param path       文件本地路径
     * @param draweeView netimageview控件
     * @param width      需要压缩的宽度
     * @param height     需要压缩的高度
     */
    public static void load(String path, NetImageView draweeView, int width, int height) {
        Uri uri = null;
        if (!path.startsWith("http://")) {
            uri = Uri.parse("file://" + path);
        } else {
            uri = Uri.parse(path);
        }
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(width, height))
                        //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                        // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();

        draweeView.setController(controller);
    }

    /**
     * 清理所有的RAM缓存
     */
    public static void clearCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();
    }
}
