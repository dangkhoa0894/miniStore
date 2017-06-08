package com.example.dangkhoa.ministore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dangkhoa.ministore.R;
import com.example.dangkhoa.ministore.model.sanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Khoa on 6/3/2017.
 */

public class AdapterSanPham extends RecyclerView.Adapter<AdapterSanPham.itemHolder>{
    Context context;
    ArrayList<sanPham> sanPhamArrayList;

    public AdapterSanPham(Context context, ArrayList<sanPham> sanPhamArrayList) {
        this.context = context;
        this.sanPhamArrayList = sanPhamArrayList;
    }

    @Override
    public itemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_sanphammoinhat, null);
        itemHolder itemH = new itemHolder(v);
        return itemH;
    }

    @Override
    ///hỗ trợ set và get thuộc tính lên layout
    public void onBindViewHolder(itemHolder holder, int position) {
        sanPham sp = sanPhamArrayList.get(position);
        holder.txtvTenSanPham.setText(sp.getTenSanPham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtvGiaSanPham.setText("Giá: " + decimalFormat.format(sp.getGiaSanPham()) + " Đ");
        Picasso.with(context).load(sp.getHinhAnhSanPham())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.imgHinhAnhSanPham);
    }

    @Override
    public int getItemCount() {
        return sanPhamArrayList.size();
    }


    public class itemHolder extends RecyclerView.ViewHolder{
        public ImageView imgHinhAnhSanPham;
        public TextView txtvTenSanPham, txtvGiaSanPham;

        public itemHolder(View itemView) {
            super(itemView);
            imgHinhAnhSanPham = (ImageView) itemView.findViewById(R.id.imgvSanPham);
            txtvTenSanPham = (TextView) itemView.findViewById(R.id.txtvTenSanPham);
            txtvGiaSanPham = (TextView) itemView.findViewById(R.id.txtvGiaSanPham);
        }
    }
}
