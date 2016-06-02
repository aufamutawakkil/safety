package app.safety.com.myapplication;

import org.json.JSONException;

import core.Api;
import core.Static;

/**
 * Created by aufa on 6/2/2016.
 */



public class OnLoad {
    private Api api= new Api(Static.API_KEY);

    public void grabDataSetting(){
        api.getSetting(new Api.Callback<Api.StaticArray>() {
            @Override
            public Void success(Api.StaticArray params) throws JSONException {
                PublicData.noHardware = params.data.get(0).get("noHardware");
                PublicData.commandRestart = params.data.get(0).get("commandRestart");
                return null;
            }
        });
    }
}
