package com.tjrac.filter.context;

import com.tjrac.filter.factory.FilterFactories;
import com.tjrac.filter.factory.PermissionFactories;
import com.tjrac.filter.stereotype.FilterInjection;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.Field;

/**
 * ContextLoaderListener 加载类
 *
 * @author : xziying
 * @create : 2020-11-14 20:13
 */
@WebListener
public class ContextLoaderListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        String filterFactoriesListenerClass = servletContext.getInitParameter("filterFactoriesListenerClass");
        if (filterFactoriesListenerClass != null){
            try {
                Class<?> aClass = Class.forName(filterFactoriesListenerClass);
                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field field : declaredFields){
                    FilterInjection annotation = field.getAnnotation(FilterInjection.class);
                    if (annotation != null && field.getType() == PermissionFactories.class){
                        field.setAccessible(true);
                        field.set(null, FilterFactories.permissionFactories);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
