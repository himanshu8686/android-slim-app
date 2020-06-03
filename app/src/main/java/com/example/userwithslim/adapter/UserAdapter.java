package com.example.userwithslim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userwithslim.R;
import com.example.userwithslim.domain.User;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>
{

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList)
    {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user_item,parent,false);
        UserViewHolder orderViewHolder=new UserViewHolder(view);
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, final int position) {
//        holder.tvComplete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity)context).GetCompleteClick(position);
//            }
//        });

        User user=userList.get(position);
        holder.tv_name.setText(user.getName());
        holder.tv_email.setText(user.getEmail());
        holder.tv_school.setText(user.getSchool());

}

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        //TextView tvComplete;
        TextView tv_name,tv_email,tv_school;
        public UserViewHolder(View itemView) {
            super(itemView);
            //  tvComplete = itemView.findViewById(R.id.tvComplete);
            tv_name= itemView.findViewById(R.id.tv_name);
            tv_email=itemView.findViewById(R.id.tv_email);
            tv_school=itemView.findViewById(R.id.tv_school);
        }
    }
}
