package com.example.chunso.nc_recommend.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chunso.nc_recommend.DAO.UpdateDAO;
import com.example.chunso.nc_recommend.DAO.UserUpdateDAO;
import com.example.chunso.nc_recommend.R;




public class UserUpdateForm extends AppCompatActivity {
    EditText idInput,pwInput,nameInput,ageInput;
    String id,pw,name,age,result,originalId;
    String[] str;
    Button updateButton;
    private Intent intent;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userupdate);

        idInput = (EditText)findViewById(R.id.updateid);
        pwInput = (EditText)findViewById(R.id.updatepw);
        nameInput = (EditText)findViewById(R.id.updatename);
        ageInput = (EditText)findViewById(R.id.updateage);
        updateButton = (Button)findViewById(R.id.update);

        bundle=getIntent().getExtras();
        id=bundle.getString("id");
        originalId=bundle.getString("id");
        Log.d("현재 아이디", "" + id);

        UserUpdateDAO userdao=new UserUpdateDAO();

        try {
            result = userdao.execute(id).get();
            Log.d("확인",result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        str=result.split("//");
        idInput.setText(str[0]);
        pwInput.setText(str[1]);
        nameInput.setText(str[2]);
        ageInput.setText(str[3]);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDAO udao=new UpdateDAO();
                id = idInput.getText().toString();
                pw = pwInput.getText().toString();
                name = nameInput.getText().toString();
                age = ageInput.getText().toString();

                try{
                    result = udao.execute(id,pw,name,age,originalId).get();
                    Log.d("업데이트 확인",result);
                    Toast.makeText(getApplicationContext(), "정보 수정 완료.", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
