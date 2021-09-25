package com.tjrac.filter.entity;

/**
 * Verification
 *
 * @author : xziying
 * @create : 2020-11-17 21:18
 */
public interface Verification {

    public Integer getId();

    public void setId(Integer id);

    public String getName();

    public void setName(String name);

    public String getLink();

    public void setLink(String link);

    boolean equals(Verification permission);
}
