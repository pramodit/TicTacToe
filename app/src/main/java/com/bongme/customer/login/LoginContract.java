package com.bongme.customer.login;

/**
 * Created by dell on 31-Oct-17.
 */

public interface LoginContract {

    interface View
    {
        void hideProgress();
        void showProgress();
        void emptyError();
        void onSuccess();
        void onError(String msg);



    }

    interface Presenter
    {
        void validate(String phoneNumber);
        void doLogin(int language,String countryCode,String phno,int type,String email);
        void onButtonClick();
        void onSuccess();
        void onError(String msg);
        void onStop();
    }
}
