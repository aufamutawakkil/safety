package app.safety.com.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.logging.Handler;

import app.safety.com.myapplication.AlarmActivity;
import app.safety.com.myapplication.PublicData;

/**
 * Created by aufa on 6/2/2016.
 */
public class AlarmService extends android.app.Service {
    Context context;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        Log.v("aufa","oke gan");

        /*Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if(PublicData.isAlarmActive == true){
                        PublicData.isAlarmActive = false;
                        Intent dialogIntent = new Intent(context, AlarmActivity.class);
                        dialogIntent                                                   .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(dialogIntent);
                    }

                }
            }
        };
        t.start();*/


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "AlarmService Destroyed", Toast.LENGTH_LONG).show();
    }
}
