package com.example.kon_boot.tour;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class profile extends AppCompatActivity  {
      DatabaseReference databaseReference;
      private List<Upload> mUploads;
      TextView Mname,Mphn,Memail;
      ImageView profilepic;
      String recieve ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle bundle =getIntent().getExtras();
        recieve = bundle.getString("phnno");
        Mname = findViewById(R.id.name);
        Mphn = findViewById(R.id.valphn);
        Memail =findViewById(R.id.valemail);
        profilepic =findViewById(R.id.inside);

        mUploads =new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("profile");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if(recieve.equals(upload.getPhn()))
                    {
                        Mname.setText(upload.getmName());
                        Mphn.setText(upload.getPhn());
                        Memail.setText(upload.getEmail());
                        Glide.with(getApplicationContext()).load(upload.getmUrl()).into(profilepic);
                        break;
                    }
                   Toast.makeText(profile.this,"Setting up your profile",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(profile.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }



}



