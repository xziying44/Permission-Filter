package com.tjrac.filter.entity.impl;

import com.tjrac.filter.entity.Verification;
import com.tjrac.filter.entity.VerificationRole;

import java.util.List;

/**
 * Role 角色类
 *
 * @author : xziying
 * @create : 2020-11-14 18:50
 */
public class Role implements VerificationRole {
    Integer id;     // 角色编号
    String name;    // 角色名称
    String desc;    // 角色描述
    List<Permission> permissions;   // 拥有的权限表

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * 验证权限
     * @param permission 权限
     */
    @Override
    public boolean verification(Verification permission) {
        for (Permission p : permissions){
            if (p.equals(permission))
                return true;
        }
        return false;
    }

    /**
     * 验证静态资源
     * @param link 权限中的静态资源地址，正则匹配
     */
    @Override
    public boolean verification(String link) {
        for (Permission p : permissions){
            String vLink = p.getLink();
            if (link.matches(vLink)){
                return true;
            }
        }
        return false;
    }
}
