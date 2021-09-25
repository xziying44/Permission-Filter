package com.tjrac.filter.factory;

/**
 * Factories
 *
 * @author : xziying
 * @create : 2020-11-15 14:40
 */
public class FilterFactories {
    /**
     * 容器
     */
    static public final PermissionFactories permissionFactories = new SingletonPermissionFactories();

    /**
     * session中储存的角色对象 接口为(VerificationRole)
     */
    static public String FILTER_SESSION_ROLE = "FILTER_SESSION_ROLE";
}
