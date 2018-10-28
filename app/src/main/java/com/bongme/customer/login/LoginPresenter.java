package com.bongme.customer.login;

import android.content.Context;
import android.util.Log;

import com.bongme.customer.R;
import com.bongme.customer.dagger.ActivityScoped;
import com.bongme.customer.networking.NetworkService;
import com.bongme.customer.networking.ServiceFactory;
import com.bongme.customer.pojos.CityListResponse;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.internal.framed.Header;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dell on 31-Oct-17.
 */

final class LoginPresenter implements LoginContract.Presenter
{
    //private  LoginContract.View mView;
    private LoginModel model;
    private CompositeDisposable compositeDisposable;
    private Context mcontext;

     /*LoginPresenter(Context context, LoginContract.View view)
     {
         mcontext=context;
         mView=view;
         model=new LoginModel();
         compositeDisposable=new CompositeDisposable();


     }*/

     @Inject
     LoginPresenter(Context context) {
         mcontext=context;
         model=new LoginModel();
         compositeDisposable=new CompositeDisposable();


     }

    @Override
    public void validate(String phoneNumber)
    {
        if(model.validatePhone(phoneNumber))
        {
            //mView.onError(mcontext.getString(R.string.not_valid_number));
            Log.e("LP",mcontext.getString(R.string.not_valid_number));
        }
        else
        {
            doLogin(0,"+91",phoneNumber,1,"");
        }
    }

    @Override
    public void doLogin(int language, String countryCode, String phno,int type, String email)
    {
        //mView.showProgress();
        NetworkService service = ServiceFactory.createRetrofitService(NetworkService.class);
        Observable<CityListResponse> bad=service.getCityList(language,countryCode,phno,type,email);
                bad.subscribeOn(Schedulers.newThread())
                 .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CityListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }
                    @Override
                    public void onNext(CityListResponse value)
                    {
                        Log.d("code",""+value.getMessage());
                        //mView.onSuccess();
                    }
                    @Override
                    public void onError(Throwable e)
                    {
                        Log.e("Error","error"+e.getMessage());
                        //mView.onError(e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                        Log.e("LP","Completed");
                        //mView.hideProgress();
                    }
                });
    }

    @Override
    public void onButtonClick()
    {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String msg) {
     //mView.onError(msg);
        Log.e("LP","on Error");
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
    }
}
