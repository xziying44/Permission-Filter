package com.tjrac.filter.entity;

/**
 * PermissionRole
 *
 * @author : xziying
 * @create : 2020-11-16 12:21
 */
public interface VerificationRole {
    boolean verification(Verification permission);
    boolean verification(String link);
}
