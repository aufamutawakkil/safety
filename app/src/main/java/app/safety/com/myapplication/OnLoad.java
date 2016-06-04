package app.safety.com.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
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

    void OnLoad(AppCompatActivity context){
        this.context = context;
    }

    public void grabDataSetting(){
        api.getSetting(new Api.Callback<Api.StaticArray>() {
            @Override
            public Void success(Api.StaticArray params) throws JSONException {
                PublicData.noHardware = params.data.get(0).get("noHardware");
                PublicData.commandRestart = params.data.get(0).get("commandRestart");
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
