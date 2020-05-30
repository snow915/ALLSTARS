package com.store;

import java.util.ArrayList;

public class Filter {

    public String filterName;
    public ArrayList<String> filterTypes = new ArrayList<>();

    public Filter(String filterName) {
        this.filterName = filterName;
    }
}
