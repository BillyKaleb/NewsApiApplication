package com.kaleb.newsapiapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SourcesRecycleAdapter extends RecyclerView.Adapter<SourcesRecycleAdapter.ViewHolder> {

    private ArrayList<SourcesObject> sourceNameList = new ArrayList<>();
    private Context context;

    public SourcesRecycleAdapter(ArrayList<SourcesObject> sourceNameList, Context context) {
        this.sourceNameList = sourceNameList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sources_recycle, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.sourceName.setText(sourceNameList.get(position).getName());

        holder.sourcesRecycleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String extra = sourceNameList.get(position).getId();
                Context context = view.getContext();
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("ID", extra); //Optional parameters
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sourceNameList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView sourceName;
        RelativeLayout sourcesRecycleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            sourceName = itemView.findViewById(R.id.source_name);
            sourcesRecycleLayout = itemView.findViewById(R.id.sources_recycle_layout);
        }
    }
}
