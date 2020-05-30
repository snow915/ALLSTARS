package com.store.famous;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.store.R;
import com.store.index;

public class RequestsAccepted extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;

    public static RequestsAccepted newInstance(String param1, String param2) {
        RequestsAccepted fragment = new RequestsAccepted();
        Bundle args = new Bundle();
        return fragment;
    }
    public RequestsAccepted() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_requests_accepted, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return v;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Fragment myFragment = new FragmentSolicitud();
                    Bundle b = new Bundle();
                    b.putString("typeRequest", "accepted");
                    myFragment.setArguments(b);
                    return myFragment;
                case 1:
                    Fragment myFragment2 = new FragmentSolicitud();
                    Bundle b2 = new Bundle();
                    b2.putString("typeRequest", "payed");
                    myFragment2.setArguments(b2);
                    return myFragment2;
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    String recent_news = "En proceso";
                    return recent_news;
                case 1:
                    String category = "Pagadas";
                    return category;
            }
            return null;
        }
    }
}
