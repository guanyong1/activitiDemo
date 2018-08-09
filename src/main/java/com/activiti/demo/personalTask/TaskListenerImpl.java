package com.activiti.demo.personalTask;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 14:38 2018/8/9
 * @Modified By:
 */
@SuppressWarnings("unused")
public class TaskListenerImpl implements TaskListener{
    private static final long serialVersionUID = -2458496198243534465L;

    /**
     * 用来指定任务的办理人
     */
    @Override
    public void notify(DelegateTask delegateTask) {

        //指定个人任务的办理人，也可以指定组任务的办理人
        //个人任务：通过类去查询数据库，将下一个任务的办理人查询获取，然后通过setAssignee()的方法指定任务的办理人
        delegateTask.setAssignee("蛮王");
    }
}
