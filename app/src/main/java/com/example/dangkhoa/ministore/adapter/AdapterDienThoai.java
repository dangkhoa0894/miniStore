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
 * Created by Khoa on 6/4/2017.
 */

public class AdapterDienThoai extends BaseAdapter {
    Context context;
    ArrayList<sanPham> arrayDienThoai;

    public AdapterDienThoai(Context context, ArrayList<sanPham> arrayDienThoai) {
        this.context = context;
        this.arrayDienThoai = arrayDienThoai;
    }

    @Override
    public int getCount() {
        return arrayDienThoai.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayDienThoai.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  class  viewHolder{
        public TextView txtTenDienThoai, txtGiaDienThoai, txtMoTaDienThoai;
        public ImageView imgDienThoai;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        if(convertView == null){
            holder = new viewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.dong_dienthoai, null);
            holder.txtTenDienThoai = (TextView) convertView.findViewById(R.id.txtvTenDienThoai);
            holder.txtGiaDienThoai = (TextView) convertView.findViewById(R.id.txtvGiaDienThoai);
            holder.txtMoTaDienThoai = (TextView) convertView.findViewById((R.id.txtvMoTaDienThoai));
            holder.imgDienThoai = (ImageView) convertView.findViewById(R.id.imgvDienThoai);

            convertView.setTag(holder);
        }else{
            holder = (viewHolder) convertView.getTag();
        }
        sanPham sanpham = (sanPham) getItem(position);
        holder.txtTenDienThoai.setText(sanpham.getTenSanPham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGiaDienThoai.setText("Giá: " + decimalFormat.format(sanpham.getGiaSanPham()) + " Đ");
        holder.txtMoTaDienThoai.setMaxLines(2);
        holder.txtMoTaDienThoai.setEllipsize(TextUtils.TruncateAt.END);// định dạng dài quá 2 lines hiện dấu ...
        holder.txtMoTaDienThoai.setText(sanpham.getMoTaSanPham());
        Picasso.with(context).load(sanpham.getHinhAnhSanPham())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.imgDienThoai);
        return convertView;
    }
}
