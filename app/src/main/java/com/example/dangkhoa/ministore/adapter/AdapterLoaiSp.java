package com.example.dangkhoa.ministore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dangkhoa.ministore.R;
import com.example.dangkhoa.ministore.model.LoaiSp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dang Khoa on 5/26/2017.
 */

public class AdapterLoaiSp extends BaseAdapter {
    ArrayList<LoaiSp> arrayListLoaiSp;// Constructor khi muốn gọi lại
    Context context; // màn hình

    public AdapterLoaiSp(ArrayList<LoaiSp> arrayListLoaiSp, Context context) {
        this.arrayListLoaiSp = arrayListLoaiSp;
        this.context = context;
    }

    @Override
    public int getCount() {

        return arrayListLoaiSp.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListLoaiSp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Không cần load lại giá trị
    public class ViewHolder{
        TextView textViewLoaiSp;
        ImageView imageViewLoaiSp;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);// hàm này giúp get service là layout ra
            convertView = inflater.inflate(R.layout.dong_listview_loaisp, null);
            viewHolder.textViewLoaiSp = (TextView) convertView.findViewById(R.id.textviewLoaiSp);
            viewHolder.imageViewLoaiSp = (ImageView) convertView.findViewById(R.id.imageviewLoaiSp);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LoaiSp loaiSp = (LoaiSp) getItem(position);
        viewHolder.textViewLoaiSp.setText(loaiSp.getTenLoaiSp());
        Picasso.with(context).load(loaiSp.getHinhAnhLoaiSp())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(viewHolder.imageViewLoaiSp);
        return convertView;
    }
}
