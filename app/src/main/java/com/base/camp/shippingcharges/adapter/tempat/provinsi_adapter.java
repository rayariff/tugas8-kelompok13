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
import com.base.camp.shippingcharges.model.provinsi.ResultProvinsi;

import java.util.ArrayList;
import java.util.List;

public class provinsi_adapter  extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ResultProvinsi> items;
    private ArrayList<ResultProvinsi> listlokasi;

    public provinsi_adapter(Activity activity, List<ResultProvinsi> items) {
        this.activity = activity;
        this.items = items;

        listlokasi = new ArrayList<>();
        listlokasi.addAll(items);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
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

        TextView tv_category =  convertView.findViewById(R.id.tv_category);
        TextView tv_detail =  convertView.findViewById(R.id.tv_detail);

        ResultProvinsi m = items.get(position);

        tv_category.setText(m.getProvince());
        tv_detail.setText(m.getProvinceId());

        return convertView;
    }

    @SuppressLint("DefaultLocale")
    public void filter(String charText) {
        charText = charText.toLowerCase();

        items.clear();
        if (charText.length() == 0) {
            /* tampilkan seluruh data */
            items.addAll(listlokasi);

        } else {
            for (ResultProvinsi lok : listlokasi) {
                if (lok.getProvince().toLowerCase().contains(charText)) {
                    items.add(lok);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void setList(List<ResultProvinsi> items){
        this.listlokasi.addAll(items);
    }
}
