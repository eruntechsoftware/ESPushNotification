package com.eruntech.espushnotification.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 窗口屏幕管理工具类
 * Created by Ming on 2017/11/22.
 */
public class PackgeManager
{
    /**
     * 获取当前应用栈顶端运行的是否是指定包名的应用
     *
     * @param packgeName 包名
     * @return 是否是当前包名的应用
     **/
    public static boolean isCurrentAppPackgeName (Context context,String packgeName) throws Exception
    {
        try
        {
            if(packgeName!=null && packgeName.trim().equals(""))
            {
                throw new Exception("比较的包名不能为空");
            }

            String currPackgeName = "";
            //如果Android版本号为5.0及以上版本，以下方式获得
            if (android.os.Build.VERSION.SDK_INT > 20)
            {
                currPackgeName = getAndroid5LaterCurrentPackgeName(context);
                if(currPackgeName!=null && !currPackgeName.toUpperCase().trim().equals("") &&currPackgeName.toUpperCase().trim().equals(packgeName.toUpperCase()))
                {
                    return true;
                }
                return false;
            }
            else
            {
                currPackgeName = getAndroid5BeforeCurrentPackgeName(context);
                if(currPackgeName!=null && !currPackgeName.toUpperCase().trim().equals("") &&currPackgeName.toUpperCase().trim().equals(packgeName.toUpperCase()))
                {
                    return true;
                }
                return false;
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    /**
     * 获取Android5.0之前的运行的应用程序包名
     * **/
    public static String getAndroid5BeforeCurrentPackgeName (Context context)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //完整类名
        String runningActivity = activityManager.getRunningTasks(1)
                .get(0).topActivity.getClassName();
        String contextActivity = runningActivity.substring(runningActivity.lastIndexOf(".") + 1);
        return contextActivity;
    }

    /**
     * 获取Android5.0之后的运行的应用程序包名
     * **/
    public static String getAndroid5LaterCurrentPackgeName (Context context)
    {
        ActivityManager.RunningAppProcessInfo currentInfo = null;
        Field field = null;
        int START_TASK_TO_FRONT = 2;
        String pkgName = null;
        try
        {
            field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo app : appList)
        {
            if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
            {
                Integer state = null;
                try
                {
                    state = field.getInt(app);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if (state != null && state == START_TASK_TO_FRONT)
                {
                    currentInfo = app;
                    break;
                }
            }
        }
        if (currentInfo != null)
        {
            pkgName = currentInfo.processName;
        }
        return pkgName;
    }
}
