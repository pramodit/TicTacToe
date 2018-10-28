package com.bongme.customer.util;

import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Pramod on 13/12/17.
 */


public class GlobalData {

    private Map<String,Observable<String>> map;

    private GlobalData() {
        map = new HashMap<String, Observable<String>>();
    }


    private static GlobalData instance;

    public static GlobalData getInstance() {
        if (instance == null) {
            instance = new GlobalData();
        }
        return instance;
    }

    public Observable<String> getClickObservable() {
        return (Observable<String>) map.get("observable");
    }

    public void setClickObservable(final View view) {
        Observable<String> clickObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                final ObservableEmitter<String> emitter = e;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        emitter.onNext(view.getTag().toString());
                    }
                });
            }
        });
        map.put("observable",clickObservable);
    }
}
