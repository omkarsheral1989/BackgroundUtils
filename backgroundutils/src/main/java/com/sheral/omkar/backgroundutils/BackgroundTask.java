package com.sheral.omkar.backgroundutils;

import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import static com.sheral.omkar.backgroundutils.BackgroundUtils.runOnBackgroundThread;
import static com.sheral.omkar.backgroundutils.BackgroundUtils.runOnMainThread;

public class BackgroundTask {

  private boolean cancelled = false;
  private DoInBackgroundListener doInBackgroundListener;
  private Thread doInBackgroundThread;
  private Object doInBackgroundResult;
  private OnPostExecuteListener onPostExecuteListener;
  private OnCancelledListener onCancelledListener;
  private OnProgressUpdateListener onProgressUpdateListener;
  private OnPreExecuteListener onPreExecuteListener;

  @AnyThread
  BackgroundTask(final DoInBackgroundListener doInBackgroundListener) {
    this.doInBackgroundListener = doInBackgroundListener;
  }

  public final BackgroundTask execute() {
    executeOnPreExecute();
    return this;
  }

  @AnyThread
  public <T> BackgroundTask onPostExecute(OnPostExecuteListener<T> onPostExecuteListener) {
    this.onPostExecuteListener = onPostExecuteListener;
    return this;
  }

  @AnyThread
  public <T> BackgroundTask onProgressUpdate(OnProgressUpdateListener<T> onProgressUpdateListener) {
    this.onProgressUpdateListener = onProgressUpdateListener;
    return this;
  }

  @AnyThread
  public <T> BackgroundTask onCancelled(OnCancelledListener<T> onCancelledListener) {
    this.onCancelledListener = onCancelledListener;
    return this;
  }

  @AnyThread
  public BackgroundTask onPreExecute(OnPreExecuteListener onPreExecuteListener) {
    this.onPreExecuteListener = onPreExecuteListener;
    return this;
  }

  @AnyThread
  synchronized public void cancel(boolean interrupt) {
    cancelled = true;
    if (interrupt && null != doInBackgroundThread) {  //TODO what if thread is null??
      doInBackgroundThread.interrupt();
    }
  }

  @AnyThread
  synchronized public boolean isCancelled() {
    return cancelled;
  }

  /**
   * Publishes progress updates to {@link OnProgressUpdateListener#onProgressUpdate(Object)}
   *
   * @param progress progress to update
   */
  @WorkerThread
  public final <T> void publishProgress(final T progress) {
    runOnMainThread(new Runnable() {
      @Override
      public void run() {
        executePublishProgress(progress);
      }
    });
  }

  @AnyThread
  private void executeOnPreExecute() {
    if (onPreExecuteListener != null) {
      runOnMainThread(new Runnable() {
        @Override
        public void run() {
          onPreExecuteListener.onPreExecute();
          executeDoInBackground();
        }
      });
    } else {
      executeDoInBackground();
    }
  }

  @AnyThread
  synchronized private void executeDoInBackground() {
    doInBackgroundThread = runOnBackgroundThread(new Runnable() {
      @Override
      public void run() {
        doInBackgroundResult = doInBackgroundListener.doInBackground(BackgroundTask.this);
        runOnMainThread(new Runnable() {
          @Override
          public void run() {
            onBackgroundThreadFinished();
          }
        });
      }
    });
  }

  @MainThread
  private void onBackgroundThreadFinished() {
    if (isCancelled()) {
      executeOnCancelled();
    } else {
      executeOnPostExecute();
    }
  }

  @MainThread
  synchronized private void executeOnPostExecute() {
    if (onPostExecuteListener != null) {
      //noinspection unchecked
      onPostExecuteListener.onPostExecute(doInBackgroundResult);
    }
  }

  @MainThread
  synchronized private void executeOnCancelled() {
    if (onCancelledListener != null) {
      //noinspection unchecked
      onCancelledListener.onCancelled(doInBackgroundResult);
    }
  }

  @MainThread
  private <T> void executePublishProgress(T progress) {
    if (onProgressUpdateListener != null) {
      //noinspection unchecked
      onProgressUpdateListener.onProgressUpdate(progress);
    }
  }
}
