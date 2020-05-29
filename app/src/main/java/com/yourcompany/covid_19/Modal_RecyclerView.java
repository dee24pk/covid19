package com.yourcompany.covid_19;

import java.util.Comparator;

public class Modal_RecyclerView {
    private String Country;
    private String Total_confirmed;
    private String Total_death;
    private String Total_recovered;
    private String New_confirmed;

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getTotal_confirmed() {
        return Total_confirmed;
    }

    public void setTotal_confirmed(String total_confirmed) {
        Total_confirmed = total_confirmed;
    }

    public String getTotal_death() {
        return Total_death;
    }

    public void setTotal_death(String total_death) {
        Total_death = total_death;
    }

    public String getTotal_recovered() {
        return Total_recovered;
    }

    public void setTotal_recovered(String total_recovered) {
        Total_recovered = total_recovered;
    }

    public String getNew_confirmed() {
        return New_confirmed;
    }

    public void setNew_confirmed(String new_confirmed) {
        New_confirmed = new_confirmed;
    }

    public String getNew_death() {
        return New_death;
    }

    public void setNew_death(String new_death) {
        New_death = new_death;
    }

    public String getNew_recovered() {
        return New_recovered;
    }

    public void setNew_recovered(String new_recovered) {
        New_recovered = new_recovered;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    private String New_death;
    private String New_recovered;
    private String Time;

    public static final Comparator<Modal_RecyclerView> BY_TOTAL_CASE = new Comparator<Modal_RecyclerView>() {
        @Override
        public int compare(Modal_RecyclerView o1, Modal_RecyclerView o2) {
            return o1.getTotal_confirmed().compareTo(o2.getTotal_confirmed());
        }
    };
}
