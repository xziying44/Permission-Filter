package com.tjrac.filter.epception;

/**
 * NullRoleException
 *
 * @author : xziying
 * @create : 2020-11-15 22:58
 */
public class NullRoleException extends FilterException{
    /**
     * 构造方法
     *
     * @param message 错误提示信息
     */
    public NullRoleException(String message) {
        super(message);
    }
}
