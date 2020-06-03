package com.example.userwithslim.domain;

import java.util.List;

public class UserListResponse
{
    private boolean error;
    private List<User> users;

    public UserListResponse(boolean error, List<User> users) {
        this.error = error;
        this.users = users;
    }

    public boolean isError() {
        return error;
    }

    public List<User> getUsers() {
        return users;
    }
}
