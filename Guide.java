package com.example.kon_boot.tour;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Guide extends AppCompatActivity implements LocationListener {
    ImageView img1, img2, img3;
    EditText edtname, edtvicinity, edtlocation;
    Button btnloc, btnsubmit;
    String file1,file2,file3,downloaduri1,downloaduri2,downloaduri3;
    Uri uri,uri1,uri2;
    ArrayList<Uri> arrayList = new ArrayList<Uri>();
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private StorageTask mplaceupload;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST1 = 18;
    private static final int MY_CAMERA_PERMISSION_CODE1 = 10;
    private static final int CAMERA_REQUEST2 = 188;
    private static final int MY_CAMERA_PERMISSION_CODE2 = 1;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        edtname = findViewById(R.id.placename);
        edtvicinity = findViewById(R.id.vicinityname);
        edtlocation = findViewById(R.id.latitude);
        img1 = findViewById(R.id.photo1);
        img2= findViewById(R.id.photo2);
        img3 =findViewById(R.id.photo3);
        btnsubmit = findViewById(R.id.placebutton);
        btnloc = findViewById(R.id.btnlat);
        storageReference = FirebaseStorage.getInstance().getReference("newplacerequest");
        databaseReference = FirebaseDatabase.getInstance().getReference("newplacerequest");
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mplaceupload != null && mplaceupload.isInProgress()) {
                    Toast.makeText(Guide.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadfile();
                    Intent intent = new Intent(Guide.this, MainActivity.class);
                    startActivity(intent);


                }
            }
        });
        btnloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST1);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST2);
            }
        });
    }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
        if (requestCode == MY_CAMERA_PERMISSION_CODE1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST1);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == MY_CAMERA_PERMISSION_CODE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST2);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img1.setImageBitmap(photo);
            uri = getImageUri(getApplicationContext(), photo);
             file1 = getRealPathFromURI(uri);
            Toast.makeText(this,uri.toString(),Toast.LENGTH_SHORT).show();
            Toast.makeText(this,file1.toString(),Toast.LENGTH_SHORT).show();

        }
        if (requestCode == CAMERA_REQUEST1 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img2.setImageBitmap(photo);
            uri1 = getImageUri(getApplicationContext(), photo);
            file2 = getRealPathFromURI(uri1);
            Toast.makeText(this,uri1.toString(),Toast.LENGTH_SHORT).show();
            Toast.makeText(this,file2.toString(),Toast.LENGTH_SHORT).show();
        }
        if (requestCode == CAMERA_REQUEST2 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img3.setImageBitmap(photo);
            uri2 = getImageUri(getApplicationContext(), photo);
           file3 = getRealPathFromURI(uri2);

            Toast.makeText(this,uri2.toString(),Toast.LENGTH_SHORT).show();
            Toast.makeText(this,file3.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void uploadfile()
    {
        if( edtname!=null && edtvicinity!=null && edtlocation!=null)
        {
            StorageReference fileRef = storageReference.child(System.currentTimeMillis()+ "."+getFileExtension(Uri.parse(file1)));

            StorageReference fileRef1 = storageReference.child(System.currentTimeMillis()+ "."+getFileExtension(Uri.parse(file2)));

            StorageReference fileRef2 = storageReference.child(System.currentTimeMillis()+ "."+getFileExtension(Uri.parse(file3)));
            mplaceupload=fileRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                             downloaduri1 = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Guide.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
                    fileRef1.putFile(uri1)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                     downloaduri2 = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Guide.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    fileRef2.putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                             downloaduri3 = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            PlaceUpload upload = new PlaceUpload(edtname.getText().toString(),edtvicinity.getText().toString(),edtlocation.getText().toString(),downloaduri1,downloaduri2,downloaduri3);
                            String uploadId =databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(upload);
                            Toast.makeText(Guide.this,"Upload Successful",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Guide.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show();
        }

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        edtlocation.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            edtlocation.setText(edtlocation.getText() + "\n" + addresses.get(0).getAddressLine(0) );
        } catch (Exception e) {

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

