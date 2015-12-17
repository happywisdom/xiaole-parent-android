package com.aibasis.parent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.aibasis.parent.R;
import com.aibasis.parent.domain.Baby;
import com.aibasis.parent.view.CircleImageView;

import java.util.List;

/**
 * Created by sniper on 2015/12/16.
 */
public class BabySpinnerAdapter extends BaseAdapter {

    private List<Baby> mList;
    private Context mContext;

    public BabySpinnerAdapter(List<Baby> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount(){
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int positon, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(R.layout.item_baby_spinner, null);
        CircleImageView circleImageView = (CircleImageView) convertView.findViewById(R.id.circle_avatar);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_baby_name);
        circleImageView.setImageResource(R.drawable.logo_uidemo);
        textView.setText(mList.get(positon).getBabyName());
        return convertView;
    }
}
