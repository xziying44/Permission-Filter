package com.tjrac.filter.epception;

/**
 * FilterException 权限插件所有异常基类
 *
 * @author : xziying
 * @create : 2020-11-15 13:06
 */
public class FilterException extends Exception{

    /**
     * 构造方法
     * @param message 错误提示信息
     */
    public FilterException(String message) {
        super(message);
    }

    /**
     * 获取提示信息
     * @return 在网页中的提示信息
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
