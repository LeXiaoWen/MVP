package cree.mvp.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Title:
 * Description:
 * CreateTime:2017/5/24  14:55
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class HeadAndFootAdapter<T, H extends BaseRecylerAdapter.BaseViewHolder, I extends BaseRecylerAdapter.BaseViewHolder, F extends BaseRecylerAdapter.BaseViewHolder> extends HeadAdapter<T, H, I> {
    private final int FOOT_TYPE = Integer.MAX_VALUE;

    public HeadAndFootAdapter(Context context, RecyclerView recyclerView, int lines) {
        super(context, recyclerView, lines);
        recyclerView.setLayoutManager(new HeadAndFootLayoutManager(context, lines, this, onDirection(), onReverse()));
    }

    @Override
    public int getItemType(int position) {
        return super.getItemType(position);
    }

    @Override
    public int getManyLayoutType(int position) {
        if (position == super.getItemCount())
            return FOOT_TYPE;
        return super.getManyLayoutType(position);
    }

    @Override
    public int getItemCount() {
        return 1 + super.getItemCount();
    }


    public int superCount() {
        return super.getItemCount();
    }

    @Override
    protected BaseViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOT_TYPE)
            return onCreateFootHolder(parent);
        return super.onCreateHolder(parent, viewType);
    }

    @Override
    protected void onBindHolder(BaseViewHolder holder, int position) {
        if (position == super.getItemCount())
            onBindFootHolder((F) holder);
        else
            super.onBindHolder(holder, position);
    }


    public abstract F onCreateFootHolder(ViewGroup viewGroup);


    public abstract void onBindFootHolder(F holder);


    private static class HeadAndFootLayoutManager extends HeadAdapter.HeadLayoutManager {


        public HeadAndFootLayoutManager(Context context, int spanCount, HeadAndFootAdapter adapter) {
            super(context, spanCount);
            init(spanCount, adapter);
        }

        public HeadAndFootLayoutManager(Context context, int spanCount, HeadAndFootAdapter adapter, int direction, boolean reverse) {
            super(context, spanCount, direction, reverse);
            init(spanCount, adapter);
        }

        @Override
        protected void init(int count) {

        }

        protected void init(int count, HeadAndFootAdapter adapter) {
            setSpanSizeLookup(new HeadAndFootLookup(count, adapter));
        }
    }

    private static class HeadAndFootLookup extends HeadAdapter.HeadLookup {
        HeadAndFootAdapter mAdapter;

        public HeadAndFootLookup(int size, HeadAndFootAdapter adapter) {
            super(size);
            mAdapter = adapter;
        }


        @Override
        public int getSpanSize(int position) {
            if (position == mAdapter.superCount()) {
                return size;
            }
            return super.getSpanSize(position);
        }
    }
}
