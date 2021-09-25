package com.tjrac.filter.epception;

/**
 * KeyRepeatException 键值重复
 *
 * @author : xziying
 * @create : 2020-11-15 13:03
 */
public class KeyRepeatException extends FilterException{

    /**
     * 构造方法
     *
     * @param message 错误提示信息
     */
    public KeyRepeatException(String message) {
        super(message);
    }
}
