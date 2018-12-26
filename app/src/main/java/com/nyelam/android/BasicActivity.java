package com.nyelam.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.nyelam.android.data.InboxData;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.inbox.InboxActivity;
import com.nyelam.android.receiver.NYBroadcastReceiver;
import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/3/2018.
 */

public abstract class BasicActivity extends AppCompatActivity implements NYBroadcastReceiver.INYBroadcastReceiver {

    private ImageView backImageView;
    protected ProgressDialog pDialog;
    private NYBroadcastReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.loading));
    }

    protected void initToolbar(boolean isHomeBackEnable) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_button_white);
        setSupportActionBar(toolbar);
        if (isHomeBackEnable) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
            toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
        }
    }

    protected void backEnable() {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.nyelam.android.CHAT_SERVICE");
            receiver = new NYBroadcastReceiver();
            receiver.setReceiver(this);
            registerReceiver(receiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void receiverBroadcast(Context context, Intent intent) {
        try {
            final String title = intent.getStringExtra(NYHelper.TITLE);
            String body = intent.getStringExtra(NYHelper.BODY);
            final String ticketId = intent.getStringExtra(NYHelper.TICKET_ID);

            if (this instanceof InboxActivity) {
                InboxActivity activity = (InboxActivity) this;
                if (String.valueOf(ticketId).equalsIgnoreCase(activity.getTicketId())) {
                    return;
                }
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);
            Alerter.create(BasicActivity.this)
                    .setTitle(title)
                    .enableSwipeToDismiss()
                    .setText(body)
                    .setIcon(bitmap)
                    .setBackgroundColorRes(R.color.colorAccent)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            LoginStorage storage = new LoginStorage(BasicActivity.this);
//                            User user = storage.user;
                            Intent i = new Intent(BasicActivity.this, InboxActivity.class);
                            i.putExtra(NYHelper.TITLE, title);
                            i.putExtra(NYHelper.TICKET_ID, ticketId);
                            startActivity(i);
                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
