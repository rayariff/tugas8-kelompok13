package com.base.camp.shippingcharges.adapter.tempat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.base.camp.shippingcharges.R;
import com.base.camp.shippingcharges.model.kota.Result;

import java.util.ArrayList;
import java.util.List;

public class kota_adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Result> movieItems;
    private ArrayList<Result> listlokasiasli;


    public kota_adapter(Activity activity, List<Result> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;

        listlokasiasli = new ArrayList<>();
        listlokasiasli.addAll(movieItems);
    }

    public kota_adapter(FragmentActivity activity, List<Result> listCity) {
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            assert inflater != null;
            convertView = inflater.inflate(R.layout.item_category, null);
        }

        TextView tv_category = convertView.findViewById(R.id.tv_category);
        TextView tv_detail = convertView.findViewById(R.id.tv_detail);

        Result m = movieItems.get(position);

        tv_category.setText(m.getCityName());
        String pro = "Provinsi - "+m.getProvince();
        tv_detail.setText(pro);

        return convertView;
    }

    @SuppressLint("DefaultLocale")
    public void filter(String charText)
    {
        charText = charText.toLowerCase();

        movieItems.clear();
        if (charText.length() == 0) {
            /* tampilkan seluruh data */
            movieItems.addAll(listlokasiasli);

        } else {
            for (Result lok : listlokasiasli) {
                if (lok.getCityName().toLowerCase().contains(charText)) {
                    movieItems.add(lok);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void setList(List<Result> movieItems){
        this.listlokasiasli.addAll(movieItems);
    }
}
