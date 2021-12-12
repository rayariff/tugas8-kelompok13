package com.base.camp.shippingcharges.model.kecamatan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultKecamatan {

    @SerializedName("subdistrict_id")
    @Expose
    private String subdistrict_id;

    @SerializedName("subdistrict_name")
    @Expose
    private String subdistrict_name;

    @SerializedName("city_id")
    @Expose
    private String cityId;

    @SerializedName("province_id")
    @Expose
    private String provinceId;

    @SerializedName("province")
    @Expose
    private String province;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("city_name")
    @Expose
    private String cityName;


    public ResultKecamatan(String subdistrict_name,String subdistrict_id, String cityId, String provinceId, String province, String type, String cityName) {
        this.subdistrict_name = subdistrict_name;
        this.cityId = cityId;
        this.provinceId = provinceId;
        this.province = province;
        this.type = type;
        this.cityName = cityName;
        this.subdistrict_id = subdistrict_id;
    }

    public String getSubdistrict_name() {
        return subdistrict_name;
    }

    public void setSubdistrict_name(String subdistrict_name) {
        this.subdistrict_name = subdistrict_name;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSubdistrict_id() {
        return subdistrict_id;
    }

    public void setSubdistrict_id(String subdistrict_id) {
        this.subdistrict_id = subdistrict_id;
    }
}
