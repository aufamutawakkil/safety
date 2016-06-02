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

public class PoliceAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<String> address;
    private ArrayList<String> num;

    public PoliceAdapter(Context context, ArrayList<String> address, ArrayList<String> num) {
        super(context, R.layout.activity_police_adapter,num);
        this.context = context;
        this.address = address;
        this.num = num;
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
        txtAddress.setText(address.get(position));
        txtNum.setText(num.get(position));

        return rowView;
    }
}
