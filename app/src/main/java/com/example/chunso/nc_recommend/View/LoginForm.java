package com.example.chunso.nc_recommend.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chunso.nc_recommend.DAO.LoginDAO;
import com.example.chunso.nc_recommend.MainActivity;
import com.example.chunso.nc_recommend.R;

public class LoginForm  extends AppCompatActivity {
    EditText idInput, pwInput;
    CheckBox autoLogin;
    Button loginButton,joinButton;
    SharedPreferences auto;
    SharedPreferences.Editor editor;

    private LoginDAO loginDAO;
    String result;
    String id, pw;
    private Intent intent;
    String autoId, autoPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); //합칠때 이름을 login.xml이런식으로 해서 보기 편하게 하쟙
        auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        editor = auto.edit();

        idInput = (EditText) findViewById(R.id.user_id);
        pwInput = (EditText) findViewById(R.id.user_pw);
        autoLogin = (CheckBox) findViewById(R.id.autologin);
        loginButton = (Button) findViewById(R.id.loginButton);
        autoId = auto.getString("autoId", null);
        autoPw = auto.getString("autoPw", null);
        joinButton = (Button) findViewById(R.id.joinButton);

        if (auto.getBoolean("autoLogin", false)) {
            idInput.setText(auto.getString("autoName", null));
            pwInput.setText(auto.getString("autoPhone", null));
            autoLogin.setChecked(true);
        }

        if (autoId != null && autoPw != null) {//문제가 발생 로그아웃이 안됌
            Intent intent = new Intent(LoginForm.this, MainActivity.class);
            intent.putExtra("id", autoId);
            intent.putExtra("pw", autoPw);
            startActivity(intent);
            Log.d("자동로그인", "" + autoId + "   " + autoPw);
            finish();

        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginDAO = new LoginDAO();
                id = idInput.getText().toString();
                pw = pwInput.getText().toString();

                Log.d("아이디, 비밀번호" + "", id + pw);
                try {
                    result = loginDAO.execute(id, pw).get();
                    Log.d("로그인 결과값", "" + result);

                    if (result.equals("true")) {
                        editor.putString("autoId", id);
                        editor.putString("autoPw", pw);
                        editor.putBoolean("autoLogin", true);
                        editor.commit();
                        intent = new Intent(LoginForm.this, MainActivity.class);

                        intent.putExtra("id", id);
                        intent.putExtra("pw", pw);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginForm.this, JoinForm.class);
                startActivity(intent);
            }
        });
    }
}
