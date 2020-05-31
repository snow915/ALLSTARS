package com.store;

import java.util.ArrayList;

public class FilterApp {

    public String filterName;
    public ArrayList<String> filterTypes = new ArrayList<>();

    public FilterApp(String filterName) {
        this.filterName = filterName;
    }
}
