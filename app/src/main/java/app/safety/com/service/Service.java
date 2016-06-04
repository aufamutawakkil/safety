package app.safety.com.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import app.safety.com.myapplication.AlarmActivity;

/**
 * Created by aufa on 6/2/2016.
 */
public class Service extends android.app.Service {
    Context context;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        // Let it continue running until it is stopped.
     /*   new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
              *//*  Intent i = context.getPackageManager().getLaunchIntentForPackage("app.safety.com.myapplicataion.AlarmActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);*//*

                *//*Intent intent = new Intent();
                intent.setClassName("app.safety.com", "myapplicataion.AlarmActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*//*

                Intent dialogIntent = new Intent(context, AlarmActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(dialogIntent);
            }
        }.start();*/

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
