package com.example.shah.ilovezappos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by shah on 2/8/2017.
 */

public class SearchAdapter extends ArrayAdapter<SearchHistory> {

    Context mContext;
    List<SearchHistory> searchHistories;
    int mResource;

    public SearchAdapter(Context context, int resource, List<SearchHistory> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.searchHistories = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        SearchHistory search = searchHistories.get(position);

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.imageViewSearchRow);
        Picasso.with(mContext).load(search.getImg()).into(ivImage);

        TextView tvTerm = (TextView) convertView.findViewById(R.id.textViewSearchRow);
        tvTerm.setText(search.getProductName());

        return convertView;
    }
}
