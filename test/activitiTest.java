import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 10:21 2018/8/6
 * @Modified By:
 */
public class activitiTest {

    /**
     * 使用代码创建工作流需要的表
     */
    public void createTable(){
        //创建单列的流程对象
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        //连接数据库配置
        processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://10.0.0.11:3306/activitiDemo?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
        processEngineConfiguration.setJdbcUsername("admin");
        processEngineConfiguration.setJdbcPassword("Hkju]@(x5t6VJ,#sR45@BctYXunEkVfRPJttANSAEmsQWl7sA123");

        /**
         * ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP 先删除表再创建表
         * ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE 不能自动创建表，表必须先存在
         * ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE 如果表不存在，自动创建表
         */
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        //工作流的核心对象，ProcessEngine对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    }

    /**
     * 使用配置文件创建工作流需要的表
     */
    @Test
    public void createTable_2(){
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    }
}
