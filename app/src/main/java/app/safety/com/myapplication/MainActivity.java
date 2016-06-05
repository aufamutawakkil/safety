package app.safety.com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.safety.com.R;
import app.safety.com.service.AlarmService;
import core.DBHelper;
import core.SmsUtils;

public class MainActivity extends AppCompatActivity {
    public DBHelper db;
    private OnLoad onload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onload  = new OnLoad(getApplicationContext());
        onload.grabDataSetting();
        //db = new DBHelper(this);

        /*Cursor dbRes = db.query("select * from setting");
        dbRes.moveToFirst();
        PublicData.noHardware = dbRes.getString(0);
        PublicData.commandRestart = dbRes.getString(1);
        PublicData.user = dbRes.getString(2);
        PublicData.pass = dbRes.getString(3);*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start service
        startService(new Intent(getBaseContext(),AlarmService.class));

        Button btn_monitoring = (Button) findViewById(R.id.btn_monitoring);
        Button btn_kontak_polisi = (Button) findViewById(R.id.btn_kontak_polisi);
        Button btn_setting = (Button) findViewById(R.id.btn_setting);
        Button btnAdd = (Button) findViewById(R.id.btbn_add_police);

        btn_monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MonitoringActivity.class);
                startActivity(i);
            }
        });

        btn_kontak_polisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PoliceAcitvity.class);
                startActivity(i);
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(i);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getApplicationContext(),AddPoliceActivity.class);
                //startActivity(i);
                if( PublicData.noHardware == "" || PublicData.commandRestart == ""){
                    Toast.makeText(getApplicationContext(),"Nomor Hardware atau Command belum di setting",Toast.LENGTH_SHORT).show();
                }else{
                    SmsUtils sms = new SmsUtils(getApplicationContext());
                    sms.send(PublicData.noHardware,PublicData.commandRestart);
                }
            }
        });

    }
}
