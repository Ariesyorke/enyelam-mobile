package com.danzoye.lib.util;

import java.util.Vector;

/**
 * Created by Ramdhany Dwi Nugroho on Feb 2015.
 */
public class RunnablePooler {
    private int limit = 1;
    private Vector<Runnable> pooler;

    public synchronized void add(Runnable runnable) {
        if (pooler == null) {
            pooler = new Vector<Runnable>();
        }
        pooler.addElement(runnable);
        if (pooler.size() <= getLimit()) {
            new Thread(runnable).start();
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public synchronized void remove(Runnable runnable) {
        if (pooler == null) {
            pooler = new Vector<Runnable>();
        }
        pooler.removeElement(runnable);
        if (pooler.size() >= getLimit()) {
            Runnable nextRunnable;
            nextRunnable = (Runnable) pooler.elementAt(getLimit() - 1);
            new Thread(nextRunnable).start();
        }
    }
}
