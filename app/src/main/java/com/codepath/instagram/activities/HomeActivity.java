package com.codepath.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.InstagramPostsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fetchPostsOnLine();
    }


    public void onClickLike(View view) {

    }

    public void onClickComment(View view) {

    }

    public void onClickShare(View view) {


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<InstagramPost>  fetchPosts() {

        try {

            JSONObject jsonObject = Utils.loadJsonFromAsset(this.getApplicationContext(), "popular.json");
            List<InstagramPost> instagramPostList = Utils.decodePostsFromJsonResponse(jsonObject);
            return instagramPostList;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new LinkedList<InstagramPost>();
    }

    private class InstagramResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            // Root JSON in response is an dictionary i.e { "data : [ ... ] }
            // Handle resulting parsed JSON response here
            RecyclerView rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
            InstagramPostsAdapter adapter = new InstagramPostsAdapter(Utils.decodePostsFromJsonResponse(response));
            rvPosts.setAdapter(adapter);
            // Set layout manager to position the items
            rvPosts.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            Toast.makeText(getApplicationContext(), getString(R.string.fetch_error), Toast.LENGTH_SHORT);
        }
    }

    private void fetchPostsOnLine() {
        InstagramClient.getPopularFeed(new InstagramResponseHandler());
    }
}
