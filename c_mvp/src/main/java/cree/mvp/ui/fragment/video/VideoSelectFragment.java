package cree.mvp.ui.fragment.video;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedList;

import cn.finalteam.rxgalleryfinal.ui.widget.MarginDecoration;
import cree.mvp.R;
import cree.mvp.base.adapter.BaseRecylerAdapter;
import cree.mvp.net.netimg.NetImageView;
import cree.mvp.ui.dialog.CrDialog;
import cree.mvp.ui.fragment.video.bean.VideoSelectBean;
import cree.mvp.util.data.FileUtils;
import cree.mvp.util.data.VideoUtils;
import cree.mvp.util.permissions.PermissionsUtils;
import cree.mvp.util.permissions.rx.PerSubscriber;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Title:
 * Description:
 * CreateTime:2017/7/10  17:36
 *
 * @author luyongjiang
 * @version 1.0
 */
public class VideoSelectFragment extends Fragment implements BaseRecylerAdapter.OnItemClickListener, FileUtils.TraverseListListener {
    private View mRootView;
    private CrDialog mCrDialog;
    private LinkedList<File> mPathList;
    private RecyclerView mRecyclerView;
    private VideoListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.cmvp_fr_video_select, container, false);
            mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_videoList);
            mRecyclerView.addItemDecoration(new MarginDecoration(getActivity()));
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            mAdapter = new VideoListAdapter(getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
        }
        init();
        return mRootView;
    }

    private void init() {
        mCrDialog = new CrDialog.Builder(getActivity()).isNoAutoCancel()
                .isProgress()
                .setTitle("遍历中...")
                .setMessage("...")
                .setCancelButton("取消", (dialog, which) -> {
                    dismiss();
                }).create();
        mCrDialog.setOnDismissListener(dialog -> {
            dismiss();
        });
        PermissionsUtils.getInstance(getActivity()).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscrbe(new PerSubscriber() {
            @Override
            public void onCallError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onCallNext(Boolean aBoolean) {
                mCrDialog.show();
                findAllFiles();
            }
        });
    }

    private void dismiss() {
        mCrDialog.dismiss();
        VideoUtils.getInstance().onDestroy();
        getActivity().finish();
    }

    private void findAllFiles() {
        Observable.create((Observable.OnSubscribe<LinkedList<File>>) subscriber -> {
            LinkedList<File> mkv = FileUtils.getSDTheEndForFiles(this, ".mp4", ".rmvb", ".avi", "mkv");
            subscriber.onNext(mkv);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LinkedList<File>>() {
                    @Override
                    public void onCompleted() {
                        mCrDialog.setOnDismissListener(null);
                        mCrDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mCrDialog.setOnDismissListener(null);
                        mCrDialog.dismiss();
                    }

                    @Override
                    public void onNext(LinkedList<File> strings) {
                        mPathList = strings;
                        mAdapter.setData(mPathList);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
        VideoUtils.getInstance().onDestroy();
    }


    //---------------------------adapter---------------------------------
    private class VideoListAdapter extends BaseRecylerAdapter<File, VideoHolder> {
        public VideoListAdapter(Context context) {
            super(context);
        }

        @Override
        protected VideoHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new VideoHolder(mFrom.inflate(R.layout.cmvp_i_fr_video, parent, false));
        }

        @Override
        protected void onBindHolder(VideoHolder holder, int position) {
            holder.tvTitle.setText(getData(position).getName());
            holder.tvPath.setText(getData(position).getAbsolutePath());
            holder.tvPath.setSelected(true);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                AnimationDrawable animationDrawable = (AnimationDrawable) mContext.getDrawable(R.drawable.cmvp_loading_anim);
                holder.ivPreview.setImageDrawable(animationDrawable);
                animationDrawable.start();
            }
            VideoUtils.getInstance().setLocalVideoBitmapForImageView(getActivity(), getData(position).getAbsolutePath(), holder.ivPreview);
        }
    }


    private class VideoHolder extends BaseRecylerAdapter.BaseViewHolder {
        TextView tvTitle;
        TextView tvPath;
        NetImageView ivPreview;

        public VideoHolder(View itemView) {
            super(itemView);
            tvTitle = $(R.id.tv_title);
            tvPath = $(R.id.tv_path);
            ivPreview = $(R.id.iv_preview);
        }
    }

    private OnVideoSelectListener mOnVideoSelectListener;


    public void setOnVideoSelectListener(OnVideoSelectListener onVideoSelectListener) {
        mOnVideoSelectListener = onVideoSelectListener;
    }

    // TODO: 2017/7/11 listener
    //---------------------------OnItemClickListener---------------------------------
    @Override
    public void onItemClick(int position, View view) {
        if (mOnVideoSelectListener != null) {
            VideoSelectBean videoSelectBean = new VideoSelectBean();
            videoSelectBean.setFile(mAdapter.getData(position));
            mOnVideoSelectListener.onVideoSelect(videoSelectBean);
        }
    }

    public interface OnVideoSelectListener {
        void onVideoSelect(VideoSelectBean videoSelectBean);
    }

    //---------------------------TraverseListListener---------------------------------
    @Override
    public void onFile(File file) {
        getActivity().runOnUiThread(() -> {
            if (mCrDialog != null && mCrDialog.isShowing()) {
                mCrDialog.setContentMessage(file.getName());
            }
        });
    }
}
