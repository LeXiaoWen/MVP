package cree.mvp.ui.zoom.common.zoomable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ScrollingView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchyInflater;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;

import cree.mvp.net.netimg.CustomNetImage;
import cree.mvp.ui.zoom.common.ZoomPointBean;

/**
 * Created by idisfkj on 17/3/2.
 * Email : idisfkj@gmail.com.
 * githubUrl:https://github.com/idisfkj/Zoomable
 */

public class ZoomableDraweeView extends DraweeView<GenericDraweeHierarchy>
        implements ScrollingView, CustomNetImage {

    private static final Class<?> TAG = ZoomableDraweeView.class;

    private static final float HUGE_IMAGE_SCALE_FACTOR_THRESHOLD = 1.1f;
    private static final boolean DEFAULT_ALLOW_TOUCH_INTERCEPTION_WHILE_ZOOMED = true;

    private boolean mUseSimpleTouchHandling = false;

    private final RectF mImageBounds = new RectF();
    private final RectF mViewBounds = new RectF();

    private DraweeController mHugeImageController;
    private AnimatedZoomableController mZoomableController;
    private GestureDetector mTapGestureDetector;
    private boolean mAllowTouchInterceptionWhileZoomed =
            DEFAULT_ALLOW_TOUCH_INTERCEPTION_WHILE_ZOOMED;

    private final ControllerListener mControllerListener = new BaseControllerListener<Object>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable Object imageInfo,
                @Nullable Animatable animatable) {
            ZoomableDraweeView.this.onFinalImageSet();
        }

        @Override
        public void onRelease(String id) {
            ZoomableDraweeView.this.onRelease();
        }
    };

    private final ZoomableController.Listener mZoomableListener = new ZoomableController.Listener() {
        @Override
        public void onTransformChanged(Matrix transform) {
            ZoomableDraweeView.this.onTransformChanged(transform);
        }
    };

    private final GestureListenerWrapper mTapListenerWrapper = new GestureListenerWrapper();

    public ZoomableDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context);
        setHierarchy(hierarchy);
        init();
    }

    public ZoomableDraweeView(Context context) {
        super(context);
        inflateHierarchy(context, null);
        init();
    }

    public ZoomableDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateHierarchy(context, attrs);
        init();
    }

    public ZoomableDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflateHierarchy(context, attrs);
        init();
    }

    protected void inflateHierarchy(Context context, @Nullable AttributeSet attrs) {
        Resources resources = context.getResources();
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(resources)
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        GenericDraweeHierarchyInflater.updateBuilder(builder, context, attrs);
        setAspectRatio(builder.getDesiredAspectRatio());
        setHierarchy(builder.build());
    }

    private void init() {
        mZoomableController = createZoomableController();
        mZoomableController.setListener(mZoomableListener);
        mTapGestureDetector = new GestureDetector(getContext(), mTapListenerWrapper);
        mZoomableController.setOnDoubleClickZoomListener(mDoubleClickZoomListener);
    }

    // TODO: 2017/7/20 cree  start
    //设置点阵
    public void setPoint(ZoomPointBean pointBean) {
        Matrix matrix = new Matrix();
        matrix.setScale(pointBean.getSx(), pointBean.getSy(), pointBean.getPx(), pointBean.getPy());
        mZoomableController.setTransform(matrix);
    }

    public static final int sx = 0, px = 2, sy = 4, py = 5;

    /**
     * 创建需要返回的point对象
     *
     * @param matrix
     * @return
     */
    public static ZoomPointBean createPointBean(Matrix matrix) {
        float[] points = new float[9];
        matrix.getValues(points);
        return ZoomPointBean.getPointBean()
                .setSx(points[sx])
                .setPx(Math.abs(points[px]))
                .setSy(points[sy])
                .setPy(Math.abs(points[py]));
    }

    //得到点阵坐标
    private ZoomPointBean getImgPoint() {
        Matrix activeTransform = mZoomableController.getActiveTransform();
        ZoomPointBean zoomPointBean = createPointBean(activeTransform);
//        Log.e("cree", zoomPointBean.toString());//0sx 1no 2px=tx 3no 4sy 5py=ty 6no 7no 8no
        if (mZoomPointListener != null) {
            mZoomPointListener.onZoomPoint(zoomPointBean);
        }
        return zoomPointBean;

    }


    private ZoomPointListener mZoomPointListener;

    public ZoomPointListener getZoomPointListener() {
        return mZoomPointListener;
    }

    public void setOnZoomPointListener(ZoomPointListener zoomPointListener) {
        mZoomPointListener = zoomPointListener;
    }

    public interface ZoomPointListener {
        void onZoomPoint(ZoomPointBean zoomPointBean);
    }


    public void setImageURI(String url) {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(url)
                .build();
        //加载图片
        this.setController(draweeController);
    }


    private long mCurrentTime = 0;
    private boolean mIsLongClick = false;
    private final int LONG_CLICK = 0x11;
    private final int LONG_TIME = 1500;//毫秒为单位
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LONG_CLICK:
                    if (mIsLongClick) {
                        if (mOnLongClickListener != null) {
                            mOnLongClickListener.onLongClick(ZoomableDraweeView.this);
                        }
                    }
                    break;
            }
            return false;
        }
    });

    private OnLongClickListener mOnLongClickListener;
    private OnClickListener mOnClickListener;

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        mOnLongClickListener = l;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    private void clickEvent(MotionEvent event) {
        //---------------------------cree---------------------------------
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentTime = System.currentTimeMillis();
                mIsLongClick = true;
                Message message = new Message();
                message.what = LONG_CLICK;
                mHandler.sendMessageDelayed(message, LONG_TIME);
                break;
            case MotionEvent.ACTION_UP:
                mIsLongClick = false;
                mHandler.removeMessages(LONG_CLICK);
                if (System.currentTimeMillis() - mCurrentTime < 500) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(this);
                    }
                }
                break;
        }
        //---------------------------cree---------------------------------
    }

    /**
     * 设置是否启动捏合缩放
     *
     * @param isOpen
     */
    public void setOpenZoomed(boolean isOpen) {
        this.setAllowTouchInterceptionWhileZoomed(isOpen);
    }

    /**
     * 设置是否启动双击缩放以及还原
     *
     * @param isOpen
     */
    public void setOpenDoubleClient(boolean isOpen) {
        if (isOpen) {
            this.setTapListener(new DoubleTapGestureListener(this));
        } else {
            this.setTapListener(null);
        }
    }

    /**
     * 给animazoom控制器设置的双击回调
     */
    private AnimatedZoomableController.DoubleClickZoomListener mDoubleClickZoomListener = new AnimatedZoomableController.DoubleClickZoomListener() {
        @Override
        public void onZoomPoint(ZoomPointBean zoomPointBean) {
            if (mZoomPointListener != null) {
                mZoomPointListener.onZoomPoint(zoomPointBean);
            }
        }
    };
    // TODO: 2017/7/20 cree  end

    /**
     * Gets the original image bounds, in view-absolute coordinates.
     * <p>
     * <p> The original image bounds are those reported by the hierarchy. The hierarchy itself may
     * apply scaling on its own (e.g. due to scale type) so the reported bounds are not necessarily
     * the same as the actual bitmap dimensions. In other words, the original image bounds correspond
     * to the image bounds within this view when no zoomable transformation is applied, but including
     * the potential scaling of the hierarchy.
     * Having the actual bitmap dimensions abstracted away from this view greatly simplifies
     * implementation because the actual bitmap may change (e.g. when a high-res image arrives and
     * replaces the previously set low-res image). With proper hierarchy scaling (e.g. FIT_CENTER),
     * this underlying change will not affect this view nor the zoomable transformation in any way.
     */
    protected void getImageBounds(RectF outBounds) {
        getHierarchy().getActualImageBounds(outBounds);
    }

    /**
     * Gets the bounds used to limit the translation, in view-absolute coordinates.
     * <p>
     * <p> These bounds are passed to the zoomable controller in order to limit the translation. The
     * image is attempted to be centered within the limit bounds if the transformed image is smaller.
     * There will be no empty spaces within the limit bounds if the transformed image is bigger.
     * This applies to each dimension (horizontal and vertical) independently.
     * <p> Unless overridden by a subclass, these bounds are same as the view bounds.
     */
    protected void getLimitBounds(RectF outBounds) {
        outBounds.set(0, 0, getWidth(), getHeight());
    }

    /**
     * Sets a custom zoomable controller, instead of using the default one.
     */
    public void setZoomableController(ZoomableController zoomableController) {
        Preconditions.checkNotNull(zoomableController);
        mZoomableController.setListener(null);
        mZoomableController = (AnimatedZoomableController) zoomableController;
        mZoomableController.setListener(mZoomableListener);
    }

    /**
     * Gets the zoomable controller.
     * <p>
     * <p> Zoomable controller can be used to zoom to point, or to map point from view to image
     * coordinates for instance.
     */
    public ZoomableController getZoomableController() {
        return mZoomableController;
    }

    /**
     * Check whether the parent view can intercept touch events while zoomed.
     * This can be used, for example, to swipe between images in a view pager while zoomed.
     *
     * @return true if touch events can be intercepted
     */
    public boolean allowsTouchInterceptionWhileZoomed() {
        return mAllowTouchInterceptionWhileZoomed;
    }

    /**
     * If this is set to true, parent views can intercept touch events while the view is zoomed.
     * For example, this can be used to swipe between images in a view pager while zoomed.
     *
     * @param allowTouchInterceptionWhileZoomed true if the parent needs to intercept touches
     */
    public void setAllowTouchInterceptionWhileZoomed(
            boolean allowTouchInterceptionWhileZoomed) {
        mAllowTouchInterceptionWhileZoomed = allowTouchInterceptionWhileZoomed;
    }

    /**
     * Sets the tap listener.
     */
    public void setTapListener(GestureDetector.SimpleOnGestureListener tapListener) {
        mTapListenerWrapper.setListener(tapListener);
    }

    /**
     * Sets whether long-press tap detection is enabled.
     * Unfortunately, long-press conflicts with onDoubleTapEvent.
     */
    public void setIsLongpressEnabled(boolean enabled) {
        mTapGestureDetector.setIsLongpressEnabled(enabled);
    }

    /**
     * Sets the image controller.
     */
    @Override
    public void setController(@Nullable DraweeController controller) {
        setControllers(controller, null);
    }

    /**
     * Sets the controllers for the normal and huge image.
     * <p>
     * <p> The huge image controller is used after the image gets scaled above a certain threshold.
     * <p>
     * <p> IMPORTANT: in order to avoid a flicker when switching to the huge image, the huge image
     * controller should have the normal-image-uri set as its low-res-uri.
     *
     * @param controller          controller to be initially used
     * @param hugeImageController controller to be used after the client starts zooming-in
     */
    public void setControllers(
            @Nullable DraweeController controller,
            @Nullable DraweeController hugeImageController) {
        setControllersInternal(null, null);
        mZoomableController.setEnabled(false);
        setControllersInternal(controller, hugeImageController);
    }

    private void setControllersInternal(
            @Nullable DraweeController controller,
            @Nullable DraweeController hugeImageController) {
        removeControllerListener(getController());
        addControllerListener(controller);
        mHugeImageController = hugeImageController;
        super.setController(controller);
    }

    private void maybeSetHugeImageController() {
        if (mHugeImageController != null &&
                mZoomableController.getScaleFactor() > HUGE_IMAGE_SCALE_FACTOR_THRESHOLD) {
            setControllersInternal(mHugeImageController, null);
        }
    }

    private void removeControllerListener(DraweeController controller) {
        if (controller instanceof AbstractDraweeController) {
            ((AbstractDraweeController) controller)
                    .removeControllerListener(mControllerListener);
        }
    }

    private void addControllerListener(DraweeController controller) {
        if (controller instanceof AbstractDraweeController) {
            ((AbstractDraweeController) controller)
                    .addControllerListener(mControllerListener);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.save();
        canvas.concat(mZoomableController.getTransform());
        super.onDraw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getImgPoint();
        // TODO: 2017/7/20 cree  单击和长按事件的获取
        clickEvent(event);
        //cree end

        int a = event.getActionMasked();
        FLog.v(getLogTag(), "onTouchEvent: %d, view %x, received", a, this.hashCode());
        if (mTapGestureDetector.onTouchEvent(event)) {
            FLog.v(
                    getLogTag(),
                    "onTouchEvent: %d, view %x, handled by tap gesture detector",
                    a,
                    this.hashCode());
            return true;
        }

        if (mUseSimpleTouchHandling) {
            if (mZoomableController.onTouchEvent(event)) {
                return true;
            }
        } else if (mZoomableController.onTouchEvent(event)) {
            // Do not allow the parent to intercept touch events if:
            // - we do not allow swiping while zoomed and the image is zoomed
            // - we allow swiping while zoomed and the transform was corrected
            if ((!mAllowTouchInterceptionWhileZoomed && !mZoomableController.isIdentity()) ||
                    (!mAllowTouchInterceptionWhileZoomed && !mZoomableController.wasTransformCorrected())) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            FLog.v(
                    getLogTag(),
                    "onTouchEvent: %d, view %x, handled by zoomable controller",
                    a,
                    this.hashCode());
            return true;
        }
        if (super.onTouchEvent(event)) {
            FLog.v(getLogTag(), "onTouchEvent: %d, view %x, handled by the super", a, this.hashCode());
            return true;
        }
        // None of our components reported that they handled the touch event. Upon returning false
        // from this method, our parent won't send us any more events for this gesture. Unfortunately,
        // some componentes may have started a delayed action, such as a long-press timer, and since we
        // won't receive an ACTION_UP that would cancel that timer, a false event may be triggered.
        // To prevent that we explicitly send one last cancel event when returning false.
        MotionEvent cancelEvent = MotionEvent.obtain(event);
        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
        mTapGestureDetector.onTouchEvent(cancelEvent);
        mZoomableController.onTouchEvent(cancelEvent);
        cancelEvent.recycle();
        return false;
    }


    @Override
    public int computeHorizontalScrollRange() {
        return mZoomableController.computeHorizontalScrollRange();
    }

    @Override
    public int computeHorizontalScrollOffset() {
        return mZoomableController.computeHorizontalScrollOffset();
    }

    @Override
    public int computeHorizontalScrollExtent() {
        return mZoomableController.computeHorizontalScrollExtent();
    }

    @Override
    public int computeVerticalScrollRange() {
        return mZoomableController.computeVerticalScrollRange();
    }

    @Override
    public int computeVerticalScrollOffset() {
        return mZoomableController.computeVerticalScrollOffset();
    }

    @Override
    public int computeVerticalScrollExtent() {
        return mZoomableController.computeVerticalScrollExtent();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        FLog.v(getLogTag(), "onLayout: view %x", this.hashCode());
        super.onLayout(changed, left, top, right, bottom);
        updateZoomableControllerBounds();
    }

    private void onFinalImageSet() {
        FLog.v(getLogTag(), "onFinalImageSet: view %x", this.hashCode());
        if (!mZoomableController.isEnabled()) {
            updateZoomableControllerBounds();
            mZoomableController.setEnabled(true);
        }
    }

    private void onRelease() {
        FLog.v(getLogTag(), "onRelease: view %x", this.hashCode());
        mZoomableController.setEnabled(false);
    }

    protected void onTransformChanged(Matrix transform) {
        FLog.v(getLogTag(), "onTransformChanged: view %x, transform: %s", this.hashCode(), transform);
        maybeSetHugeImageController();
        invalidate();
    }

    protected void updateZoomableControllerBounds() {
        getImageBounds(mImageBounds);
        getLimitBounds(mViewBounds);
        mZoomableController.setImageBounds(mImageBounds);
        mZoomableController.setViewBounds(mViewBounds);
        FLog.v(
                getLogTag(),
                "updateZoomableControllerBounds: view %x, view bounds: %s, image bounds: %s",
                this.hashCode(),
                mViewBounds,
                mImageBounds);
    }

    protected Class<?> getLogTag() {
        return TAG;
    }

    protected AnimatedZoomableController createZoomableController() {
        return AnimatedZoomableController.newInstance();
    }

    public void setExperimentalSimpleTouchHandlingEnabled(boolean enabled) {
        mUseSimpleTouchHandling = enabled;
    }
}
