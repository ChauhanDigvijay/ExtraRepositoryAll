package com.example.cleancode.home;

import com.bluelinelabs.conductor.Controller;
import com.example.cleancode.R;
import com.example.cleancode.base.BaseActivity;
import com.example.cleancode.trending.TrendingReposController;


public class MainActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected Controller initialScreen() {
        return new TrendingReposController();
    }
}
