package com.example.kon_boot.tour;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Customizedlistview  {

    Context context;
    LayoutInflater layoutInflater;



    public Customizedlistview(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.customized, null);



        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=10;
        //Bitmap bm= BitmapFactory.decodeResource(parent.getResources(),R.drawable.ic_menu_send,options);

        return convertView;
    }
}