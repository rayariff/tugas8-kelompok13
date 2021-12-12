package com.base.camp.shippingcharges.model.tarif;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Costs {

    @SerializedName("cost")
    @Expose
    private String cost;
    @Expose
    private String value;
    @SerializedName("etd")
    @Expose
    private String etd;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("costs")
    @Expose
    private String costs;

    @SerializedName("code")
    @Expose
    private String code;


    @SerializedName("name")
    @Expose
    private String name;

    public Costs(String code, String name, String costs, String service, String description, String cost, String value, String etd, String note) {

        this.value = value;
        this.etd = etd;
        this.note = note;
        this.cost = cost;
        this.costs = costs;
        this.service = service;
        this.description = description;
        this.name = name;
        this.code = code;

    }

    public String getCosts() {
        return costs;
    }

    public void setCosts(String costs) {
        this.costs = costs;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCost() {
        return note;
    }

    public void setCost(String cost) {
        this.note = note;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
