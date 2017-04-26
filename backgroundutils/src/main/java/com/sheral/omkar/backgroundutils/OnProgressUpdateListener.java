package com.sheral.omkar.backgroundutils;

import android.support.annotation.MainThread;

public interface OnProgressUpdateListener<T> {
  @MainThread
  void onProgressUpdate(T progress);
}
