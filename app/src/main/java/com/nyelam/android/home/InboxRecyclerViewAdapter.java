package com.nyelam.android.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class InboxRecyclerViewAdapter extends RecyclerView.Adapter<InboxRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<InboxData> inboxData;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvSubject;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvSubject = (TextView) view.findViewById(R.id.tv_subject);
        }
    }

    public InboxRecyclerViewAdapter(Context context, List<InboxData> inboxData) {
        this.context = context;
        this.inboxData = inboxData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_inbox, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        InboxData singleInbox = inboxData.get(position);
        holder.tvName.setText(singleInbox.getnama());
        holder.tvSubject.setText(singleInbox.getsubject());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InboxActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inboxData.size();
    }
}