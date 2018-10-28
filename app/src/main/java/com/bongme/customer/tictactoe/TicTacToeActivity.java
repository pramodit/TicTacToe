package com.bongme.customer.tictactoe;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bongme.customer.R;
import com.bongme.customer.util.GlobalData;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class TicTacToeActivity extends DaggerAppCompatActivity implements TicTacToeView {

    private static String TAG = TicTacToeActivity.class.getName();

    @BindView(R.id.winnerPlayerLabel)
    TextView winnerPlayerLabel;
s
    @BindView(R.id.winnerPlayerViewGroup)
    View winnerPlayerViewGroup;

    @BindView(R.id.buttonGrid)
    ViewGroup buttonGrid;

    @Inject
    TicTacToePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictactoe);
        ButterKnife.bind(this);
        winnerPlayerLabel = (TextView) findViewById(R.id.winnerPlayerLabel);
        winnerPlayerViewGroup = findViewById(R.id.winnerPlayerViewGroup);
        buttonGrid = (ViewGroup) findViewById(R.id.buttonGrid);


        int childCount = buttonGrid.getChildCount();

        for(int i=0;i<childCount;i++){
            final Button button = (Button) buttonGrid.getChildAt(i);

            // Approach 1 RxJava
           /* Observable<View> clickObservable = Observable.create(new ObservableOnSubscribe<View>() {
                @Override
                public void subscribe(ObservableEmitter<View> e) throws Exception {
                    //Log.e(TAG,"Subscribe");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //e.onNext();
                            Button button = (Button) view;
                            final String tag = button.getTag().toString();
                            //Log.e(TAG,"INside "+tag);
                            presenter.onCellClicked(tag);
                        }
                    });
                }
            });

            clickObservable.
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribeOn(Schedulers.io()).
                    throttleWithTimeout(2, TimeUnit.SECONDS).
                    subscribe(new Consumer<View>() {
                        @Override
                        public void accept(View view) throws Exception {
                            Log.e(TAG,"Observer :: "+view.getTag());
                            view.setEnabled(true);
                        }
                    });*/


            //Approach 2 - Emit and subscribe

            GlobalData.getInstance().setClickObservable(button);

            presenter.getObservable();



            /*clickBtnObservable.
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribeOn(Schedulers.io()).
                    subscribe(new Consumer<View>() {
                        @Override
                        public void accept(View view) {
                            try {
                                Button button = (Button) view;
                                final String tag = button.getTag().toString();
                                Log.e(TAG,"Clicked tag :: "+tag);
                                presenter.onCellClicked(tag);
                            }  catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });*/



        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //presenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (disposable!=null && !disposable.isDisposed()) {
            disposable.dispose();
        }*/
        //presenter.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tictactoe, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                presenter.onResetSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public void onCellClicked(View v) {

        Button button = (Button) v;
        String tag = button.getTag().toString();
        int row = Integer.valueOf(tag.substring(0,1));
        int col = Integer.valueOf(tag.substring(1,2));
        Log.i(TAG, "Click Row: [" + row + "," + col + "]");

        presenter.onButtonSelected(row, col);

    }*/

    @Override
    public void setButtonText(int row, int col, String text) {
        Button btn = (Button) buttonGrid.findViewWithTag("" + row + col);
        if(btn != null) {
            btn.setText(text);
        }
    }

    public void clearButtons() {
        for( int i = 0; i < buttonGrid.getChildCount(); i++ ) {
            ((Button) buttonGrid.getChildAt(i)).setText("");
        }
    }

    public void showWinner(String winningPlayerDisplayLabel) {
        winnerPlayerLabel.setText(winningPlayerDisplayLabel);
        winnerPlayerViewGroup.setVisibility(View.VISIBLE);
    }

    public void clearWinnerDisplay() {
        winnerPlayerViewGroup.setVisibility(View.GONE);
        winnerPlayerLabel.setText("");
    }
}
