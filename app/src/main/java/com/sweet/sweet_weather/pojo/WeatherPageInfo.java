package com.sweet.sweet_weather.pojo;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;

/**
 * 每页天气的页面信息
 */
public class WeatherPageInfo {
    /**
     * 关于城市的一些信息
     */
    private Basic basic;
    /**
     * 当前实况天气
     */
    private NowBase nowBase;
    /**
     * 逐天预报
     */
    private List<ForecastBase> dailyForecast;
    /**
     * 生活建议，如紫外线啊穿衣啥的
     */
    private List<LifestyleBase> lifeStyle;
    /**
     * 城市空气质量
     */
    private AirNowCity airNowCity;

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public NowBase getNowBase() {
        return nowBase;
    }

    public void setNowBase(NowBase nowBase) {
        this.nowBase = nowBase;
    }

    public List<ForecastBase> getDailyForecast() {
        return dailyForecast;
    }

    public void setDailyForecast(List<ForecastBase> dailyForecast) {
        this.dailyForecast = dailyForecast;
    }

    public List<LifestyleBase> getLifeStyle() {
        return lifeStyle;
    }

    public void setLifeStyle(List<LifestyleBase> lifeStyle) {
        this.lifeStyle = lifeStyle;
    }

    public AirNowCity getAirNowCity() {
        return airNowCity;
    }

    public void setAirNowCity(AirNowCity airNowCity) {
        this.airNowCity = airNowCity;
    }
}
