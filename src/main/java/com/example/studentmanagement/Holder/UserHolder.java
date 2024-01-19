package com.example.studentmanagement.Holder;


import com.example.studentmanagement.Enitities.UserEnitity;

public final class UserHolder {
    private UserEnitity userEnitity;
    private static UserHolder INSTANCE;

    public static UserHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserHolder();
        };
        return INSTANCE;
    };

    public void setUser(UserEnitity u) {
        this.userEnitity = u;
    }

    public UserEnitity getUser() {
        return this.userEnitity;
    }
}
