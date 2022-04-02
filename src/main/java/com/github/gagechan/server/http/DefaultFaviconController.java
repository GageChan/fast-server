package com.github.gagechan.server.http;

import com.github.gagechan.server.annotation.Route;
import com.github.gagechan.server.config.AppConfigProperties;

/**
 * The type Default favicon controller.
 * @author  : GageChan
 * @version  : DefaultFaviconController.java, v 0.1 2022年04月01 23:02 GageChan
 */
@Route(path = "/favicon.ico")
public class DefaultFaviconController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.renderStatic(AppConfigProperties.getInstance().getStaticPrefix() + "/favicon.ico");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {

    }
}
