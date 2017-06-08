package com.example.dangkhoa.ministore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dangkhoa.ministore.R;
import com.example.dangkhoa.ministore.activity.GioHang;
import com.example.dangkhoa.ministore.activity.MainActivity;
import com.example.dangkhoa.ministore.model.gioHang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Khoa on 6/7/2017.
 */

public class AdapterGioHang extends BaseAdapter {
    Context context;
    ArrayList<gioHang> arrayGioHang;

    public AdapterGioHang(Context context, ArrayList<gioHang> arrayGioHang) {
        this.context = context;
        this.arrayGioHang = arrayGioHang;
    }

    @Override
    public int getCount() {
        return arrayGioHang.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayGioHang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        public TextView textviewtenGH, textviewgiaGH;
        public ImageView imageviewGH;
        public Button btntru, btngiatri, btncong;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_giohang, null);
            viewHolder.textviewtenGH = (TextView) convertView.findViewById(R.id.textViewTenGioHang);
            viewHolder.textviewgiaGH = (TextView) convertView.findViewById(R.id.textViewGiaGioHang);
            viewHolder.imageviewGH = (ImageView) convertView.findViewById(R.id.imageViewGioHang);
            viewHolder.btncong = (Button) convertView.findViewById(R.id.btnCong);
            viewHolder.btntru = (Button) convertView.findViewById(R.id.btnTru);
            viewHolder.btngiatri = (Button) convertView.findViewById(R.id.btnGiaTri);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        gioHang giohang = (gioHang) getItem(position);
        viewHolder.textviewtenGH.setText(giohang.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.textviewgiaGH.setText(decimalFormat.format(giohang.getGiasp()) + " Đ");
        Picasso.with(context).load(giohang.getHinhanhsp())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(viewHolder.imageviewGH);
        viewHolder.btngiatri.setText(giohang.getSoluongsp() + "");
        int sl = Integer.parseInt(viewHolder.btngiatri.getText().toString());
        if (sl >= 10) {
            viewHolder.btncong.setVisibility(View.INVISIBLE);
            viewHolder.btntru.setVisibility(View.VISIBLE);
        }else if(sl <= 1){
            viewHolder.btntru.setVisibility(View.INVISIBLE);
        }else if(sl >= 1){
            viewHolder.btncong.setVisibility(View.VISIBLE);
            viewHolder.btntru.setVisibility(View.VISIBLE);
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.btncong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluongmoinhat = Integer.parseInt(finalViewHolder.btngiatri.getText().toString()) + 1;
                int soluonghientai = MainActivity.mangGioHang.get(position).getSoluongsp();
                long giaht = MainActivity.mangGioHang.get(position).getGiasp();
                MainActivity.mangGioHang.get(position).setSoluongsp(soluongmoinhat);
                long giamoinhat = (giaht*soluongmoinhat)/soluonghientai;
                MainActivity.mangGioHang.get(position).setGiasp(giamoinhat);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.textviewgiaGH.setText(decimalFormat.format(giamoinhat) + " Đ");
                GioHang.EventUltil();
                if(soluongmoinhat > 9){
                    finalViewHolder.btncong.setVisibility(View.INVISIBLE);
                    finalViewHolder.btntru.setVisibility(View.VISIBLE);
                    finalViewHolder.btngiatri.setText(String.valueOf(soluongmoinhat));
                }else{
                    finalViewHolder.btncong.setVisibility(View.VISIBLE);
                    finalViewHolder.btntru.setVisibility(View.VISIBLE);
                    finalViewHolder.btngiatri.setText(String.valueOf(soluongmoinhat));
                }
            }
        });
        viewHolder.btntru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluongmoinhat = Integer.parseInt(finalViewHolder.btngiatri.getText().toString()) - 1;
                int soluonghientai = MainActivity.mangGioHang.get(position).getSoluongsp();
                long giaht = MainActivity.mangGioHang.get(position).getGiasp();
                MainActivity.mangGioHang.get(position).setSoluongsp(soluongmoinhat);
                long giamoinhat = (giaht*soluongmoinhat)/soluonghientai;
                MainActivity.mangGioHang.get(position).setGiasp(giamoinhat);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.textviewgiaGH.setText(decimalFormat.format(giamoinhat) + " Đ");
                GioHang.EventUltil();
                if(soluongmoinhat < 2){

                    finalViewHolder.btntru.setVisibility(View.INVISIBLE);
                    finalViewHolder.btncong.setVisibility(View.VISIBLE);
                    finalViewHolder.btngiatri.setText(String.valueOf(soluongmoinhat));
                }else{
                    finalViewHolder.btncong.setVisibility(View.VISIBLE);
                    finalViewHolder.btntru.setVisibility(View.VISIBLE);
                    finalViewHolder.btngiatri.setText(String.valueOf(soluongmoinhat));
                }
            }
        });
        return convertView;
    }
}
