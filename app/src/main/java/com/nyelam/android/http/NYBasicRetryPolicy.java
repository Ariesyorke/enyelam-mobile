package com.nyelam.android.http;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

public class NYBasicRetryPolicy implements RetryPolicy {

    /** The default number of retry attempts. */
    public static final int DEFAULT_RETRY_COUNT = 3;

    /** The default delay before retry a request (in ms). */
    public static final long DEFAULT_DELAY_BEFORE_RETRY = 2500;

    /** The default backoff multiplier. */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    /** The number of retry attempts. */
    private int retryCount = DEFAULT_RETRY_COUNT;

    /**
     * The delay to wait before next retry attempt. Will be multiplied by
     * {@link #backOffMultiplier} between every retry attempt.
     */
    private long delayBeforeRetry = DEFAULT_DELAY_BEFORE_RETRY;

    /**
     * The backoff multiplier. Will be multiplied by {@link #delayBeforeRetry}
     * between every retry attempt.
     */
    private float backOffMultiplier = DEFAULT_BACKOFF_MULT;

    // ----------------------------------
    // CONSTRUCTORS
    // ----------------------------------
    public NYBasicRetryPolicy(int retryCount, long delayBeforeRetry, float backOffMultiplier) {
        this.retryCount = retryCount;
        this.delayBeforeRetry = delayBeforeRetry;
        this.backOffMultiplier = backOffMultiplier;
    }

    public NYBasicRetryPolicy() {
        this(DEFAULT_RETRY_COUNT, DEFAULT_DELAY_BEFORE_RETRY, DEFAULT_BACKOFF_MULT);
    }

    // ----------------------------------
    // PUBLIC API
    // ----------------------------------

    @Override
    public int getRetryCount() {
        return retryCount;
    }

    @Override
    public void retry(SpiceException e) {
        delayBeforeRetry = (long) (delayBeforeRetry * backOffMultiplier);
        if(e != null) {
            if (e.getCause() instanceof NYCartExpiredException || e.getCause() instanceof NYStatusFailedException || e.getCause() instanceof  NYStatusInvalidTokenException) {
                retryCount = 0;
            }
        }
        retryCount--;
    }

    @Override
    public long getDelayBeforeRetry() {
        return delayBeforeRetry;
    }
}
