package com.iot.kou.intelligenttrafficsystem.model;

/**
 * Created by DESTAN on 25.10.2017.
 */

public class RoadSiteUnit {
    public RoadSiteUnit() {

    }

    public RoadSiteUnit(int rsuId, String lng, String ltd, String weather, String kayganlik) {
        this.rsuId = rsuId;
        this.lng = lng;
        this.ltd = ltd;
        this.weather = weather;
        this.kayganlik = kayganlik;
    }

    public int getRsuId() {
        return rsuId;
    }

    public void setRsuId(int rsuId) {
        this.rsuId = rsuId;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLtd() {
        return ltd;
    }

    public void setLtd(String ltd) {
        this.ltd = ltd;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getKayganlik() {
        return kayganlik;
    }

    public void setKayganlik(String kayganlik) {
        this.kayganlik = kayganlik;
    }

    private int rsuId;
    private String  lng;
    private String  ltd;
    private String  weather;
    private String  kayganlik;


}
