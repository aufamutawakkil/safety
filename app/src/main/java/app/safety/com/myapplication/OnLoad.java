package app.safety.com.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import core.Api;
import core.Static;

/**
 * Created by aufa on 6/2/2016.
 */



public class OnLoad {
    private Api api= new Api(Static.API_KEY);
    Context context;

    public OnLoad(Context applicationContext) {
        this.context = applicationContext;
    }


    public void grabDataSetting(){
        api.getSetting(new Api.Callback<Api.StaticArray>() {
            @Override
            public Void success(Api.StaticArray params) throws JSONException {

                Log.v("aufa ",params.data.toString());

                PublicData.noHardware = params.data.get(0).get("noHardware");
                PublicData.commandRestart = params.data.get(0).get("commandRestart");
                PublicData.commandAlarm = params.data.get(0).get("commandAlarm");

                return null;
            }
            @Override
            public Void failed(String msg) {
                Toast.makeText(context, "ERROR : " + msg , Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }
}
