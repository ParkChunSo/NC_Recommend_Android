package com.example.chunso.nc_recommend.DAO;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class DeleteCommentDAO extends AsyncTask<String, Void, String> {
    String sendMsg,receiveMsg;
    String result;

    private Context mContext = null;

    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL("http://117.17.142.207:80/testDB/deletecomment.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            sendMsg = "msg="+strings[0]+"&loginid="+strings[1];

            Log.d("보냄보냄",sendMsg);
            osw.write(sendMsg);//OutputStreamWriter에 담아 전송합니다.
            osw.flush();

            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                //jsp에서 보낸 값을 받겠죠?
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();

            } else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");
                // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}