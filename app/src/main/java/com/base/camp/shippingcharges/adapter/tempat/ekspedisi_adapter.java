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
import com.base.camp.shippingcharges.model.ekspedisi.ItemEkspedisi;

import java.util.ArrayList;
import java.util.List;

public class ekspedisi_adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ItemEkspedisi> movieItems;
    private ArrayList<ItemEkspedisi> listlokasiasli;

    public ekspedisi_adapter(Activity activity, List<ItemEkspedisi> movieItems) {
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
            convertView = inflater.inflate(R.layout.item_ekspedisi, null);
        }

        TextView tv_category = convertView.findViewById(R.id.tv_category);
        TextView tv_detail = convertView.findViewById(R.id.tv_detail);
        TextView tv_des = convertView.findViewById(R.id.des);


        ItemEkspedisi m = movieItems.get(position);

        tv_category.setText(m.getName());
        tv_des.setText(m.getDes());
        tv_detail.setText(m.getId());

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
            for (ItemEkspedisi lok : listlokasiasli) {
                if (lok.getName().toLowerCase().contains(charText)) {
                    movieItems.add(lok);
                }

            }
        }

        notifyDataSetChanged();
    }

    public void setList(List<ItemEkspedisi> movieItems){
        this.listlokasiasli.addAll(movieItems);
    }
}
