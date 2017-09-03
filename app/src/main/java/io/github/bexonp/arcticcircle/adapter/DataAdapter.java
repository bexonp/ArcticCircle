package io.github.bexonp.arcticcircle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.github.bexonp.arcticcircle.R;
import io.github.bexonp.arcticcircle.entity.Data;

/**
 * Created by Bexon Pak on 2017/08/30.
 */

public class DataAdapter extends BaseAdapter {
    public Context con;
    public List<Data> list;
    public LayoutInflater inflater;

    public DataAdapter(Context context, List<Data> user) {
        this.con = context;
        this.list = user;
        inflater = LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup paren) {
        View view = inflater.inflate(R.layout.listview_item, null);
        TextView tv1 = view.findViewById(R.id.title);
        TextView tv2 = view.findViewById(R.id.date);
        TextView tv3 = view.findViewById(R.id.excerpt);
        tv1.setText(list.get(position).getTitle());
        tv2.setText(list.get(position).getDate());
        tv3.setText(list.get(position).getExcerpt());
        return view;
    }
}
