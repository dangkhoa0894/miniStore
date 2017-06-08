package com.example.dangkhoa.ministore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dangkhoa.ministore.R;
import com.example.dangkhoa.ministore.util.Server;
import com.example.dangkhoa.ministore.util.checkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ThongTinKhachHang extends AppCompatActivity {
    EditText edtTenKH, edtSoDT, edtEmail;
    Button btnXacNhan, btnTroVe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_khach_hang);

        AnhXa();
    }

    private void AnhXa() {
        btnXacNhan = (Button) findViewById(R.id.btnXacNhan);
        btnTroVe = (Button) findViewById(R.id.btnTroVe);
        edtTenKH = (EditText) findViewById(R.id.editTextTenKH);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtSoDT = (EditText) findViewById(R.id.editTextSDT);

        btnTroVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(checkConnection.haveNetworkConnection(getApplicationContext())){
            EventButton();
        }else{
            checkConnection.ShowToast(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
        }

        Bundle bd = getIntent().getExtras();
        if(bd != null){
            edtTenKH.setText(bd.getString("hoten"));
            edtEmail.setText(bd.getString("email"));
        }

    }

    private void EventButton() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ten = edtTenKH.getText().toString().trim();
                final String sdt = edtSoDT.getText().toString().trim();
                final String email = edtEmail.getText().toString().trim();
                if(ten.length() > 0 && sdt.length() > 0 && email.length()>0){
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlDonHang, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String maDonHang) {
                            Log.d("madonhang", maDonHang);
                            if(Integer.parseInt(maDonHang) > 0 ){
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                StringRequest request = new StringRequest(Request.Method.POST, Server.urlChiTietDonHang, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("1")){
                                            MainActivity.mangGioHang.clear();
                                            checkConnection.ShowToast(getApplicationContext(), "Bạn đã thêm dữ liệu giỏ hàng thành công");
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            checkConnection.ShowToast(getApplicationContext(), "Mời bạn tiếp tục mua hàng");
                                        }else{
                                            checkConnection.ShowToast(getApplicationContext(), "Dữ liệu giỏ hàng của bạn đã bị lỗi");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        JSONArray jsonArray = new JSONArray();
                                        for(int i = 0; i < MainActivity.mangGioHang.size(); i++){
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("maDonHang", maDonHang);
                                                jsonObject.put("maSanPham", MainActivity.mangGioHang.get(i).getIdsp());
                                                jsonObject.put("tenSanPham", MainActivity.mangGioHang.get(i).getTensp());
                                                jsonObject.put("giaSanPham", MainActivity.mangGioHang.get(i).getGiasp());
                                                jsonObject.put("soLuongSanPham", MainActivity.mangGioHang.get(i).getSoluongsp());
                                                jsonObject.put("emailKhachHang", email);
                                                jsonObject.put("tenKhachHang", ten);
                                                jsonObject.put("soDienThoai", sdt);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(jsonObject);
                                        }
                                        HashMap<String, String> hashMap = new HashMap<String, String>();
                                        hashMap.put("json", jsonArray.toString());
                                        return hashMap;
                                    }
                                };
                                queue.add(request);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("tenkhachhang",ten);
                            hashMap.put("sodienthoai", sdt);
                            hashMap.put("email", email);
                            return  hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);

                }else{
                    checkConnection.ShowToast(getApplicationContext(), "Hãy kiểm tra lại dữ liệu");
                }
            }
        });
    }
}
