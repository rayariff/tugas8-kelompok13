package com.base.camp.shippingcharges.fragment;

import static com.base.camp.shippingcharges.constans.DataPreference.DATA_CEK;
import static com.base.camp.shippingcharges.constans.DataPreference.DESTINATION;
import static com.base.camp.shippingcharges.constans.DataPreference.ORIGIN;
import static com.base.camp.shippingcharges.constans.URL.API_KEY;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.base.camp.shippingcharges.adapter.HargaAdapter;
import com.base.camp.shippingcharges.adapter.tempat.kecamatan_adapter;
import com.base.camp.shippingcharges.adapter.tempat.kota_adapter;
import com.base.camp.shippingcharges.constans.DataPreference;
import com.base.camp.shippingcharges.constans.URL;
import com.base.camp.shippingcharges.R;
import com.base.camp.shippingcharges.model.kecamatan.ResultKecamatan;
import com.base.camp.shippingcharges.model.kota.Result;
import com.base.camp.shippingcharges.model.tarif.Costs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HargaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "CekOngkirFragment";

    private String id_kota_dari, id_kota_ke, id_kec_dari, id_kec_to;
    private TextView etFromCity, etToCity;
    private EditText etWeight;
    private EditText searchList;
    private ListView mListView;
    private ProgressDialog progressDialog, pDialogGet;

    private AlertDialog.Builder alert;
    private AlertDialog ad;

    private kota_adapter adapter_city;
    private final List<Result> ListCity = new ArrayList<>();

    private kecamatan_adapter adapter_kecamatan;
    private final List<ResultKecamatan> ListKecamatan = new ArrayList<>();

    private HargaAdapter adapter_harga;
    private final List<Costs> ListHarga = new ArrayList<>();
    private RecyclerView recyclerView;

    View viewGaris;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cek_ongkir, container, false);
        etFromCity = view.findViewById(R.id.etFromCity);
        etToCity = view.findViewById(R.id.etToCity);
        etWeight = view.findViewById(R.id.etWeight);

        recyclerView = view.findViewById(R.id.listHarga);
        viewGaris = view.findViewById(R.id.view);
        viewGaris.setVisibility(View.GONE);
        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        pDialogGet = new ProgressDialog(getContext());
        pDialogGet.setMessage("Sedang menyiapkan..");
        pDialogGet.setCancelable(false);

        SharedPreferences data_cek = mContext.getSharedPreferences(DATA_CEK, Context.MODE_PRIVATE);
        id_kec_dari = data_cek.getString(ORIGIN,"");
        id_kec_to = data_cek.getString(DESTINATION,"");

        DataPreference.FIRST_SET_TEXT(mContext,etFromCity,etToCity,etWeight);
        adapter_harga = new HargaAdapter(mContext);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etFromCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ListHarga.clear();
                    adapter_harga.notifyDataSetChanged();
                    popUpCity(etFromCity, "from");
                } catch (NullPointerException ignored) {
                }
            }
        });

        etToCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ListHarga.clear();
                    adapter_harga.notifyDataSetChanged();
                    popUpCity(etToCity, "to");
                } catch (NullPointerException ignored) {
                }
            }
        });

        LinearLayoutManager layoutManagerKategori = new LinearLayoutManager(getContext());
        layoutManagerKategori.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManagerKategori);
        recyclerView.setAdapter(adapter_harga);

        TextView clear = view.findViewById(R.id.clear_text);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPreference.clearData(mContext, etFromCity, etToCity, etWeight);
                ListHarga.clear();
                adapter_harga.notifyDataSetChanged();
                etWeight.setError(null);
                id_kec_dari="";
                id_kec_to="";
                etFromCity.setHint("-");
                etToCity.setHint("-");
            }
        });

        TextView btnProcess = view.findViewById(R.id.btnProcess);
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_kec_dari.equalsIgnoreCase("")){
                    etFromCity.setError(""); etFromCity.setFocusable(true);
                }else  if (id_kec_to.equalsIgnoreCase("") ) {
                    etToCity.setError(""); etToCity.setFocusable(true);
                }else if (etWeight.getText().toString().equals("")) {
                    etWeight.setError("Tidak boleh kosong");
//                    etWeight.setError("Tidak boleh kosong", getResources().getDrawable(R.drawable.ic_info_outline));
                    etWeight.setFocusable(true);
                    etWeight.setText("");

                    DataPreference.DELETE_BERAT(mContext);
                }else prosesCek();

            }
        });
    }

    @Override
    public void onRefresh() {
        if (id_kec_dari.equalsIgnoreCase("")){
            etFromCity.setError(null); etFromCity.setFocusable(true);
        }else  if (id_kec_to.equalsIgnoreCase("") ) {
            etToCity.setError(null); etToCity.setFocusable(true);
        }else if (etWeight.getText().toString().equals("")){
            DataPreference.DELETE_BERAT(mContext);
            etWeight.setError(null);
            refreshLayout.setRefreshing(false);
            return;
        }else prosesCek();
        refreshLayout.setRefreshing(false);
    }

    private void prosesCek() {
        if (Long.parseLong(etWeight.getText().toString()) < 1000 ){
            Log.e(TAG, "input berat "+etWeight.getText().toString());
            etWeight.setError("Minimal 1000", getResources().getDrawable(R.drawable.ic_info_outline));
            etWeight.setFocusable(true);
            etWeight.setText("");
            DataPreference.DELETE_BERAT(mContext);
        }else {
            Log.e(TAG, "berat konversi "+Long.parseLong(etWeight.getText().toString()));
            String a= ""+Long.parseLong(etWeight.getText().toString());
            etWeight.setText(a);
            ListHarga.clear();
            DataPreference.SAVE_BERAT(mContext, etWeight.getText().toString());
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"cahaya");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"dse");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"esl");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"first");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"idl");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"jet");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"jne");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"jnt");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"lion");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"ncs");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"ninja");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"pahala");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"pandu");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"pcp");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"pos");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"rex");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"sap");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"sicepat");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"slis");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"star");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"rpx");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"tiki");
            onGetCost(mContext,id_kec_dari,id_kec_to,etWeight.getText().toString(),"wahana");
        }
    }

    private class MyTextWatcherCity implements TextWatcher {

        private final View view;

        private MyTextWatcherCity(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence s, int i, int before, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (view.getId() == R.id.searchItem) {
                adapter_city.filter(editable.toString());
            }
        }
    }

    private class MyTextWatcherKecamatan implements TextWatcher {

        private final View view;

        private MyTextWatcherKecamatan(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence s, int i, int before, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (view.getId() == R.id.searchItem) {
                adapter_kecamatan.filter(editable.toString());
            }
        }
    }

    private void popUpCity(final TextView editText,  final String status) {
        LayoutInflater inflater = (LayoutInflater) requireActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.dialog_pencarian, null);

        alert = new AlertDialog.Builder(Objects.requireNonNull(mContext));
        alert.setTitle(" ");
        alert.setIcon(getResources().getDrawable(R.drawable.logo));
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = alertLayout.findViewById(R.id.searchItem);
        searchList.setHint(getResources().getString(R.string.search_kota));
        searchList.addTextChangedListener(new MyTextWatcherCity(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = alertLayout.findViewById(R.id.listItem);

        ListCity.clear();
        adapter_city = new kota_adapter(getActivity(), ListCity);

        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                Result cn = (Result) o;
                ad.dismiss();
                editText.setError(null);
                editText.setText(cn.getCityName());
                if (status.equalsIgnoreCase("from")){
                    id_kota_dari = cn.getCityId();
                    id_kec_dari = "";
                    DataPreference.SAVE_KOTA_DARI(mContext, cn.getCityName(),cn.getCityId() );
                    try {
                        if (id_kota_dari.equalsIgnoreCase("")) {
                            etFromCity.setError("");
                        } else {
                            popUpKecamatan(editText, "from", id_kota_dari);
                        }
                    } catch (NullPointerException e) {
                        etFromCity.setError("");
                    }
                }else {
                    id_kota_ke = cn.getCityId();
                    id_kec_to = "";
                    DataPreference.SAVE_KOTA_KE(mContext, cn.getCityName(),cn.getCityId() );
                    try {
                        if (id_kota_ke.equalsIgnoreCase("")) {
                            etToCity.setError("");
                        } else {
                            popUpKecamatan(editText, "to", id_kota_ke);
                        }
                    } catch (NullPointerException e) {
                        etToCity.setError("");
                    }
                }

            }
        });

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Sedang menyiapkan..");
        progressDialog.show();

        getCity();

    }

    private void popUpKecamatan(final TextView editText, final String status, String id) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.dialog_pencarian, null);

        alert = new AlertDialog.Builder(Objects.requireNonNull(mContext));
