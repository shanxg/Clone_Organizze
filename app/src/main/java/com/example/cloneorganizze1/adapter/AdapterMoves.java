package com.example.cloneorganizze1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloneorganizze1.R;
import com.example.cloneorganizze1.model.Transaction;

import java.util.List;

public class AdapterMoves extends RecyclerView.Adapter<AdapterMoves.MyViewHolder> {

    List<Transaction> moves;
    Context context;

    public AdapterMoves(List<Transaction> moves, Context context) {
        this.moves = moves;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView title, value, category, date;

         MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textRV_title);
            value = itemView.findViewById(R.id.textRV_value);
            category = itemView.findViewById(R.id.textRV_category);
            date = itemView.findViewById(R.id.textRV_date);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_moves, parent, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Transaction transaction = moves.get(position);

        holder.title.setText(transaction.getCategory());

        holder.category.setText(transaction.getDescription());
        holder.date.setText(transaction.getDate());

        if(transaction.getType() == "d" || transaction.getType().equals("d")){

            holder.value.setTextColor(context.getResources().getColor(R.color.colorDepesaDark));
            holder.value.setText("- R$ "+transaction.getTransactionValue());

        }else {

            holder.value.setTextColor(context.getResources().getColor(R.color.colorReceitaDark));
            holder.value.setText("+ R$ "+transaction.getTransactionValue());
        }
    }

    @Override
    public int getItemCount() {
        return moves.size();
    }
}
