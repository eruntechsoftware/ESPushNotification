package com.eruntech.espushnotification.interfaces;

/**
 * 任务执行接口
 * 2018/4/25.
 */

public interface ITask
{
    public void run();

    /**
     * 任务是否是运行状态
     * **/
    public Boolean isRuning();
}
