package com.mapara.sendmyapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapara.sendmyapp.helper.SendAppUtility;

import java.util.List;

/**
 * Created by harshit on 2/28/14.
 */
public class GridAdapter extends BaseAdapter {
    private Context _ctx;
    private List<SendAppUtility.ApkInfo> _imageList;
    private LayoutInflater inflater;

    public GridAdapter(Context ctx, List<SendAppUtility.ApkInfo> imageList) {
        _ctx = ctx;
        _imageList = imageList;
        inflater = (LayoutInflater) _ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _imageList.size();
    }

    @Override
    public SendAppUtility.ApkInfo getItem(int position) {
        return _imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.grid_item, null);
            holder = new Holder();
            holder.apkImg = (ImageView) convertView.findViewById(R.id.apk_image);
            holder.apkName = (TextView) convertView.findViewById(R.id.apk_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        holder.apkImg.setImageDrawable(getItem(position).apkImg);
        holder.apkName.setText(getItem(position).apkName);
        holder.apkPath = getItem(position).apkPath;
        return convertView;
    }


}
class Holder {
    ImageView apkImg;
    TextView apkName;
    String apkPath;
}
