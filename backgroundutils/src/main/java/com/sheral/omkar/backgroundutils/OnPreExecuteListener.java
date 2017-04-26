package com.sheral.omkar.backgroundutils;

import android.support.annotation.MainThread;

public interface OnPreExecuteListener {
  @MainThread
  void onPreExecute();
}
