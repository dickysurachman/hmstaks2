package itts.dicky.surachman.maps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class loginsuccess extends AppCompatActivity implements  View.OnClickListener {
    public static final String Name = "Name";
    public static final String Gambar = "Picture";
    public static final String Email1 = "Email";
    private Context context;
    private static final int GET_BY_CROP = 804;
    private static final int GET_BY_ALBUM1 = 801;
    private static final int GET_BY_CAMERA = 805;
    List<String> mPermissionList = new ArrayList<>();
    String[] permissions = new String[] {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private final int mRequestCode = 100;

    TextView teks;
    Button but1,but2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsuccess);
        teks = findViewById(R.id.textView);
        String name1 = getIntent().getStringExtra(Name);
        String gambar1 = getIntent().getStringExtra(Gambar);
        String emails = getIntent().getStringExtra(Email1);
        teks.setText("Welcome "+  name1 +"\n"+"Your Email "+  emails );
        but1 = findViewById(R.id.maps);
        but1.setOnClickListener(this);
        but2 = findViewById(R.id.imagekit2);
        but2.setOnClickListener(this);
        //Gbr = findViewById(R.id.imageView);
        new DownloadImageFromInternet((ImageView) findViewById(R.id.imageView)).execute(gambar1);

        if (Build.VERSION.SDK_INT >= 23) {
            initPermission();
        }

    }
    private void initPermission() {
        // Clear the permissions that fail the verification.
        mPermissionList.clear();
        //Check whether the required permissions are granted.
        for (int i = 0; i < permissions.length; i++) {
            if (PermissionChecker.checkSelfPermission(this, permissions[i])
                    != PermissionChecker.PERMISSION_GRANTED) {
                // Add permissions that have not been granted.
                mPermissionList.add(permissions[i]);
            }
        }
        //Apply for permissions.
        if (mPermissionList.size() > 0) {//The permission has not been granted. Please apply for the permission.
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.maps:
                Toast.makeText(loginsuccess.this, "Please wait for download sample", Toast.LENGTH_LONG)
                        .show();
                Intent goto3 = new Intent(loginsuccess.this,maps.class);
                startActivity(goto3);
                break;
            case R.id.imagekit2:
                String uriString = "petalmaps://nearbySearch?text=" + "Puskesmas"+"&utm_source="+"fb";
                Uri content_url = Uri.parse(uriString);
                Intent intent = new Intent(Intent.ACTION_VIEW, content_url);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(loginsuccess.this, "Please install Petal Maps first", Toast.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }
}