package com.example.new_application;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("db")
    Call<FacilitiesResponse> getFacilities();
}


