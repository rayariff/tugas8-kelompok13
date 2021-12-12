package com.base.camp.shippingcharges.adapter.tempat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.base.camp.shippingcharges.R;
import com.base.camp.shippingcharges.model.kecamatan.ResultKecamatan;

import java.util.ArrayList;
import java.util.List;

public class kecamatan_adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ResultKecamatan> movieItems;
    private ArrayList<ResultKecamatan> listlokasiasli;

    public kecamatan_adapter(Activity activity, List<ResultKecamatan> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;

        listlokasiasli = new ArrayList<>();
        listlokasiasli.addAll(movieItems);
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
            convertView = inflater.inflate(R.layout.item_kec, null);
        }

        TextView tv_category = convertView.findViewById(R.id.tv_category);
        TextView tv_detail = convertView.findViewById(R.id.tv_detail);

        ResultKecamatan m = movieItems.get(position);

        tv_category.setText(m.getSubdistrict_name());

        String detail =m.getProvince()+" - " +m.getCityName();
        tv_detail.setText(detail);

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
            for (ResultKecamatan lok : listlokasiasli) {
                if (lok.getSubdistrict_name().toLowerCase().contains(charText)) {
                    movieItems.add(lok);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void setList(List<ResultKecamatan> movieItems){
        this.listlokasiasli.addAll(movieItems);
    }

}
