package com.codepath.instagram.activities;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.InstagramCommentsAdapter;
import com.codepath.instagram.helpers.InstagramPostsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        String mediaId = getIntent().getStringExtra("media-id");
        fetchComments(mediaId);
        setTitle("COMMENTS");

    }

    private class CommentsResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            // Root JSON in response is an dictionary i.e { "data : [ ... ] }
            // Handle resulting parsed JSON response here
            RecyclerView rvComments = (RecyclerView) findViewById(R.id.rvComments);
            InstagramCommentsAdapter adapter = new InstagramCommentsAdapter(Utils.decodeCommentsFromJsonResponse(response));
            rvComments.setAdapter(adapter);
            // Set layout manager to position the items
            rvComments.setLayoutManager(new LinearLayoutManager(CommentsActivity.this));

        }
    }

    private void fetchComments(String mediaId) {
        InstagramClient.getComments(mediaId, new CommentsResponseHandler());

    }
}
