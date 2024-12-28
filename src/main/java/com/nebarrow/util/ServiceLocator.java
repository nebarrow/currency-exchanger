package com.nebarrow.util;

import jakarta.servlet.ServletContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ServiceLocator {

    private static ServletContext servletContext = null;

    public static <T> T getService(Class<T> clazz) {
        if (servletContext == null) {
            throw new IllegalStateException("Service not found: " + clazz.getName());
        }
        return clazz.cast(servletContext.getAttribute(clazz.getName()));
    }

    public static synchronized void init(ServletContext context) {
        if (servletContext == null) {
            servletContext = context;
            return;
        }
        throw new IllegalStateException("Service already initialized");
    }

}
