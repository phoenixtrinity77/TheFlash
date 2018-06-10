package com.trinity.phoenix.theflash;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    List<WallpaperData> wallpaperData;
    private int height, width;
    private ImageView imageForPromt, placeholderImage;
    TextView setAsWallPaperButton;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.listView_pic);
        wallpaperData = new ArrayList<>();

        MobileAds.initialize(this, "ca-app-pub-3530215583120862~8804856867");

        AdView adView1 = (AdView)findViewById(R.id.ironmanadView1);
        AdView adView2 = (AdView)findViewById(R.id.flashadView2);
        AdView adView3 = (AdView)findViewById(R.id.flashadView3);
        AdView adView4 = (AdView)findViewById(R.id.flashadView4);
        AdView adView5 = (AdView)findViewById(R.id.flashadView5);
        AdView adView6 = (AdView)findViewById(R.id.flashadView6);
        AdView adView7 = (AdView)findViewById(R.id.flashadView7);
        AdView adView8 = (AdView)findViewById(R.id.flashadView8);
        AdView adView17 = (AdView)findViewById(R.id.flashadView17);

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
        AdRequest adRequest17 = new AdRequest.Builder().build();
        adView17.loadAd(adRequest17);

        getIronManWallpaper();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //listViewCallingMethods(position);
                Intent intent = new Intent(MainActivity.this,DownloadActivity.class);
                intent.putExtra("url",wallpaperData.get(position).getUrl());
                startActivity(intent);
            }
        });
        permission();
    }

    private void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    permission();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void getIronManWallpaper(){
        final FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference myref = fd.getReference().child("FLASH");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        wallpaperData.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            WallpaperData a = ds.getValue(WallpaperData.class);
            wallpaperData.add(a);
        }

        Collections.shuffle(wallpaperData);
        WallpaperAdapter adapter = new WallpaperAdapter(MainActivity.this,wallpaperData);
        gridView.setAdapter(adapter);
    }

    public void listViewCallingMethods(int position) {

        AlertDialog.Builder mWallpaperDialog = wallPaperPromt();

        GetDisplaySize();

        Glide.with(MainActivity.this).
                load(wallpaperData.get(position).getUrl())
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        bitmap = resource;
                        SaveImage(bitmap);
                        return false;
                    }
                })
                .into(placeholderImage);

        mWallpaperDialog
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                });


        AlertDialog alertDialog = mWallpaperDialog.create();
        alertDialog.show();

        alertDialog.getWindow().setLayout(width - (width / 100) * 20, height - (height / 100) * 20);

    }

    private AlertDialog.Builder wallPaperPromt() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.item_promt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(promptsView);

        placeholderImage = (ImageView) promptsView.findViewById(R.id.placeHolderImage);

        setAsWallPaperButton = (TextView) promptsView
                .findViewById(R.id.setWallpaper);

        setAsWallPaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveImage(bitmap);
            }
        });
        return alertDialogBuilder;
    }

    public void GetDisplaySize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
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
            Toast.makeText(MainActivity.this,"Downloaded", Toast.LENGTH_LONG).show();
            out.flush();
            out.close();
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
