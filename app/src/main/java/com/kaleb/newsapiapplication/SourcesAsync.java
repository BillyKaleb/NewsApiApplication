package com.kaleb.newsapiapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class SourcesAsync extends AsyncTask<Void, Void, ArrayList<SourcesObject>> {
    Context context;
    private RecyclerView recyclerView;
    private SourcesRecycleAdapter recyclerViewAdapter;
    TextView textView;
    Button button;
    ArrayList<SourcesObject> arraySource = new ArrayList<SourcesObject>();
    MainActivity activity;
    ProgressBar bar;

    public SourcesAsync(Context context, RecyclerView recyclerView, ProgressBar bar) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.bar = bar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<SourcesObject> sourcesObjects) {
        super.onPostExecute(sourcesObjects);
        recyclerViewAdapter = new SourcesRecycleAdapter(arraySource, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected ArrayList<SourcesObject> doInBackground(Void... voids) {
        return this.jsonParse();
    }

    private ArrayList<SourcesObject> jsonParse() {
        String url = "https://newsapi.org/v2/sources?language=en&apiKey=c459cc563c9746dc9c483f73079e7693";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("sources");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject sources = jsonArray.getJSONObject(i);

                        String id = sources.getString("id");
                        String name = sources.getString("name");
                        String description = sources.getString("description");

                        arraySource.add(new SourcesObject(id, name));
                        Log.d("TAG", name);
                        //textView.append(name + ", " + title + ", " + url + "\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("TAG", Integer.toString(arraySource.size()));
                recyclerViewAdapter.notifyDataSetChanged();
                if (bar != null) {
                    bar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);

        return arraySource;
    }
}
