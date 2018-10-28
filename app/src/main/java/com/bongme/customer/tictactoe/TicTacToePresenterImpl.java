package com.bongme.customer.tictactoe;

import android.util.Log;

import com.bongme.customer.model.Board;
import com.bongme.customer.model.Player;
import com.bongme.customer.util.GlobalData;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by Pramod on 11/12/17.
 */

public class TicTacToePresenterImpl implements TicTacToePresenter {
    private TicTacToeView view;
    private Board model;


    @Inject
    TicTacToePresenterImpl(TicTacToeView view) {
        this.view = view;
        this.model = new Board();
    }

    @Override
    public void getObservable() {
        Observable<String> clickObservable = GlobalData.getInstance().getClickObservable();

        //Log.e(TAG,"Observable ::  "+clickObservable);

        clickObservable.
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String tag) {
                        try {

                            onCellClicked(tag);

                        }  catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void onCellClicked(String tag) {
        int row = Integer.valueOf(tag.substring(0,1));
        int col = Integer.valueOf(tag.substring(1,2));
        Log.e(TAG, "Click Row: [" + row + "," + col + "]");

        onButtonSelected(row,col);
    }

    private void onButtonSelected(int row, int col) {
        Player playerThatMoved = model.mark(row, col);

        if(playerThatMoved != null) {
            view.setButtonText(row, col, playerThatMoved.toString());

            if (model.getWinner() != null) {
                view.showWinner(playerThatMoved.toString());
            }
        }
    }

    @Override
    public void onResetSelected() {
        view.clearWinnerDisplay();
        view.clearButtons();
        model.restart();
    }
}
