package com.example.userwithslim.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userwithslim.R;
import com.example.userwithslim.apiplaceholder.RetrofitClient;
import com.example.userwithslim.domain.LoginResponse;
import com.example.userwithslim.storage.SharedPreferenceManager;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail,editTextPassword;
    private TextView tv_login_hint;
    private Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        tv_login_hint=findViewById(R.id.tv_login_hint);
        btn_login=findViewById(R.id.btn_login);
        tv_login_hint.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==tv_login_hint)
        {
            Intent intent=new Intent(this,RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (v==btn_login)
        {
            userLogin();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPreferenceManager.getInstance(this).isLoggedIn())
        {
            Intent intent=new Intent(this,ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void userLogin()
    {
        String email=editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (email.isEmpty())
        {
            editTextEmail.setError("Email is required !");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty())
        {
            editTextPassword.setError("Password is required !");
            editTextPassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please Enter valid Email address");
            editTextEmail.requestFocus();
            return;
        }

        if (password.length() < 5)
        {
            editTextPassword.setError("Password should be greater than 5 characters");
            editTextPassword.requestFocus();
            return;
        }

        /**
         * user login with API call
         */

        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("email",email);
        jsonObject.addProperty("password",password);

         Call<LoginResponse> call=RetrofitClient.getInstance().getApi().userLogin(jsonObject);
         call.enqueue(new Callback<LoginResponse>() {
             @Override
             public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
             {
                 LoginResponse loginResponse=response.body();
                 if (response.code()==200)
                    {
                        //Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        SharedPreferenceManager.getInstance(LoginActivity.this)
                                .saveUser(loginResponse.getUser());
                        Intent intent=new Intent(LoginActivity.this,ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        try {
                            String str=null;
                            str=response.errorBody().string();
                            JSONObject json=new JSONObject(str);
                            Toast.makeText(LoginActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
             }

             @Override
             public void onFailure(Call<LoginResponse> call, Throwable t) {
                 Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });
    }
}
