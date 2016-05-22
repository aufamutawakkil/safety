package app.safety.com.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent i = new Intent(getApplicationContext(),AddPoliceActivity.class);
                startActivity(i);
            }
        });


    }
}
