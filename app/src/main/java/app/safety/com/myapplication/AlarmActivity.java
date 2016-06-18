package app.safety.com.myapplication;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import app.safety.com.R;
import app.safety.com.adptr.PoliceAdapter;
import core.Api;
import core.Static;

public class AlarmActivity extends AppCompatActivity implements LocationListener{
    Button btnCall;
    Button btnMon;
    Api api;
    double myLat;double myLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        btnCall = (Button)findViewById(R.id.btnCall);
        btnMon = (Button)findViewById(R.id.btnMonitoring);
        api = new Api(Static.API_KEY);
        final float dist[] = new float[1000];

        /*mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.sound1);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();*/

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.getAllPolice(new Api.Callback<Api.StaticArray>() {
                    @Override
                    public Void success(Api.StaticArray params) throws JSONException {
                        int jumdata = params.data.size();

                        for (int i = 0; i < jumdata; i++) {
                            double endLat = Double.parseDouble(params.data.get(i).get("lat").toString());
                            double endLng = Double.parseDouble(params.data.get(i).get("lng").toString());
                            Location.distanceBetween(myLat,myLng,endLat,endLng,dist);
                            Log.v("loc " , dist.toString());
                        }


                        return null;
                    }

                    @Override
                    public Void failed(String msg) {
                        Toast.makeText(getApplicationContext(), "ERROR : " + msg , Toast.LENGTH_SHORT).show();
                        return null;
                    }

                });
            }
        });

        btnMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MonitoringActivity.class);
                i.putExtra("lat",PublicData.latAlarm);
                i.putExtra("lng",PublicData.lngAlarm);
                i.putExtra("from","alarm");
                startActivity(i);
            }
        });



    }

    public void checkDistance(double lat,double lng){

    }

    @Override
    public void onLocationChanged(Location location) {
        myLat = location.getLatitude();
        myLng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
