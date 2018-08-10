package com.activiti.demo.groupTask;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 9:54 2018/8/10
 * @Modified By:
 */
@SuppressWarnings("unused")
public class TaskListenerImpl implements TaskListener{
    private static final long serialVersionUID = 1697597472587361823L;

    @Override
    public void notify(DelegateTask delegateTask) {
        //指定个人任务的办理人，也可以指定组任务的办理人
        //个人任务：通过类去查询数据库，将下一个任务的办理人查询获取，然后通过setAssignee()的方法指定任务的办理人
        //delegateTask.setAssignee("蛮王");

        //组任务
        delegateTask.addCandidateUser("阿卡丽");
        delegateTask.addCandidateUser("慎");
        delegateTask.addCandidateUser("凯南");
    }
}
