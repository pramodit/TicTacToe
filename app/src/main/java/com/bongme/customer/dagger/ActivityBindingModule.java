package com.bongme.customer.dagger;

import com.bongme.customer.login.LoginActivity;
import com.bongme.customer.login.LoginModule;
import com.bongme.customer.tictactoe.TicTacToeActivity;
import com.bongme.customer.tictactoe.TicTacToeModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Pramod on 11/12/17.
 */

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = TicTacToeModule.class)
    abstract TicTacToeActivity ticTacToeActivity();
}
