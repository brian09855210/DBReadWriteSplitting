package com.hyena.backstage.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * 定義DataSource Key
 *
 * @author brian.chang
 * @version 2022/01/21 
 *
 */
public abstract class DynamicDataSourceId {

    public static final String MASTER = "master";
    public static final String SLAVE = "slave";
    //... 可以繼續無限擴展


    // 保存著有效的（從DataSourceConfig設置進來）所有的DATA_SOURCE_IDS
    public static final List<String> DATA_SOURCE_IDS = new ArrayList<>();

    public static boolean containsDataSourceId(final String dataSourceId) {
        return dataSourceId != null && !dataSourceId.trim().isEmpty() ? DATA_SOURCE_IDS.contains(dataSourceId) : false;
    }
}
