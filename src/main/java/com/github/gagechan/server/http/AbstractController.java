package com.github.gagechan.server.http;

import com.github.gagechan.server.exception.NotSupportMethodException;

import io.netty.handler.codec.http.HttpMethod;

/**
 * The type Abstract controller.
 * @author  : GageChan
 * @version  : AbstractController.java, v 0.1 2022年04月01 20:17 GageChan
 */
public abstract class AbstractController implements Controller {

    @Override
    public void doService(HttpRequest request, HttpResponse response) {
        HttpMethod method = request.method();
        if (method.equals(HttpMethod.GET)) {
            doGet(request, response);
        } else if (method.equals(HttpMethod.POST)) {
            doPost(request, response);
        } else {
            throw new NotSupportMethodException("not support '" + method.name() + "'.");
        }
    }

    @Override
    public abstract void doGet(HttpRequest request, HttpResponse response);

    @Override
    public abstract void doPost(HttpRequest request, HttpResponse response);
}
