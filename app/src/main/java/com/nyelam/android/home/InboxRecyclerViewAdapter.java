package com.nyelam.android.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.InboxData;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.Summary;
import com.nyelam.android.doshop.DoShopAdapter;
import com.nyelam.android.doshop.DoShopCategoryAdapter;
import com.nyelam.android.doshop.DoShopRecommendedAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.inbox.InboxActivity;
import com.nyelam.android.view.font.NYStrikethroughTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class InboxRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    private OnLoadMoreListener onLoadMoreListener;

    private Context context;
    private List<InboxData> inboxDatas;


    public InboxRecyclerViewAdapter(RecyclerView recyclerView, Context context) {
        this.context = context;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return inboxDatas.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_item_inbox, parent, false);
            return new ItemInboxViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemInboxViewHolder) {
            final InboxData inboxData = inboxDatas.get(position);
            ItemInboxViewHolder userViewHolder = (ItemInboxViewHolder) holder;
            userViewHolder.tvIdTicket.setText("# " + String.valueOf(inboxData.getTicketId()));
            userViewHolder.tvName.setText(inboxData.getnama());
            userViewHolder.tvSubject.setText(inboxData.getsubject());
            userViewHolder.tvStatus.setText(inboxData.getStatus());
            if(inboxData.getStatus().toLowerCase().equals("open")){
                userViewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.ny_green1));
            }else{
                userViewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InboxActivity.class);
                    intent.putExtra("ticketid", String.valueOf(inboxData.getTicketId()));
                    intent.putExtra("title", String.valueOf(inboxData.getsubject()));
                    intent.putExtra("status", String.valueOf(inboxData.getStatus()));
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return inboxDatas == null ? 0 : inboxDatas.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void clear() {
        this.inboxDatas = new ArrayList<>();
        isLoading = false;
    }

    public void addScroll(){
        this.inboxDatas.add(null);
        notifyItemInserted(inboxDatas.size()-1);
    }

    public void removeScroll(){
        if(this.inboxDatas.size() != 0){
            for(int i=0; i<inboxDatas.size(); i++){
                if(inboxDatas.get(i) == null){
                    this.inboxDatas.remove(inboxDatas.size()-1);
                    notifyItemRemoved(inboxDatas.size());
                }
            }
        }
    }

    public void addResult(InboxData inboxList) {
        if (this.inboxDatas == null) {
            this.inboxDatas = new ArrayList<>();
        }
        this.inboxDatas.add(inboxList);
    }

    public void addResults(List<InboxData> inboxList) {
        if (this.inboxDatas == null) {
            this.inboxDatas = new ArrayList<>();
        }
        this.inboxDatas.addAll(inboxList);
    }

    // "Loading item" ViewHolder
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    // "Normal item" ViewHolder
    private class ItemInboxViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIdTicket;
        public TextView tvName;
        public TextView tvSubject;
        public TextView tvStatus;
        public ItemInboxViewHolder(View view) {
            super(view);
            tvIdTicket = (TextView) view.findViewById(R.id.tv_id_ticket);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvSubject = (TextView) view.findViewById(R.id.tv_subject);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);
        }
    }
}