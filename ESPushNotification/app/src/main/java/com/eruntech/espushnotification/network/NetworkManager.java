package com.eruntech.espushnotification.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Ming on 2017/11/17.
 */

public class NetworkManager
{
    private static final String TAG = "Eruntech";
    public static final String TAG1 = "NetWork";
    public NetworkManager ()
    {
    }

    /**
     *获取网络连接状态
    * */
    public static boolean getNetworkConnected(Context context)
    {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.i(TAG1, "CONNECTIVITY_ACTION");

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null)
        {
            return activeNetwork.isConnected();
        }
        else
        {
            Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
        }
        return false;
    }


}
