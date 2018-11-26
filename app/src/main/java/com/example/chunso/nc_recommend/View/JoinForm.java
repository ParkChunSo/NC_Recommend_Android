package com.example.chunso.nc_recommend.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chunso.nc_recommend.DAO.JoinDAO;
import com.example.chunso.nc_recommend.R;



public class JoinForm extends AppCompatActivity {
    EditText idInput,pwInput,nameInput,ageInput;
    String id,pw,name,age,result;
    Button joinButton;
    private Intent intent;
    private JoinDAO joinDAO;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        idInput = (EditText) findViewById(R.id.newid);
        pwInput = (EditText) findViewById(R.id.newpw);
        nameInput = (EditText) findViewById(R.id.newname);
        ageInput = (EditText) findViewById(R.id.newage);
        joinButton = (Button) findViewById(R.id.join);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinDAO = new JoinDAO();
                id = idInput.getText().toString();
                pw = pwInput.getText().toString();
                name = nameInput.getText().toString();
                age = ageInput.getText().toString();

                Log.d("회원 정보", id + pw+ name+ age);
                try {
                    result = joinDAO.execute(id, pw,name,age).get();
                    Log.d("로그인 결과값", "" + result);
                    if(result.equals("true")){
                        Toast.makeText(getApplicationContext(), "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                        intent = new Intent(JoinForm.this, LoginForm.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
