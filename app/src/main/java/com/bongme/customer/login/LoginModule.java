package com.bongme.customer.login;


import android.app.Activity;

import com.bongme.customer.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * Created by Pramod on 11/12/17.
 */

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link LoginPresenter}.
 */
@Module
public abstract class LoginModule {

    @ActivityScoped
    @Binds
    abstract Activity LoginActivity(LoginActivity loginActivity);

    @ActivityScoped
    @Binds abstract LoginContract.Presenter loginPresenter(LoginPresenter presenter);
}
