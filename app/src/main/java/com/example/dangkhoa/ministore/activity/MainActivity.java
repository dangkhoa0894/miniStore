package com.example.dangkhoa.ministore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.dangkhoa.ministore.R;
import com.example.dangkhoa.ministore.adapter.AdapterLoaiSp;
import com.example.dangkhoa.ministore.adapter.AdapterSanPham;
import com.example.dangkhoa.ministore.model.LoaiSp;
import com.example.dangkhoa.ministore.model.gioHang;
import com.example.dangkhoa.ministore.model.sanPham;
import com.example.dangkhoa.ministore.util.Server;
import com.example.dangkhoa.ministore.util.checkConnection;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listView;
    ArrayList<LoaiSp> mangLoaiSp;
    CardView cardView;

    TextView textViewTenKhachHang, textViewEmailKhachHang;
    ProfilePictureView profilePictureView;
    LoginButton loginButton;
    ImageButton btnlogout;
    LinearLayout linearLayout;

    AdapterLoaiSp adapterLoaiSp;

    int id = 0;
    String tenloaisp = "";
    String hinhanhloaisp = "";
    ArrayList<sanPham> mangSanPham;
    AdapterSanPham adapterSanPham;
    public static ArrayList<gioHang> mangGioHang;
    CallbackManager callbackManager;//khi gui tu app len server 1 doan tin nhan| server gui ve thong qua callback nay

    public static String fbName, fbEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        AnhXa();
        btnlogout.setVisibility(View.INVISIBLE);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        setLogin();
        setLogout();
        if( checkConnection.haveNetworkConnection(getApplicationContext())){
            ActionBar();
            ActionViewFlipper();
            GetDuLieuLoaiSanPham();
            GetDuLieuSanPhamMoiNhat();
            CatchOnItemListView();

        }
        else{
            checkConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại kết nối");
            finish();
        }

    }

    private void setLogout() {
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                btnlogout.setVisibility(View.INVISIBLE);
                //textViewTenKhachHang.setVisibility(View.VISIBLE);
                textViewTenKhachHang.setText("Xin chào");
                profilePictureView.setProfileId(null);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void result(){
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON", response.getJSONObject().toString());
                try {
                    fbName = object.getString("name");
                    fbEmail = object.getString("email");
                    //fbName = object.getString("first_name");
                    profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
                    textViewTenKhachHang.setText(fbName + "\n" + "Email: " + fbEmail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","name,email,first_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    private void setLogin() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                loginButton.setVisibility(View.INVISIBLE);
                btnlogout.setVisibility(View.VISIBLE);
                result();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menucart:
                Intent intent = new Intent(getApplicationContext(),GioHang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void CatchOnItemListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if(checkConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                    }else{
                            checkConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 1:
                        if(checkConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, DienThoaiActivity.class);
                            intent.putExtra("idSanPham", mangLoaiSp.get(position).getId());
                            startActivity(intent);
                        }else{
                            checkConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if(checkConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, TabletActivity.class);
                            intent.putExtra("idSanPham", mangLoaiSp.get(position).getId());
                            startActivity(intent);
                        }else{
                            checkConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 3:
                        if(checkConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, LaptopActivity.class);
                            intent.putExtra("idSanPham", mangLoaiSp.get(position).getId());
                            startActivity(intent);
                        }else{
                            checkConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 4:
                        if(checkConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, PhuKienActivity.class);
                            intent.putExtra("idSanPham", mangLoaiSp.get(position).getId());
                            startActivity(intent);
                        }else{
                            checkConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 5:
                        if(checkConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, LienHeActivity.class);
                            startActivity(intent);
                        }else{
                            checkConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 6:
                        if(checkConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, ThongTinActivity.class);
                            startActivity(intent);
                        }else{
                            checkConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });
    }

    private void GetDuLieuSanPhamMoiNhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlSanPhamMoiNhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null){
                    int ID = 0;
                    String tensanpham = "";
                    Integer giasanpham = 0;
                    String hinhanhsanpham = "";
                    String motasanpham = "";
                    int IDsanPham = 0;
                    for(int i = 0; i < response.length(); i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            tensanpham = jsonObject.getString("tenSanPham");
                            giasanpham = jsonObject.getInt("giaSanPham");
                            hinhanhsanpham = jsonObject.getString("hinhAnhSanPham");
                            motasanpham = jsonObject.getString("moTaSanPham");
                            IDsanPham = jsonObject.getInt("idSanPham");
                            mangSanPham.add(new sanPham(ID, tensanpham, giasanpham, hinhanhsanpham, motasanpham, IDsanPham));
                            adapterSanPham.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void GetDuLieuLoaiSanPham() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());// hàm này thực hiện các phương thức gửi lên cho server
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlLoaiSP, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null){
                    for(int i = 0; i<response.length(); i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenloaisp = jsonObject.getString("tenloaisp");
                            hinhanhloaisp = jsonObject.getString("hinhanhloaisp");
                            mangLoaiSp.add( new LoaiSp(id, tenloaisp, hinhanhloaisp));
                            adapterLoaiSp.notifyDataSetChanged();// Update lai ban ve
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mangLoaiSp.add(5, new LoaiSp(0, "Liên hệ", "https://ministoreserver.000webhostapp.com/loaiSanPham/contact.png"));
                    mangLoaiSp.add(6, new LoaiSp(0, "Thông tin","https://ministoreserver.000webhostapp.com/loaiSanPham/about.png"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkConnection.ShowToast(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);//add vao request muon gui len server
    }

    //Hien thi viewFlipper
    private void ActionViewFlipper() {
        ArrayList<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add("https://cdn4.tgdd.vn/qcao/12_05_2017_16_59_28_Galaxy-A5-800-300.png");
        mangQuangCao.add("http://cdn.fptshop.com.vn/Uploads/Originals/2017/5/21/636310068719558795_H1%20Oppo%20F3.png");
        mangQuangCao.add("http://cdn.fptshop.com.vn/Uploads/Originals/2017/5/18/636306696268033745_Banner-iPhone7-RED-Tro-lai.jpg");
        mangQuangCao.add("http://cdn.fptshop.com.vn/Uploads/Originals/2017/5/23/636311278400483349_Banner-VivoV5S-H1.jpg");
        for(int i = 0; i < mangQuangCao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setInAnimation(animation_slide_out);
    }

    //Hien thi icon drawerlayout
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void AnhXa() {
        toolbar = (Toolbar) findViewById(R.id.toolbarManHinhChinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewManHinhChinh);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        listView = (ListView) findViewById(R.id.listViewManHinhChinh);
        cardView = (CardView) findViewById(R.id.cardView);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnlogout = (ImageButton) findViewById(R.id.btnLogout);
        profilePictureView = (ProfilePictureView) findViewById(R.id.imgProfilePicture);
        textViewTenKhachHang = (TextView) findViewById(R.id.txtvten);
        mangLoaiSp = new ArrayList<>();
        mangSanPham = new ArrayList<>();
        mangLoaiSp.add(0,new LoaiSp(0, "Trang chính", "https://ministoreserver.000webhostapp.com/loaiSanPham/home.png"));
        adapterLoaiSp = new AdapterLoaiSp(mangLoaiSp, getApplicationContext());
        adapterSanPham = new AdapterSanPham(getApplicationContext(), mangSanPham);
        listView.setAdapter(adapterLoaiSp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setAdapter(adapterSanPham);
        if(mangGioHang != null){

        }else{
            mangGioHang = new ArrayList<>();
        }
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);
                        intent.putExtra("thongtinsanpham", mangSanPham.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }
}
