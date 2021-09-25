package com.tjrac.filter.stereotype;

public @interface Param {
    int index();            // 参数顺序
    String sessionKey();    // 对应session中的键值

}
