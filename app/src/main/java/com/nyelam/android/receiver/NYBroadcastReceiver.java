package com.nyelam.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NYBroadcastReceiver extends BroadcastReceiver {
    private INYBroadcastReceiver receiver;
    public NYBroadcastReceiver(){
        super();
    }

    public void setReceiver(INYBroadcastReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (receiver != null){
            receiver.receiverBroadcast(context, intent);
        }
    }

    public interface INYBroadcastReceiver {
        void receiverBroadcast(Context context, Intent intent);
    }
}
