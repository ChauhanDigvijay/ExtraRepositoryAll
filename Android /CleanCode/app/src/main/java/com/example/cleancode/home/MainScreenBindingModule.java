package com.example.cleancode.home;

import com.bluelinelabs.conductor.Controller;
import com.example.cleancode.di.ControllerKey;
import com.example.cleancode.trending.TrendingReposComponent;
import com.example.cleancode.trending.TrendingReposController;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;


@Module(subcomponents = {
        TrendingReposComponent.class,
})
public abstract class MainScreenBindingModule {

    @Binds
    @IntoMap
    @ControllerKey(TrendingReposController.class)
    abstract AndroidInjector.Factory<? extends Controller> bindTrendingReposInjector(TrendingReposComponent.Builder builder);
}
