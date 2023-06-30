package com.example.new_application;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView facilitiesRecyclerView;
    private FacilityAdapter facilityAdapter;
    private List<Facility> facilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        facilitiesRecyclerView = findViewById(R.id.facilitiesRecyclerView);
        facilitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchFacilitiesFromAPI();
    }

    private void fetchFacilitiesFromAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/iranjith4/ad-assignment/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<FacilitiesResponse> call = apiInterface.getFacilities();

        call.enqueue(new Callback<FacilitiesResponse>() {
            @Override
            public void onResponse(Call<FacilitiesResponse> call, Response<FacilitiesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FacilitiesResponse facilitiesResponse = response.body();
                    facilities = facilitiesResponse.getFacilities();

                    facilityAdapter = new FacilityAdapter(facilities);
                    facilitiesRecyclerView.setAdapter(facilityAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch facilities", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FacilitiesResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch facilities: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
