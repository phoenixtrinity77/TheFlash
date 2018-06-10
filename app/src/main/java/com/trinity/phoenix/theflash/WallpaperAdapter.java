package com.trinity.phoenix.theflash;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WallpaperAdapter extends ArrayAdapter<WallpaperData> {
    private Activity context;
    private List<WallpaperData> infoList;

    public WallpaperAdapter(Activity context, List<WallpaperData> profileList)
    {
        super(context, R.layout.dataviewaads, profileList);
        this.context=context;
        this.infoList = profileList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.dataviewaads,null,true);

        ImageView image = (ImageView) listViewItem.findViewById(R.id.dataImageView);

        WallpaperData dataInfo = infoList.get(position);

        Glide.with(context).load(dataInfo.getUrl()).placeholder(R.mipmap.ic_launcher).thumbnail(0.1f).centerCrop().into(image);

        return listViewItem;
    }

}
