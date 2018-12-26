package com.nyelam.android.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.doshop.DoShopAdapter;
import com.nyelam.android.doshop.DoShopCategoryAdapter;
import com.nyelam.android.doshop.DoShopRecommendedAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.inbox.InboxActivity;
import com.nyelam.android.storage.LoginStorage;
import com.nyelam.android.view.font.NYStrikethroughTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class InboxRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
            userViewHolder.tvIdTicket.setText("#" + String.valueOf(inboxData.getTicketId()));
            userViewHolder.tvSubject.setText(inboxData.getsubject());
            if (inboxData.getnama() != null) {
                userViewHolder.tvName.setText(inboxData.getnama());
            }
            if (inboxData.getStatus() != null) {
                userViewHolder.tvStatus.setText(inboxData.getStatus());
            }
            if (inboxData.getDate() != null) {
                if (NYHelper.isToday(inboxData.getDate())) {
                    String DISPLAY_DATE_FORMAT = "h:mm a";
                    SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
                    userViewHolder.tvDate.setText(sdf.format(inboxData.getDate()));
                } else {
                    String DISPLAY_DATE_FORMAT = "dd/MM/yyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
                    userViewHolder.tvDate.setText(sdf.format(inboxData.getDate()));
                }
            }
            //TODO load firebase count read chat
//            initLoadDataFirebase(inboxData);
//            if (inboxData.getNewMessage() != null) {
//                NYLog.e("cek count msg = " + inboxData.getNewMessage());
//                if (!inboxData.getNewMessage().equals("0")) {
//                    userViewHolder.layoutNotifBadge.setVisibility(View.VISIBLE);
//                    userViewHolder.tvCountRead.setText(inboxData.getNewMessage());
//                } else {
//                    userViewHolder.layoutNotifBadge.setVisibility(View.INVISIBLE);
//                }
//            }
            if (inboxData.getStatus() != null) {
                if (inboxData.getStatus().toLowerCase().equals("open")) {
                    userViewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.ny_green1));
                } else {
                    userViewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                }
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

    public void addScroll() {
        this.inboxDatas.add(null);
        notifyItemInserted(inboxDatas.size() - 1);
    }

    public void removeScroll() {
        if (this.inboxDatas.size() != 0) {
            for (int i = 0; i < inboxDatas.size(); i++) {
                if (inboxDatas.get(i) == null) {
                    this.inboxDatas.remove(inboxDatas.size() - 1);
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

//    private void initLoadDataFirebase(final InboxData inboxData) {
//        final LoginStorage loginStorage = new LoginStorage(context);
//        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        databaseReference.child(NYHelper.ARGS_THREAD(context)).child(loginStorage.user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        NYLog.e("Cek value = " + dataSnapshot1.getValue());
//                        if (String.valueOf(dataSnapshot1.getKey()).equals(String.valueOf(inboxData.getTicketId()))) {
//                            String count = dataSnapshot1.child("new_message").getValue(String.class);
//                            NYLog.e("Cek count thread = " + count);
//                            inboxData.setNewMessage(count);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    // "Normal item" ViewHolder
    private class ItemInboxViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIdTicket;
        public TextView tvName;
        public TextView tvSubject;
        public TextView tvStatus;
        public TextView tvDate;
        public RelativeLayout layoutNotifBadge;
        public TextView tvCountRead;

        public ItemInboxViewHolder(View view) {
            super(view);
            tvIdTicket = (TextView) view.findViewById(R.id.tv_id_ticket);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvSubject = (TextView) view.findViewById(R.id.tv_subject);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            layoutNotifBadge = view.findViewById(R.id.layout_notif_chat);
            tvCountRead = (TextView) view.findViewById(R.id.count_read_tv);
        }
    }
}