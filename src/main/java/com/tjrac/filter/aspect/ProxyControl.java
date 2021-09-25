package com.tjrac.filter.aspect;

import com.tjrac.filter.entity.Verification;
import com.tjrac.filter.entity.VerificationParam;
import com.tjrac.filter.entity.VerificationRole;
import com.tjrac.filter.epception.KeyNullException;
import com.tjrac.filter.epception.PermissionDeniedException;
import com.tjrac.filter.factory.FilterFactories;
import com.tjrac.filter.stereotype.Param;
import com.tjrac.filter.stereotype.ParameterFilter;
import com.tjrac.filter.stereotype.PermissionControl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * ProxyControl
 *
 * @author : xziying
 * @create : 2020-11-15 20:56
 */
public abstract class ProxyControl {


    private HttpSession getSession() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return ((ServletRequestAttributes) requestAttributes).getRequest().getSession();
    }

    /**
     * 权限控制方式
     */
    //@Around(value = "proxyControlService() && @annotation(permissionControl)")
    public Object permissionControl(ProceedingJoinPoint pjp, PermissionControl permissionControl)
            throws Throwable {
        Signature signature = pjp.getSignature();
        String value = permissionControl.value();
        int id = permissionControl.id();
        Verification permission = null;
        try {
            permission = FilterFactories.permissionFactories.getPermission(permissionControl.value());
        } catch (KeyNullException e) {
            permission = FilterFactories.permissionFactories.getPermission(permissionControl.id());
        }
        /*权限验证*/
        if (permission != null){
            HttpSession session = getSession();
            Object attribute = session.getAttribute(FilterFactories.FILTER_SESSION_ROLE);
            if (attribute instanceof VerificationRole){
                /*权限检测*/
                if(!((VerificationRole)attribute).verification(permission)){
                    throw new PermissionDeniedException("当前会话没有访问" + signature.getName() + "的权限,因为角色权限不匹配！");
                }
            } else {
                /*权限角色对象不存在*/
                if (FilterFactories.permissionFactories.getConfigStrict()){
                    throw new PermissionDeniedException("当前会话没有访问" + signature.getName() + "的权限," +
                            "因为session中的权限角色不存在！");
                }
            }
        }
        return pjp.proceed();
    }

    /**
     * 参数控制方式
     */
    //@Around(value = "proxyControlService() && @annotation(parameterFilter)")
    public Object parameterFilter(ProceedingJoinPoint pjp, ParameterFilter parameterFilter)
            throws Throwable {
        Object[] args = pjp.getArgs();
        Signature signature = pjp.getSignature();
        HttpSession session = getSession();
        Param[] params = parameterFilter.param();
        for (Param param : params) {
            int index = param.index();
            if (index <= args.length - 1) {
                Object arg = args[index];
                Object attribute = session.getAttribute(param.sessionKey());
                if (arg != null && attribute instanceof VerificationParam){
                    if (!((VerificationParam)attribute).verification(arg)){
                        /*没有访问权限*/
                        throw new PermissionDeniedException("当前会话没有访问" + signature.getName() + "的权限,因为(" +
                                arg + ")与session中的" + param.sessionKey() + "值不匹配！");
                    }
                } else {
                    /*session不存在权限对象*/
                    if (FilterFactories.permissionFactories.getConfigStrict()){
                        throw new PermissionDeniedException("当前会话没有访问" + signature.getName() + "的权限," +
                                "因为session中的" + param.sessionKey() + "值不存在！");
                    }
                }
            }
        }
        return pjp.proceed();
    }
}
