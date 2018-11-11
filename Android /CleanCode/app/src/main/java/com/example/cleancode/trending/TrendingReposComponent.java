package com.example.cleancode.trending;

import com.example.cleancode.di.ScreenScope;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;


@ScreenScope
@Subcomponent
public interface TrendingReposComponent extends AndroidInjector<TrendingReposController> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<TrendingReposController> {

    }
}
