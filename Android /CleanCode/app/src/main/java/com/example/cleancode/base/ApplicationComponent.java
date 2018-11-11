package com.example.cleancode.base;

import com.example.cleancode.data.RepoServiceModule;
import com.example.cleancode.networking.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {
        ApplicationModule.class,
        ActivityBindingModule.class,
        ServiceModule.class,
        RepoServiceModule.class,
})
public interface ApplicationComponent {

    void inject(MyApplication myApplication);
}
