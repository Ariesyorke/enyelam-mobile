package com.nyelam.android.inbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.InboxData;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.InboxRecyclerViewAdapter;
import com.nyelam.android.home.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.himanshusoni.chatmessageview.ChatMessageView;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.nyelam.android.helper.NYHelper.TAG;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, ISLOADING = 2;

    private boolean isLoading;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;

    private OnLoadMoreListener onLoadMoreListener;

    private List<ChatMessage> mMessages;
    private Context mContext;

    public ChatMessageAdapter(RecyclerView recyclerView, Context context) {
        mContext = context;

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
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage item = mMessages.get(position);
        if(item != null){
            if (item.isMine()){
                return MY_MESSAGE;
            }else{
                return OTHER_MESSAGE;
            }
        }else{
            return ISLOADING;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MY_MESSAGE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_mine_message, parent, false);
            return new MessageHolder(view);
        } else if(viewType == OTHER_MESSAGE){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_other_message, parent, false);
            return new MessageHolder(view);
        } else if(viewType == ISLOADING){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MessageHolder) {
            ChatMessage chatMessage = mMessages.get(position);
            final MessageHolder messageHolder = (MessageHolder) holder;
            if (chatMessage.isImage()) {
                messageHolder.ivImage.setVisibility(View.VISIBLE);
            } else {
                messageHolder.ivImage.setVisibility(View.GONE);
            }

            if(chatMessage.getUserName() != null) {
                messageHolder.tvUser.setText(chatMessage.getUserName());
            }

            if(chatMessage.getContent() != null) {
                messageHolder.tvMessage.setText(chatMessage.getContent());
            }

            if (chatMessage.getImageFile() != null) {
                Picasso.get().load(chatMessage.getImageFile()).placeholder(R.drawable.example_pic).noFade().into(messageHolder.ivImage);
                messageHolder.ivImage.setVisibility(View.VISIBLE);

                /*//SET IMAGE
                final NYApplication application = (NYApplication) mContext.getApplicationContext();
                Bitmap b = application.getCache("drawable://"+R.drawable.bg_placeholder);
                if(b != null) {
                    messageHolder.ivImage.setImageBitmap(b);
                } else {
                    messageHolder.ivImage.setImageResource(R.drawable.bg_placeholder);
                }

                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
                if (NYHelper.isStringNotEmpty(chatMessage.getImageFile())) {

                    if (application.getCache(chatMessage.getImageFile()) != null){
                        messageHolder.ivImage.setImageBitmap(application.getCache(chatMessage.getImageFile()));
                    } else {

                        ImageLoader.getInstance().loadImage(chatMessage.getImageFile(), NYHelper.getOption(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                messageHolder.ivImage.setImageResource(R.drawable.example_pic);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                messageHolder.ivImage.setImageBitmap(loadedImage);
                                application.addCache(imageUri, loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                messageHolder.ivImage.setImageResource(R.drawable.example_pic);
                            }
                        });

                        ImageLoader.getInstance().displayImage(chatMessage.getImageFile(), messageHolder.ivImage , NYHelper.getOption());
                    }

                } else {
                    messageHolder.ivImage.setImageResource(R.drawable.example_pic);
                }*/
            }else {
                messageHolder.ivImage.setVisibility(View.GONE);
            }

            if(chatMessage.getDate() != null) {
                if(NYHelper.isToday(chatMessage.getDate())){
                    String DISPLAY_DATE_FORMAT = "h:mm a";
                    SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
                    messageHolder.tvTime.setText(sdf.format(chatMessage.getDate()));
                }else {
                    String DISPLAY_DATE_FORMAT = "dd/MM/yyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
                    messageHolder.tvTime.setText(sdf.format(chatMessage.getDate()));
                }
            }

            messageHolder.chatMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    // "Loading item" ViewHolder
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvTime;
        TextView tvUser;
        ImageView ivImage;
        ChatMessageView chatMessageView;

        MessageHolder(View itemView) {
            super(itemView);

            chatMessageView = (ChatMessageView) itemView.findViewById(R.id.chatMessageView);
            tvUser = (TextView) itemView.findViewById(R.id.tv_user);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void clear() {
        this.mMessages = new ArrayList<>();
        isLoading = false;
    }

    public void addScroll(){
        this.mMessages.add(null);
        notifyItemInserted(mMessages.size()-1);
    }

    public void removeScroll(){
        if(this.mMessages.size() != 0){
            for(int i=0; i<mMessages.size(); i++){
                if(mMessages.get(i) == null){
                    this.mMessages.remove(mMessages.size()-1);
                    notifyItemRemoved(mMessages.size());
                }
            }
        }
    }

    public void addResult(ChatMessage chatMessage) {
        if (this.mMessages == null) {
            this.mMessages = new ArrayList<>();
        }
        this.mMessages.add(chatMessage);
    }

    public void addResult(List<ChatMessage> chatMessages) {
        if (this.mMessages == null) {
            this.mMessages = new ArrayList<>();
        }
        this.mMessages.addAll(chatMessages);
    }
}