//        alert.setTitle("Daftar Kecamatan yang ada di " + etCity.getText());
        alert.setTitle(" ");

        alert.setIcon(getResources().getDrawable(R.drawable.logo));
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = alertLayout.findViewById(R.id.searchItem);
        searchList.setHint(getResources().getString(R.string.search_kec));
        searchList.addTextChangedListener(new MyTextWatcherKecamatan(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = alertLayout.findViewById(R.id.listItem);

        ListKecamatan.clear();
        adapter_kecamatan = new kecamatan_adapter(getActivity(), ListKecamatan);

        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                ResultKecamatan cn = (ResultKecamatan) o;

                editText.setError(null);
                String text = editText.getText().toString().trim();
                String tampil = text + " - "+cn.getSubdistrict_name();
                editText.setText(tampil);
                if (status.equalsIgnoreCase("from")){
                    id_kec_dari =cn.getSubdistrict_id();
                    DataPreference.SAVE_KECAMATAN_DARI(mContext, cn.getSubdistrict_name(), cn.getSubdistrict_id() );
                }else {
                    id_kec_to =cn.getSubdistrict_id();
                    DataPreference.SAVE_KECAMATAN_KE(mContext, cn.getSubdistrict_name(), cn.getSubdistrict_id() );

                }
                ad.dismiss();
            }
        });

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Sedang menyiapkan..");
        progressDialog.show();

        getKec(id);

    }

    private void getCity() {
        ListCity.clear();
        String url = URL.URL_ROOT_HTTPS+"city";
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(mContext));
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(TAG," Response From URL city: " + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String result = object.getString("rajaongkir");
                            JSONObject object1 = new JSONObject(result);
                            String data = object1.getString("results");
                            JSONArray array = new JSONArray(data);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                String province = obj.getString("province");
                                String provinceId =obj.getString("province_id");
                                String cityId =obj.getString("city_id");
                                String type =obj.getString("type");
                                String cityName =obj.getString("city_name");
                                String postalCode =obj.getString("postal_code");
                                Result itemProvince = new Result(cityId, provinceId, province, type, cityName, postalCode);
                                ListCity.add(itemProvince);
                                mListView.setAdapter(adapter_city);
                            }
                            adapter_city.setList(ListCity);
                            adapter_city.filter("");

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error from url", "" + error.getMessage());
                Log.e("Error from url", "" + error);
                Toast.makeText(mContext,"Kesalahan memuat data" ,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<>();
                headers.put("key", API_KEY);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void getKec(String id_city) {
        ListKecamatan.clear();
        String url = URL.URL_ROOT_HTTPS+"subdistrict?city="+id_city;
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(mContext));
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(TAG," Response From URL Kecamatan: " + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String result = object.getString("rajaongkir");
                            JSONObject object1 = new JSONObject(result);
                            String data = object1.getString("results");
                            JSONArray array = new JSONArray(data);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                String subdistrict_name = obj.getString("subdistrict_name");
                                String province = obj.getString("province");
                                String provinceId =obj.getString("province_id");
                                String cityId =obj.getString("city_id");
                                String type =obj.getString("type");
                                String cityName =obj.getString("city");
                                String subdistrict_id =obj.getString("subdistrict_id");
                                ResultKecamatan itemProvince = new ResultKecamatan(subdistrict_name,subdistrict_id, cityId, provinceId, province, type, cityName);
                                ListKecamatan.add(itemProvince);
                                mListView.setAdapter(adapter_kecamatan);
                            }
                            adapter_kecamatan.setList(ListKecamatan);
                            adapter_kecamatan.filter("");
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error from url", "" + error.getMessage());
                Log.e("Error from url", "" + error);
                Toast.makeText(mContext,"Kesalahan memuat data" ,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<>();
                headers.put("key", API_KEY);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public HargaFragment() {
    }

    private void onGetCost(Context context, final String origin, final String destination,
                          final String weight, final String courier) {
        pDialogGet.show();

        String url = URL.URL_ROOT_HTTPS + "cost";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        refreshLayout.setRefreshing(false);
                        Costs itemCost;
                        String code, name;
                        Log.e(TAG, " Response From URL cost: " + response);
                        try {
                            String cost;
                            JSONObject obj, obj1, obj2;
                            JSONObject object = new JSONObject(response);
                            String result = object.getString("rajaongkir");
                            JSONObject object1 = new JSONObject(result);
                            String data = object1.getString("results");
                            JSONArray array = new JSONArray(data);
                            for (int i = 0; i < array.length(); i++) {
                                obj = array.getJSONObject(i);
                                code = obj.getString("code");
                                name = obj.getString("name");
                                Log.e(TAG, " code " + code + " name " + name);

                                //JSONObject object2 = new JSONObject(String.valueOf(obj));
                                String costs = obj.getString("costs");
                                if (costs.equals("[]")){
                                    break;
                                }else {
                                    //JSONObject object3 = new JSONObject(costs);
                                    JSONArray array1 = new JSONArray(costs);
                                    for (int ri = 0; ri < array1.length(); ri++) {
                                        obj1 = array1.getJSONObject(ri);
                                        String service = obj1.getString("service");
                                        String description = obj1.getString("description");
                                        Log.e(TAG, " service " + service + " description " + description);

                                        cost = obj1.getString("cost");
                                        JSONArray array2 = new JSONArray(cost);
                                        for (int i1 = 0; i1 < array2.length(); i1++) {
                                            obj2 = array2.getJSONObject(i1);
                                            String value = obj2.getString("value");
                                            String etd = obj2.getString("etd");
                                            String note = obj2.getString("note");
                                            itemCost = new Costs(code, name, cost, service, description, cost, value, etd, note);
                                            Log.e(TAG, " cost " + cost + " value " + value + " etd " + etd + " note " + note);
                                            ListHarga.add(itemCost);
                                        }
                                    }
                                }
                                adapter_harga.notifyDataSetChanged();
                            }

                            adapter_harga.addList(ListHarga);
                            pDialogGet.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error from url", "" + error.getMessage());
                Log.e("Error from url", "" + error);
                pDialogGet.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("key", API_KEY);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> bodys = new HashMap<>();
                bodys.put("origin", origin);
                bodys.put("destination", destination);
                bodys.put("originType", "subdistrict");
                bodys.put("destinationType", "subdistrict");
                bodys.put("weight", weight);
                bodys.put("courier", courier);
                bodys.put("Content-Type", "application/x-www-form-urlencoded");
                return bodys;
            }
        };
        request.setRetryPolicy( new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
}
