package app.safety.com.adptr;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.safety.com.myapplication.R;

public class MonitoringLocationAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<String> nameSetting;
    private ArrayList<Integer> iconSetting;

    public MonitoringLocationAdapter(Context context, ArrayList<String> nameSetting, ArrayList<Integer> iconSetting) {
        super(context, R.layout.activity_monitoring_location_adapter,nameSetting);
        this.context = context;
        this.nameSetting = nameSetting;
        this.iconSetting = iconSetting;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_monitoring_location_adapter, parent, false);
        //RelativeLayout rlayout = (RelativeLayout) rowView.findViewById(R.id.categorylist);

        //TextView txtnameSetting = (TextView) rowView.findViewById(R.id.nameSetting);
        //txtnameSetting.setText(nameSetting.get(position));

        ImageView img = (ImageView) rowView.findViewById(R.id.iconSetting);
        img.setImageResource(iconSetting.get(position));

        return rowView;
    }
}
