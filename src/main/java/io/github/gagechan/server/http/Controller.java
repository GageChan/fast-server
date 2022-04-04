package io.github.gagechan.server.http;

/**
 * The interface Controller.
 * @author  : GageChan
 * @version  : Controller.java, v 0.1 2022年04月01 20:14 GageChan
 */
public interface Controller {

    /**
    * Do get.
    *
    * @param request the request
    * @param response the response
    */
    void doGet(HttpRequest request, HttpResponse response);

    /**
    * Do post.
    *
    * @param request the request
    * @param response the response
    */
    void doPost(HttpRequest request, HttpResponse response);

    /**
    * Do service.
    *
    * @param request the request
    * @param response the response
    */
    void doService(HttpRequest request, HttpResponse response);

}
