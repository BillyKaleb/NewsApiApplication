package com.kaleb.newsapiapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

public class SourcesActivity extends AppCompatActivity {

    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);
        RecyclerView recyclerView = findViewById(R.id.sources_recycle_view);

        progressBar = findViewById(R.id.sources_progress_bar);
        progressBar.setProgress(0);

        new SourcesAsync(this, recyclerView, progressBar).execute();
    }
}
