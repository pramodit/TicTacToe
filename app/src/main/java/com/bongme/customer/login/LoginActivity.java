package com.bongme.customer.login;

import android.database.Observable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bongme.customer.R;
import com.bongme.customer.pojos.CityListResponse;

import java.util.Observer;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.Disposable;


public class LoginActivity extends DaggerAppCompatActivity implements LoginContract.View {

    private static final String TAG = "LoginActivity" ;
    private EditText phoneNumberEt;
    @Inject
    LoginPresenter presenter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mInitialization();

    }

    private void mInitialization()
    {
        //presenter=new LoginPresenter(this);
        phoneNumberEt= (EditText) findViewById(R.id.phoneNumberEt);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.validate(phoneNumberEt.getText().toString());
            }
        });
    }



    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void emptyError() {
        Toast.makeText(LoginActivity.this,"Enter Phone number",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccess()
    {
        Toast.makeText(LoginActivity.this,"Sucess",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();

    }

}
