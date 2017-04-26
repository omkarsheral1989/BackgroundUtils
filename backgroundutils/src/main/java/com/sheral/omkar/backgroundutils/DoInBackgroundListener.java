package com.sheral.omkar.backgroundutils;

import android.support.annotation.WorkerThread;

public interface DoInBackgroundListener<RESULT> {
  /**
   * Called from background
   *
   * @return return the result to be passed to {@link OnPostExecuteListener#onPostExecute(Object)}
   */
  @WorkerThread
  RESULT doInBackground(BackgroundTask backgroundTask);
}
