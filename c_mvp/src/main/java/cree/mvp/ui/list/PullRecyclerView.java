package cree.mvp.ui.list;

import android.content.Context;
import android.util.AttributeSet;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * Title:RecyclerView
 * Description:支持下拉刷新和上拉加载的RecyclerView
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/14  11:48
 *
 * @author luyongjiang
 * @version 1.0
 */
public class PullRecyclerView extends XRecyclerView implements XRecyclerView.LoadingListener {
    private boolean isRefresh = false;
    private boolean isLoadMore = true;
    private OnLoadingListener mOnLoadingListener;



    public PullRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public PullRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.setLoadingListener(this);
    }

    @Override
    public void loadMoreComplete() {
        super.loadMoreComplete();
        isLoadMore = false;
    }

    @Override
    public void refreshComplete() {
        super.refreshComplete();
        isRefresh = false;
    }


    @Override
    public void onRefresh() {
        if (mOnLoadingListener != null) {
            isRefresh = true;
            mOnLoadingListener.onRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (mOnLoadingListener != null) {
            isLoadMore = true;
            mOnLoadingListener.onLoadMore();
        }
    }

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        mOnLoadingListener = onLoadingListener;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    @Deprecated
    @Override
    public void setLoadingListener(LoadingListener listener) {
        super.setLoadingListener(listener);
    }

    /**
     * 使用这个回调接口
     */
    public interface OnLoadingListener extends XRecyclerView.LoadingListener {

    }

}
