package app.safety.com.myapplication;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by aufa on 5/23/2016.
 */
public class PublicData {
    public static String noHardware = "";
    public static String commandRestart = "";
    public static String commandAlarm = "";
    public static String user = "";
    public static String pass = "";

    public static ArrayList<LatLng> points = new ArrayList();
    public  static GoogleMap publicGmap;
    public static boolean updateUI = false;

    public static boolean isAlarmActive = false;
    public static String latAlarm = "";
    public static String lngAlarm = "";

}
