package com.sheral.omkar.backgroudutils;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sheral.omkar.backgroundutils.BackgroundTask;
import com.sheral.omkar.backgroundutils.DoInBackgroundListener;
import com.sheral.omkar.backgroundutils.OnCancelledListener;
import com.sheral.omkar.backgroundutils.OnPostExecuteListener;
import com.sheral.omkar.backgroundutils.OnPreExecuteListener;
import com.sheral.omkar.backgroundutils.OnProgressUpdateListener;

import static com.sheral.omkar.backgroundutils.BackgroundUtils.doInBackground;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onButton1(View view) {
    doInBackground(new DoInBackgroundListener<String>() {
      @Override
      public String doInBackground(BackgroundTask backgroundTask) {
        Log.e("qwe", "doInBackground");
        printThreadName();

        for (int i = 0; i < 10; i++) {
          if (backgroundTask.isCancelled()) {
            return "CancelledResult";
          }

          SystemClock.sleep(500);

          backgroundTask.publishProgress(i);

          if (i == 7) {
            backgroundTask.cancel(false);
          }
        }

        SystemClock.sleep(500);
        return "SuccessResult";
      }
    }).onPreExecute(new OnPreExecuteListener() {
      @Override
      public void onPreExecute() {
        Log.e("qwe", "PreExecute");
        printThreadName();
      }
    }).onProgressUpdate(new OnProgressUpdateListener<Integer>() {
      @Override
      public void onProgressUpdate(Integer progress) {
        Log.e("qwe", "Progress = " + progress);
        printThreadName();
      }

    }).onPostExecute(new OnPostExecuteListener<String>() {
      @Override
      public void onPostExecute(String result) {
        Log.e("qwe", "result = " + result);
        printThreadName();
      }
    }).onCancelled(new OnCancelledListener<String>() {
      @Override
      public void onCancelled(String result) {
        Log.e("qwe", "Cancelled. result = " + result);
        printThreadName();
      }
    }).execute();
  }

  private void printThreadName() {
    Log.e("qwe", "Thread name = " + Thread.currentThread().getName());
  }
}
