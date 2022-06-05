package com.example.ezee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.myHolder>{
    List<String> data;
    String wsem;

    public SubjectAdapter(List<String> data,String wsem) {
        this.data = data;
        this.wsem = wsem;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_row_subject,parent,false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.SubjectName.setText(data.get(position).toString());
        holder.cardOfSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.cardOfSubject.getContext(),SubjectPdfActivity.class);
                intent.putExtra("subName",data.get(position).toString());
                intent.putExtra("wsem",wsem);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.cardOfSubject.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myHolder extends RecyclerView.ViewHolder
    {
        CardView cardOfSubject;
        TextView SubjectName;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            cardOfSubject = itemView.findViewById(R.id.cardOfSubject);
            SubjectName = itemView.findViewById(R.id.SubjectName);

        }
    }
}
