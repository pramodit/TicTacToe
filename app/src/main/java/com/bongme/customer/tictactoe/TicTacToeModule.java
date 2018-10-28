package com.bongme.customer.tictactoe;

import android.app.Activity;
import android.view.View;

import com.bongme.customer.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;
import io.reactivex.Observable;

/**
 * Created by Pramod on 11/12/17.
 */

@Module
public abstract class TicTacToeModule {
    @ActivityScoped
    @Binds
    abstract Activity TicTacToeActivity(TicTacToeActivity ticTacToeActivity);

    @ActivityScoped
    @Binds
    abstract TicTacToePresenter ticTacToePresenter(TicTacToePresenterImpl presenter);

    @ActivityScoped
    @Binds
    abstract TicTacToeView ticTacToeView(TicTacToeActivity ticTacToeActivity);
}
