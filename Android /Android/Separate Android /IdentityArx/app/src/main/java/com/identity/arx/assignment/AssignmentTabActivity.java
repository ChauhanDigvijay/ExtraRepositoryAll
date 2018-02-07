package com.identity.arx.assignment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.identity.arx.R;
import com.identity.arx.NotSubmittedFragment;
import com.identity.arx.SubmittedFragment;
import com.identity.arx.general.MasterActivity;


public class AssignmentTabActivity extends MasterActivity {
    static int course_Id;
    static String course_Name;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        int course_id;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt("COURSE_ID", AssignmentTabActivity.course_Id);
            fragment.setArguments(args);
            return fragment;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_assignment_tab, container, false);
            ((TextView) rootView.findViewById(R.id.section_label)).setText(getString(R.string.section_format, Integer.valueOf(getArguments().getInt(ARG_SECTION_NUMBER))));
            this.course_id = getArguments().getInt("COURSE_ID", 0);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            if (position == 0) {
                return new NotSubmittedFragment();
            }
            if (position == 1) {
                return new SubmittedFragment();
            }
            if (position == 2) {
                return new SubmittedFragment();
            }
            return null;
        }

        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "New";
                case 1:
                    return "Submitted";
                case 2:
                    return "Graded";
                default:
                    return null;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_assignment_tab);
        course_Name = getIntent().getStringExtra("COURSE_NAME");
        course_Id = getIntent().getIntExtra("COURSE_id", 0);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        this.mViewPager = (ViewPager) findViewById(R.id.container);
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        ((TabLayout) findViewById(R.id.tabs)).setupWithViewPager(this.mViewPager);
    }

    public int getCourseId() {
        return course_Id;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assignment_tab, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
