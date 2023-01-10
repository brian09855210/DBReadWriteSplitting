package com.hyena.backstage.datasource;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * 定義DataSourceConfig
 *
 * @author brian.chang
 * @version 2022/01/21 
 *
 */
//@EnableTransactionManagement
//@Configuration
public class DataSourceConfig implements TransactionManagementConfigurer {

	@Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;

    // 從庫配置
    @Value("${spring.datasource.slave.url}")
    private String slaveUrl;
    @Value("${spring.datasource.slave.username}")
    private String slaveUserName;
    @Value("${spring.datasource.slave.password}")
    private String slavePassword;

    // 配置主從兩庫數據源
    private DataSource masterDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(userName);
        dataSource.setPassword(password);
        dataSource.setURL(url);
        return dataSource;
    }
    
    private DataSource slaveDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(slaveUserName);
        dataSource.setPassword(slavePassword);
        dataSource.setURL(slaveUrl);
        return dataSource;
    }
    
    @Bean
    public DataSource dataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        final DataSource masterDataSource = masterDataSource();
        final DataSource slaveDataSource = slaveDataSource();
        // 初始化必須設置進去，且給默認數據源
        dataSource.setTargetDataSources(new HashMap<Object, Object>() {{
            put(DynamicDataSourceId.MASTER, masterDataSource);
            put(DynamicDataSourceId.SLAVE, slaveDataSource);

            // 順便註冊，方便後續判斷
            DynamicDataSourceId.DATA_SOURCE_IDS.add(DynamicDataSourceId.MASTER);
            DynamicDataSourceId.DATA_SOURCE_IDS.add(DynamicDataSourceId.SLAVE);
        }});

        dataSource.setDefaultTargetDataSource(masterDataSource);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
        dataSourceTransactionManager.setEnforceReadOnly(true); // 讓事務管理器進行只讀事務層面上的優化，建議開啟
        return dataSourceTransactionManager;
    }

    // 指定註解使用的事務管理器
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }
}
