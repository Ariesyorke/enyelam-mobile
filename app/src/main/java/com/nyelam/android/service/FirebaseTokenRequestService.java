package com.nyelam.android.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.http.NYSendFirebaseTokenRequest;

public class FirebaseTokenRequestService extends IntentService {

    public FirebaseTokenRequestService() {
        super("FirebaseTokenRequestService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                NYLog.e("Cek token firebase = " + newToken);
                try {
                    AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(String... strings) {
                            try {
                                NYSendFirebaseTokenRequest req = new NYSendFirebaseTokenRequest(getApplicationContext(), strings[0]);
                                req.loadDataFromNetwork();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };
                    asyncTask.execute(newToken);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
