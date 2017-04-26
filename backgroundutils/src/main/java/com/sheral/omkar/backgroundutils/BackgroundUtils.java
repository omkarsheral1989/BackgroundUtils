package com.sheral.omkar.backgroundutils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AnyThread;

public class BackgroundUtils {
  private BackgroundUtils() {
  }

  private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

  @AnyThread
  public static BackgroundTask doInBackground(DoInBackgroundListener doInBackgroundListener) {
    return new BackgroundTask(doInBackgroundListener);
  }

  @AnyThread
  public static void runOnMainThread(Runnable runnable) {
    mainThreadHandler.post(runnable);
  }

  @AnyThread
  public static Thread runOnBackgroundThread(Runnable runnable) {
    Thread thread = new Thread(runnable);
    thread.start();
    return thread;
  }
}
