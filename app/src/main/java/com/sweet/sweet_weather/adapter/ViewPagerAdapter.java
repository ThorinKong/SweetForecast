package com.sweet.sweet_weather.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sweet.sweet_weather.MainActivity;
import com.sweet.sweet_weather.R;
import com.sweet.sweet_weather.pojo.WeatherPageInfo;
import com.sweet.sweet_weather.util.WeatherInfoGetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static android.content.Context.MODE_PRIVATE;

/**
 * 主界面viewPage2的迭代器
 */
public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private List<WeatherPageInfo> mData;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;


    public ViewPagerAdapter(Context context, List<WeatherPageInfo> data, ViewPager2 viewPager2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //viewPage创建，将main_weather_info.xml绑定到activity_main.xml的viewPage2上
        View view = mInflater.inflate(R.layout.main_weather_info, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        WeatherPageInfo data = mData.get(position);
        Basic basic = data.getBasic();
        NowBase nowBase = data.getNowBase();
        AirNowCity airNowCity = data.getAirNowCity();
        List<ForecastBase> forecastBaseList = data.getDailyForecast();
        List<LifestyleBase> lifeStyle = data.getLifeStyle();
        if (nowBase!=null){
            String tmpStr =nowBase.getTmp();
            holder.mainNowCond.setText(nowBase.getCond_txt());
            holder.mainNowTemperature.setText(tmpStr);

            holder.mainNowWindDir.setText(nowBase.getWind_dir());
            holder.mainNowWindSc.setText(nowBase.getWind_sc()+"级");
            holder.mainNowHum.setText(nowBase.getHum()+"%");
            holder.mainNowFl.setText(nowBase.getFl()+"°");
            holder.mainNowPres.setText(nowBase.getPres());
            holder.mainNowVis.setText(nowBase.getVis()+"km");
            int backgroundColor = 0x4286d7;
            switch (nowBase.getCond_code().charAt(0)){
                case '3':
                    //雨
                    backgroundColor = 0xff66778b;break;
                case '4':
                    //雪
                    backgroundColor = 0xff4286d7;break;
                case '5':
                    //雾霾
                    backgroundColor = 0xff8da1b9;break;
                default:{
                    if(nowBase.getCond_code().equals("100")){
                        //晴天
                        backgroundColor = 0xff4286d7;break;
                    }else  if (nowBase.getCond_code().equals("104")){
                        //阴天
                        backgroundColor = 0xff8da1b9;break;
                    }else {
                        //多云或未知
                        backgroundColor = 0xff7392c1;break;
                    }
                }
            }holder.mainContainer.setBackgroundColor(backgroundColor);
        }
        if (basic!=null){
            holder.mainLocation.setText(basic.getLocation());
        }
        if(airNowCity!=null){
            String str = airNowCity.getQlty();
            if(airNowCity.getQlty().length()<2)
                str = "空气质量"+str;
            holder.mainAirState.setText(str);
        }
        if(forecastBaseList!=null){
            holder.forecastBaseList.clear();
            holder.forecastBaseList.addAll(forecastBaseList);
            holder.forecastAdapter.notifyDataSetChanged();
        }
        if(lifeStyle!=null){
            holder.lifeStyle.clear();
            holder.lifeStyle.addAll(lifeStyle);
            holder.lifestyleAdapter.notifyDataSetChanged();
        }
        holder.delete.setOnClickListener(v->{
            if(position==0){
                Toast.makeText(v.getContext(),"无法删除当前位置",Toast.LENGTH_SHORT).show();
                return;
            }
            if(MainActivity.cityList.size()<=position){return;}
            String cityRemove = MainActivity.cityList.get(position);
            MainActivity.cityList.remove(position);
            MainActivity.pageInfoList.remove(position);
            MainActivity.mainAdapter.notifyDataSetChanged();

            //读取储存的地址列表
            SharedPreferences sp = v.getContext().getSharedPreferences("cityList",MODE_PRIVATE);
            String cityListStr = sp.getString("cityListStr","[\"北京\",\"上海\"]");
            Gson gson = new Gson();
            //把地址删除
            List<String> l =gson.fromJson(cityListStr,new TypeToken<List<String>>(){}.getType());
            Iterator<String > iterator = l.iterator();
            while (iterator.hasNext()){
                if (iterator.next().equals(cityRemove)){
                    iterator.remove();
                }
            }
            //储存修改后的地址
            sp.edit().putString("cityListStr",gson.toJson(l)).apply();

        });
        holder.add.setOnClickListener(v->{
            showAddCityDialog(v.getContext());
        });

    }

    @Override
    public int getItemCount() {
        //直接返回元素的个数
        return mData.size();
    }

    /**
     * 描述每个viewPage下每个页面内容,类似于spring中的bean
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView delete;//删除按钮
        TextView add;//添加按钮

        TextView mainLocation;
        TextView mainNowTemperature;
        TextView mainNowCond;
        TextView mainAirState;
        ConstraintLayout mainContainer;

        TextView mainNowWindDir;//风向
        TextView mainNowWindSc;//风力
        TextView mainNowHum;//相对湿度
        TextView mainNowFl;//体感温度
        TextView mainNowPres;//大气压强
        TextView mainNowVis;//能见度

        RecyclerView forecastListView;
        ForecastAdapter forecastAdapter;
        final List<ForecastBase> forecastBaseList = new ArrayList<>(4);//未来n天的预报

        GridView mainNowLifestyleView;//生活状态的网格
        LifeStyleAdapter lifestyleAdapter;
        final List<LifestyleBase> lifeStyle = new ArrayList<>(8);//生活指数

        ViewHolder(View v) {
            super(v);
            delete = v.findViewById(R.id.main_delete);
            add = v.findViewById(R.id.main_add);

            mainContainer = v.findViewById(R.id.main_container);
            mainLocation = v.findViewById(R.id.main_location);
            mainNowTemperature = v.findViewById(R.id.main_temperature);
            mainNowCond = v.findViewById(R.id.main_cond);
            mainAirState = v.findViewById(R.id.main_air_state);
            forecastListView = v.findViewById(R.id.main_forecast_view);

            forecastListView.setLayoutManager(new LinearLayoutManager(v.getContext()));
            forecastListView.setHasFixedSize(true);
            forecastAdapter = new ForecastAdapter(v.getContext(),forecastBaseList);
            forecastListView.setAdapter(forecastAdapter);

            mainNowWindDir = v.findViewById(R.id.main_now_wind_dir);
            mainNowWindSc = v.findViewById(R.id.main_now_wind_sc);
            mainNowHum = v.findViewById(R.id.main_now_hum);
            mainNowFl = v.findViewById(R.id.main_now_fl);
            mainNowPres = v.findViewById(R.id.main_now_pres);
            mainNowVis = v.findViewById(R.id.main_now_vis);

            mainNowLifestyleView = v.findViewById(R.id.main_now_lifestyle_view);

            lifestyleAdapter = new LifeStyleAdapter(lifeStyle);
            mainNowLifestyleView.setAdapter(lifestyleAdapter);

        }
    }
    private void showAddCityDialog(Context context) {
        final EditText editText = new EditText(context);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(context);
        inputDialog.setTitle("请输入要添加的城市名称").setView(editText);
        inputDialog.setPositiveButton("确定",(dialog, which) -> {

            String cityName = editText.getText().toString();
            WeatherPageInfo pageInfo = new WeatherPageInfo();
            WeatherInfoGetter getter = new WeatherInfoGetter(MainActivity.mainAdapter,context);
            getter.addWeather(pageInfo,cityName,()->{
                MainActivity.cityList.add(cityName);
                MainActivity.pageInfoList.add(pageInfo);
                MainActivity.mainAdapter.notifyDataSetChanged();
                //读取储存的地址列表
                SharedPreferences sp = context.getSharedPreferences("cityList",MODE_PRIVATE);
                String cityListStr = sp.getString("cityListStr","[\"北京\",\"上海\"]");
                Gson gson = new Gson();
                //把地址添加上
                List<String> l =gson.fromJson(cityListStr,new TypeToken<List<String>>(){}.getType());
                l.add(cityName);
                //储存修改后的地址
                sp.edit().putString("cityListStr",gson.toJson(l)).apply();

            },()->{
                Toast.makeText(context, "无法获取城市天气", Toast.LENGTH_SHORT).show();
            });

        });
        inputDialog.create().show();

    }

}
