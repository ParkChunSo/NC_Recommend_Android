package com.example.chunso.nc_recommend.Controller;

import android.util.Log;

import com.example.chunso.nc_recommend.DAO.MovieDAO;
import com.example.chunso.nc_recommend.VO.CosSimVO;
import com.example.chunso.nc_recommend.VO.MovieVO;

import java.util.ArrayList;

public class Movie {
    ArrayList<MovieVO> MV;
    ArrayList<MovieVO> resultMV;

    public ArrayList<MovieVO> findMovieWithCF(CosSimVO user, CosSimVO sim_user){
        String result = getData();
        MV= parseResultData(result);
        resultMV = getMovieList(user.getFavor(), sim_user.getFavor(), MV);
        for(int i=0 ; i<resultMV.size() ; i++) {
            Log.d("1",resultMV.get(i).getTitle());
            Log.d("2",resultMV.get(i).getImgUrl());
            Log.d("3",resultMV.get(i).getSummary());
            Log.d("4",String.valueOf(resultMV.get(i).getProductYear()));
            Log.d("5",String.valueOf(resultMV.get(i).getRunningTime()));
        }
        return resultMV;
    }

    private String getData(){
        MovieDAO movieDAO = new MovieDAO();
        String result = new String();
        try {
            result = movieDAO.execute().get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<MovieVO> parseResultData(String result){
        String[] firstParse;
        String[] secondParse;
        ArrayList<MovieVO> MV = new ArrayList<>();

        firstParse = result.split("@");

        for(int i=0 ; i<firstParse.length ; i++){
            MovieVO _MV = new MovieVO();
            secondParse = firstParse[i].split("&");
            Log.d("second",secondParse[1]);

            _MV.setTitle(secondParse[0]);
            _MV.setImgUrl(secondParse[1]);
            _MV.setRunningTime(secondParse[2]);
            _MV.setProductYear(secondParse[3]);
            _MV.setSummary(secondParse[4]);

            MV.add(_MV);
        }

        for(int i=0 ; i<MV.size() ; i++) {
            Log.d("1",MV.get(i).getImgUrl());
            Log.d("2",MV.get(i).getSummary());
            Log.d("3",MV.get(i).getTitle());
            Log.d("4",String.valueOf(MV.get(i).getProductYear()));
            Log.d("5",String.valueOf(MV.get(i).getRunningTime()));
        }

        return MV;
    }

    private ArrayList<MovieVO> getMovieList(int[] userFavor, int[] sim_userFavor, ArrayList<MovieVO> MV){
        ArrayList<MovieVO> _MV = new ArrayList<>();

        for(int i=0 ; i<userFavor.length ; i++){
            if(userFavor[i] < sim_userFavor[i])
                _MV.add(MV.get(i));
        }

        return _MV;
    }
}
