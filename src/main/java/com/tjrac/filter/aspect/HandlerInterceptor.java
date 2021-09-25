package com.tjrac.filter.aspect;

import com.tjrac.filter.entity.VerificationRole;
import com.tjrac.filter.factory.FilterFactories;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor
 *
 * @author : xziying
 * @create : 2020-11-29 22:14
 */
public abstract class HandlerInterceptor extends HandlerInterceptorAdapter {

    /**
     * 权限为空时处理方法
     */
    public abstract void permissionIsEmpty(HttpServletRequest request, HttpServletResponse response)throws Exception;

    /**
     * 没有权限访问时处理方法
     */
    public abstract void handler(HttpServletRequest request, HttpServletResponse response) throws Exception;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object role = request.getSession().getAttribute(FilterFactories.FILTER_SESSION_ROLE);
        if (role instanceof VerificationRole){
            String requestURI = request.getRequestURI();
            if (!((VerificationRole) role).verification(requestURI)){
                handler(request, response);
                return false;
            }
        }
        if (role == null){
            permissionIsEmpty(request, response);
            return false;
        }
        return true;
    }
}
