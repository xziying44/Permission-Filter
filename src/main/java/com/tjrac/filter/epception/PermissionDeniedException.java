package com.tjrac.filter.epception;

/**
 * PermissionDeniedException
 *
 * @author : xziying
 * @create : 2020-11-15 22:36
 */
public class PermissionDeniedException extends FilterException{
    /**
     * 构造方法
     *
     * @param message 错误提示信息
     */
    public PermissionDeniedException(String message) {
        super(message);
    }
}
