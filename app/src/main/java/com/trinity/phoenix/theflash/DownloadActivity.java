package com.trinity.phoenix.theflash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class DownloadActivity extends AppCompatActivity {

    private Bitmap bitmap;
    ImageView imageView;
    FloatingActionButton floatingActionButton;
    boolean imagedownload = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_download);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        imageView = (ImageView)findViewById(R.id.downloadimageview);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        MobileAds.initialize(this, "ca-app-pub-3530215583120862~8804856867");

        AdView adView1 = (AdView)findViewById(R.id.ironmanadView9);
        AdView adView2 = (AdView)findViewById(R.id.flashadView10);
        AdView adView3 = (AdView)findViewById(R.id.flashadView11);
        AdView adView4 = (AdView)findViewById(R.id.flashadView12);
        AdView adView5 = (AdView)findViewById(R.id.flashadView13);
        AdView adView6 = (AdView)findViewById(R.id.flashadView14);
        AdView adView7 = (AdView)findViewById(R.id.flashadView15);
        AdView adView8 = (AdView)findViewById(R.id.flashadView16);
        AdView adView18 = (AdView)findViewById(R.id.flashadView18);

        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        adView2.loadAd(adRequest2);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        adView3.loadAd(adRequest3);
        AdRequest adRequest4 = new AdRequest.Builder().build();
        adView4.loadAd(adRequest4);
        AdRequest adRequest5 = new AdRequest.Builder().build();
        adView5.loadAd(adRequest5);
        AdRequest adRequest6 = new AdRequest.Builder().build();
        adView6.loadAd(adRequest6);
        AdRequest adRequest7 = new AdRequest.Builder().build();
        adView7.loadAd(adRequest7);
        AdRequest adRequest8 = new AdRequest.Builder().build();
        adView8.loadAd(adRequest8);
        AdRequest adRequest18 = new AdRequest.Builder().build();
        adView18.loadAd(adRequest18);


        Glide.with(DownloadActivity.this).
                load(url)
                .asBitmap()
                .centerCrop()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        bitmap = resource;
                        imagedownload = true;
                        return false;
                    }
                })
                .into(imageView);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagedownload){
                    SaveImage(bitmap);
                }else{
                    Toast.makeText(DownloadActivity.this, "Image is not Loaded please wait...", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private Uri SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/IRONMAN");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(DownloadActivity.this,"Downloaded",Toast.LENGTH_LONG).show();
            out.flush();
            out.close();
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
