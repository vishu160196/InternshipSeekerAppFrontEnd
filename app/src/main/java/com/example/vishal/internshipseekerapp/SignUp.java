package com.example.vishal.internshipseekerapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by vishal on 16/7/17.
 */

public interface SignUp {
    @POST("/")
    Call<SignupForm> createNewUser(
      @Body SignupForm form
    );
}
