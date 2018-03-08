package com.eruntech.espushnotification.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.eruntech.espushnotification.receiver.StartRunMessageServiceReceiver;
import com.eruntech.espushnotification.utils.Contants;
import com.eruntech.espushnotification.utils.ScreenManager;
import com.eruntech.espushnotification.utils.SystemUtils;


/**
 * 1像素Activity
 * <p>
 * Created by jianddongguo on 2017/7/8.
 */

public class SinglePixelActivity extends AppCompatActivity
{
    private static final String TAG = "SinglePixelActivity";

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Contants.DEBUG)
            Log.d(TAG, "onCreate--->启动1像素保活");
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 0;
        attrParams.width = 0;
        mWindow.setAttributes(attrParams);
        // 绑定SinglePixelActivity到ScreenManager
        ScreenManager.getScreenManagerInstance(this).setSingleActivity(this);
    }

    @Override
    protected void onDestroy ()
    {
        if (Contants.DEBUG)
            Log.d(TAG, "onDestroy--->1像素保活被终止");
        if (!SystemUtils.isAPPALive(this, Contants.PACKAGE_NAME))
        {
            Intent intent = new Intent(StartRunMessageServiceReceiver.class.getName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendBroadcast(intent);
            Log.i(TAG, "SinglePixelActivity---->APP被干掉了，我要重启它");
        }
        super.onDestroy();
    }
}
