package com.yourcompany.covid_19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_recylerview extends RecyclerView.Adapter<Adapter_recylerview.MyViewHolder_recyclerview> {
    private Context mcontext_recylerview;
    private List<Modal_RecyclerView> mData_recylerview;


    public Adapter_recylerview(Context mcontext_manage, List<Modal_RecyclerView> mData_manage) {
        this.mcontext_recylerview = mcontext_manage;
        this.mData_recylerview = mData_manage;
    }
    public static class MyViewHolder_recyclerview extends RecyclerView.ViewHolder {

        TextView total_confirmed, total_death, total_recovered, new_confirmed, new_death, new_recovered, country;

        LinearLayout view_container_manage;


        public MyViewHolder_recyclerview(@NonNull final View itemView) {
            super(itemView);



             total_confirmed = (TextView) itemView.findViewById(R.id.total_confirmed1);
            total_death = (TextView) itemView.findViewById(R.id.total_death1);
            total_recovered = (TextView) itemView.findViewById(R.id.total_recovered1);
            new_confirmed = (TextView) itemView.findViewById(R.id.new_confirm1);
            new_death = (TextView) itemView.findViewById(R.id.new_death1);
            new_recovered = (TextView) itemView.findViewById(R.id.new_recovered1);
            country = (TextView) itemView.findViewById(R.id.country);
//


        }
    }
    @NonNull
    @Override
    public MyViewHolder_recyclerview onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater minflator = LayoutInflater.from(mcontext_recylerview);
        view = minflator.inflate(R.layout.item_recyclerview, viewGroup, false);
        return new MyViewHolder_recyclerview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder_recyclerview myViewHolder_recyclerview, final int position) {

        myViewHolder_recyclerview.total_confirmed.setText(mData_recylerview.get(position).getTotal_confirmed());
        myViewHolder_recyclerview.total_death.setText(mData_recylerview.get(position).getTotal_death());
        myViewHolder_recyclerview.total_recovered.setText(mData_recylerview.get(position).getTotal_recovered());
        myViewHolder_recyclerview.new_confirmed.setText(mData_recylerview.get(position).getNew_confirmed());
        myViewHolder_recyclerview.new_death.setText(mData_recylerview.get(position).getNew_death());
        myViewHolder_recyclerview.new_recovered.setText(mData_recylerview.get(position).getNew_recovered());
        myViewHolder_recyclerview.country.setText(mData_recylerview.get(position).getCountry());



    }

    @Override
    public int getItemCount() {
        return mData_recylerview.size();
    }

}
