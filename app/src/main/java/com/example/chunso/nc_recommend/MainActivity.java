package com.example.chunso.nc_recommend;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chunso.nc_recommend.Controller.CollaborationFilltering;
import com.example.chunso.nc_recommend.View.LoginForm;
import com.example.chunso.nc_recommend.View.UserUpdateForm;
import com.example.chunso.nc_recommend.VO.MovieVO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private Bundle bundle = new Bundle();
    private String loginid;

    private CollaborationFilltering CF;
    private ArrayList<MovieVO> MV;

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();
        loginid = intent.getStringExtra("id");
        ActionBar ab = getSupportActionBar();

        CF = new CollaborationFilltering(loginid);
        MV = CF.excuteCF();

        pager = (ViewPager)findViewById(R.id.viewpager);
        final Item mPagerAdapter = new Item(loginid,this,MV);
        pager.setAdapter(mPagerAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_updating) {
            Toast.makeText(this, "정보수정", Toast.LENGTH_SHORT).show();
            bundle.putString("id",loginid);

            intent = new Intent(MainActivity.this,UserUpdateForm.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = auto.edit();

            editor.clear();
            editor.commit();
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show();

            intent = new Intent(MainActivity.this,LoginForm.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
