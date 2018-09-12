package com.nyelam.android.doshop;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopProduct;

import java.util.ArrayList;
import java.util.List;


public class DoShopRecommendedAdapter extends RecyclerView.Adapter<DoShopRecommendedAdapter.MyViewHolder> {
    private List<DoShopProduct> data = new ArrayList<>();

    public DoShopRecommendedAdapter(List<DoShopProduct> data) {
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recommended_do_shop_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //holder.image.setImageResource(data.get(position).getImage());
        //holder.title.setText(data.get(position).getHeader());
        //holder.description.setText(data.get(position).getSubHeader());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            //description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
