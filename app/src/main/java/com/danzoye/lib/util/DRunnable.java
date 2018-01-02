package com.danzoye.lib.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public abstract class DRunnable<INPUT, PROGRESS, OUTPUT> implements Runnable {
    private INPUT[] input;
    private PROGRESS lastProgress;
    private OUTPUT output;
    private Handler handler;

    protected DRunnable(INPUT... inputs) {
        this.input = inputs;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    onProgress(lastProgress);
                } else if (msg.what == 2) {
                    onOutput(output);
                }
            }
        };
    }

    public void publishProgress(PROGRESS progresses) {
        lastProgress = progresses;
        handler.sendEmptyMessage(1);
    }

    @Override
    public void run() {
        output = runInBackground(input);
        handler.sendEmptyMessage(2);
    }

    protected void onOutput(OUTPUT output) {
        // DO NOTHING by default
    }

    protected void onProgress(PROGRESS progresses) {
        // DO NOTHING by default
    }

    protected abstract OUTPUT runInBackground(INPUT... inputs);
}
