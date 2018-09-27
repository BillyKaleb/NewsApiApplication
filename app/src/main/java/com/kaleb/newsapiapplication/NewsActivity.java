package com.kaleb.newsapiapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    ArrayList<NewsObject> arrayNews = new ArrayList<NewsObject>();
    private NewsRecycleAdapter newsRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        String ID = intent.getStringExtra("ID");

        jsonParse(ID);
        recycleInflate();
    }

    //Json Parser, not in Async because search button reason
    private ArrayList<NewsObject> jsonParse(String ID) {
        String url = "https://newsapi.org/v2/everything?sources=" + ID + "&apiKey=c459cc563c9746dc9c483f73079e7693";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("articles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject news = jsonArray.getJSONObject(i);

                        String title = news.getString("title");
                        String url = news.getString("url");

                        arrayNews.add(new NewsObject(title, url));
                        //textView.append(name + ", " + title + ", " + url + "\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("TAG", Integer.toString(arrayNews.size()));
                newsRecycleAdapter.updateSearchedList();
                newsRecycleAdapter.notifyDataSetChanged();
                /*if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);
        return arrayNews;
    }

    //RecyclerView Inflater
    private void recycleInflate() {
        RecyclerView recyclerView = findViewById(R.id.news_recycle_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        newsRecycleAdapter = new NewsRecycleAdapter(arrayNews, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsRecycleAdapter);
    }

    //Controlling text change on search function
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newsRecycleAdapter.getFilter().filter(newText);
                newsRecycleAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }
}
