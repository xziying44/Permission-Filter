package com.tjrac.filter.entity.impl;

import com.tjrac.filter.entity.Verification;

import java.util.Objects;

/**
 * Permit 权限表
 *
 * @author : xziying
 * @create : 2020-11-14 18:52
 */
public class Permission implements Verification {
    Integer id;     // 权限编号
    String name;    // 权限名称
    String desc;    // 权限描述
    String link;            //资源链接  支持正则表达式

    public Permission(Integer id) {
        this.id = id;
    }

    public Permission(String name) {
        this.name = name;
    }

    public Permission(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Verification o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, o.getId()) &&
                Objects.equals(name, o.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
