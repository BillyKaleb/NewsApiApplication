package com.kaleb.newsapiapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsRecycleAdapter extends RecyclerView.Adapter<NewsRecycleAdapter.NewsViewHolder> implements Filterable {

    private ArrayList<NewsObject> newsNameList;
    private ArrayList<NewsObject> newsNameListFull;
    ;
    private Context context;

    public NewsRecycleAdapter(ArrayList<NewsObject> newsName, Context context) {
        this.context = context;
        this.newsNameList = newsName;
        this.newsNameListFull = new ArrayList<>(newsNameList);
    }

    //To update ArrayList, because API and JSON reason, file usually empty the first time it's called
    public void updateSearchedList() {
        this.newsNameListFull.addAll(newsNameList);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycle, parent, false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {
        holder.newsName.setText(newsNameList.get(position).getTitle());

        holder.newsRecycleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String extra = newsNameList.get(position).getUrl();
                Context context = view.getContext();
                Intent intent = new Intent(context, NewsWebview.class);
                intent.putExtra("url", extra); //Optional parameters
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return newsNameList.size();
    }

    @Override
    public Filter getFilter() {
        return newsFilter;
    }

    private Filter newsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<NewsObject> filteredNews = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredNews.addAll(newsNameListFull);
            } else {
                String filterChar = charSequence.toString().toLowerCase().trim();

                for (NewsObject item : newsNameListFull) {
                    if (item.getTitle().toLowerCase().contains(filterChar)) {
                        filteredNews.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredNews;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            newsNameList.clear();
            newsNameList.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView newsName;
        RelativeLayout newsRecycleLayout;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsName = itemView.findViewById(R.id.news_name);
            newsRecycleLayout = itemView.findViewById(R.id.news_recycle_layout);

        }
    }
}
