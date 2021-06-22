package com.sweet.sweet_weather.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.sweet.sweet_weather.adapter.ViewPagerAdapter;
import com.sweet.sweet_weather.pojo.WeatherPageInfo;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class WeatherInfoGetter {
    private ViewPagerAdapter adapter;
    private Context activity;

    public WeatherInfoGetter(){

    }

    public WeatherInfoGetter(ViewPagerAdapter adapter, Context activity) {
        this.adapter = adapter;
        this.activity = activity;
    }
    public void notifyChange(){
        adapter.notifyDataSetChanged();
    }

    public void addWeather(final WeatherPageInfo info,String cityName,Runnable finish){
        addWeather(info,cityName,finish,finish);
    }

    public void addWeather(final WeatherPageInfo info,String cityName,Runnable success,Runnable fail){

        HeWeather.OnResultAirNowBeansListener airListener = new HeWeather.OnResultAirNowBeansListener() {
            @Override
            public void onError(Throwable throwable) {
//                activity.runOnUiThread(()->
                        Toast.makeText(activity, "获取空气质量错误!"+throwable.getMessage(), Toast.LENGTH_LONG).show();
//                );
                fail.run();
            }

            @Override
            public void onSuccess(AirNow airNow) {

                String status = airNow.getStatus();
                if(Code.OK.getCode().equalsIgnoreCase(status)){
//                    activity.runOnUiThread(()-> Toast.makeText(activity, "获取本地空气质量错误!错误码:"+status, Toast.LENGTH_LONG).show());
//                    return;
                    info.setAirNowCity(airNow.getAir_now_city());
//                    success.run();
                }
                success.run();
            }
        };

        HeWeather.OnResultWeatherDataListBeansListener weatherListener = new HeWeather.OnResultWeatherDataListBeansListener() {
            @Override
            public void onError(Throwable throwable) {
//                activity.runOnUiThread(()->
                        Toast.makeText(activity, "获取天气错误!"+throwable.getMessage(), Toast.LENGTH_LONG).show();
//                );
                fail.run();
            }

            @Override
            public void onSuccess(Weather w) {
                String status = w.getStatus();
                if(!Code.OK.getCode().equalsIgnoreCase(status)){
//                    activity.runOnUiThread(()->
                            Toast.makeText(activity, "获取本地天气错误!错误码:"+status, Toast.LENGTH_LONG).show();
//                    );
                    return;
                }

                info.setBasic(w.getBasic());
                info.setDailyForecast(w.getDaily_forecast());
                info.setLifeStyle(w.getLifestyle());
                info.setNowBase(w.getNow());

                HeWeather.getAirNow(activity, w.getBasic().getParent_city(),Lang.CHINESE_SIMPLIFIED, Unit.METRIC, airListener);
            }
        };




        if(cityName==null){
            HeWeather.getWeather(activity, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, weatherListener);
        }else {
            HeWeather.getWeather(activity, cityName,Lang.CHINESE_SIMPLIFIED, Unit.METRIC, weatherListener);
        }
    }
}
