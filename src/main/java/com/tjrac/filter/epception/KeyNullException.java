package com.tjrac.filter.epception;

/**
 * KeyNullExcption 空键值
 *
 * @author : xziying
 * @create : 2020-11-15 13:09
 */
public class KeyNullException extends FilterException{
    /**
     * 构造方法
     *
     * @param message 错误提示信息
     */
    public KeyNullException(String message) {
        super(message);
    }
}
