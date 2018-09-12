package cree.mvp.base.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Title:
 * Description:
 * CreateTime:2017/5/24  10:11
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class HeadAdapter<T, H extends BaseRecylerAdapter.BaseViewHolder, I extends BaseRecylerAdapter.BaseViewHolder> extends BaseRecylerAdapter<T, BaseRecylerAdapter.BaseViewHolder> implements Direction {
    private static final int HEAD_TYPE = 0;
    private static final int ITEM_TYPE = 1;
    private static final int ITEM_LINES = 1;

    public HeadAdapter(Context context, RecyclerView recyclerView, int lines) {
        super(context);
        if (lines < 1)
            lines = 1;
        recyclerView.setLayoutManager(new HeadLayoutManager(context, lines, onDirection(), onReverse()));
    }

    @Override
    public int onDirection() {
        return LinearLayout.VERTICAL;
    }

    @Override
    public boolean onReverse() {
        return false;
    }

    @Override
    protected BaseViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD_TYPE)
            return onCreateHeadHolder(parent);
        return onCreateItemHolder(parent, viewType);
    }

    @Override
    protected void onBindHolder(BaseViewHolder holder, int position) {
        if (onPositionChange() == 0 && position == 0) {
            holder.itemView.setOnClickListener(null);
            onBindHeadHolder((H) holder);
        } else {
            onBindItemHolder((I) holder, position - 1);
        }
    }


    @Override
    public int getItemCount() {
        return 1 + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEAD_TYPE;
        else
            return getItemType(position);
    }

    public int getItemType(int position) {
        return getManyLayoutType(position);
    }

    public int getManyLayoutType(int position) {
        return ITEM_TYPE;
    }


    public abstract H onCreateHeadHolder(ViewGroup viewGroup);


    public abstract I onCreateItemHolder(ViewGroup viewGroup, int viewType);

    public abstract void onBindHeadHolder(H holder);

    public abstract void onBindItemHolder(I holder, int position);


    public static class HeadLayoutManager extends GridLayoutManager {

        public HeadLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init(1);
        }

        public HeadLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
            init(spanCount);
        }

        public HeadLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
            init(spanCount);
        }

        public HeadLayoutManager(Context context) {
            this(context, 1);
        }

        protected void init(int count) {
            setSpanSizeLookup(new HeadLookup(count));
        }
    }

    public static class HeadLookup extends GridLayoutManager.SpanSizeLookup {
        int size = 1;

        public HeadLookup(int size) {
            this.size = size;
        }

        @Override
        public int getSpanSize(int position) {
            if (position == HeadAdapter.HEAD_TYPE)
                return size;
            else
                return HeadAdapter.ITEM_LINES;
        }
    }
}
