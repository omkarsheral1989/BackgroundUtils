package com.sheral.omkar.backgroundutils;

import android.support.annotation.MainThread;

public interface OnPostExecuteListener<T> {
  @MainThread
  void onPostExecute(T result);
}
