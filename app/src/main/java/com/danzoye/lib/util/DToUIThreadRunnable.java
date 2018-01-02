package com.danzoye.lib.util;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public abstract class DToUIThreadRunnable<INPUT, PROGRESS, OUTPUT> implements Runnable {
    private Activity context;
    private INPUT[] input;

    protected DToUIThreadRunnable(Activity context, INPUT... inputs) {
        this.context = context;
        this.input = inputs;
    }

    public void publishProgress(final PROGRESS progresses) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onProgressInForeground(progresses);
            }
        });
    }

    @Override
    public void run() {
        final OUTPUT output = runInBackground(input);
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onOutputInForeground(output);
            }
        });
    }

    protected void onOutputInForeground(OUTPUT output) {
        // DO NOTHING by default
    }

    protected void onProgressInForeground(PROGRESS progresses) {
        // DO NOTHING by default
    }

    protected abstract OUTPUT runInBackground(INPUT... inputs);
}
