package com.example.chunso.nc_recommend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chunso.nc_recommend.DAO.CommentDAO;
import com.example.chunso.nc_recommend.DAO.DeleteCommentDAO;
import com.example.chunso.nc_recommend.DAO.FavoriteDAO;
import com.example.chunso.nc_recommend.DAO.SendCommentDAO;
import com.example.chunso.nc_recommend.VO.MovieVO;



import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Item extends PagerAdapter {
    Context context;
    ArrayList<MovieVO> MV;
    String loginID;
    Bitmap bm;
    String result;
    int pos=0;
/*    TextView m_title;
    TextView m_runningTime;
    TextView m_productYear;
    TextView summary;
    ImageView Img;
    ListView comment;*/

    private ArrayAdapter arrayAdapter;
    private String Allcomment,movietitle,delete;
    private String[] comment;
    private List data = new ArrayList<>();
    //private ArrayList<String> movietitle = new ArrayList<String>();
    Item(String LoginID, Context context, ArrayList<MovieVO> MV){
        this.loginID = LoginID;
        this.context = context;
        this.MV = MV;
    }

    @Override
    public int getCount() {
        return MV.size();
    }

    public String movieTitle(){
        return MV.get(pos).getTitle();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.recommend, container, false);



        TextView m_title = (TextView)layout.findViewById(R.id.m_title);
        TextView m_runningTime= (TextView)layout.findViewById(R.id.m_runningTime);
        TextView m_productYear = (TextView)layout.findViewById(R.id.m_year);
        TextView summary = (TextView)layout.findViewById(R.id.summary);
        ImageView Img = (ImageView)layout.findViewById(R.id.m_img);
        final ListView commentlistview = (ListView)layout.findViewById(R.id.comment);
        Button sendBtn=(Button)layout.findViewById(R.id.sendMsg);
        final EditText inputText = (EditText)layout.findViewById(R.id.inputMsg);

        final FavoriteDAO favoriteDAO = new FavoriteDAO();

        m_title.setText(MV.get(position).getTitle());
        m_runningTime.setText(MV.get(position).getRunningTime());
        m_productYear.setText(MV.get(position).getProductYear());
        summary.setText(MV.get(position).getSummary());
        summary.setMovementMethod(new ScrollingMovementMethod());

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(MV.get(position).getImgUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bm = BitmapFactory.decodeStream(input);
                } catch (Exception e) {
                }
            }
        };

        mThread.start();
        try{
            mThread.join();
            Img.setImageBitmap(bm);

        } catch (InterruptedException e){
        }

        Button btn = (Button)layout.findViewById(R.id.likeBTN);
        Button disbtn = (Button)layout.findViewById(R.id.dislikeBTN);
        btn.setOnClickListener(new View.OnClickListener(){
            String result = "";
            @Override
            public void onClick(View v) {
                try {
                    result = favoriteDAO.execute(loginID, MV.get(position).getTitle(), "1").get();
                }catch (Exception e){
                }
                if(result.equals("true")){
                    Toast.makeText(context, "좋아요", Toast.LENGTH_SHORT).show();
                    Log.d("수신완료", result);
                }
            }
        });
        disbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "싫어요", Toast.LENGTH_SHORT).show();
            }
        });
        arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,data);

        data.clear();
        movietitle = m_title.getText().toString();
        Log.d("영화제목", movietitle);
        addComment(movietitle);
        commentlistview.setAdapter(arrayAdapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCommentDAO scdao = new SendCommentDAO();
                String msg = inputText.getText().toString();
                String insertMsg =loginID+" : "+msg;

                commentlistview.setAdapter(arrayAdapter);
                arrayAdapter.add(insertMsg);

                Log.d("msgmsg",insertMsg+" "+movietitle);
                try {
                    result = scdao.execute(movietitle,loginID,msg).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                inputText.setText(null);
            }
        });
        commentlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id){
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                delete =  (String) data.get(position);
                alert.setTitle("삭제");
                alert.setMessage("댓글을 삭제하시겠습니까?");
                Log.d("선택된 댓글", delete);
                alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DeleteCommentDAO ddao = new DeleteCommentDAO();
                        try {
                            result = ddao.execute(delete,loginID).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        if(result.equals("true")){
                            data.remove(position);
                            Toast.makeText(context, "삭제됨", Toast.LENGTH_SHORT).show();
                            Log.d("포지션", String.valueOf(position));
                            arrayAdapter.notifyDataSetChanged();
                        }else if(result.equals("false")){
                            Toast.makeText(context, "삭제안됨", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
                return false;
            }
        });


        container.addView(layout, 0);
        this.pos = position;
        return layout;

    }
    private void addComment(String movietitle){
        CommentDAO cdao = new CommentDAO();

        try{
            Allcomment = cdao.execute(movietitle).get();
            Log.d("comment 불러오기",""+Allcomment);
        }catch (Exception e){
            e.printStackTrace();
        }
        comment = Allcomment.split("//");
        for(int i=0;i<comment.length;i++){
            data.add(comment[i]);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View)object;
    }

    private Bitmap getBitmap(String domain){
        HttpURLConnection connection = null;

        try {
            URL url = new URL(domain);
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bm = BitmapFactory.decodeStream(input);
            return bm;

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if(connection!=null)connection.disconnect();
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
