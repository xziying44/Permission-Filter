package com.tjrac.filter.factory;

import com.tjrac.filter.entity.Verification;
import com.tjrac.filter.epception.KeyNullException;
import com.tjrac.filter.epception.KeyRepeatException;

import java.util.List;

/**
 * Factories 容器工厂接口
 *
 * @author : xziying
 * @create : 2020-11-14 23:34
 */
public interface PermissionFactories {

    void writePermission(Verification permission) throws KeyRepeatException, KeyNullException, InterruptedException;
    void writePermission(List<Verification> permissions) throws KeyNullException, KeyRepeatException, InterruptedException;


    void DelPermission(String permissionName) throws KeyNullException, InterruptedException;
    void DelPermission(int permissionId) throws KeyNullException, InterruptedException;

    Verification getPermission(String permissionName) throws KeyNullException, InterruptedException;
    Verification getPermission(int permissionId) throws KeyNullException, InterruptedException;

    void setConfigStrict(Boolean b);
    Boolean getConfigStrict();

}
