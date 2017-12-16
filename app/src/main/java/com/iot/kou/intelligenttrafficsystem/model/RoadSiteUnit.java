package com.iot.kou.intelligenttrafficsystem.model;


public class RoadSiteUnit
{
    private int rsuId;
    private Double lng;
    private Double ltd;
    private String weather;
    private String smoothness;
    private String status;
    private int risk;

    public RoadSiteUnit()
    {

    }

    public RoadSiteUnit(int rsuId, Double lng, Double ltd, String weather, String smoothness)
    {
        this.rsuId = rsuId;
        this.lng = lng;
        this.ltd = ltd;
        this.weather = weather;
        this.smoothness = smoothness;
    }

    public int getRsuId()
    {
        return rsuId;
    }

    public void setRsuId(int rsuId)
    {
        this.rsuId = rsuId;
    }


    public String getWeather()
    {
        return weather;
    }

    public void setWeather(String weather)
    {
        this.weather = weather;
    }


    public Double getLng()
    {
        return lng;
    }

    public void setLng(Double lng)
    {
        this.lng = lng;
    }

    public Double getLtd()
    {
        return ltd;
    }

    public void setLtd(Double ltd)
    {
        this.ltd = ltd;
    }


    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public int getRisk()
    {
        return risk;
    }

    public void setRisk(int risk)
    {
        this.risk = risk;
    }


    public String getSmoothness()
    {
        return smoothness;
    }

    public void setSmoothness(String smoothness)
    {
        this.smoothness = smoothness;
    }
}
