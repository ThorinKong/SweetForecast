package com.sweet.sweet_weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sweet.sweet_weather.R;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;

public class LifeStyleAdapter extends BaseAdapter {

    private List<LifestyleBase> styleData;


    public LifeStyleAdapter(List<LifestyleBase> styleData){
        this.styleData = styleData;
    }
    @Override
    public int getCount() {
        return styleData.size();
    }

    @Override
    public Object getItem(int position) {
        return styleData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.lifestyle_info, parent, false);
        }
        LifestyleBase style = styleData.get(position);
        ImageView imageView = convertView.findViewById(R.id.lifestyle_img);
        TextView textView = convertView.findViewById(R.id.lifestyle_disc);
        textView.setText(style.getTxt());
        switch (style.getType()){
            case "comf":{
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.comf,null));
            }break;
            case "cw":{
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.cw,null));
            }break;
            case "drsg":{
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.drsg,null));
            }break;
            case "flu":{
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.flu,null));
            }break;
            case "sport":{
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.sport,null));
            }break;
            case "trav":{
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.trav,null));
            }break;
            case "uv":{
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.uv,null));
            }break;
            case "air":{
                imageView.setImageDrawable(convertView.getResources().getDrawable(R.drawable.air,null));
            }break;
        }
        return convertView;
    }
}
