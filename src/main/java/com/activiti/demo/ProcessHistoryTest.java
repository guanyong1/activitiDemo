package com.activiti.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

import java.util.List;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 14:55 2018/8/8
 * @Modified By:
 */
public class ProcessHistoryTest {

    //流程引擎对象
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    /**
     * 查询历史流程实例
     */
    @Test
    public void findHistoryProcessInstance(){
        String peocessId = "37501";
        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
                        .createHistoricProcessInstanceQuery()
                        .processInstanceId(peocessId)
                        .singleResult();
        System.out.println(historicProcessInstance.getId());
    }

    /**
     * 查询历史活动
     */
    @Test
    public void findHistoryActiviti(){
        String processInstanceId="37501";
        List<HistoricActivityInstance> list = processEngine.getHistoryService()
                        .createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .orderByHistoricActivityInstanceStartTime().asc()
                        .list();
        if(list != null && list.size() >0){
            for (HistoricActivityInstance h:list) {
                System.out.println(h);
            }
        }
    }

    /**
     * 查询历史任务
     */
    @Test
    public void findHistoryTask(){
        String taskAssignee = "张三";
        List<HistoricTaskInstance> list = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()//创建任务历史查询对象
                .taskAssignee(taskAssignee)
                .list();
        if(list != null && list.size() > 0){
            for (HistoricTaskInstance history:list) {
                System.out.println(history);
            }
        }
    }

    /**
     * 查询历史流程变量
     */
    @Test
    public void findHitoryProcessVariables(){
        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                        .createHistoricVariableInstanceQuery()
                        .variableName("请假天数")
                        .list();
        if(list != null  && list.size() > 0 ){
            for (HistoricVariableInstance h:list) {
                System.out.println(h);
            }
        }
    }
}
