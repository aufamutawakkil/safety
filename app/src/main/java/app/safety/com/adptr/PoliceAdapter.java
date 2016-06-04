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

import app.safety.com.R;

public class PoliceAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<String> address;
    private ArrayList<String> num;
    private ArrayList<String> id;
    private ArrayList<String> lat;
    private ArrayList<String> lng;

    public PoliceAdapter(Context context, ArrayList<String> address, ArrayList<String> num,ArrayList<String> id,ArrayList<String> lat,ArrayList<String> lng) {
        super(context, R.layout.activity_police_adapter,id);
        this.context = context;
        this.address = address;
        this.num = num;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_police_adapter, parent, false);
        //RelativeLayout rlayout = (RelativeLayout) rowView.findViewById(R.id.categorylist);

        TextView txtAddress = (TextView) rowView.findViewById(R.id.address);
        TextView txtNum= (TextView) rowView.findViewById(R.id.num);
        TextView txtLat= (TextView) rowView.findViewById(R.id.lat);
        TextView txtLng= (TextView) rowView.findViewById(R.id.lng);

        txtAddress.setText(address.get(position));
        txtNum.setText(num.get(position));
        txtLat.setText(lat.get(position));
        txtLng.setText(lng.get(position));

        return rowView;
    }
}
