package com.example.userwithslim.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.userwithslim.R;
import com.example.userwithslim.activities.LoginActivity;
import com.example.userwithslim.activities.ProfileActivity;
import com.example.userwithslim.activities.RegisterActivity;
import com.example.userwithslim.apiplaceholder.RetrofitClient;
import com.example.userwithslim.domain.DefaultResponse;
import com.example.userwithslim.domain.LoginResponse;
import com.example.userwithslim.domain.User;
import com.example.userwithslim.storage.SharedPreferenceManager;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements View.OnClickListener{

    private EditText editTextName,editTextEmail,editTextSchool;
    private EditText editTextPassword,editTextNewPassword;

    private LinearLayout ll_delete,ll_logout;

    private Button btn_update,btn_update_pass;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextEmail= view.findViewById(R.id.editTextEmail);
        editTextSchool=view.findViewById(R.id.editTextSchool);
        editTextName=view.findViewById(R.id.editTextName);
        editTextPassword=view.findViewById(R.id.editTextPassword);
        editTextNewPassword=view.findViewById(R.id.editTextNewPassword);

        ll_delete=view.findViewById(R.id.ll_delete);
        ll_delete.setOnClickListener(this);
        ll_logout=view.findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(this);

        btn_update=view.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
        btn_update_pass=view.findViewById(R.id.btn_update_pass);
        btn_update_pass.setOnClickListener(this);

    }

    private void updateProfile()
    {
        String name=editTextName.getText().toString();
        String school=editTextSchool.getText().toString();
        String email=editTextEmail.getText().toString();


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

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please Enter valid Email address");
            editTextEmail.requestFocus();
            return;
        }


        /**
         * Do user updation with the API call
         */
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email",email);
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("school",school);

        User user= SharedPreferenceManager.getInstance(getActivity()).getUser();

        Call<LoginResponse> call= RetrofitClient.getInstance().getApi().updateUser(user.getId(),jsonObject);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (!response.body().isError())
                {
                    SharedPreferenceManager.getInstance(getActivity()).saveUser(response.body().getUser());
                }
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v==ll_delete)
        {
            deleteUser();
        }
        else if (v==ll_logout)
        {
            logout();
        }
        else if (v==btn_update)
        {
            updateProfile();
        }
        else if (v==btn_update_pass)
        {
            updatePassword();
        }
    }

    private void deleteUser()
    {


        AlertDialog.Builder aBuilder=new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure ?")
                .setMessage("This action is irreversible")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User user=SharedPreferenceManager.getInstance(getActivity()).getUser();
                       Call<DefaultResponse> call= RetrofitClient.getInstance().getApi().deleteAccount(user.getId());
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                if (!response.body().isError())
                                {
                                    SharedPreferenceManager.getInstance(getActivity()).clear();
                                    Intent intent=new Intent(getActivity(), RegisterActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog=aBuilder.create();
        alertDialog.show();
    }

    /**
     *
     */
    private void logout() {
        SharedPreferenceManager.getInstance(getActivity()).clear();
        Intent intent=new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     *
     */
    private void updatePassword()
    {

        String password=editTextPassword.getText().toString().trim();
        String newPassword=editTextNewPassword.getText().toString().trim();

        if (password.isEmpty())
        {
            editTextPassword.setError("Password is required !");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 5)
        {
            editTextPassword.setError("Password should be greater than 5 characters");
            editTextPassword.requestFocus();
            return;
        }
        if (newPassword.isEmpty())
        {
            editTextPassword.setError("Password is required !");
            editTextPassword.requestFocus();
            return;
        }
        if (newPassword.length() < 5)
        {
            editTextNewPassword.setError("Password should be greater than 5 characters");
            editTextNewPassword.requestFocus();
            return;
        }

        User user=SharedPreferenceManager.getInstance(getActivity()).getUser();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("currentpassword",password);
        jsonObject.addProperty("newpassword",newPassword);
        jsonObject.addProperty("email",user.getEmail());

        Call<DefaultResponse> call=RetrofitClient.getInstance().getApi().updatePassword(user.getId(),jsonObject);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                  Toast.makeText(getActivity() , response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
