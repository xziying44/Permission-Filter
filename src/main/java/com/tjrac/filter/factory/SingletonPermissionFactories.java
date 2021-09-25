package com.tjrac.filter.factory;

import com.tjrac.filter.entity.Verification;
import com.tjrac.filter.epception.KeyNullException;
import com.tjrac.filter.epception.KeyRepeatException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * SingletonFactories 所有角色权限工厂
 *
 * @author : xziying
 * @create : 2020-11-14 23:23
 */
public class SingletonPermissionFactories implements PermissionFactories {
    List<Verification> entity = new CopyOnWriteArrayList<>();

    /**
     * 索引
     */
    Map<String, Verification> index_name = new ConcurrentHashMap<>();
    Map<Integer, Verification> index_id = new ConcurrentHashMap<>();

    Boolean ConfigStrict = false;   // 检测模式是否严格



    @Override
    public void writePermission(Verification permission) throws KeyRepeatException, KeyNullException, InterruptedException {
        Integer id = permission.getId();
        String name = permission.getName();
        if (id == null && name == null) {
            throw new KeyNullException("键值为空!");
        } else if (id != null && index_id.containsKey(id)) {
            throw new KeyRepeatException("键值id为" + id + "的实体对象已存在！");
        } else if (index_name.containsKey(name)) {
            throw new KeyRepeatException("键值name为" + name + "的实体对象已存在！");
        }
        entity.add(permission);
        if (name != null) {
            index_name.put(name, permission);
        }
        if (id != null) {
            index_id.put(id, permission);
        }
    }

    @Override
    public void writePermission(List<Verification> permissions) throws KeyNullException, KeyRepeatException, InterruptedException {
        for (Verification permission : permissions){
            writePermission(permission);
        }
    }

    @Override
    public void DelPermission(String permissionName) throws KeyNullException, InterruptedException {
        if (!index_name.containsKey(permissionName)) {
            throw new KeyNullException("匹配的键值为空!");
        }
        /*删除索引*/
        Verification permission = index_name.get(permissionName);
        Integer id = permission.getId();
        if (id != null){
            index_id.remove(id);
        }
        index_name.remove(permissionName);
        /*删除实体*/
        entity.remove(permission);
    }

    @Override
    public void DelPermission(int permissionId) throws KeyNullException, InterruptedException {
        if (!index_id.containsKey(permissionId)) {
            throw new KeyNullException("匹配的键值为空!");
        }
        /*删除索引*/
        Verification permission = index_id.get(permissionId);
        String name = permission.getName();
        if (name != null){
            index_name.remove(name);
        }
        index_id.remove(permissionId);
        /*删除实体*/
        entity.remove(permission);
    }

    @Override
    public Verification getPermission(String permissionName) throws KeyNullException, InterruptedException {
        if (!index_name.containsKey(permissionName)) {
            throw new KeyNullException("匹配的键值为空!");
        }
        return index_name.get(permissionName);

    }

    @Override
    public Verification getPermission(int permissionId) throws KeyNullException, InterruptedException {
        if (!index_id.containsKey(permissionId)) {
            throw new KeyNullException("匹配的键值为空!");
        }
        return index_id.get(permissionId);
    }

    @Override
    public Boolean getConfigStrict() {
        return ConfigStrict;
    }

    @Override
    public void setConfigStrict(Boolean configStrict) {
        ConfigStrict = configStrict;
    }


}