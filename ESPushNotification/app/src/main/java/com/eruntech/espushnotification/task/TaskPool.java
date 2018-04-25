package com.eruntech.espushnotification.task;

import com.eruntech.espushnotification.interfaces.ITask;

import java.util.LinkedList;
import java.util.List;

/**
 * 任务池
 * 2018/4/25.
 */

public class TaskPool
{
    //任务列表
    private static List<ITask> taskList = new LinkedList<>();

    public TaskPool()
    {}

    /**
     * 添加任务
     * @param task 任务
     * **/
    public static void addTask(ITask task)
    {
        if(taskList==null)
        {
            taskList = new LinkedList<>();
        }
        taskList.add(task);
    }

    public static List<ITask> getTaskList ()
    {
        return taskList;
    }

    public static void setTaskList (List<ITask> taskList)
    {
        TaskPool.taskList = taskList;
    }
}
