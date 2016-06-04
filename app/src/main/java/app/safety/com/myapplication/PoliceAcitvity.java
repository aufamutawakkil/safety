package app.safety.com.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;

import java.util.ArrayList;

import app.safety.com.R;
import app.safety.com.adptr.PoliceAdapter;
import core.Api;
import core.Static;
import me.zhanghai.android.materialprogressbar.IndeterminateProgressDrawable;

public class PoliceAcitvity extends AppCompatActivity {

    Api api = null;
    ArrayList<String> address = null;
    ArrayList<String> num = null;
    ArrayList<String> id = null;
    ArrayList<String> lat = null;
    ArrayList<String> lng = null;
    ListView lv;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private  Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_acitvity);

        final ProgressBar loadingbar = (ProgressBar) findViewById(R.id.indeterminate_progress_native);
        loadingbar.setIndeterminateDrawable(new IndeterminateProgressDrawable(getApplicationContext()));
        loadingbar.setVisibility(View.VISIBLE);

        api = new Api(Static.API_KEY);
        address = new ArrayList();
        num = new ArrayList();
        id = new ArrayList();
        lat = new ArrayList();
        lng = new ArrayList();

        this.api.getAllPolice(new Api.Callback<Api.StaticArray>() {
            @Override
            public Void success(Api.StaticArray params) throws JSONException {
                loadingbar.setVisibility(View.INVISIBLE);
                int jumdata = params.data.size();
                for (int i = 0; i < jumdata; i++) {
                    address.add(params.data.get(i).get("address").toString());
                    num.add(params.data.get(i).get("number").toString());
                    id.add(params.data.get(i).get("id").toString());
                    lat.add(params.data.get(i).get("lat").toString());
                    lng.add(params.data.get(i).get("lng").toString());
                }

                  /*add to adapter*/
                lv = (ListView) findViewById(android.R.id.list);
                lv.setAdapter(new PoliceAdapter(getApplicationContext(), address, num,id,lat,lng));

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent i = new Intent(getApplicationContext(), AddPoliceActivity.class);

                        i.putExtra("id",id);
                        i.putExtra("lat",((TextView) view.findViewById(R.id.lat)).getText().toString());
                        i.putExtra("lng",((TextView) view.findViewById(R.id.lng)).getText().toString());
                        i.putExtra("address",((TextView) view.findViewById(R.id.address)).getText().toString());
                        i.putExtra("number",((TextView) view.findViewById(R.id.num)).getText().toString());

                        startActivity(i);


                    }
                });

                return null;
            }

            @Override
            public Void failed(String msg) {
                Toast.makeText(getApplicationContext(), "ERROR : " + msg , Toast.LENGTH_SHORT).show();
                return null;
            }

        });



        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddPoliceActivity.class);
                startActivity(i);
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PoliceAcitvity Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.safety.com.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PoliceAcitvity Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.safety.com.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
