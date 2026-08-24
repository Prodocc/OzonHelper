package com.example.OzonHelper.util;

import org.springframework.stereotype.Component;

@Component
public class SimpleRateLimiter {

    private final long intervalMs = 300;
    private long nextAllowedTime = 0;

    public void acquire() throws InterruptedException {
        long waitTime;

        synchronized (this) {
            long now = System.currentTimeMillis();

            long myStartTime = Math.max(now, nextAllowedTime);
            waitTime = myStartTime - now;

            nextAllowedTime = myStartTime + intervalMs;
        }

        if (waitTime > 0) {
            Thread.sleep(waitTime);
        }
    }
}
