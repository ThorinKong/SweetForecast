package com.sweet.sweet_weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sweet.sweet_weather.adapter.ViewPagerAdapter;
import com.sweet.sweet_weather.animations.ZoomOutPageTransformer;
import com.sweet.sweet_weather.pojo.WeatherPageInfo;
import com.sweet.sweet_weather.util.WeatherInfoGetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

/**
 * 使用和风天气sdk，介绍文档见
 * @see <a href="https://dev.heweather.com/docs/sdk/android">和风天气Android SDK</a>
 */
public class MainActivity extends AppCompatActivity {
    /**
     * 页面显示不同城市天气信息的viewPage
     */
    private ViewPager2 viewPage2;
    public static ViewPagerAdapter mainAdapter;
    /**
     * 保证线程安全，使用CopyOnWriteArrayList
     */
    public final static List<WeatherPageInfo> pageInfoList = new CopyOnWriteArrayList<>();

    public final static List<String > cityList = new ArrayList<>(16);

    private WeatherInfoGetter weatherSetter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //和风天气sdk账号初始化
        HeConfig.init("****PublicId*****","*******PrivateKey******");
        //使用免费节点
        HeConfig.switchToFreeServerNode();


        Log.i("来自男朋友的消息", "甜甜最棒！");

        viewPage2 = findViewById(R.id.main_viewpage2);
        mainAdapter =new ViewPagerAdapter(this,pageInfoList,viewPage2);
        viewPage2.setAdapter(mainAdapter);
        viewPage2.setPageTransformer(new ZoomOutPageTransformer());
        weatherSetter = new WeatherInfoGetter(mainAdapter,this);
        //读取储存的地址列表，默认里面有北京和上海
        SharedPreferences pref = getSharedPreferences("cityList",MODE_PRIVATE);
        String cityListStr = pref.getString("cityListStr","[\"北京\",\"上海\"]");
        Gson gson = new Gson();
        cityList.addAll(gson.fromJson(cityListStr,new TypeToken<List<String>>(){}.getType()));
        //第0个元素为null,获取天气信息时如果地址是null那么自动获取当地的天气信息,需要提前在设置里开启定位权限
        cityList.add(0,null);

        //刷新视图
        refreshWeatherList(cityList);
    }

    public void refreshWeatherList(@NonNull List<String > cityList){
        pageInfoList.clear();//清空原始的天气列表
        //开启回调地狱吧啊哈哈哈哈哈！
        List<Runnable> runnableList = new ArrayList<>(cityList.size());
        for (int i=0;i<cityList.size();i++){
            int currentLevel = i;//当前回调的层数,从0开始记
            int totalLevel = cityList.size()-1;//总共回调多少次
            runnableList.add(()->{
                WeatherPageInfo info = new WeatherPageInfo();
                weatherSetter.addWeather(info,cityList.get(currentLevel),()->{
                    pageInfoList.add(info);
                    mainAdapter.notifyDataSetChanged();
                    if(currentLevel!=totalLevel){
                        //如果当前所处的回调不是最后一个回调，那么就调用下一个回调
                        runnableList.get(currentLevel+1).run();
                    }
                });
            });
        }
        if(runnableList.size()>0){
            runnableList.get(0).run();
        }
    }
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }


}