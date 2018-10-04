package com.nyelam.android.doshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopCategoryModule;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.DoShopProductModule;

import java.util.ArrayList;
import java.util.List;

public class DoShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Object> items;
    private DoShopList doShopListModule = new DoShopList();

    public static final int VIEW_TYPE_CATEGORY = 1;
    public static final int VIEW_TYPE_RECOMMENDED = 2;

    public void addModules(DoShopList doShopList) {
        doShopListModule = doShopList;
    }

    public DoShopAdapter(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case VIEW_TYPE_CATEGORY:
                view = inflater.inflate(R.layout.do_shop_category_list, parent, false);
                holder = new CategoryViewHolder(view);
                break;
            case VIEW_TYPE_RECOMMENDED:
                view = inflater.inflate(R.layout.do_shop_recommended_list, parent, false);
                holder = new RecommendedViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.do_shop_recommended_list, parent, false);
                holder = new RecommendedViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_CATEGORY)
            categoryView((CategoryViewHolder) holder);
        else if (holder.getItemViewType() == VIEW_TYPE_RECOMMENDED)
            recommendedView((RecommendedViewHolder) holder);
    }

    private void categoryView(CategoryViewHolder holder) {
        DoShopCategoryAdapter adapter1 = new DoShopCategoryAdapter(doShopListModule.getCategories());
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        holder.recyclerView.setAdapter(adapter1);
    }

    private void recommendedView(RecommendedViewHolder holder) {
        DoShopRecommendedAdapter adapter = new DoShopRecommendedAdapter(context, doShopListModule.getRecommended());
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 )
            return VIEW_TYPE_CATEGORY;
        if (position == 1)
            return VIEW_TYPE_RECOMMENDED;
        return VIEW_TYPE_CATEGORY;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        CategoryViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
        }
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        RecommendedViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
        }
    }
}
