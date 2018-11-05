package com.omegaauto.shurik.mobilesklad.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.omegaauto.shurik.mobilesklad.R;
import com.omegaauto.shurik.mobilesklad.container.NumberListLast;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;

public class NumberListAdapter extends BaseAdapter {

    private NumberListLast numberListLast;
    Context context;

    public NumberListAdapter(Context context) {
        this.context = context;
        numberListLast = NumberListLast.getInstance();
    }






//    @NonNull
//    @Override
//    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_number_container_item, parent, false);
//        NumberViewHolder numberViewHolder = new NumberViewHolder(v);
//        return numberViewHolder;
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder numberViewHolder, int position) {
//        ((NumberViewHolder) numberViewHolder).textViewNumber.setText(numberListLast.getNumber(position));
//    }

//    @Override
//    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }

    @Override
    public int getCount() {
        return numberListLast.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {


        //ContainerProperty containerProperty = properties.get(position);
        String number = numberListLast.getNumber(position);

        View row = view;
        NumberViewHolder viewHolder = new NumberViewHolder();

        if (row == null){
            // если элемент еще не отображался, то необходимо его создать

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            row = layoutInflater.inflate(R.layout.activity_number_container_item, viewGroup, false);

            viewHolder.textViewNumber = (TextView) row.findViewById(R.id.activity_number_container_item_text_view);

            row.setTag(viewHolder);

        } else {
            viewHolder = (NumberViewHolder) row.getTag();
        }

        //row.setOnLongClickListener(new PropertyOnLongClickListener());

        viewHolder.textViewNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, MobileSkladSettings.getInstance().getFontSize().getSizeValue());
        viewHolder.textViewNumber.setText(number);

        return row;

    }

    public static class NumberViewHolder {
        TextView textViewNumber;
//        public NumberViewHolder(View itemView) {
//            super(itemView);
//            textViewNumber = (TextView) itemView.findViewById(R.id.activity_number_container_item_text_view);
//        }
    }
}
