package com.example.yena.donotlate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddDataActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);

            Fragment fragment = null;
            Bundle args = null;
            switch(position) {
                case 0:
                    fragment = new CurrentListFragment();
                    args = new Bundle();
                    break;
                case 1:
                    fragment = new CompletedListFragment();
                    args = new Bundle();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.current_tab);
                case 1:
                    return getString(R.string.completed_tab);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public static class CurrentListFragment extends Fragment {
        public CurrentListFragment(){

        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.activity_current_showing_list,container,false);
            RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.rv_current);
            LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            List<ListData> appointmentList=new ArrayList<>();

            //TODO 지워야될거 test하는거
            Day current = new Day();
            current = current.currentTime();
            ListData appointment = new ListData("집가야지",current);
            ListData appointment1 = new ListData("으에에엥");
            ListData appointment2 = new ListData("으에에엥2");
            ListData appointment3 = new ListData("으에에엥3");
            ListData appointment4 = new ListData("으에에엥4");
            ListData appointment5 = new ListData("으에에엥5");
            ListData appointment6 = new ListData("으에에엥6");
            appointmentList.add(appointment);
            appointmentList.add(appointment1);
            appointmentList.add(appointment2);
            appointmentList.add(appointment3);
            appointmentList.add(appointment4);
            appointmentList.add(appointment5);
            appointmentList.add(appointment6);
            Log.d("현재 시간? ", current.year + "년 " + current.month + "월 " + current.day + "일 " + current.hour + "시 " + current.minute + "분");

            recyclerView.setAdapter(new ListAdapter(getContext(),appointmentList,R.layout.current_appointment_list));

            return rootView;
        }
    }

    public static class CompletedListFragment extends Fragment {
        public CompletedListFragment(){

        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_completed_showing_list, container, false);

            RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.rv_completed);
            LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            List<ListData> appointmentList=new ArrayList<>();


            Day current = new Day();
            current = current.currentTime();
            ListData appointment = new ListData("이건 끝난곳",current);
            appointmentList.add(appointment);

            recyclerView.setAdapter(new ListAdapter(getContext(), appointmentList, R.layout.completed_appointment_list));

            return rootView;
        }
    }
}
