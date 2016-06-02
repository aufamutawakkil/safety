package app.safety.com.myapplication;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import core.Api;
import core.DBHelper;
import core.Static;

public class SettingActivity extends AppCompatActivity {
    Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        api = new Api(Static.API_KEY);

        /*get edittext value*/
        final EditText noHardware = (EditText)findViewById(R.id.noHardware);
        final EditText commandRestart = (EditText)findViewById(R.id.commandRestart);
        final EditText user = (EditText)findViewById(R.id.user);
        final EditText pass = (EditText)findViewById(R.id.pass);

        noHardware.setText(PublicData.noHardware);
        commandRestart.setText(PublicData.commandRestart);
        user.setText(PublicData.user);
        pass.setText(PublicData.pass);

        Button btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save to temp
                PublicData.noHardware = noHardware.getText().toString();
                PublicData.commandRestart = commandRestart.getText().toString();
                PublicData.user = user.getText().toString();
                PublicData.pass = pass.getText().toString();

                Api.Setting setting = new Api.Setting();
                setting.commandRestart = commandRestart.getText().toString();
                setting.noHardware = noHardware.getText().toString();
                setting.user = user.getText().toString();
                setting.pass = pass.getText().toString();

                //save to server
                api.pushSetting(setting, new Api.Callback<Api.Setting>() {
                    @Override
                    public Void success(Api.Setting params) throws JSONException {
                        Log.i("setting",params.toString());
                        return null;
                    }
                });


                /*update db*/
                /*Cursor dbRes = db.query("update setting set no_hardware='"+ PublicData.noHardware+"',command_restart='"+ PublicData.commandRestart+"',username='"+ PublicData.user+"',password='"+ PublicData.pass+"'");
                if(dbRes.moveToFirst()){ Log.i("info","insert setting berhasil"); }*/
            }
        });
    }
}
