package com.tjrac.filter.entity.impl;

import com.tjrac.filter.entity.VerificationParam;

import java.util.List;

/**
 * ListParam
 *
 * @author : xziying
 * @create : 2020-11-16 12:34
 */
public class ListParam implements VerificationParam {
    List<Object> list;

    public ListParam(List<Object> list) {
        this.list = list;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    @Override
    public boolean verification(Object o) {
        for (Object l : list){
            if (l.equals(o)){
                return true;
            }
        }
        return false;
    }
}
