import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 15:12 2018/8/6
 * @Modified By:
 */
public class helloworld {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinition(){
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                        .createDeployment()//创建一个部署对象
                        .name("helloworld入门程序")//添加部署的名称
                        .addClasspathResource("diagram/helloWorld.bpmn")//从classpath的资源中加载，一次只能加载一个文件
                        .addClasspathResource("diagram/helloWorld.png")
                        .deploy();//完成部署
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        ProcessInstance processInstance = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的service
                        .startProcessInstanceByKey("helloworld");//使用流程定义的key启动流程实例，key对应bpmn文件中id的属性值,使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println(processInstance.getId());//流程实例ID
        System.out.println(processInstance.getProcessDefinitionId());//流程定义ID
    }

    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonalTask(){
        List<Task> tasks = processEngine.getTaskService()//与正在执行的任务管理相关的service
                        .createTaskQuery()//创建任务查询对象
                        .taskAssignee("李四")//指定个人任务查询，指定办理人
                        .list();
        if(tasks != null && tasks.size()> 0){
            for (Task task:tasks) {
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间："+task.getCreateTime());
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行对象ID："+task.getExecutionId());
                System.out.println("流程定义ID："+task.getProcessDefinitionId());
            }
        }
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask(){
        String taskid="2504";
        processEngine.getTaskService()//与正在执行的任务管理相关的service
                        .complete(taskid);
        System.out.println("完成任务：任务ID："+taskid);
    }
}
