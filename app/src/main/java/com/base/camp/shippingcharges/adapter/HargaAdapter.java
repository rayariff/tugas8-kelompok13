package com.base.camp.shippingcharges.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.base.camp.shippingcharges.R;
import com.base.camp.shippingcharges.model.tarif.Costs;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HargaAdapter extends RecyclerView.Adapter<HargaAdapter.Holder> {
    private final Context context;
    private List<Costs> list;

    public HargaAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void addList(List<Costs> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HargaAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_harga, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HargaAdapter.Holder holder, int position) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        Costs m = list.get(position);
//        String description = m.getDescription().toUpperCase();
//        String note = m.getNote();
//        String cost = m.getCost();
//        String costs = m.getCosts();
        String value = m.getValue();
        String service = m.getService().toUpperCase();
        String etd = m.getEtd();
        String name = m.getName();
        String code = m.getCode().toUpperCase();

        if (value.equals("")) {
            holder.tv_info.setVisibility(View.VISIBLE);
        }

        holder.tv_code.setText(code);
        holder.tv_name.setText(name);
        holder.tv_value.setText(formatRupiah.format(Double.parseDouble(value)));
        holder.tv_service.setText(service);
        if(etd.contains("HARI")) {
            holder.tv_etd.setText(etd);
        }else{
            String hari = etd + " HARI";
            holder.tv_etd.setText(hari);
        }
        if (etd.equals("")){
            holder.tv_etd.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }


    public static class Holder extends RecyclerView.ViewHolder {
        TextView tv_code;
        TextView tv_name;
        TextView tv_value;
        TextView tv_etd;
        TextView tv_service;
        TextView tv_info;

        public Holder(@NonNull View v) {
            super(v);
            tv_code = v.findViewById(R.id.tv_code);
            tv_name = v.findViewById(R.id.tv_name);
            tv_value = v.findViewById(R.id.tv_value);
            tv_etd = v.findViewById(R.id.tv_etd);
            tv_service = v.findViewById(R.id.tv_service);
            tv_info = v.findViewById(R.id.info_null);
        }
    }
}
