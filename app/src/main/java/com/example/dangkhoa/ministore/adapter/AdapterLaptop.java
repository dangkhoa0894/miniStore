package com.example.dangkhoa.ministore.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dangkhoa.ministore.R;
import com.example.dangkhoa.ministore.model.sanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Khoa on 6/6/2017.
 */

public class AdapterLaptop extends BaseAdapter {
    Context context;
    ArrayList<sanPham> laptopArrayList;

    public AdapterLaptop(Context context, ArrayList<sanPham> LaptopArrayList) {
        this.context = context;
        laptopArrayList = LaptopArrayList;
    }

    @Override
    public int getCount() {
        return laptopArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return laptopArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        public TextView txtTenLaptop, txtGiaLaptop, txtMoTaLaptop;
        public ImageView imgLaptop;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.dong_laptop, null);
            viewHolder.txtTenLaptop = (TextView) convertView.findViewById(R.id.textviewTenLaptop);
            viewHolder.txtGiaLaptop = (TextView) convertView.findViewById(R.id.textviewGiaLaptop);
            viewHolder.txtMoTaLaptop = (TextView) convertView.findViewById(R.id.textviewMoTaLaptop);
            viewHolder.imgLaptop = (ImageView) convertView.findViewById(R.id.imageviewLaptop);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        sanPham sp = (sanPham) getItem(position);
        viewHolder.txtTenLaptop.setText(sp.getTenSanPham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtGiaLaptop.setText("Giá: " + decimalFormat.format(sp.getGiaSanPham()) + " Đ");
        viewHolder.txtMoTaLaptop.setMaxLines(2);
        viewHolder.txtMoTaLaptop.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtMoTaLaptop.setText(sp.getMoTaSanPham());
        Picasso.with(context).load(sp.getHinhAnhSanPham())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(viewHolder.imgLaptop);
        return convertView;
    }
}
