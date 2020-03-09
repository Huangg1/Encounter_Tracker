package com.example.offlineencountertracker.RV;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.offlineencountertracker.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<RVItem> listItems;
    ItemClicked activity;

    public interface ItemClicked{
        void onItemClicked(int index);
    }

    public MyAdapter(Context context, List<RVItem> list){
        listItems = list;
        activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvDescrip;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDescrip = itemView.findViewById(R.id.tvDescrip);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onItemClicked(listItems.indexOf(view.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(listItems.get(position));

        holder.tvName.setText(listItems.get(position).getTitle());
        holder.tvDescrip.setText(listItems.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
