package com.github.gagechan.server.ioc;

import com.github.gagechan.server.annotation.Route;
import com.github.gagechan.server.exception.IllegalSyntaxException;
import com.github.gagechan.server.exception.NoUniqueUrlException;
import com.github.gagechan.server.http.AbstractController;

/**
 * The type Url container.
 * @author  : GageChan
 * @version  : UrlContainer.java, v 0.1 2022年04月01 20:06 GageChan
 */
public class UrlContainer extends BeanContainer {
    @Override
    protected void loadRoute(Class<?> clazz) {
        Route route = clazz.getAnnotation(Route.class);
        if (route == null) {
            return;
        }
        String key = route.path();
        checkBeanValid(clazz, route);
        urlMap.put(key, clazz);
    }

    /**
    * Gets clazz.
    *
    * @param url the url
    * @return the clazz
    */
    public static Class<?> getClazz(String url) {
        return urlMap.get(url);
    }

    private void checkBeanValid(Class<?> clazz, Route route) {
        if (!clazz.getSuperclass().equals(AbstractController.class)) {
            throw new IllegalSyntaxException(
                "syntax error: the controller must be extend AbstractController");
        }
        if (urlMap.containsKey(route.path())) {
            throw new NoUniqueUrlException("found 2 url '" + route.path() + "' in ioc.");
        }
    }
}
