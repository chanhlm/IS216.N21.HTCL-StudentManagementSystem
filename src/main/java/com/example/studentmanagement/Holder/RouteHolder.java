package com.example.studentmanagement.Holder;


import com.example.studentmanagement.Enitities.UserEnitity;

public final class RouteHolder {
    private String route = "";
    private static RouteHolder INSTANCE;

    public static RouteHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RouteHolder();
        };
        return INSTANCE;
    };

    public void setRoute(String u) {
        this.route = u;
    }

    public String getRoute() {
        return this.route;
    }
}
