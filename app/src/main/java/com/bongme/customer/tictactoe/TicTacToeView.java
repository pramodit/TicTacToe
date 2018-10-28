package com.bongme.customer.tictactoe;

/**
 * Created by Pramod on 11/12/17.
 */

public interface TicTacToeView {
    void showWinner(String winningPlayerDisplayLabel);
    void clearWinnerDisplay();
    void clearButtons();
    void setButtonText(int row, int col, String text);
}
