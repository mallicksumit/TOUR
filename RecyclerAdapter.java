package com.example.kon_boot.tour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    private List<Result> item;
    double sourceLatitude ,sourceLongitude;
  Context context;
  String name;

    public RecyclerAdapter(List<Result>item,double sourceLatitude,double sourceLongitude,Context context)
    {
       this.item=item;
        this.context=context;
        this.sourceLatitude =sourceLatitude;
        this.sourceLongitude =sourceLongitude;


        }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.customized,parent,false);
        return new RecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position)
    { final Result item1= item.get(position);
        List<Photo> Sum = item1.getPhotos();
        Integer width = 1300;
        String Ref = Sum.get(0).getPhotoReference();
        Geometry geom =item1.getGeometry();
        Location loc = geom.getLocation();
        final String lati=loc.getLat().toString();
        final String longi =loc.getLng().toString();

       holder.longit.setText(longi.substring(0,5));
       holder.latit.setText(lati.substring(0,5));
       holder.txt1.setText(item1.getName());
       holder.txt2.setText(item1.getVicinity());
        name = item1.getName().replaceAll(" ", "_");
        Random r = new Random();
        float min = 1.0F;
        float max =4.0F;
        float random = min + r.nextFloat() * (max - min);
        holder.rate.setRating(random);

        Log.d("Photo","" + Ref);
       String str ="https://maps.googleapis.com/maps/api/place/photo?maxwidth="+width+"&photoreference="+Ref+"&key=AIzaSyAKFbBnnGqe0rv6RCnIVFAJZL4uBaoGndc";
       Glide.with(context).load(str).into(holder.imagelist);
        holder.btnnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?saddr=" + sourceLatitude + "," + sourceLongitude + "&daddr=" + lati + "," + longi;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView imagelist;
        TextView txt1,txt2,latit,longit;
        RatingBar rate;
        Button btnnav;
      public RecyclerViewHolder(View itemView)
      {
          super(itemView);
          imagelist =(ImageView)itemView.findViewById(R.id.postimage);
          txt1=(TextView)itemView.findViewById(R.id.Heading);
          txt2=(TextView)itemView.findViewById(R.id.snippet);
          rate =itemView.findViewById(R.id.rating);
          latit=itemView.findViewById(R.id.lat);
          longit=itemView.findViewById(R.id.longitude);
          btnnav =itemView.findViewById(R.id.navigate);

      }
    }
}
