package com.example.pawsome2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {
    @Headers({
            "content-type: application/json",
            "x-api-key: da2396a3-2f31-4346-8d2c-97543f419603"
    })

    @POST("votes/")
    Call<UserResponse> saveUser(@Body UserRequest userRequest);

}

