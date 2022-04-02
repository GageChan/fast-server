package com.github.gagechan.server;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.github.gagechan.server.annotation.EnableIoc;
import com.github.gagechan.server.annotation.Route;
import com.github.gagechan.server.http.AbstractController;
import com.github.gagechan.server.http.HttpRequest;
import com.github.gagechan.server.http.HttpResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * The type Main test.
 * @author  : GageChan
 * @version  : MainTest.java, v 0.1 2022年04月03 01:31 GageChan
 */
@EnableIoc
@Route(path = "/example")
@Slf4j
public class MainTest extends AbstractController {
    /**
    * Run.
    */
    @Test
    void run() {
        Main.run(MainTest.class);
    }

    // curl --location --request GET 'localhost:8000/example?name=gagechan'
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        log.info("test get, name: {}", request.getParam("name"));
        // 渲染template/test.html
        response.renderHtml("test");
        // 渲染静态资源
        // response.renderStatic("jquery.min.js")
    }

    // curl --location --request POST 'localhost:8000/example?name=gagechan' \
    //--header 'Content-Type: application/json' \
    //--data-raw '{
    //    "name": "gagechan"
    //}'
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String jsonStr = new String(request.getBody(), StandardCharsets.UTF_8);
        log.info("test post, jsonBody: {}", jsonStr);
        // render json
        response.buildJsonResponse(jsonStr);
    }
}