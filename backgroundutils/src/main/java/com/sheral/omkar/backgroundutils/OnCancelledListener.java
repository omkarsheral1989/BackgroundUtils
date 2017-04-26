package com.sheral.omkar.backgroundutils;

import android.support.annotation.MainThread;

public interface OnCancelledListener<T> {
  @MainThread
  void onCancelled(T result);
}
