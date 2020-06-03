package com.example.userwithslim.apiplaceholder;

import com.example.userwithslim.domain.DefaultResponse;
import com.example.userwithslim.domain.LoginResponse;
import com.example.userwithslim.domain.UserListResponse;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface
{
    @POST("createuser")
    Call<DefaultResponse> createUser(@Body JsonObject jsonObject);

    @POST("userlogin")
    Call<LoginResponse> userLogin(@Body JsonObject jsonObject);

    @GET("allusers")
    Call<UserListResponse> getAllUsers();

    @PUT("updateuser/{id}")
    Call<LoginResponse> updateUser(@Path("id") int id, @Body JsonObject jsonObject);

    @PUT("updatepassword/{id}")
    Call<DefaultResponse> updatePassword(@Path("id") int id,@Body JsonObject jsonObject);

    @DELETE("deleteuser/{id}")
    Call<DefaultResponse> deleteAccount(@Path("id") int id);
}
