package com.base.camp.shippingcharges.model.ekspedisi;

public class ItemEkspedisi {
    private String id;
    private String name;
    private String des;

    public ItemEkspedisi(String id, String name, String des) {
        this.id = id;
        this.des = des;
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
