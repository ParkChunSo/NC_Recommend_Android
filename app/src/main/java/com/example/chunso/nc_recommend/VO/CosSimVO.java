package com.example.chunso.nc_recommend.VO;

public class CosSimVO {
    private String u_id;
    private double rate;
    private int[] favor;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int[] getFavor() {
        return favor;
    }

    public void setFavor(int[] favor) {
        this.favor = favor;
    }
}
