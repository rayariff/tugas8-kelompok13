package com.base.camp.shippingcharges.model.provinsi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultProvinsi {
    @SerializedName("province_id")
    @Expose
    private String provinceId;
    @SerializedName("province")
    @Expose
    private String province;

    public ResultProvinsi(String provinceId, String province) {
        this.provinceId = provinceId;
        this.province = province;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

}
