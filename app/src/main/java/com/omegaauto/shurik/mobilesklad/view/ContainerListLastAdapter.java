package com.omegaauto.shurik.mobilesklad.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omegaauto.shurik.mobilesklad.R;
import com.omegaauto.shurik.mobilesklad.container.ContainerListLast;



/////////////////////////////////////////////////////////////

//НЕ ИСПОЛЬЗОВАТЬ !!!

////////////////////////////////////////////////////////////


public class ContainerListLastAdapter extends RecyclerView.Adapter {

    private ContainerListLast containerListLast;

    public ContainerListLastAdapter() {
        containerListLast = ContainerListLast.getInstance();
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_number_container_item, parent, false);
        NumberViewHolder numberViewHolder = new NumberViewHolder(v);
        return numberViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder numberViewHolder, int position) {
        ((NumberViewHolder) numberViewHolder).textViewNumber.setText(containerListLast.getContainer(position+1).getNumber());
    }

//    public void onBindViewHolder(@NonNull NumberViewHolder numberViewHolder, int position) {
//        numberViewHolder.textViewNumber.setText(containerListLast.getContainer(position+1).getNumber());
//    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return containerListLast.size();
    }

    private class NumberViewHolder extends RecyclerView.ViewHolder{
        TextView textViewNumber;
        public NumberViewHolder(View itemView) {
            super(itemView);
            textViewNumber = (TextView) itemView.findViewById(R.id.activity_number_container_item_text_view);
        }
    }
}
