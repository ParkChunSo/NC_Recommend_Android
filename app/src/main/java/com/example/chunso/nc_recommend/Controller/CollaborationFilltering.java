package com.example.chunso.nc_recommend.Controller;

import android.util.Log;

import com.example.chunso.nc_recommend.VO.CosSimVO;
import com.example.chunso.nc_recommend.DAO.CFDAO;
import com.example.chunso.nc_recommend.VO.MovieVO;

import java.util.ArrayList;

public class CollaborationFilltering {
    //This class is Collaboration Filtering Algorithm
    private String id;
    private CosSimVO sim_CS;
    private ArrayList<CosSimVO> CS;
    private ArrayList<MovieVO> MV;

    //현재 사용자의 id를 받아온다.
    public CollaborationFilltering(String id){
        this.id = id;
    }

    //user_Based CF Control 함수
    public ArrayList<MovieVO> excuteCF(){
        int index;
        String result = getData();
        sim_CS = new CosSimVO();
        CS = parseResultData(result);

        Log.d("CS.size()) = ", String.valueOf(CS.size()));
        for(int i = 0 ; i<CS.size() ; i++){
            for(int j = 0 ; j<CS.get(i).getFavor().length ;j++)
                Log.d("CS.getFavor("+String.valueOf(i)+") = ", String.valueOf(CS.get(i).getFavor()[j]));
        }

        //현재 사용자dml Arraylist index 추출
        if((index = lookUpUser(CS)) < 0){
            Log.d("Lookup함수 실패", "");
            return null;
        }

        CosSimVO n_CS = CS.get(index);
        for(int i = 0 ; i< CS.size() ; i++) {
            CS.get(i).setRate(cosSim(n_CS, CS.get(i))); //코사인 유사도를 사용하여 유사도값 구함
            Log.d("각 유사도값", String.valueOf(CS.get(i).getRate()));
        }
        sim_CS = findUserSimilarity(CS);//코사인 유사도값을 활용하여 가장 유사한 사용자 추출

        Movie movie = new Movie();
        MV = movie.findMovieWithCF(n_CS,sim_CS); // 현재 사용자와 유사도값이 가장 높은 사용자를 인자값으로 넣어 영화정보를 MovieVO ArrayList에 넣는다.

        return MV;
    }

    //Cosine Similarity Algorithm
    private double cosSim(CosSimVO n_CS, CosSimVO CS){
        double up = 0d;
        double bottom = 0d;
        double bottom_a = 0d;
        double bottom_b = 0d;

        for(int i=0; i<CS.getFavor().length ; i++){
            double aValue = (double)n_CS.getFavor()[i];
            double bValue = (double)CS.getFavor()[i];

            if(aValue != 0 && bValue != 0)
                up += (aValue*bValue);

            if(aValue != 0)
                bottom_a += Math.pow(aValue, 2);

            if(bValue != 0)
                bottom_b += Math.pow(bValue, 2);
        }

        bottom = Math.sqrt(bottom_a) * Math.sqrt(bottom_b);

        return up/bottom;
    }

    //DataBase에서 필요한 데이터를 불러오는 함수
    private String getData() {
        String result = "아직 없음";
        CFDAO cfDAO = new CFDAO();
        try {
            result = cfDAO.execute(result).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //현재 id를 사용하는 user를 찾는 함수
    private int lookUpUser(ArrayList<CosSimVO> CS){
        CosSimVO _CS = new CosSimVO();
        _CS.setU_id(id);

        for(int i=0 ; i<CS.size() ; i++) {
            if (CS.get(i).getU_id().equals(_CS.getU_id())) {
                Log.d("uid",CS.get(i).getU_id());
                return i;
            }
        }
        return -1;
    }

    //가장 유사도가 높은 user탐색
    private CosSimVO findUserSimilarity(ArrayList<CosSimVO> CS){
        CosSimVO _CS = new CosSimVO();
        _CS = CS.get(0);
        for(int i=1 ; i<CS.size() ; i++){
            if(CS.get(i).getRate() < 1 && CS.get(i).getRate() > _CS.getRate())
                _CS = CS.get(i);
        }
        return _CS;
    }

    //웹서버에서 response해준 결과값을 원하는방식으로 splite하는 함수
    private ArrayList<CosSimVO> parseResultData(String result) {
        ArrayList<CosSimVO> CS = new ArrayList<>();
        String[] firstParse;
        String[] secondParse;
        ArrayList<String> thirdParse = new ArrayList<>();
        int[] trans;

        firstParse = result.split("!");
        secondParse = firstParse[1].split("//");

        for(int i = 0; i< secondParse.length ; i++){ //get U_id
            CosSimVO _CS = new CosSimVO();
            firstParse = secondParse[i].split("&");
            _CS.setU_id(firstParse[0]);
            thirdParse.add(firstParse[1]);
            CS.add(_CS);
            Log.d("CS.getUID("+String.valueOf(i)+") = ", CS.get(i).getU_id());
        }

        for(int i =0 ;i<thirdParse.size() ; i++){
            CosSimVO _CS = new CosSimVO();
            trans = new int[thirdParse.get(0).length()];
            firstParse =thirdParse.get(i).split(" ");

            for(int j = 0 ; j<firstParse.length ; j++)
                trans[j] = Integer.parseInt(firstParse[j]);

            CS.get(i).setFavor(trans);
            Log.d("CS.getFavor("+String.valueOf(i)+") = ", String.valueOf(CS.get(i).getFavor()));
        }
        return CS;
    }

}
