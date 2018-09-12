package cree.mvp.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Title:
 * Description:
 * CreateTime:2017/6/1  09:11
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class HeadAndFootListenerAdapter<T, H extends BaseRecylerAdapter.BaseViewHolder, I extends BaseRecylerAdapter.BaseViewHolder, F extends BaseRecylerAdapter.BaseViewHolder> extends HeadAndFootAdapter<T, H, I, F> {

    private OnHeadHolderListener<H> mOnHeadHolderListener;
    private OnFoodHolderListener<F> mOnFoodHolderListener;

    public HeadAndFootListenerAdapter(Context context, RecyclerView recyclerView, int lines) {
        super(context, recyclerView, lines);
    }


    @Override
    public void onBindHeadHolder(H holder) {
        holder.itemView.setOnClickListener(null);
        if (mOnHeadHolderListener != null) {
            mOnHeadHolderListener.onHeadView(holder);
        }
        onBindHead(holder);
    }

    @Override
    public void onBindFootHolder(F holder) {
        holder.itemView.setOnClickListener(null);
        if (mOnFoodHolderListener != null) {
            mOnFoodHolderListener.onHeadView(holder);
        }
        onBindFoot(holder);
    }

    public abstract void onBindHead(H holder);

    public abstract void onBindFoot(F holder);


    public interface OnHeadHolderListener<H> {
        void onHeadView(H holder);
    }

    public interface OnFoodHolderListener<F> {
        void onHeadView(F holder);
    }

    public HeadAndFootListenerAdapter<T, H, I, F> setOnHeadHolderListener(OnHeadHolderListener<H> onHeadHolderListener) {
        mOnHeadHolderListener = onHeadHolderListener;
        return this;
    }

    public HeadAndFootListenerAdapter<T, H, I, F> setOnFoodHolderListener(OnFoodHolderListener<F> onFoodHolderListener) {
        mOnFoodHolderListener = onFoodHolderListener;
        return this;
    }


    public abstract static class OnListenerItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(int position, View view) {
            onListenerItemClick(position-1, view);
        }

        public abstract void onListenerItemClick(int position, View view);
    }


    public abstract static class OnListenreItemLongClickListener implements OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(int position, View view) {
            return onListenerItemLongClick(position - 1, view);
        }

        public abstract boolean onListenerItemLongClick(int position, View view);


    }

}
