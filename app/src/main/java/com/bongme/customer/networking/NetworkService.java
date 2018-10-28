package com.bongme.customer.networking;



import com.bongme.customer.pojos.CityListResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;


/**
 * Created by ennur on 6/25/16.
 */
public interface NetworkService
{
    @POST("/customer/emailPhoneValidate")
    @FormUrlEncoded
    Observable<CityListResponse> getCityList(@Header("language") int language , @Field("countryCode")String countryCode, @Field("mobile") String mobile,
                                             @Field("verifyType") int verifyType, @Field("email") String email);
}
