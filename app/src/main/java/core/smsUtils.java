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

        public ArrayList read(){
            ArrayList<Long> _id = new ArrayList();
            ArrayList _number = new ArrayList();
            ArrayList _content = new ArrayList();

            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor cursor = this.app.getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"},null,null,null);

            cursor.moveToFirst();
            while  (cursor.moveToNext())
            {
                long id = cursor.getLong(0);
                String address = cursor.getString(1);
                String body = cursor.getString(3);
                _number.add(address);
                _content.add(body);
                _id.add(id);
            }

            ArrayList data = new ArrayList();
            data.add(_id);
            data.add(_number);
            data.add(_content);

            return data;
        }

        public void delete(Context context,long id){
                context.getContentResolver().delete(
                        Uri.parse("content://sms/" + id), null, null);
        }

}
