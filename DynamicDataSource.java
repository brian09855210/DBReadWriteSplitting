package com.hyena.backstage.datasource;

import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 定義自己的動態數據源DataSource類
 *
 * @author brian.chang
 * @version 2022/01/21 
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() { // 所有的請求都會經過此處，所以沒有切換數據源的時候，不輸出Log
        String dataSourceId = DynamicDataSourceContextHolder.getDataSourceId();
        if (dataSourceId != null) { // 有指定切換數據源的時候，才輸出Log
            LogManager.getLogger().debug("執行緒[{}]，目前切換的數據源為:{}", Thread.currentThread().getId(), dataSourceId);
        }
        return dataSourceId;
    }
}
