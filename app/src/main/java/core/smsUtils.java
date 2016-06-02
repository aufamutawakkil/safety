package core;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aufa on 5/24/2016.
 */
public class SmsUtils {

        Context app = null;

        public SmsUtils(Context app){
            this.app = app;
        }

        public void send(String phoneNo, String msg){
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                Toast.makeText(this.app.getApplicationContext(), "Pesan Terkirim",
                        Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(this.app.getApplicationContext(),ex.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }

        public HashMap read(){
            HashMap sms = new HashMap<String,String>();

            int i = 0;

            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor cursor = this.app.getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"},null,null,null);

            cursor.moveToFirst();
            while  (cursor.moveToNext())
            {
                String address = cursor.getString(1);
                String body = cursor.getString(3);
                sms.put("address",address);
                sms.put("body",body);
                i++;
                if(i == 10) break;
            }
            return sms;
        }

}
