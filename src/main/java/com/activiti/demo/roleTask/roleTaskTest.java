package com.activiti.demo.roleTask;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 10:34 2018/8/10
 * @Modified By:
 */
public class roleTaskTest {

    //流程引擎对象
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinittion(){
        InputStream inputStreamBpmn = this.getClass().getClassLoader().getResourceAsStream("roleTask/roleTask.bpmn");
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name("用户角色组")
                .addInputStream("roleTask.bpmn",inputStreamBpmn)
                //.addInputStream("sequenceFlow.png",inputStreamPng)
                .deploy();
        System.out.println(deployment.getId());

        /**添加用户角色组*/
        IdentityService identityService = processEngine.getIdentityService();
        //创建角色
        identityService.saveGroup(new GroupEntity("总经理"));
        identityService.saveGroup(new GroupEntity("部门经理"));
        //创建用户
        identityService.saveUser(new UserEntity("张三"));
        identityService.saveUser(new UserEntity("李四"));
        identityService.saveUser(new UserEntity("王五"));
        //建立用户和角色的关联关系
        identityService.createMembership("张三","部门经理");
        identityService.createMembership("李四","部门经理");
        identityService.createMembership("王五","总经理");

    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey("roleTask");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessInstanceId());
    }

    /**
     * 查询当前人的组任务
     */
    @Test
    public void findMyGroupTask(){
        String name = "李四";
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()//创建任务查询对象
                .taskCandidateUser(name)//组任务的办理人查询
                .list();
        if(list != null && list.size()>0){
            for (Task task:list) {
                System.out.println("任务ID："+task.getId());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务创建时间："+task.getCreateTime());
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("流程实例："+task.getProcessInstanceId());
                System.out.println("执行对象ID："+task.getExecutionId());
                System.out.println("流程定义ID："+task.getProcessDefinitionId());
                System.out.println("###############");
            }
        }
    }


}
