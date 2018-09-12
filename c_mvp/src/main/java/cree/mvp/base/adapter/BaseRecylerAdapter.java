package cree.mvp.base.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cree.mvp.R;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/5  11:02
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseRecylerAdapter<T, E extends BaseRecylerAdapter.BaseViewHolder> extends RecyclerView.Adapter<E> {
    protected Context mContext;

    protected final LayoutInflater mFrom;
    protected List<T> mDataBeen;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    public BaseRecylerAdapter(Context context) {
        mContext = context;
        mFrom = LayoutInflater.from(context);
    }


    public void setData(List<T> dataBeen) {
        mDataBeen = dataBeen;
    }

    public List<T> getData() {
        isArrayNull();
        return mDataBeen;
    }

    public T getData(int position) {
        isArrayNull();
        if (position >= mDataBeen.size())
            return null;
        return mDataBeen.get(position);
    }

    public void replaceData(List<T> dataBeen) {
        isArrayNull();
        mDataBeen.clear();
        mDataBeen.addAll(dataBeen);
    }

    public void add(T dataBean) {
        isArrayNull();
        mDataBeen.add(dataBean);
    }

    public void addAll(List<T> dataBeen) {
        isArrayNull();
        mDataBeen.addAll(dataBeen);
    }

    public void clear() {
        isArrayNull();
        mDataBeen.clear();
    }


    public void isArrayNull() {
        if (mDataBeen == null)
            mDataBeen = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    protected int onPositionChange() {
        return 0;
    }

    @Override
    public E onCreateViewHolder(ViewGroup parent, int viewType) {
        E holder = onCreateHolder(parent, viewType);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.getLayoutPosition() + onPositionChange(), holder.itemView));
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> mOnItemLongClickListener.onItemLongClick(holder.getLayoutPosition() + onPositionChange(), holder.itemView));
        }
        Drawable background = holder.itemView.getBackground();
        if (background == null)
            holder.itemView.setBackgroundResource(R.drawable.c_mvp_touch_bg);
        return holder;
    }

    @Override
    public void onBindViewHolder(E holder, int position) {
        onBindHolder(holder, position);
    }

    /**
     * 创建holder
     *
     * @param parent   列表的父布局
     * @param viewType 视图类型
     * @return
     */
    protected abstract E onCreateHolder(ViewGroup parent, int viewType);

    /**
     * 绑定数据
     *
     * @param holder   视图缓存类
     * @param position item位置
     */
    protected abstract void onBindHolder(E holder, int position);

    @Override
    public int getItemCount() {
        if (mDataBeen == null)
            return 0;
        return mDataBeen.size();
    }

    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Deprecated
        protected final <T extends View> T $(@IdRes int id) {
            return (T) itemView.findViewById(id);
        }

    }


    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position, View view);
    }

}
