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
import com.example.userwithslim.domain.DefaultResponse;
import com.example.userwithslim.storage.SharedPreferenceManager;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextName, editTextSchool,editTextEmail,editTextPassword;
    private TextView tv_register_hint;
    private Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        editTextName=findViewById(R.id.editTextName);
        editTextSchool =findViewById(R.id.editTextSchool);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        tv_register_hint=findViewById(R.id.tv_register_hint);
        tv_register_hint.setOnClickListener(this);
        btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v==tv_register_hint)
        {
            Intent intent=new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (v==btn_register)
        {
            userSignUp();
        }
    }

    private void userSignUp()
    {
        String name=editTextName.getText().toString();
        String school=editTextSchool.getText().toString();
        String email=editTextEmail.getText().toString();
        String password=editTextPassword.getText().toString();

        if (name.isEmpty())
        {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }

        if (school.isEmpty())
        {
            editTextSchool.setError("mobile no should be of 10 digits");
            editTextSchool.requestFocus();
            return;
        }
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
         * Do user registration with the API call
         */
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email",email);
        jsonObject.addProperty("password",password);
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("school",school);

        Call<DefaultResponse> call= RetrofitClient.getInstance().getApi().createUser(jsonObject);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.code()==201)
                {
                    DefaultResponse defaultResponse=response.body();
                    Toast.makeText(RegisterActivity.this, defaultResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        String str=null;
                        str=response.errorBody().string();
                        JSONObject json=new JSONObject(str);
                        Toast.makeText(RegisterActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//               String str=null;
//                try{
//                    if (response.code() == 201) {
//                           str = response.body().string();
//                    } else{
//                       str= response.errorBody().string();
//                    }
//                 }catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if (str!=null)
//                {
//                    try {
//                        JSONObject json=new JSONObject(str);
//                        Toast.makeText(RegisterActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
    }
}
