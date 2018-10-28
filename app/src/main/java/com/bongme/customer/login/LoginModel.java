package com.bongme.customer.login;

/**
 * Created by dell on 31-Oct-17.
 */

public class LoginModel
{
    boolean validatePhone(String phone)
    {
        if(phone==null || phone.equals(""))
         return  true;
        else
            return false;
    }

}
