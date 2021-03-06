package com.tendcloud.tdalbum.browse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.tendcloud.tdalbum.R;
import com.tendcloud.tdalbum.util.ImageLoader;

import java.util.List;


public class SelectResultAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> data;

    public SelectResultAdapter(Context context, List<String> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (data != null) {
            return data.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.picture_item, parent, false);
            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String path = data.get(position);
        ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(path, holder.imageView);

        return convertView;
    }

    private static class Holder {
        ImageView imageView;
    }

}
