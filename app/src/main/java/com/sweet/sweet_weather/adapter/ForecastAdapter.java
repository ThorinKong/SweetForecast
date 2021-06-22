package com.sweet.sweet_weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sweet.sweet_weather.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder> {
    private List<ForecastBase> forecastList;
//    private LayoutInflater mInflater;


    public static class ForecastHolder extends RecyclerView.ViewHolder {

        public TextView timeText;
        public TextView weatherText;
        public TextView tmpText;
        public ForecastHolder(View v) {
            super(v);
            timeText = v.findViewById(R.id.forecast_time);
            weatherText = v.findViewById(R.id.forecast_weather);
            tmpText = v.findViewById(R.id.forecast_temp);
        }
    }


    public ForecastAdapter(Context context, List<ForecastBase> forecastList) {
        this.forecastList = forecastList;
//        this.mInflater = LayoutInflater.from(context);

    }


    @NotNull
    @Override
    public ForecastAdapter.ForecastHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.weather_forecast_info, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_forecast_info, parent, false);
        return new ForecastHolder(view);
    }


    @Override
    public void onBindViewHolder(@NotNull ForecastHolder holder, int position) {
        ForecastBase base = forecastList.get(position);
        if (base!=null){
            switch (position){
                case 0:
                    holder.timeText.setText("今天");break;
                case 1:
                    holder.timeText.setText("明天");break;
                case 2:
                    holder.timeText.setText("后天");break;
                default:
                    holder.timeText.setText(base.getDate());break;
            }
            String tmpStr =base.getTmp_max()+"°/"+base.getTmp_min()+"°";
            holder.tmpText.setText(tmpStr);
            if(base.getCond_txt_d().equals(base.getCond_txt_n())){
                holder.weatherText.setText(base.getCond_txt_d());
            }else {
                String weStr = base.getCond_txt_d()+"转"+base.getCond_txt_n();
                holder.weatherText.setText(weStr);
            }
        }

    }
    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public List<ForecastBase> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<ForecastBase> forecastList) {
        this.forecastList = forecastList;
    }
}
