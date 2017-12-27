package com.dvb.httprequest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManger;

    private ArrayList<Review> myReviews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ReviewAdapter(myReviews);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManger);
        mRecyclerView.setAdapter(mAdapter);


        String apiKey = "?api_key=957c988676c0d274a6d1cc76dd5c8a93";
        String siteUrl = "https://api.themoviedb.org/3/movie/";
        String ID = "238";
        String VIDEOS = "/reviews";

        final String URL = siteUrl + ID + VIDEOS + apiKey;

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(URL)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e("MainActivity", "Failed to fetch data.", e);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        if (response.isSuccessful()) {
                            getData(jsonData);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }

            });
        } else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }
    }


    private void getData(String jsonData) throws JSONException {
        JSONObject data = new JSONObject(jsonData);
        JSONArray reviews = data.getJSONArray("results");

        for (int i = 0; i < reviews.length(); i++){
            JSONObject review = reviews.getJSONObject(i);

            myReviews.add(new Review(
                    review.getString("author").toString(),
                    review.getString("content").toString()
            ));
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }


    private void alertUserAboutError() {
        AlertDialogueFragment dialog = new AlertDialogueFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}


