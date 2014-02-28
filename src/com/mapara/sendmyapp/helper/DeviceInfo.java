package com.mapara.sendmyapp.helper;

import android.content.Context;

import java.util.Date;

/**
 * Created by harshit on 2/16/14.
 */
public interface DeviceInfo
{
    public Date buildDate();
    public String buildVersion();
    public Context getApplicationContext();
}