package app.safety.com.service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import app.safety.com.myapplication.PublicData;

public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;

            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];

                    for(int i=0; i<msgs.length; i++){
                        char chartat = '0';

                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String numberSms="";
                        chartat = msg_from.charAt(0);
                        if( chartat == '0' ) numberSms = msg_from;
                        else if( chartat == '6' ) numberSms = "0" + msg_from.substring(2);
                        else if( chartat == '+' ) numberSms = "0" + msg_from.substring(3);

                        String msgBody = msgs[i].getMessageBody();
                        String[] mode = msgBody.toString().split("\\|");
                        if(PublicData.noHardware.equals(numberSms)){
                            if(mode[0].equals("monitoring")){
                                PublicData.updateUI = true;
                                PublicData.points.add( new LatLng(Double.parseDouble(mode[1]),Double.parseDouble(mode[2])));
                                Log.v("aufapoints",PublicData.points.toString());

                            }
                        }

                        if(mode[0].equals("alarm")){
                            PublicData.isAlarmActive = true;
                            Log.v("aufa","alram aktif");
                            PublicData.latAlarm = mode[1];
                            PublicData.lngAlarm = mode[2];
                        }
                    }

                }catch(Exception e){
                            Log.d("aufa ec","a"+e.getMessage());
                }
            }
        }
    }
}