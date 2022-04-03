# fast-server

> Fast-Server是一款基于Netty实现的高性能的Web应用服务器。有Spring的味道，有ServletApi的味道。

是否有遇到想做一些小的Web程序，苦于spring太过臃肿而迟迟为下手，那么，现在除了SpringBoot，大家又多了一个选择[FastServer](https://github.com/GageChan/fast-server.git)。

## 快速上手

```xml
<dependency>
  <groupId>com.github.gagechan</groupId>
  <artifactId>fast-server</artifactId>
  <version>1.0</version>
</dependency>
```

```java
@Slf4j
@Route(path = "/example")
@EnableIoc
public class ExampleController extends AbstractController {
    /**
    * Run.
    */
    @Test
    void run() {
        Main.run(ExampleController.class);
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
        byte[] requestBody = request.getBody();
        String jsonStr = new String(requestBody, StandardCharsets.UTF_8);
        log.info("test post, jsonBody: {}", jsonStr);
        // render json
        response.buildJsonResponse(jsonStr);
    }
}
```

## IOC

[FastServer](https://github.com/GageChan/fast-server.git)使用@Bean将类托管给容器管理，通过`XXX bean = BeanContainer.getBean(XXX.class)`获取容器中的bean。同时也提供了bean相关的扩展，用户可实现其接口完成自定义业务。

- `BeforeInitBean` bean初始化前的钩子函数。
- `InitializedBean` bean初始化完成的钩子函数。
- `BeanPostProcess` 容器中所有bean初始化完成的钩子函数。可实现该接口的`int getOrder()`来控制bean初始化顺序。

## 配置

[FastServer](https://github.com/GageChan/fast-server.git)默认加载classpath下名为`application.properties`的文件，当然，用户也可自定义。

目前支持的配置有(后续可能会增加)：

|            名称            |   默认值   |       描述       |
| :------------------------: | :--------: | :--------------: |
|     `application.name`     | FastServer |     应用名称     |
|   `application.version`    |   1.0.0    |     应用版本     |
|      `web.http.port`       |    8000    | http服务监听端口 |
|  `web.http.static.prefix`  |   static   |   静态资源前缀   |
| `web.http.template.prefix` |  template  |   模板资源前缀   |
| `web.http.template.suffix` |    html    |   模板资源后缀   |

## 更新记录

### V1.0.0

* 基础功能建设

## TODO

- [ ] 集成模板引擎
- [ ] 拦截器
- [ ] Https的支持

## 特别感谢

* [Netty](https://github.com/netty/netty.git)
