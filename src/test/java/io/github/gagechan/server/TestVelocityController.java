package io.github.gagechan.server;

import io.github.gagechan.server.annotation.Route;
import io.github.gagechan.server.http.AbstractController;
import io.github.gagechan.server.http.HttpRequest;
import io.github.gagechan.server.http.HttpResponse;

/**
 * The type Test velocity controller.
 *
 * @author : GageChan
 * @version : TestVelocityController.java, v 0.1 2022年04月04 17:03 GageChan
 */
@Route(path = "/test/velocity")
public class TestVelocityController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        request.addAttribute("key", "hello wrold");
        response.renderHtml("testVelocity");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {

    }
}
