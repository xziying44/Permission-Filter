package com.tjrac.filter.entity.impl;

import com.tjrac.filter.entity.VerificationParam;

/**
 * ParamBind
 *
 * @author : xziying
 * @create : 2020-11-16 12:33
 */
public class ObjectParam implements VerificationParam {
    Object object;

    public ObjectParam(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public boolean verification(Object o) {
        return o.equals(object);
    }
}
