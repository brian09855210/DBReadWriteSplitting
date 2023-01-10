package com.hyena.backstage.datasource;

/**
 * 數據源名稱和當前執行緒綁定，提升易用性
 *
 * @author brian.chang
 * @version 2022/01/21 
 *
 */
public abstract class DynamicDataSourceContextHolder {
	// 每個執行緒都可以獨立的改變自己的副本，而不會影響其他執行序所對應的副本
	private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 注意：使用靜態方法setDataSourceId設置當前執行緒需要使用的數據源ID(和當前執行緒綁定)
     */
    public static void setDataSourceId(final String dataSourceId) {
        CONTEXT_HOLDER.set(dataSourceId);
    }

    /**
     * 獲取當前執行緒使用的數據源ID
     */
    public static String getDataSourceId() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清空當前執行緒使用的數據源ID
     */
    public static void clearDataSourceId() {
        CONTEXT_HOLDER.remove();
    }
}
