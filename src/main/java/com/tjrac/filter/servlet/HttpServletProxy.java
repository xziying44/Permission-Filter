package com.tjrac.filter.servlet;

import com.tjrac.filter.entity.Verification;
import com.tjrac.filter.entity.VerificationRole;
import com.tjrac.filter.epception.PermissionDeniedException;
import com.tjrac.filter.factory.FilterFactories;
import com.tjrac.filter.stereotype.PermissionControl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * HttpServletProxy 基于普通servlet管理
 *
 * @author : xziying
 * @create : 2020-11-14 23:00
 */
public class HttpServletProxy extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        /*获取权限*/
        PermissionControl annotation = this.getClass().getAnnotation(PermissionControl.class);
        Verification permission = null;
        if (annotation != null) {
            try {
                permission = FilterFactories.permissionFactories.getPermission(annotation.value());
            } catch (Exception e) {
                try {
                    permission = FilterFactories.permissionFactories.getPermission(annotation.id());
                } catch (Exception keyNullException) {
                    keyNullException.printStackTrace();
                    throw new ServletException(annotation.toString()+" 找不到该权限！");
                }
            }
            /*权限验证*/
            if (permission != null){
                HttpSession session = req.getSession();
                Object attribute = session.getAttribute(FilterFactories.FILTER_SESSION_ROLE);
                if (attribute instanceof VerificationRole){
                    /*权限检测*/
                    if(((VerificationRole)attribute).verification(permission)){
                        try {
                            throw new PermissionDeniedException("当前会话没有访问的权限,因为角色权限不匹配！");
                        } catch (PermissionDeniedException e) {
                            e.printStackTrace();
                            throw new ServletException(e.getMessage());
                        }
                    }
                } else {
                    /*权限角色对象不存在*/
                    if (FilterFactories.permissionFactories.getConfigStrict()){
                        try {
                            throw new PermissionDeniedException("当前会话没有访问的权限," +
                                    "因为session中的权限角色不存在！");
                        } catch (PermissionDeniedException e) {
                            e.printStackTrace();
                            throw new ServletException(e.getMessage());
                        }
                    }
                }
            }
        }
        /*允许访问*/
        super.service(req, resp);
    }
}
